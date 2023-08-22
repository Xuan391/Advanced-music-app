package example.Advanced.Music.app.dto;

import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.enums.RoleEnum;
import example.Advanced.Music.app.validator.FieldMatch;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword")
public class CreateUserRequestDto {
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

	@NotNull
	@Pattern(regexp = Constants.PATTERN_PHONENUMBER)
	private String phoneNumber;

	private Date expireDate;

	@Enumerated(EnumType.STRING)
	private RoleEnum role;
}
