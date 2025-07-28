package ui_mobile_tests;

import config.AppiumConfig;
import dto.ContactLombok;
import dto.UserLombok;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import screens.*;
import utils.*;

@Listeners(TestNGListener.class)
public class EditContactTests extends AppiumConfig {

    private static final Logger logger = LoggerFactory.getLogger(EditContactTests.class);

    @BeforeMethod(alwaysRun = true)
    public void precondition() {
        new SplashScreen(driver).goToAuthenticationScreen();
        registrationUser();
        addNewContact();
        new ContactListScreen(driver).swipeToLeftForEditFirstContact();
    }

    public void registrationUser() {
        UserLombok user = TestDataFactoryUser.validUser();
        new AuthenticationScreen(driver).registerUser(user);
    }

    public void addNewContact() {
        ContactLombok contact = TestDataFactoryContact.validContact();
        new ContactListScreen(driver).clickAddContact();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
    }

    private void verifyErrorMessage(String expectedMsg) {
        ErrorScreen errorScreen = new ErrorScreen(driver);
        String actualMsg = errorScreen.getErrorMessage();
        logger.info("Actual error message: '{}'", actualMsg);
        logger.info("Expected to contain: '{}'", expectedMsg);

        Assert.assertTrue(actualMsg.contains(expectedMsg),
                "Expected error message to contain: " + expectedMsg +
                        ", but got: '" + actualMsg + "'");
        errorScreen.closeErrorMsg();
    }

    // Positive tests
    @Test(groups = {"smoke", "regression"})
    public void testContactEditSuccessful() {
        ContactLombok updatedContact = TestDataFactoryContact.validContact();
        new EditContactScreen(driver).editContact(updatedContact);
        String fullName = updatedContact.getName() + " " + updatedContact.getLastName();
        boolean isPresent = new ContactListScreen(driver)
                .isContactPresent(fullName, updatedContact.getPhone());
        Assert.assertTrue(isPresent, "Contact not displayed");
    }

    // Negative tests
    @Test(groups = {"regression"})
    public void testContactEdit_withEmptyName() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutName();
        new EditContactScreen(driver).editContact(contact);
        verifyErrorMessage(ErrorMessages.NAME_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withEmptyLastName() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutLastName();
        new EditContactScreen(driver).editContact(contact);
        verifyErrorMessage(ErrorMessages.LASTNAME_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withEmptyPhone() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutPhone();
        new EditContactScreen(driver).editContact(contact);
        verifyErrorMessage(ErrorMessages.PHONE_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withInvalidPhone() {
        ContactLombok contact = TestDataFactoryContact.invalidPhoneFormat();
        new EditContactScreen(driver).editContact(contact);
        verifyErrorMessage(ErrorMessages.INVALID_PHONE);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withInvalidPhoneTooShort() {
        ContactLombok contact = TestDataFactoryContact.invalidPhoneFormatTooShort();
        new EditContactScreen(driver).editContact(contact);
        verifyErrorMessage(ErrorMessages.INVALID_PHONE_LONG_OR_SHORT);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withEmptyEmail() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutEmail();
        new EditContactScreen(driver).editContact(contact);
        verifyErrorMessage(ErrorMessages.EMAIL_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withInvalidEmail() {
        ContactLombok contact = TestDataFactoryContact.invalidEmailFormatNoDomain();
        new EditContactScreen(driver).editContact(contact);
        verifyErrorMessage(ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_allFieldsEmpty() {
        ContactLombok contact = TestDataFactoryContact.allFieldsEmpty();
        new EditContactScreen(driver).editContact(contact);
        verifyErrorMessage(ErrorMessages.INVALID_INPUT);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_tooLongInputInAllFields() {
        ContactLombok contact = TestDataFactoryContact.tooLongFields();
        new EditContactScreen(driver).editContact(contact);
        verifyErrorMessage(ErrorMessages.INVALID_REQUIRED_FIELDS);
    }
}