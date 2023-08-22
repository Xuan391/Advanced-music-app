package example.Advanced.Music.app.jwt;

import example.Advanced.Music.app.models.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.Date;

public interface JwtService {
    Authentication getAuthentication (HttpServletRequest request);
    String generateAccessToken(CustomUserDetails userDetails, Date issueDate, Date expireDate);
    String generateRefreshToken(CustomUserDetails userDetails, Date issueDate, Date expireDate);
    String generateResetToken(CustomUserDetails userDetails, Date expireDate);
    String getUserIdOnResetToken(String token);
}
