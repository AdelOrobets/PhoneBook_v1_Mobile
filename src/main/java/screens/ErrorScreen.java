package screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;

public class ErrorScreen extends BaseScreen {

    public static final Logger logger = LoggerFactory.getLogger(ErrorScreen.class);

    public ErrorScreen(AppiumDriver driver) {
        super(driver);
    }

    @FindBy(id = "android:id/message")
    WebElement errorText;

    @FindBy(id = "android:id/button1")
    WebElement btnOk;

    public String getErrorMessage() {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOf(errorText));
        return errorText.getText();
    }

    public void closeErrorMsg() {
        clickWhenReady(btnOk);
    }

    public void verifyErrorMessage(String expectedMsg) {
        ErrorScreen errorScreen = new ErrorScreen(driver);
        String actualMsg = errorScreen.getErrorMessage();
        logger.info("Actual error message: '{}'", actualMsg);
        logger.info("Expected to contain: '{}'", expectedMsg);

        Assert.assertTrue(actualMsg.contains(expectedMsg),
                "Expected error message to contain: " + expectedMsg +
                        ", but got: '" + actualMsg + "'");
        errorScreen.closeErrorMsg();
    }
}
