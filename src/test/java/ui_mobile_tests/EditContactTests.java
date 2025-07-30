package ui_mobile_tests;

import config.AppiumConfig;
import dto.ContactLombok;
import dto.UserLombok;
import io.qameta.allure.AllureId;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
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
        UserLombok user = TestDataFactoryUser.validUser();
        new AuthenticationScreen(getDriver()).registerUser(user);
        addNewValidContact();
    }

    public void addNewValidContact() {
        ContactLombok contact = TestDataFactoryContact.validContact();
        new ContactListScreen(getDriver()).clickAddContact();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
    }

    public void swipeAndEdit(ContactLombok contact){
        ContactListScreen contactScreen = new ContactListScreen(getDriver());
        contactScreen.swipeToLeftForEditFirstContact();
        EditContactScreen editScreen = new EditContactScreen(getDriver());
        editScreen.editContact(contact);
    }

    // Positive tests
    @AllureId("EC_001")
    @Story("Contact management")
    @Test(groups = {"smoke", "regression"})
    public void testContactEditSuccessful_allFields() {
        ContactLombok updatedContact = TestDataFactoryContact.validContact();
        swipeAndEdit(updatedContact);

        String fullName = updatedContact.getName() + " " + updatedContact.getLastName();
        boolean isPresent = new ContactListScreen(getDriver())
                .isContactPresent(fullName, updatedContact.getPhone());
        Assert.assertTrue(isPresent, "Contact not displayed");
    }

    @AllureId("EC_002")
    @Story("Contact management")
    @Test(groups = {"smoke", "regression"})
    public void testContactEditSuccessful_onlyRequiredFields() {
        ContactLombok updatedContact = TestDataFactoryContact.withOnlyRequiredFields();
        swipeAndEdit(updatedContact);
        String fullName = updatedContact.getName() + " " + updatedContact.getLastName();
        boolean isPresent = new ContactListScreen(getDriver())
                .isContactPresent(fullName, updatedContact.getPhone());
        Assert.assertTrue(isPresent, "Contact not displayed");
    }

    // Negative tests
    @Test(groups = {"regression"})
    public void testContactEdit_withEmptyName() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutName();
        swipeAndEdit(contact);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.NAME_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withEmptyLastName() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutLastName();
        swipeAndEdit(contact);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LASTNAME_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withEmptyPhone() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutPhone();
        swipeAndEdit(contact);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.PHONE_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withInvalidPhone() {
        ContactLombok contact = TestDataFactoryContact.invalidPhoneFormat();
        swipeAndEdit(contact);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_PHONE);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withInvalidPhoneTooShort() {
        ContactLombok contact = TestDataFactoryContact.invalidPhoneFormatTooShort();
        swipeAndEdit(contact);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_PHONE_LONG_OR_SHORT);
    }

    @Issue("BUG: EC_001. Contact edit succeeds with empty email field")
    @Test(groups = {"regression"})
    public void testContactEdit_withEmptyEmail() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutEmail();
        swipeAndEdit(contact);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.EMAIL_REQUIRED);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_withInvalidEmail() {
        ContactLombok contact = TestDataFactoryContact.invalidEmailFormatNoDomain();
        swipeAndEdit(contact);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_allFieldsEmpty() {
        ContactLombok contact = TestDataFactoryContact.allFieldsEmpty();
        swipeAndEdit(contact);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_INPUT);
    }

    @Test(groups = {"regression"})
    public void testContactEdit_tooLongInputInAllFields() {
        ContactLombok contact = TestDataFactoryContact.tooLongFields();
        swipeAndEdit(contact);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_REQUIRED_FIELDS);
    }
}