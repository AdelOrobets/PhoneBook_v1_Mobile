package screens;

import dto.ContactLombok;
import dto.UserLombok;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestDataFactoryContact;
import utils.TestDataFactoryUser;

import java.time.Duration;

public class BaseScreen {
    protected AppiumDriver driver;
    protected WebDriverWait wait;

    public BaseScreen(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    protected WebElement waitForVisibility(WebElement element, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOf(element));
    }

    protected void enterText(WebElement element, String text) {
        waitForVisibility(element, 20);
        element.clear();
        element.sendKeys(text);
    }

    protected void clickWhenReady(WebElement element) {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitUntilElementIsVisible(WebElement element) {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOf(element));
    }

    public void registrationValidUser() {
        UserLombok user = TestDataFactoryUser.validUser();
        new AuthenticationScreen(driver).registerUser(user);
    }

    public void addNewValidContact() {
        ContactLombok contact = TestDataFactoryContact.validContact();
        new ContactListScreen(driver).clickAddContact();
        new AddContactScreen(driver).fillContactForm(contact).clickCreateContact();
    }
}
