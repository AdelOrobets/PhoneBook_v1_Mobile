package utils;

import dto.UserLombok;

public class TestDataFactoryUser {

    // USERS
    public static UserLombok validUser() {
        return new UserLombok(RandomUtils.generateEmail(8),
                RandomUtils.generatePassword(10));
    }

    private static UserLombok.UserLombokBuilder baseUser() {
        return UserLombok.builder()
                .username(RandomUtils.generateEmail(8))
                .password(RandomUtils.generatePassword(10));
    }

    public static UserLombok userWithoutEmail() {
        return baseUser().username("").build();
    }

    public static UserLombok userWithoutPassword() {
        return baseUser().password("").build();
    }

    public static UserLombok invalidEmailNoAtSymbol() {
        return baseUser().username(RandomUtils.generateInvalidEmailNoAtSymbol(10)).build();
    }

    public static UserLombok invalidEmailNoDomain() {
        return baseUser().username(RandomUtils.generateInvalidEmailNoDomain(10)).build();
    }

    public static UserLombok invalidEmailWithSpace() {
        return baseUser().username(RandomUtils.generateEmail(4) + " " + RandomUtils.
                generateEmail(4)).build();
    }

    public static UserLombok invalidPasswordTooShort() {
        return baseUser().password(RandomUtils.generatePassword(1)).build();
    }

    public static UserLombok invalidPasswordTooLong() {
        return baseUser().password(RandomUtils.generatePassword(16)).build();
    }

    public static UserLombok invalidPasswordNoDigit() {
        return baseUser().password(RandomUtils.generatePasswordInvalidNoDigit(10)).build();
    }

    public static UserLombok invalidPasswordNoSymbol() {
        return baseUser().password(RandomUtils.generatePasswordInvalidNoSymbol(10)).build();
    }
}
