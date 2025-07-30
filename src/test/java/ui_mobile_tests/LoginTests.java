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
import utils.RandomUtils;
import utils.TestDataFactoryUser;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class LoginTests extends AppiumConfig {

    @BeforeMethod(alwaysRun = true)
    public void goToAuthScreen(){
        new AuthenticationScreen(getDriver());
    }

    private UserLombok registerAndLogout(UserLombok user) {
        new AuthenticationScreen(getDriver()).registerUser(user);
        new ContactListScreen(getDriver()).logout();
        return user;
    }

    private void login(String email, String password) {
        new AuthenticationScreen(getDriver()).login(email, password);
    }

    // Positive tests
    @AllureId("UL_001")
    @Story("Authentication")
    @Test(retryAnalyzer = utils.RetryAnalyzer.class, groups = {"smoke", "regression"})
    public void testLogin_afterSuccessfulRegistration() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        login(user.getUsername(), user.getPassword());
        Assert.assertTrue(new ContactListScreen(getDriver()).isContactListScreenDisplayed(),
                "Login failed");
    }

    @Issue("BUG: UL_001. Cannot login after registering with the same uppercase email")
    @Test(groups = "regression")
    public void testLogin_withUpperCaseEmail() {
        UserLombok user = TestDataFactoryUser.validUser();
        String upperEmail = user.getUsername().toUpperCase();
        registerAndLogout(new UserLombok(upperEmail, user.getPassword()));
        login(upperEmail, user.getPassword());
        Assert.assertTrue(new ContactListScreen(getDriver()).isContactListScreenDisplayed(),
                "Login failed");
    }

    @Test(retryAnalyzer = utils.RetryAnalyzer.class, groups = "regression")
    public void testLogin_withLowerCaseEmail_afterUpperCaseRegistration() {
        UserLombok user = TestDataFactoryUser.validUser();
        String upperEmail = user.getUsername().toUpperCase();
        registerAndLogout(new UserLombok(upperEmail, user.getPassword()));
        String lowerEmail = upperEmail.toLowerCase();
        login(lowerEmail, user.getPassword());
        Assert.assertTrue(new ContactListScreen(getDriver()).isContactListScreenDisplayed(),
                "Login failed");
    }

    // Negative tests
    @Test(groups = "regression")
    public void testLogin_withUnregisteredUser() {
        UserLombok user = TestDataFactoryUser.validUser();
        login(user.getUsername(), user.getPassword());
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withWrongPassword() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String wrongPassword = RandomUtils.generatePassword(8);
        login(user.getUsername(), wrongPassword);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withEmptyUsername() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        login("", user.getPassword());
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withEmptyPassword() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        login(user.getUsername(), "");
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withInvalidEmail_NoAtSymbol() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String emailNoAt = user.getUsername().replace("@", "");
        login(emailNoAt, user.getPassword());
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withInvalidEmail_NoDomain() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String emailNoDomain = user.getUsername().replaceAll("\\.\\w+$", "");
        login(emailNoDomain, user.getPassword());
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withEmailContainingSpace_first() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        login(" " + user.getUsername(), user.getPassword());
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withEmailContainingSpace_last() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        login(user.getUsername() + " ", user.getPassword());
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withShortPassword() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String shortPassword = RandomUtils.generatePassword(3);
        login(user.getUsername(), shortPassword);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withLongPassword() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String tooLongPassword = user.getPassword() + RandomUtils.generatePassword(10);
        login(user.getUsername(), tooLongPassword);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withPasswordNoDigit() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String passwordNoDigit = user.getPassword().replaceAll("\\d", "");
        login(user.getUsername(), passwordNoDigit);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }

    @Test(groups = "regression")
    public void testLogin_withPasswordNoSymbol() {
        UserLombok user = registerAndLogout(TestDataFactoryUser.validUser());
        String passwordNoSymbol = user.getPassword().replaceAll("[^a-zA-Z0-9]", "");
        login(user.getUsername(), passwordNoSymbol);
        new ErrorScreen(getDriver()).verifyErrorMessage(ErrorMessages.LOGIN_FAILED);
    }
} 
