package screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ErrorScreen extends BaseScreen {

    public ErrorScreen(AppiumDriver driver) {
        super(driver);
    }

    @FindBy(id = "android:id/message")
    WebElement errorText;

    @FindBy(id = "android:id/button1")
    WebElement btnOk;


    public String getErrorMessage() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(errorText));
        return errorText.getText();
    }

    public void closeAlert() {
        if (btnOk.isDisplayed()) {
            btnOk.click();
        }
    }
}
