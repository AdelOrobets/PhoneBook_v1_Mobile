package ui_mobile_tests;

import config.AppiumConfig;
import dto.ContactLombok;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import screens.*;
import utils.ErrorMessages;
import utils.TestDataFactoryContact;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class AddNewContactsTests extends AppiumConfig {

    @BeforeMethod(alwaysRun = true)
    public void precondition() {
        new BaseScreen(driver).registrationValidUser();
        new ContactListScreen(driver).clickAddContact();
    }

    // Positive tests
    @Test(groups = {"smoke", "regression"})
    public void testSuccessfulAddNewContact_AllFields() {
        ContactLombok contact = TestDataFactoryContact.validContact();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        String fullName = contact.getName() + " " + contact.getLastName();
        boolean isPresent = new ContactListScreen(driver)
                .isContactPresent(fullName, contact.getPhone());
        Assert.assertTrue(isPresent, "Contact was not added or not displayed");
    }

    @Test(groups = "regression")
    public void testSuccessfulAddContact_onlyRequiredFields() {
        ContactLombok contact = TestDataFactoryContact.withOnlyRequiredFields();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        String fullName = contact.getName() + " " + contact.getLastName();
        boolean isPresent = new ContactListScreen(driver)
                .isContactPresent(fullName, contact.getPhone());
        Assert.assertTrue(isPresent, "Contact was not added or not displayed");
    }

    // Negative tests
    /**
     BUG: A duplicate contact is created when the same contact is added twice
     using identical test data. The application should prevent duplicate entries
     or notify the user that the contact already exists
     */
    @Test(groups = "regression")
    public void testAddContact_duplicateContact() {
        ContactLombok contact = TestDataFactoryContact.validContact();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        new ContactListScreen(driver).clickAddContact();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.DUPLICATE_CONTACT);
    }

    @Test(groups = "regression")
    public void testAddContact_allFieldsEmpty() {
        ContactLombok contact = TestDataFactoryContact.allFieldsEmpty();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.REQUIRED_FIELDS_EMPTY);
    }

    @Test(groups = "regression")
    public void testAddContact_withoutName() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutName();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.NAME_REQUIRED);
    }

    @Test(groups = "regression")
    public void testAddContact_withoutLastName() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutLastName();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.LASTNAME_REQUIRED);
    }

    @Test(groups = "regression")
    public void testAddContact_withoutPhone() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutPhone();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.PHONE_REQUIRED);
    }

    @Test(groups = "regression")
    public void testAddContact_InvalidEmailFormat() {
        ContactLombok contact = TestDataFactoryContact.invalidEmailFormatNoDomain();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = "regression")
    public void testAddContact_InvalidPhoneFormat() {
        ContactLombok contact = TestDataFactoryContact.invalidPhoneFormat();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.INVALID_PHONE);
    }

    @Test(groups = "regression")
    public void testAddContact_tooLongInputInAllFields() {
        ContactLombok contact = TestDataFactoryContact.tooLongFields();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.INVALID_REQUIRED_FIELDS);
    }

    @Test(groups = "regression")
    public void testAddContact_withSpecialCharactersInAllFields() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldsWithSpecialCharacters();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        new ErrorScreen(driver).verifyErrorMessage(ErrorMessages.INVALID_REQUIRED_FIELDS);
    }
}
