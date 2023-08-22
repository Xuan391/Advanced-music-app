package example.Advanced.Music.app.jwt;

import example.Advanced.Music.app.enums.TokenTypeEnum;

import java.util.Date;

public interface TokenService {
    void createToken(Long userId, TokenTypeEnum tokenType, String token, Date issueDate, Date expireDate);
    boolean isTokenValid(Long userId, String token);
}
