package ui_mobile_tests;

import config.AppiumConfig;
import dto.ContactLombok;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import screens.*;
import utils.*;

@Listeners(TestNGListener.class)
public class EditContactTests extends AppiumConfig {

    @BeforeMethod(alwaysRun = true)
    public void precondition() {
        new SplashScreen(driver).goToAuthenticationScreen();
        new BaseScreen(driver).registrationValidUser();
        new BaseScreen(driver).addNewValidContact();
        new ContactListScreen(driver).swipeToLeftForEditFirstContact();
    }

    // Positive tests
    @Test(groups = {"smoke", "regression"})
    public void testContactEditSuccessful_allFields() {
        ContactLombok updatedContact = TestDataFactoryContact.validContact();
        new EditContactScreen(driver).editContact(updatedContact);
        String fullName = updatedContact.getName() + " " + updatedContact.getLastName();
        boolean isPresent = new ContactListScreen(driver)
                .isContactPresent(fullName, updatedContact.getPhone());
        Assert.assertTrue(isPresent, "Contact not displayed");
    }

    @Test(groups = {"smoke", "regression"})
    public void testContactEditSuccessful_onlyRequiredFields() {
        ContactLombok updatedContact = TestDataFactoryContact.withOnlyRequiredFields();
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
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.NAME_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withEmptyLastName() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutLastName();
        new EditContactScreen(driver).editContact(contact);
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.LASTNAME_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withEmptyPhone() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutPhone();
        new EditContactScreen(driver).editContact(contact);
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.PHONE_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withInvalidPhone() {
        ContactLombok contact = TestDataFactoryContact.invalidPhoneFormat();
        new EditContactScreen(driver).editContact(contact);
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.INVALID_PHONE);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withInvalidPhoneTooShort() {
        ContactLombok contact = TestDataFactoryContact.invalidPhoneFormatTooShort();
        new EditContactScreen(driver).editContact(contact);
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.INVALID_PHONE_LONG_OR_SHORT);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withEmptyEmail() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutEmail();
        new EditContactScreen(driver).editContact(contact);
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.EMAIL_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withInvalidEmail() {
        ContactLombok contact = TestDataFactoryContact.invalidEmailFormatNoDomain();
        new EditContactScreen(driver).editContact(contact);
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_allFieldsEmpty() {
        ContactLombok contact = TestDataFactoryContact.allFieldsEmpty();
        new EditContactScreen(driver).editContact(contact);
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.INVALID_INPUT);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_tooLongInputInAllFields() {
        ContactLombok contact = TestDataFactoryContact.tooLongFields();
        new EditContactScreen(driver).editContact(contact);
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.INVALID_REQUIRED_FIELDS);
    }
}