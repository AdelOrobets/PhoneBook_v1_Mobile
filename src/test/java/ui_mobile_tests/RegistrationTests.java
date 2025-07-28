package ui_mobile_tests;

import config.AppiumConfig;
import dto.UserLombok;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import screens.*;
import utils.ErrorMessages;
import utils.TestDataFactoryUser;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class RegistrationTests extends AppiumConfig {

    AuthenticationScreen authenticationScreen;
    ContactListScreen contactListScreen;

    private static final Logger logger = LoggerFactory.getLogger(RegistrationTests.class);

    @BeforeMethod(alwaysRun = true)
    public void goToAuthScreen(){
        new SplashScreen(driver).goToAuthenticationScreen();
    }

    private void registerUser(UserLombok user) {
        authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.registerUser(user);
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

    // Positive test
    @Test(groups = {"smoke", "regression"})
    public void testSuccessfulRegistration() {
        UserLombok user = TestDataFactoryUser.validUser();
        registerUser(user);
        Assert.assertTrue(new ContactListScreen(driver).isContactListDisplayed(),
                "Registration failed: Contact list screen not shown");
    }

    // Negative tests
    @Test(groups = "regression")
    public void testNegative_UserAlreadyExist() {
        UserLombok user = TestDataFactoryUser.validUser();
        // First registration
        registerUser(user);
        contactListScreen = new ContactListScreen(driver);
        if (contactListScreen.isContactListDisplayed()) {
            contactListScreen.logout();
        }
        // Second registration
        registerUser(user);
        verifyErrorMessage(ErrorMessages.USER_ALREADY_EXISTS);
    }

    @Test(groups = "regression")
    public void testNegative_emptyUsername() {
        UserLombok invalidUser = TestDataFactoryUser.userWithoutEmail();
        registerUser(invalidUser);
       verifyErrorMessage(ErrorMessages.EMAIL_REQUIRED);
    }

    /**
    BUG: When registering with an empty password,
    the user should remain on the registration screen
    and the application should display an alert or error: "Password is required"
    However, the alert does not appear and the application simply closes
     */
    @Test(groups = "regression")
    public void testNegative_emptyPassword() {
        UserLombok user = TestDataFactoryUser.userWithoutPassword();
        registerUser(user);
        verifyErrorMessage(ErrorMessages.PASSWORD_REQUIRED);
    }

    @Test(groups = "regression")
    public void testNegative_invalidUsernameFormat() {
        UserLombok invalidUser = TestDataFactoryUser.invalidEmailNoAtSymbol();
        registerUser(invalidUser);
        verifyErrorMessage(ErrorMessages.INVALID_EMAIL);
    }

    /**
    BUG: The system successfully allows registration with an invalid email
    that is missing a domain extension (e.g., .com)
     */
    @Test(groups = "regression")
    public void testNegative_invalidUsernameDomain() {
        UserLombok invalidUser = TestDataFactoryUser.invalidEmailNoDomain();
        registerUser(invalidUser);
        verifyErrorMessage(ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = "regression")
    public void testNegative_invalidUsername_withSpace() {
        UserLombok invalidUser = TestDataFactoryUser.invalidEmailWithSpace();
        registerUser(invalidUser);
        verifyErrorMessage(ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = "regression")
    public void testNegative_invalidPasswordShort() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordTooShort();
        registerUser(invalidUser);
        verifyErrorMessage(ErrorMessages.PASSWORD_TOO_SHORT);
    }

    /**
     BUG: According to requirements,
     password should be between 8 and 15 characters.
     Expected: Registration should fail when password length > 15
     Actual: Registration succeeds with password length > 15
     */
    @Test(groups = "regression")
    public void testNegative_invalidPasswordLong() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordTooLong();
        registerUser(invalidUser);
        verifyErrorMessage(ErrorMessages.PASSWORD_TOO_LONG);
    }

    @Test(groups = "regression")
    public void testNegative_invalidPasswordNoDigit() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordNoDigit();
        registerUser(invalidUser);
        verifyErrorMessage(ErrorMessages.PASSWORD_NO_DIGIT);
    }

    @Test(groups = "regression")
    public void testNegative_invalidPasswordNoSymbol() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordNoSymbol();
        registerUser(invalidUser);
        verifyErrorMessage(ErrorMessages.PASSWORD_NO_SYMBOL);
    }
}
