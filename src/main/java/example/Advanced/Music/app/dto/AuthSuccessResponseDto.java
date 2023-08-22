package example.Advanced.Music.app.dto;

import example.Advanced.Music.app.constans.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Getter
@Setter
public class AuthSuccessResponseDto {
    private String accessToken;
    private String refreshToken;
    @DateTimeFormat(pattern = Constants.DEFAULT_DATE_FORMAT)
    private Date expiresIn;
}
