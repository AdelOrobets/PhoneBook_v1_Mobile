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
        new SplashScreen(driver).goToAuthenticationScreen();
    }

    // Helper
    private UserLombok registerAndLogout(UserLombok user) {
        new AuthenticationScreen(driver).registerUser(user);
        new ContactListScreen(driver).logout();
        return user;
    }

    private void login(String email, String password) {
        new AuthenticationScreen(driver).login(email, password);
    }

    // Positive tests
    @Test(retryAnalyzer = utils.RetryAnalyzer.class, groups = {"smoke", "regression"})
    public void testLogin_afterSuccessfulRegistration() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        login(user.getUsername(), user.getPassword());
        Assert.assertTrue(new ContactListScreen(driver).isContactListDisplayed(),
                "Login failed");
    }

    /**
    Bug: A user registered with an email in uppercase,
    but cannot log in using the same email in uppercase.
    */
    @Test(groups = "regression")
    public void testLogin_withUpperCaseEmail() {
        UserLombok user = TestDataFactoryUser.validUser();
        String upperEmail = user.getUsername().toUpperCase();
        registerAndLogout(new UserLombok(upperEmail, user.getPassword()));
        login(upperEmail, user.getPassword());
        Assert.assertTrue(new ContactListScreen(driver).isContactListDisplayed(),
                "Login failed");
    }

    @Test(retryAnalyzer = utils.RetryAnalyzer.class, groups = "regression")
    public void testLogin_withLowerCaseEmail_afterUpperCaseRegistration() {
        UserLombok user = TestDataFactoryUser.validUser();
        String upperEmail = user.getUsername().toUpperCase();
        registerAndLogout(new UserLombok(upperEmail, user.getPassword()));
        String lowerEmail = upperEmail.toLowerCase();
        login(lowerEmail, user.getPassword());
        Assert.assertTrue(new ContactListScreen(driver).isContactListDisplayed(),
                "Login failed");
    }

    // Helper
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

    // Negative tests
    @Test(groups = "regression")
    public void testLogin_withUnregisteredUser() {
        UserLombok user = TestDataFactoryUser.validUser();
        login(user.getUsername(), user.getPassword());
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withWrongPassword() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String wrongPassword = RandomUtils.generatePassword(8);
        login(user.getUsername(), wrongPassword);
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withEmptyUsername() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        login("", user.getPassword());
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withEmptyPassword() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        login(user.getUsername(), "");
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withInvalidEmail_NoAtSymbol() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String emailNoAt = user.getUsername().replace("@", "");
        login(emailNoAt, user.getPassword());
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withInvalidEmail_NoDomain() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String emailNoDomain = user.getUsername().replaceAll("\\.\\w+$", "");
        login(emailNoDomain, user.getPassword());
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withEmailContainingSpace_first() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        login(" " + user.getUsername(), user.getPassword());
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withEmailContainingSpace_last() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        login(user.getUsername() + " ", user.getPassword());
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withShortPassword() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String shortPassword = RandomUtils.generatePassword(3);
        login(user.getUsername(), shortPassword);
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withLongPassword() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String tooLongPassword = user.getPassword() + RandomUtils.generatePassword(10);
        login(user.getUsername(), tooLongPassword);
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withPasswordNoDigit() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String passwordNoDigit = user.getPassword().replaceAll("\\d", "");
        login(user.getUsername(), passwordNoDigit);
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withPasswordNoSymbol() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String passwordNoSymbol = user.getPassword().replaceAll("[^a-zA-Z0-9]", "");
        login(user.getUsername(), passwordNoSymbol);
        verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }
} 
