package ui_mobile_tests;

import config.AppiumConfig;
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
import utils.TestDataFactoryUser;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class RegistrationTests extends AppiumConfig {

    AuthenticationScreen authenticationScreen;
    ContactListScreen contactListScreen;

    @BeforeMethod(alwaysRun = true)
    public void goToAuthScreen(){
        new AuthenticationScreen(getDriver());
    }

    private void registerUser(UserLombok user) {
        authenticationScreen = new AuthenticationScreen(getDriver());
        authenticationScreen.registerUser(user);
    }

    // Positive test
    @AllureId("UR_001")
    @Story("Authentication")
    @Test(groups = {"smoke", "regression"})
    public void testSuccessfulRegistration() {
        UserLombok user = TestDataFactoryUser.validUser();
        registerUser(user);
        Assert.assertTrue(new ContactListScreen(getDriver()).isContactListScreenDisplayed(),
                "Registration failed: Contact list screen not shown");
    }

    // Negative tests
    @Test(groups = "regression")
    public void testRegistrationNegative_UserAlreadyExist() {
        UserLombok user = TestDataFactoryUser.validUser();
        // First registration
        registerUser(user);
        contactListScreen = new ContactListScreen(getDriver());
        if (contactListScreen.isContactListScreenDisplayed()) {
            contactListScreen.logout();
        }
        // Second registration
        registerUser(user);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.USER_ALREADY_EXISTS);
    }

    @Test(groups = "regression")
    public void testRegistrationNegative_emptyUsername() {
        UserLombok invalidUser = TestDataFactoryUser.userWithoutEmail();
        registerUser(invalidUser);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.EMAIL_REQUIRED);
    }

    @Issue("BUG: UR_001. No alert shown and app closes when registering with empty password")
    @Test(groups = "regression")
    public void testRegistrationNegative_emptyPassword() {
        UserLombok user = TestDataFactoryUser.userWithoutPassword();
        registerUser(user);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.PASSWORD_REQUIRED);
    }

    @Test(groups = "regression")
    public void testRegistrationNegative_invalidUsernameFormat() {
        UserLombok invalidUser = TestDataFactoryUser.invalidEmailNoAtSymbol();
        registerUser(invalidUser);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_EMAIL);
    }

    @Issue("BUG: UR_002. Registration allowed with email missing domain extension")
    @Test(groups = "regression")
    public void testRegistrationNegative_invalidUsernameDomain() {
        UserLombok invalidUser = TestDataFactoryUser.invalidEmailNoDomain();
        registerUser(invalidUser);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = "regression")
    public void testRegistrationNegative_invalidUsername_withSpace() {
        UserLombok invalidUser = TestDataFactoryUser.invalidEmailWithSpace();
        registerUser(invalidUser);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.INVALID_EMAIL);
    }

    @Test(groups = "regression")
    public void testRegistrationNegative_invalidPasswordShort() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordTooShort();
        registerUser(invalidUser);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.PASSWORD_TOO_SHORT);
    }

    @Issue("BUG: UR_003. Registration succeeds with password longer than 15 characters")
    @Test(groups = "regression")
    public void testRegistrationNegative_invalidPasswordLong() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordTooLong();
        registerUser(invalidUser);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.PASSWORD_TOO_LONG);
    }

    @Test(groups = "regression")
    public void testRegistrationNegative_invalidPasswordNoDigit() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordNoDigit();
        registerUser(invalidUser);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.PASSWORD_NO_DIGIT);
    }

    @Test(groups = "regression")
    public void testRegistrationNegative_invalidPasswordNoSymbol() {
        UserLombok invalidUser = TestDataFactoryUser.invalidPasswordNoSymbol();
        registerUser(invalidUser);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.PASSWORD_NO_SYMBOL);
    }
}
