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
import utils.ErrorMessages;
import utils.TestDataFactoryContact;
import utils.TestDataFactoryUser;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class AddNewContactsTests extends AppiumConfig {

    @BeforeMethod(alwaysRun = true)
    public void precondition() {
        UserLombok user = TestDataFactoryUser.validUser();
        new AuthenticationScreen(getDriver()).registerUser(user);
        new ContactListScreen(getDriver()).clickAddContact();
    }

    // Positive tests
    @AllureId("AC_001")
    @Story("Contact management")
    @Test(groups = {"smoke", "regression"})
    public void testSuccessfulAddNewContact_AllFields() {
        ContactLombok contact = TestDataFactoryContact.validContact();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        String fullName = contact.getName() + " " + contact.getLastName();
        boolean isPresent = new ContactListScreen(getDriver())
                .isContactPresent(fullName, contact.getPhone());
        Assert.assertTrue(isPresent, "Contact was not added or not displayed");
    }

    @AllureId("AC_002")
    @Story("Contact management")
    @Test(groups = {"smoke", "regression"})
    public void testSuccessfulAddContact_onlyRequiredFields() {
        ContactLombok contact = TestDataFactoryContact.withOnlyRequiredFields();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        String fullName = contact.getName() + " " + contact.getLastName();
        boolean isPresent = new ContactListScreen(getDriver())
                .isContactPresent(fullName, contact.getPhone());
        Assert.assertTrue(isPresent, "Contact was not added or not displayed");
    }

    // Negative tests
    @Issue("BUG: AC_001. Duplicate contact added twice, no error message")
    @Test(groups = "regression")
    public void testAddContact_duplicateContact() {
        ContactLombok contact = TestDataFactoryContact.validContact();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        new ContactListScreen(getDriver()).clickAddContact();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.DUPLICATE_CONTACT);
    }

    @Test(groups = "regression")
    public void testAddContact_allFieldsEmpty() {
        ContactLombok contact = TestDataFactoryContact.allFieldsEmpty();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.REQUIRED_FIELDS_EMPTY);
    }

    @Test(groups = "regression")
    public void testAddContact_withoutName() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutName();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.NAME_REQUIRED);
    }

    @Test(groups = "regression")
    public void testAddContact_withoutLastName() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutLastName();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LASTNAME_REQUIRED);
    }

    @Test(groups = "regression")
    public void testAddContact_withoutPhone() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutPhone();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.PHONE_REQUIRED);
    }

    @Test(groups = "regression")
    public void testAddContact_InvalidEmailFormat() {
        ContactLombok contact = TestDataFactoryContact.invalidEmailFormatNoDomain();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = "regression")
    public void testAddContact_InvalidPhoneFormat() {
        ContactLombok contact = TestDataFactoryContact.invalidPhoneFormat();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_PHONE);
    }

    @Test(groups = "regression")
    public void testAddContact_tooLongInputInAllFields() {
        ContactLombok contact = TestDataFactoryContact.tooLongFields();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_REQUIRED_FIELDS);
    }

    @Test(groups = "regression")
    public void testAddContact_withSpecialCharactersInAllFields() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldsWithSpecialCharacters();
        new AddContactScreen(getDriver()).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_REQUIRED_FIELDS);
    }
}
