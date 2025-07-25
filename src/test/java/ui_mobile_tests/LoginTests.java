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
import utils.RandomUtils;
import utils.TestDataFactoryUser;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class LoginTests extends AppiumConfig {

    private static final Logger logger = LoggerFactory.getLogger(LoginTests.class);

    @BeforeMethod(alwaysRun = true)
    public void goToAuthScreen(){
        new SplashScreen(driver).goToLogin();
    }

    // Helper
    public UserLombok userRegistration() {
        UserLombok user = TestDataFactoryUser.validUser();
        new LoginScreen(driver).registerUser(user);
        new ContactListScreen(driver).logout();
        return user;
    }

    // Positive test
    @Test(retryAnalyzer = utils.RetryAnalyzer.class, groups = {"smoke", "regression"})
    public void testUserCanLoginAfterRegistration() {
        UserLombok validUser = userRegistration();
        new LoginScreen(driver).login(validUser.getUsername(), validUser.getPassword());
        Assert.assertTrue(new ContactListScreen(driver).isContactListDisplayed(), "Login failed");
    }

    // Helper
    private void assertLoginFailure(String expectedMsg) {
        ErrorScreen errorScreen = new ErrorScreen(driver);
        String actualMsg = errorScreen.getErrorMessage();
        logger.info("Actual error message: '{}'", actualMsg);
        logger.info("Expected to contain: '{}'", expectedMsg);

        Assert.assertTrue(actualMsg.contains(expectedMsg),
                "Expected message to contain: " + expectedMsg + ", but got: " + actualMsg);
        errorScreen.closeAlert();
    }


    // Negative tests
    @Test(retryAnalyzer = utils.RetryAnalyzer.class, groups = "regression")
    public void testUserLogin_uppercaseEmail() {
        UserLombok user = userRegistration();
        String upperEmail = user.getUsername().toUpperCase();
        new LoginScreen(driver).login(upperEmail, user.getPassword());
        Assert.assertTrue(new ContactListScreen(driver).isContactListDisplayed(),
                ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void loginNegativeTest_unregisteredUser() {
        UserLombok unregisteredUser = TestDataFactoryUser.validUser();
        new LoginScreen(driver).login(unregisteredUser.getUsername(), unregisteredUser.getPassword());
        assertLoginFailure(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void loginNegativeTest_wrongPassword() {
        UserLombok user = userRegistration();
        new LoginScreen(driver).login(user.getUsername(), RandomUtils.generatePassword(8));
        assertLoginFailure(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void loginNegativeTest_emptyUsername() {
        UserLombok user = userRegistration();
        String userName = "";
        new LoginScreen(driver).login(userName, user.getPassword());
        assertLoginFailure(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void loginNegativeTest_emptyPassword() {
        UserLombok user = userRegistration();
        String password = "";
        new LoginScreen(driver).login(user.getUsername(), password);
        assertLoginFailure(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void loginNegativeTest_invalidUsernameFormat() {
        UserLombok invalidUser = TestDataFactoryUser.invalidEmailNoAtSymbol();
        new LoginScreen(driver).login(invalidUser.getUsername(), invalidUser.getPassword());
        assertLoginFailure(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void loginNegativeTest_invalidUsernameDomain() {
        UserLombok invalidUser = TestDataFactoryUser.invalidEmailNoDomain();
        new LoginScreen(driver).login(invalidUser.getUsername(), invalidUser.getPassword());
        assertLoginFailure(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void loginNegativeTest_invalidUsername_withSpace() {
        UserLombok user = userRegistration();
        String space = " ";
        new LoginScreen(driver).login(user.getUsername() + space, user.getPassword());
        assertLoginFailure(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void loginNegativeTest_invalidPasswordShort() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordTooShort();
        new LoginScreen(driver).login(invalidUser.getUsername(), invalidUser.getPassword());
        assertLoginFailure(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void loginNegativeTest_invalidPasswordLong() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordTooLong();
        new LoginScreen(driver).login(invalidUser.getUsername(), invalidUser.getPassword());
        assertLoginFailure(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void loginNegativeTest_invalidPasswordNoDigit() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordNoDigit();
        new LoginScreen(driver).login(invalidUser.getUsername(), invalidUser.getPassword());
        assertLoginFailure(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void loginNegativeTest_invalidPasswordNoSymbol() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordNoSymbol();
        new LoginScreen(driver).login(invalidUser.getUsername(), invalidUser.getPassword());
        assertLoginFailure(ErrorMessages.LOGIN_FAILED);
    }
} 
