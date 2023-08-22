package example.Advanced.Music.app.dto;

import example.Advanced.Music.app.constans.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@Setter
@Getter
public class LoginRequestDto {
    @NotNull
    @Pattern(regexp = Constants.PATTERN_USERNAME)
    private String username;
    @NotNull
    @Pattern(regexp = Constants.PATTERN_PASSWORD)
    private String password;
}
