package example.Advanced.Music.app.enums;

import lombok.Getter;

public enum TokenTypeEnum {
	AccessToken("AccessToken"),
	RefreshToken("RefreshToken"),
	ResetPasswordToken("ResetPasswordToken")
	;
	
	@Getter
	private final String value;
	private TokenTypeEnum(String value) {
		this.value = value;
	}
}
