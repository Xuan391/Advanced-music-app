package example.Advanced.Music.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ForgetPasswordRequestDto {
	@NotNull
	@ApiModelProperty(notes = "username", example = "testUser", required = true)
	private String name;
	@Email
	@NotNull
	@ApiModelProperty(notes = "email", example = "usertest@gmail.com", required = true)
	private String email;
}
