package utils;

import dto.ContactLombok;

public class TestDataFactoryContact {

    // CONTACTS
    public static ContactLombok validContact() {
        return new ContactLombok(
                null,
                RandomUtils.generateFirstNameFromList(),
                RandomUtils.generateLastNameFromList(),
                RandomUtils.generatePhoneNumber(),
                RandomUtils.generateEmail(8),
                RandomUtils.generateAddressList(),
                RandomUtils.generateDescription()
        );
    }

    private static ContactLombok.ContactLombokBuilder baseContact() {
        return ContactLombok.builder()
                .name(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .phone(RandomUtils.generatePhoneNumber())
                .email(RandomUtils.generateEmail(8))
                .address(RandomUtils.generateAddressList())
                .description(RandomUtils.generateDescription());
    }

    public static ContactLombok withOnlyRequiredFields(){
        return ContactLombok.builder()
                .name(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .phone(RandomUtils.generatePhoneNumber())
                .email("")
                .address(RandomUtils.generateAddressList())
                .description("")
                .build();
    }

    public static ContactLombok allFieldsEmpty() {
        return ContactLombok.builder()
                .name("")
                .lastName("")
                .phone("")
                .email("")
                .address("")
                .description("")
                .build();
    }

    public static ContactLombok invalidFieldWithoutName() {
        return baseContact().name("").build();
    }

    public static ContactLombok invalidFieldWithoutLastName() {
        return baseContact().lastName("").build();
    }

    public static ContactLombok invalidFieldWithoutPhone() {
        return baseContact().phone("").build();
    }

    public static ContactLombok invalidFieldWithoutEmail() {
        return baseContact().email("").build();
    }

    public static ContactLombok invalidEmailFormatNoDomain() {
        return baseContact().email("invalidEmailFormat@").build();
    }

    public static ContactLombok invalidPhoneFormat() {
        return baseContact().phone("123abc4561").build();
    }

    public static ContactLombok invalidPhoneFormatTooShort() {
        return baseContact().phone("05123456").build();
    }

    public static ContactLombok tooLongFields() {
        String longText = "A".repeat(300);
        return ContactLombok.builder()
                .name(longText)
                .lastName(longText)
                .phone("12345678901234567890")
                .email(longText + "@test.com")
                .address(longText)
                .description(longText)
                .build();
    }

    public static ContactLombok invalidFieldsWithSpecialCharacters() {
        return ContactLombok.builder()
                .name("@@@")
                .lastName("###")
                .phone("1234567!!")
                .email("test@@example.com")
                .address("!!! Address ???")
                .description("### Description ***")
                .build();
    }
}
