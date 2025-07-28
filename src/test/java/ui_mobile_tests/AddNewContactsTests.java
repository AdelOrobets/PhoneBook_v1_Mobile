package ui_mobile_tests;

import config.AppiumConfig;
import dto.ContactLombok;
import dto.UserLombok;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import screens.AddContactScreen;
import screens.ContactListScreen;
import screens.ErrorScreen;
import screens.AuthenticationScreen;
import utils.ErrorMessages;
import utils.TestDataFactoryContact;
import utils.TestDataFactoryUser;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class AddNewContactsTests extends AppiumConfig {

    @BeforeMethod(alwaysRun = true)
    public void registrationUser() {
        UserLombok user = TestDataFactoryUser.validUser();
        new AuthenticationScreen(driver).registerUser(user);
        new ContactListScreen(driver).clickAddContact();
    }

    // Positive tests
    @Test(groups = {"smoke", "regression"})
    public void testSuccessful_addNewContact() {
        ContactLombok contact = TestDataFactoryContact.validContact();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();

        boolean isPresent = new ContactListScreen(driver)
                .isContactPresent(contact.getName(), contact.getPhone());
        Assert.assertTrue(isPresent, "Contact was not added or not displayed");
    }

    // Helper
    private void assertContactCreationError(String expectedMsg) {
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.getErrorMessage().contains(expectedMsg),
                "Expected error message to contain: " + expectedMsg +
                        " but got: " + errorScreen.getErrorMessage());
        errorScreen.closeErrorMsg();
    }

    // Negative tests
    @Test(groups = "regression")
    public void testAddContact_DuplicateContact() {
        ContactLombok contact = TestDataFactoryContact.validContact();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();

        boolean isPresent = new ContactListScreen(driver)
                .isContactPresent(contact.getName(), contact.getPhone());
        Assert.assertTrue(isPresent, "Contact was not added the first time");

        new ContactListScreen(driver).clickAddContact();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        assertContactCreationError(ErrorMessages.DUPLICATE_CONTACT);
    }

    @Test(groups = "regression")
    public void testAddContact_AllFieldsEmpty() {
        ContactLombok contact = TestDataFactoryContact.allFieldsEmpty();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        assertContactCreationError(ErrorMessages.REQUIRED_FIELDS);
    }

    @Test(groups = "regression")
    public void testAddContact_WithoutName() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutName();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        assertContactCreationError(ErrorMessages.NAME_REQUIRED);
    }

    @Test(groups = "regression")
    public void testAddContact_WithoutLastName() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutLastName();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        assertContactCreationError(ErrorMessages.LASTNAME_REQUIRED);
    }

    @Test(groups = "regression")
    public void testAddContact_WithoutPhone() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutPhone();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        assertContactCreationError(ErrorMessages.PHONE_REQUIRED);
    }

    @Test(groups = "regression")
    public void testAddContact_WithoutEmail() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldWithoutEmail();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        assertContactCreationError(ErrorMessages.EMAIL_REQUIRED);
    }

    @Test(groups = "regression")
    public void testAddContact_InvalidEmailFormat() {
        ContactLombok contact = TestDataFactoryContact.invalidEmailFormat();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        assertContactCreationError(ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = "regression")
    public void testAddContact_InvalidPhoneFormat() {
        ContactLombok contact = TestDataFactoryContact.invalidPhoneFormat();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        assertContactCreationError(ErrorMessages.INVALID_PHONE);
    }

    @Test(groups = "regression")
    public void testAddContact_TooLongFields() {
        ContactLombok contact = TestDataFactoryContact.tooLongFields();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        assertContactCreationError(ErrorMessages.INVALID_INPUT_TOLONG);
    }

    @Test(groups = "regression")
    public void testAddContact_SpecialCharacters() {
        ContactLombok contact = TestDataFactoryContact.invalidFieldsWithSpecialCharacters();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
        assertContactCreationError(ErrorMessages.INVALID_INPUT);
    }
}
