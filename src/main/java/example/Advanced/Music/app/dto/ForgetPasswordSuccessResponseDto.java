package example.Advanced.Music.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ForgetPasswordSuccessResponseDto {
	private Date expiresIn;
}
