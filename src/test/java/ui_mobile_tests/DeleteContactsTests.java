package ui_mobile_tests;

import config.AppiumConfig;
import dto.ContactDto;
import dto.ContactLombok;
import dto.UserLombok;
import io.qameta.allure.AllureId;
import io.qameta.allure.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import screens.*;
import utils.TestDataFactoryContact;
import utils.TestDataFactoryUser;
import utils.TestNGListener;

import java.util.List;

@Listeners(TestNGListener.class)
public class DeleteContactsTests extends AppiumConfig {

    public static final Logger logger = LoggerFactory.getLogger(DeleteContactsTests.class);

    @BeforeMethod(alwaysRun = true)
    public void precondition() {
        UserLombok user = TestDataFactoryUser.validUser();
        new AuthenticationScreen(getDriver()).registerUser(user);
        addNewValidContact();
        addNewValidContact();
    }

    public void addNewValidContact() {
        ContactLombok contact = TestDataFactoryContact.validContact();
        new ContactListScreen(getDriver()).clickAddContact();
        new AddContactScreen(getDriver()).fillContactForm(contact);
        new AddContactScreen(getDriver()).clickCreateContact();
    }

    // Positive tests
    @AllureId("DC_001")
    @Story("Contact management")
    @Test(groups = {"smoke", "regression", "all_way"})
    public void testSuccessful_ContactDelete() {
        ContactListScreen contactListScreen = new ContactListScreen(getDriver());
        List<ContactDto> contactsBefore = contactListScreen.getContactDtoList();
        logger.info("ContactsList before delete:\n {}", contactsBefore);
        ContactDto contactToDelete = contactsBefore.get(0);
        logger.info("Deleting contact:\n {}", contactToDelete);

        contactListScreen.swipeToRightForDeleteFirstContact();
        contactListScreen.clickDeleteContact();

        List<ContactDto> contactsAfter = new ContactListScreen(getDriver()).getContactDtoList();
        logger.info("ContactsList after delete:\n {}", contactsAfter);
        Assert.assertFalse(contactsAfter.contains(contactToDelete),
                "Contact was not deleted: " + contactToDelete);
    }

    @AllureId("DC_002")
    @Story("Contact management")
    @Test(groups = {"smoke", "regression", "all_way"})
    public void testContactNotDeleted_ifClickCancel_afterSwipe() {
        ContactListScreen contactListScreen = new ContactListScreen(getDriver());
        List<ContactDto> contactsBefore = contactListScreen.getContactDtoList();
        logger.info("ContactsList before:\n {}", contactsBefore);
        ContactDto contactToKeep = contactsBefore.get(0);
        logger.info("Will swipe, but NOT delete this contact:\n {}", contactToKeep);

        contactListScreen.swipeToRightForDeleteFirstContact();
        contactListScreen.clickDeleteCancel();

        List<ContactDto> contactsAfter = new ContactListScreen(getDriver()).getContactDtoList();
        Assert.assertTrue(contactsAfter.contains(contactToKeep),
                "Contact was deleted: " + contactToKeep);
    }
}
