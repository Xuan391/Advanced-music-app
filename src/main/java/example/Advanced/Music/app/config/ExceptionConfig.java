package example.Advanced.Music.app.config;

import example.Advanced.Music.app.dto.ErrorResponse;
import example.Advanced.Music.app.enums.ErrorEnum;
import example.Advanced.Music.app.exception.ACTException;
import example.Advanced.Music.app.validator.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionConfig {
	@ExceptionHandler(ACTException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
	protected ErrorResponse handleBlException(ACTException ex) {
		ErrorEnum err = ex.getError();
		String code = err.getCode();
		String message = "";
		if (Validator.isHaveDataString(code)) {
			message = ex.getAdditionalData();
		} else {
			message = err.getMessageId();
		}
		String msg = getMessage(message);
		return new ErrorResponse(code, msg);
	}

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
	protected ErrorResponse handleOtherThrowable(Throwable ex) {
		return new ErrorResponse(ex.getClass().getCanonicalName(), ex.getMessage());
	}

	private String getMessage(String messageId) {
		// TODO: Get message from resource file
		return "MessageId: " + messageId;
	}
}
