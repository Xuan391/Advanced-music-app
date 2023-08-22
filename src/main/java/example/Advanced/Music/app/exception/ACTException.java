package example.Advanced.Music.app.exception;

import example.Advanced.Music.app.enums.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ACTException extends Exception{
	    private ErrorEnum error;
	    private String additionalData;
	    private static final long serialVersionUID = 8442358956304045349L;

	}
