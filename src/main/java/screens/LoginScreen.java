package screens;

import dto.UserLombok;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginScreen extends BaseScreen {

    private static final Logger logger = LoggerFactory.getLogger(LoginScreen.class);

    public LoginScreen(AppiumDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//*[@resource-id='com.sheygam.contactapp:id/inputEmail']")
    WebElement inputEmail;

    @FindBy(xpath = "//*[@resource-id='com.sheygam.contactapp:id/inputPassword']")
    WebElement inputPassword;

    @FindBy(id = "com.sheygam.contactapp:id/regBtn")
    WebElement btnRegistration;

    @FindBy(id = "com.sheygam.contactapp:id/loginBtn")
    WebElement btnLogin;

    public void login(String email, String password) {
        logger.info("Login Email: {}", email);
        enterText(inputEmail, email);
        enterText(inputPassword, password);
        logger.info("Login Password: {}", password);
        clickWhenReady(btnLogin);
    }

    public void register(String email, String password) {
        logger.info("Reg Email: {}", email);
        enterText(inputEmail, email);
        enterText(inputPassword, password);
        logger.info("Reg Password: {}", password);
        clickWhenReady(btnRegistration);
    }

    public void registerUser(UserLombok user) {
        register(user.getUsername(), user.getPassword());
    }
}
