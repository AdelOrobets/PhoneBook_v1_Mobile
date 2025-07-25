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

    private static final Logger logger = LoggerFactory.getLogger(RegistrationTests.class);

    @BeforeMethod(alwaysRun = true)
    public void goToAuthScreen(){
        new SplashScreen(driver).goToReg();
    }

    // Positive test
    @Test(groups = {"smoke", "regression"})
    public void testSuccessfulRegistration() {
        UserLombok user = TestDataFactoryUser.validUser();
        new LoginScreen(driver).registerUser(user);
        Assert.assertTrue(new ContactListScreen(driver).isContactListDisplayed(),
                "Registration failed: Contact list screen not shown");
    }

    // Helper
    private void assertRegistrationFailure(UserLombok user, String expectedMsg) {
        new LoginScreen(driver).registerUser(user);
        ErrorScreen errorScreen = new ErrorScreen(driver);
        String actualMsg = errorScreen.getErrorMessage();
        logger.info("Actual error message: '{}'", actualMsg);
        logger.info("Expected to contain: '{}'", expectedMsg);

        Assert.assertTrue(actualMsg.contains(expectedMsg), "Expected error message to contain: "
                + expectedMsg + ", but got: '" + actualMsg);
        errorScreen.closeAlert();
    }

    // Negative tests
    @Test(groups = "regression")
    public void testNegative_UserAlreadyExist() {
        UserLombok user = TestDataFactoryUser.validUser();
        // First registration
        new LoginScreen(driver).registerUser(user);
        new ContactListScreen(driver).logout();
        // Second registration
        assertRegistrationFailure(user, ErrorMessages.USER_ALREADY_EXISTS);
    }

    @Test(groups = "regression")
    public void testNegative_emptyUsername() {
        UserLombok invalidUser = TestDataFactoryUser.userWithoutEmail();
        assertRegistrationFailure(invalidUser, ErrorMessages.EMAIL_REQUIRED);
    }

    @Test(groups = "regression")
    public void testNegative_emptyPassword() {
        UserLombok invalidUser = TestDataFactoryUser.userWithoutPassword();
        assertRegistrationFailure(invalidUser, ErrorMessages.PASSWORD_REQUIRED);
    }

    @Test(groups = "regression")
    public void testNegative_invalidUsernameFormat() {
        UserLombok invalidUser = TestDataFactoryUser.invalidEmailNoAtSymbol();
        assertRegistrationFailure(invalidUser, ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = "regression")
    public void testNegative_invalidUsernameDomain() {
        UserLombok invalidUser = TestDataFactoryUser.invalidEmailNoDomain();
        assertRegistrationFailure(invalidUser, ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = "regression")
    public void testNegative_invalidUsername_withSpace() {
        UserLombok invalidUser = TestDataFactoryUser.invalidEmailWithSpace();
        assertRegistrationFailure(invalidUser, ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = "regression")
    public void testNegative_invalidPasswordShort() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordTooShort();
        assertRegistrationFailure(invalidUser, ErrorMessages.PASSWORD_TOO_SHORT);
    }

    @Test(groups = "regression")
    public void testNegative_invalidPasswordLong() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordTooLong();
        assertRegistrationFailure(invalidUser, ErrorMessages.PASSWORD_TOO_LONG);
    }

    @Test(groups = "regression")
    public void testNegative_invalidPasswordNoDigit() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordNoDigit();
        assertRegistrationFailure(invalidUser, ErrorMessages.PASSWORD_NO_DIGIT);
    }

    @Test(groups = "regression")
    public void testNegative_invalidPasswordNoSymbol() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordNoSymbol();
        assertRegistrationFailure(invalidUser, ErrorMessages.PASSWORD_NO_SYMBOL);
    }
}
