package example.Advanced.Music.app.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.Advanced.Music.app.enums.TokenTypeEnum;
import example.Advanced.Music.app.models.CustomUserDetails;
import example.Advanced.Music.app.models.RequestContextHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsMutator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtServiceImpl implements JwtService{
    private static final String SCOPES = "scopes";
    private static final String ENABLED = "enabled";
    private static final String PRINCIPAL = "principal";
    @Value("${app.security.jwtSecret}")
    private String jwtSecret;
    @Value("${app.security.jwtExpiration}")
    private int jwtExpiration;
    @Value("${app.security.jwtExpirationResetPwd}")
    private int jwtExpirationResetPwd;
    @Value("${app.security.jwtIssuerApp}")
    private String issuerApp;
    @Value("${app.security.jwtTokenPrefix}")
    private String jwtTokenPrefix;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String generateAccessToken(CustomUserDetails userDetails, Date issueDate, Date expireDate) {
        List<String> authorities = new ArrayList<>();
        for(GrantedAuthority grantedAuthority : userDetails.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }
        Date now = new Date();
        if(issueDate == null){
            issueDate = now;
        }
        if(expireDate == null){
            expireDate = new Date((now.getTime()+jwtExpiration*1000));
        }
        Claims claims = Jwts.claims();
        claims.setSubject(userDetails.getUsername());
        claims.setId(UUID.randomUUID().toString());
        claims.setIssuedAt(issueDate);
        claims.setExpiration(expireDate);
        claims.put(SCOPES, authorities);
        claims.put(ENABLED, userDetails.isEnabled());
        claims.put(PRINCIPAL, userDetails);

        String token = Jwts.builder()
                .setIssuer(issuerApp)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .setClaims(claims).compact();
        if(tokenService!=null){
            tokenService.createToken(userDetails.getId(), TokenTypeEnum.AccessToken, token, issueDate, expireDate);
        }
        return token;
    }

    @Override
    public String generateRefreshToken(CustomUserDetails userDetails, Date issueDate, Date expireDate) {
        Claims claims = Jwts.claims();
        claims.setSubject(userDetails.getUsername());
        claims.put(SCOPES, Collections.singletonList("REFRESH_TOKEN"));
        claims.put(ENABLED, userDetails.isEnabled());
        claims.setIssuedAt(issueDate);
        claims.setExpiration(expireDate);
        claims.put(PRINCIPAL, userDetails);

        Date now = new Date();
        if(issueDate == null){
            issueDate = now;
        }
        if(expireDate == null){
            expireDate = new Date(now.getTime() + jwtExpiration*1000);
        }
        String token = Jwts.builder()
                .setIssuer(issuerApp)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getUsername())
                .setClaims(claims).compact();
        if(tokenService != null){
            tokenService.createToken(userDetails.getId(), TokenTypeEnum.RefreshToken, token, issueDate, expireDate);
        }
        return token;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(token == null || !token.startsWith(jwtTokenPrefix)){
            return null;
        }
        token = token.substring(jwtTokenPrefix.length()+1);
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

        String userName = claims.getSubject();
        if(userName == null || userName.isEmpty()){
            return null;
        }
        Object authObj = claims.get(PRINCIPAL);
        CustomUserDetails principal = objectMapper.convertValue(authObj, new TypeReference<CustomUserDetails>() {
        });
        if(tokenService.isTokenValid(principal.getId(),token)){
            return null;
        }

        Object scopesObj = claims.get(SCOPES);
        List<String> scopes = objectMapper.convertValue(scopesObj, new TypeReference<List<String>>() {
        });

//        Object scopesObj = claims.get(SCOPES);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        List<String> scopes = objectMapper.convertValue(scopesObj, new TypeReference<List<String>>() {
//        });
        scopes.forEach(scope -> {
            authorities.add(new SimpleGrantedAuthority(scope));
        });

        return new UsernamePasswordAuthenticationToken(principal,null, authorities);
    }

    @Override
    public String generateResetToken(CustomUserDetails userDetails, Date expireDate) {
        Claims claims = Jwts.claims();
        claims.setSubject(userDetails.getUsername());
        claims.put(SCOPES, Collections.singletonList("RESET_PASSWORD_TOKEN"));
        claims.put(ENABLED, userDetails.isEnabled());
        claims.put("userId",userDetails.getId());
        claims.put("displayName", userDetails.getDisplayName());
        String key = RequestContextHolder.get().getClientMessageId();
        claims.put("key", key);
        Date issueDate = new Date();
        if(expireDate== null){
            issueDate = new Date();
            expireDate = new Date(issueDate.getTime() + jwtExpirationResetPwd*1000);
        }
        String token = Jwts.builder()
                .setIssuer(issuerApp)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(issueDate)
                .setExpiration(expireDate)
                .setClaims(claims).compact();
        if(tokenService!=null){
            tokenService.createToken(userDetails.getId(), TokenTypeEnum.ResetPasswordToken,token,issueDate,expireDate );
        }
        return token;
    }

    @Override
    public String getUserIdOnResetToken(String token) {
        if(token == null || token.isEmpty()){
            return  null;
        }
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        String userName = claims.getSubject();
        if(userName == null || userName.isEmpty()){
            return null;
        }

        Object scopesObj = claims.get("userId");
        String userId = objectMapper.convertValue(scopesObj, new TypeReference<String>() {
        });
        return userId;
    }
}
