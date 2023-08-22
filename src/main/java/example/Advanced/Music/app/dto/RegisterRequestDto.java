package example.Advanced.Music.app.dto;

import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.validator.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@FieldMatch(first = "password", second = "confirmPassword")
public class RegisterRequestDto {
    @NotNull
    @Pattern(regexp = Constants.PATTERN_USERNAME)
    private String username;
    @NotNull
    @Pattern(regexp = Constants.PATTERN_PASSWORD)
    private String password;
    @NotNull
    @Pattern(regexp = Constants.PATTERN_PASSWORD)
    private String confirmPassword;
    @NotNull
    @Pattern(regexp = Constants.PATTERN_NAME)
    private String firstName;
    @NotNull
    @Pattern(regexp = Constants.PATTERN_NAME)
    private String lastName;
    @NotNull
    @Email
    private String email;
}
