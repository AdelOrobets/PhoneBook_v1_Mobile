package ui_mobile_tests;

import config.AppiumConfig;
import dto.ContactDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import screens.*;
import utils.TestNGListener;

import java.util.List;

@Listeners(TestNGListener.class)
public class DeleteContactsTests extends AppiumConfig {

    public static final Logger logger = LoggerFactory.getLogger(DeleteContactsTests.class);

    @BeforeMethod(alwaysRun = true)
    public void precondition() {
        new SplashScreen(driver).goToAuthenticationScreen();
        new BaseScreen(driver).registrationValidUser();
        new BaseScreen(driver).addNewValidContact();
        new BaseScreen(driver).addNewValidContact();
    }

    // Positive tests
    @Test(groups = {"smoke", "regression"})
    public void testSuccessful_ContactDelete() {
        ContactListScreen contactListScreen = new ContactListScreen(driver);
        List<ContactDto> contactsBefore = contactListScreen.getContactDtoList();
        logger.info("ContactsList before delete:\n {}", contactsBefore);
        ContactDto contactToDelete = contactsBefore.get(0);
        logger.info("Deleting contact:\n {}", contactToDelete);

        contactListScreen.swipeToRightForDeleteFirstContact();
        contactListScreen.clickDeleteContact();

        List<ContactDto> contactsAfter = new ContactListScreen(driver).getContactDtoList();
        logger.info("ContactsList after delete:\n {}", contactsAfter);
        Assert.assertFalse(contactsAfter.contains(contactToDelete),
                "Contact was not deleted: " + contactToDelete);
    }

    @Test(groups = {"regression"})
    public void testContactNotDeleted_ifClickCancel_afterSwipe() {
        ContactListScreen contactListScreen = new ContactListScreen(driver);
        List<ContactDto> contactsBefore = contactListScreen.getContactDtoList();
        logger.info("ContactsList before:\n {}", contactsBefore);
        ContactDto contactToKeep = contactsBefore.get(0);
        logger.info("Will swipe, but NOT delete this contact:\n {}", contactToKeep);

        contactListScreen.swipeToRightForDeleteFirstContact();
        contactListScreen.clickDeleteCancel();

        List<ContactDto> contactsAfter = new ContactListScreen(driver).getContactDtoList();
        Assert.assertTrue(contactsAfter.contains(contactToKeep),
                "Contact was deleted: " + contactToKeep);
    }
}
