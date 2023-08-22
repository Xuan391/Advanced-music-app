package example.Advanced.Music.app.enums;

import lombok.Getter;

public enum OTPType {
	REGISTER_ACCOUNT("REGISTER_ACCOUNT")
	;
	
	@Getter
	private final String value;
	private OTPType(String value) {
		this.value = value;
	}
}
