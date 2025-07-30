package utils;

public class ErrorMessages {
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String DUPLICATE_CONTACT = "Contact already exists";

    public static final String EMAIL_REQUIRED = "must not be blank";
    public static final String PASSWORD_REQUIRED = "must not be blank";
    public static final String NAME_REQUIRED = "must not be blank";
    public static final String LASTNAME_REQUIRED = "must not be blank";
    public static final String PHONE_REQUIRED = "length min 10";
    public static final String REQUIRED_FIELDS_EMPTY ="must not be blank";

    public static final String INVALID_EMAIL = "must be a well-formed email address";
    public static final String INVALID_PHONE = "must contain only digits";
    public static final String INVALID_PHONE_LONG_OR_SHORT = "Phone number must contain only digits! " +
            "And length min 10, max 15!";
    public static final String INVALID_INPUT_LONG_OR_SHORT = "min 10 max 15";
    public static final String INVALID_INPUT = "must not be blank";
    public static final String INVALID_REQUIRED_FIELDS ="Phone number must contain only digits! " +
            "And length min 10, max 15!, email=must be a well-formed email address";

    public static final String PASSWORD_TOO_SHORT = "8 characters";
    public static final String PASSWORD_TOO_LONG = "Password too long";
    public static final String PASSWORD_NO_DIGIT = "Must contain at least 1 uppercase letter, " +
            "1 lowercase letter, and 1 number; Can contain special characters";
    public static final String PASSWORD_NO_SYMBOL = "Must contain at least 1 uppercase letter, " +
            "1 lowercase letter, and 1 number; Can contain special characters";

    public static final String LOGIN_FAILED = "Login or Password incorrect";
}
