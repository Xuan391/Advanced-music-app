package example.Advanced.Music.app.services;

import example.Advanced.Music.app.Util.MaskingUtil;
import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.entities.OTP;
import example.Advanced.Music.app.entities.Roles;
import example.Advanced.Music.app.entities.Token;
import example.Advanced.Music.app.entities.Users;
import example.Advanced.Music.app.enums.ErrorEnum;
import example.Advanced.Music.app.enums.OTPType;
import example.Advanced.Music.app.enums.RoleEnum;
import example.Advanced.Music.app.enums.TokenTypeEnum;
import example.Advanced.Music.app.exception.ACTException;
import example.Advanced.Music.app.jwt.JwtService;
import example.Advanced.Music.app.jwt.TokenService;
import example.Advanced.Music.app.models.CustomUserDetails;
import example.Advanced.Music.app.repositories.OTPRepository;
import example.Advanced.Music.app.repositories.RoleRepository;
import example.Advanced.Music.app.repositories.TokenRepository;
import example.Advanced.Music.app.repositories.UserRepository;
import example.Advanced.Music.app.validator.Validator;
import jakarta.transaction.Transactional;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private JwtService jwtService;
    @Value("${app.security.jwtExpiration}")
    private long jwtExpiration;
    @Value("${app.security.jwtExpirationResetPwd}")
    private long jwtExpirationResetPwd;

    @Override
    public String register(RegisterRequestDto requestDto) throws Exception {
        try{
            Users newUser = new Users();
            PropertyUtils.copyProperties(newUser, requestDto);
            newUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            Roles userRole = roleRepository.findByRoleName(RoleEnum.ROLE_USER).orElseThrow(()->new RuntimeException("Error: role not found"));
            Set<Roles> listRole = new HashSet<>();
            listRole.add(userRole);
            newUser.setListRoles(listRole);
            newUser.setIsLock(true);
            Users user = userRepository.save(newUser);

            String sendText = MaskingUtil.Randomization.randomizeStringNumber(6);
            OTP otp = new OTP();
            otp.setUserId(user.getId());
            otp.setType(OTPType.REGISTER_ACCOUNT);
            otp.setValue(sendText);
            Date now = new Date();
            otp.setIssueDate(now);
            otp.setExpireDate(new Date(now.getTime() + jwtExpirationResetPwd*1000));
            otpRepository.save(otp);
            sendSimpleEmail(user.getEmail(),"Accept otp register account email ", sendText);
            return "Register account success, your username is: "+ user.getUsername()
                    + " !Please check mail for complete register!";
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public UserDto acceptRegister(String username, String opt) throws Exception {
        try {
            Optional<Users> oUser = userRepository.findByUsername(username);
            if(!oUser.isPresent()){
                throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
            }
            if(!oUser.get().getIsLock()){
                throw new Exception();
            }
            OTP otp = new OTP();
            List<OTP> otps = otpRepository.findByUserIdAndType(oUser.get().getId(), OTPType.REGISTER_ACCOUNT);
            if(!Validator.isHaveDataLs(otps)){
                throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
            } else {
                otp = otps.get(0);
            }
            if(otp.getValue().equals(opt)){
                userRepository.unLockUser(username);
            }else{
                throw new ACTException(ErrorEnum.PASSWORD_INVALID, ErrorEnum.PASSWORD_INVALID.getMessageId());
            }
            otpRepository.deleteById(otp.getId());
            UserDto userDto = new UserDto();
            PropertyUtils.copyProperties(userDto, oUser.get());
            return userDto;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public AuthSuccessResponseDto login( String username, String password) throws Exception {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails authUser = (CustomUserDetails) authentication.getPrincipal();
            Date issueDate = new Date();
            Date aExpireDate = new Date(issueDate.getTime() + jwtExpiration * 1000);
            Date rExpireDate = new Date(issueDate.getTime() + jwtExpiration * 1000);
            String tokenAccess = jwtService.generateAccessToken(authUser, issueDate, aExpireDate);
            String tokenRefresh = jwtService.generateRefreshToken(authUser, issueDate, rExpireDate);
            AuthSuccessResponseDto ret = new AuthSuccessResponseDto();
            ret.setAccessToken(tokenAccess);
            ret.setRefreshToken(tokenRefresh);
            ret.setExpiresIn(aExpireDate);
            userRepository.loginSuccess(username);
            return ret;
        } catch (Exception e) {
            Optional<Users> oUser = userRepository.findByUsername(username);
            if (oUser.get().getIsLock()) {
                throw new ACTException(ErrorEnum.USER_HAS_BEEN_LOCKED, ErrorEnum.USER_HAS_BEEN_LOCKED.getMessageId());
            }
            if (oUser.get().getFailLoginCount() >= 6) {
                userRepository.lockUser(username);
            } else {
                userRepository.failLogin(username);
            }
            throw new ACTException(ErrorEnum.UNAUTHORIZED, ErrorEnum.UNAUTHORIZED.getMessageId());
        }
    }

    @Override
    public AuthSuccessResponseDto generateToken(String authorization) throws Exception {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();
            userDetails.setAuthorities((Collection< GrantedAuthority >) SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            List<Token> tokenList = tokenRepository.findByValue(authorization);
            if((!Validator.isHaveDataLs(tokenList)) || (!tokenList.get(0).getTokenType().equals(TokenTypeEnum.RefreshToken))
                    || userDetails.getId().equals(tokenList.get(0).getId())){
                throw new ACTException(ErrorEnum.TOKEN_INVALID, ErrorEnum.TOKEN_INVALID.getMessageId());
            }
            Date issueDate = new Date();
            Date aExpireDate = new Date(issueDate.getTime() + jwtExpiration * 1000);
            Date rExpireDate = new Date(issueDate.getTime() + jwtExpiration * 1000);
            String tokenAccess = jwtService.generateAccessToken(userDetails, issueDate, aExpireDate);
            String tokenRefresh = jwtService.generateRefreshToken(userDetails, issueDate, rExpireDate);
            AuthSuccessResponseDto ret = new AuthSuccessResponseDto();
            ret.setAccessToken(tokenAccess);
            ret.setRefreshToken(tokenRefresh);
            ret.setExpiresIn(aExpireDate);
            return ret;
        }catch (Exception e){
            throw new ACTException(ErrorEnum.UNAUTHORIZED, ErrorEnum.UNAUTHORIZED.getMessageId());
        }
    }

    @Override
    public CurrentUserResponseDto currentUserInfo() {
        CustomUserDetails authUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Users> oUser = userRepository.findById(authUser.getId());
        Users user = oUser.get();
        CurrentUserResponseDto ret = new CurrentUserResponseDto();
        ret.setId(user.getId());
        ret.setUsername(user.getUsername());
        ret.setDisplayName(user.getDisplayName());
        ret.setFirstName(user.getFirstName());
        ret.setLastName(user.getLastName());
        ret.setEmail(user.getEmail());
        ret.setAvatar_url(user.getAvatarUrl());
        List<String> roles = new ArrayList<>();
        SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .forEach(auth -> roles.add(auth.toString()));
        ret.setRoles(roles);
        return ret;
    }

    @Override
    public String changePassword(ChangePasswordRequestDto requestDto) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Users> oUser = userRepository.findById(userDetails.getId());
        if(!oUser.isPresent()){
            throw  new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        } else {
          if(!passwordEncoder.matches(requestDto.getCurrentPassword(), oUser.get().getPassword())){
              throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
          }
          String pwd = passwordEncoder.encode(requestDto.getNewPassword());
          userRepository.updatePassword(pwd, userDetails.getId());
          return "Change password success; new password: " +
                  MaskingUtil.Destruction.defaultMasking(requestDto.getNewPassword());
        }
    }

    @Override
    public ForgetPasswordSuccessResponseDto forgetPassword(ForgetPasswordRequestDto requestDto) throws Exception {
        Optional<Users> oUser = userRepository.findByUsernameAndEmail(requestDto.getName(),requestDto.getEmail());
        if (!oUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        } else {
            Users user = oUser.get();
            CustomUserDetails customUserDetails = new CustomUserDetails(user.getId(), user.getUsername(),
                    user.getPassword(), user.getDisplayName(), user.getIsLock());
            Date now = new Date();
            Date expiredDate = new Date(now.getTime() + jwtExpirationResetPwd * 1000);
            String resetPasswordToken = jwtService.generateResetToken(customUserDetails, expiredDate);
            ForgetPasswordSuccessResponseDto ret = new ForgetPasswordSuccessResponseDto();
            ret.setExpiresIn(expiredDate);
            sendSimpleEmail(user.getEmail(), "Reset password ", resetPasswordToken);
            return ret;
        }
    }

    @Override
    public String resetPassword(String resetPasswordToken, ResetPasswordRequestDto request) throws Exception {
        String userIdStr = jwtService.getUserIdOnResetToken(resetPasswordToken);
        long userId = Long.parseLong(userIdStr);
        if (tokenService.isTokenValid(userId, resetPasswordToken)){
            Optional<Users> oUser = userRepository.findById(userId);
            if (!oUser.isPresent()){
                throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
            } else {
                userRepository.unLockUser(oUser.get().getUsername());
                userRepository.loginSuccess(oUser.get().getUsername());
                String pwd = passwordEncoder.encode(request.getNewPassword());
                userRepository.updatePassword(pwd, userId);
                tokenRepository.deleteByUserId(userId);
                return "Reset Password Success; New Password: "
                    + MaskingUtil.Destruction.defaultMasking(request.getNewPassword());
            }
        } else {
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
    }

    public void sendSimpleEmail(String sendTo, String subject, String sendText) throws Exception{
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(sendTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(sendText);
        emailSender.send(mailMessage);
    }
}
