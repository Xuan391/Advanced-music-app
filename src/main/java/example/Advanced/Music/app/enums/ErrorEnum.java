package example.Advanced.Music.app.enums;

import lombok.Getter;

public enum ErrorEnum {
// @formatter:off
	VALIDATION_ERROR("1000", "validation-error"),
	DUPPLICATE_ENTITY("1001", "dupplicate-entity"),
	
	
//	user
	USER_ENTITY_NOT_FOUND("1100", "the.user.does.not.exist.on.the.system"),
	USER_HAS_BEEN_LOCKED("1113", "the.user.has.been.locked"),
	USER_HAS_BEEN_DELETED("1101", "the.user.has.been.deleted"),
	ERROR_UNKNOW("1102", "error.please.retry"),
	VALUE_NOT_CORRECT("1103", "the.passed.value.is.not.correct"),
	PASSWORD_INVALID("1004", "password.invalid"),
	NOT_FOUND("1005", "not.found"),
	ACCOUNTNAME_NOT_FOUND("1006", "accountname.not.found"),
	TOKEN_INVALID("1007", "token.invalid"),
	UNAUTHORIZED("404", "unauthorization"),
	VALUE_NOT_RANGE("1008", "ss.value.not.range"),
	VALUE_RANGE_TOO_LONG("1009", "ss.value.range.too.long"),
	NODE_IS_NOT_ENVIRONMENT("1010", "ss.node.is.not.environment"),
	REQUEST_TIME_OUT("1011", "ss.request.time.out"),
	CONNECT_FAILURE("1011", "ss.connect.failue"),
	ACCESS_DENIED("1012", "ss.access.denied"),
	RIGHT_CONTROLL_EXIT("1013", "ss.this.device.has.root"),
	DATA_NOT_VALID("1014", "ss.data.not.valid"),
	DELETE_FAILUE("1015", "ss.delete.failue"),
	;
	;
// @formatter:on

	@Getter
	private final String messageId;
	@Getter
	private final String code;

	private ErrorEnum(String code, String messageId) {
		this.messageId = messageId;
		this.code = code;
	}
}