package example.Advanced.Music.app.jwt;

import example.Advanced.Music.app.entities.Token;
import example.Advanced.Music.app.enums.TokenTypeEnum;
import example.Advanced.Music.app.repositories.TokenRepository;
import example.Advanced.Music.app.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TokenServiceImpl implements TokenService{
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void createToken(Long userId, TokenTypeEnum tokenType, String token, Date issueDate, Date expireDate) {
        Token t = new Token();
        t.setIssueDate(issueDate);
        t.setExpireDate(expireDate);
        t.setTokenType(tokenType);
        t.setUserId(userId);
        t.setValue(token);
        tokenRepository.save(t);
    }
    @Override
    public boolean isTokenValid(Long userId, String token) {
        List<Token> t = tokenRepository.findByValue(token);
        if(Validator.isHaveDataLs(t)){
            Token tk = t.get(0);
            if(tk.getUserId().equals(userId)){
                Date now = new Date();
                return (now.after(tk.getIssueDate()) && now.before(tk.getExpireDate()));
            }
        }
        return false;
    }
}
