package example.Advanced.Music.app.constans;

public class Constants {
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String DEFAULT_TIME_ZONE = "GMT+7";
	public static final long DEFAULT_TIME_START = 604700000;
	public static final String DEFAULT_DATE_FORMAT_DATE = "yyyy-MM-dd";
	public static final String DEFAULT_TIME_FORMAT_TIME = "HH:mm:ss.SSS";
	public static final String DEFAULT_USER = "SYSTEM";

	public static final int ID_MAX_LENGTH = 36;
	public static final String ZERO_UUID = "00000000-0000-0000-0000-000000000000";
	public static final String PATTERN_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,32}$";
	public static final String PATTERN_NAME = "^([A-Z][a-z]+([\\s][A-Z][a-z]+)*)+$";
	public static final String PATTERN_USERNAME = "^(?=[a-zA-Z0-9-._]{5,50}$)(?!.*[-_.]{2})[^-_.].*[^-_.]$";
	public static final String PATTERN_PHONENUMBER = "^\\+?[0-9]{2,20}$";
	public static final int DEFAULT_PAGE_SIZE_MAX = 1000;

	public static final int PASSWORD_MAX_LENGTH = 256;
	public static final int PASSWORD_MIN_LENGTH = 32;
	public static final int NAME_MAX_LENGTH = 100;
	public static final int NAME_MIN_LENGTH = 5;
	public static final int DISPLAY_NAME_MAX_LENGTH = 255;
	public static final int DISPLAY_NAME_MIN_LENGTH = 1;
	public static final int FAIL_LOGIN_COUNT_MIN = 0;
	public static final int FAIL_LOGIN_COUNT_MAX = 6;

	public static final String KEY_CHECK_EVN_DATA = "currentTime";
	public static final int S_TIME_TO_MICRO = 1000;

	public class Role {
		public static final String ROLE_ADMIN = "ROLE_ADMIN";

		public static final String ROLE_USER = "ROLE_USER";
	}

}
