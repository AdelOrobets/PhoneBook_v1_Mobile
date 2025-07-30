package screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplashScreen extends BaseScreen {

    public SplashScreen(AppiumDriver driver) {
        super(driver);
    }

    private static final Logger logger = LoggerFactory.getLogger(SplashScreen.class);

    @FindBy(id = "com.sheygam.contactapp:id/version_text")
    WebElement versionText;

    public boolean validateVersionApp(String expectedVersion) {
        logger.info("SplashScreen version expected: '{}'", expectedVersion);

        try {
            String actualVersion = waitForVisibility(versionText, 10).getText();
            logger.info("Actual version: '{}'", actualVersion);

            boolean result = actualVersion.equals(expectedVersion);
            if (!result) {
                logger.warn("Version mismatch! Expected: '{}', but was: '{}'", expectedVersion, actualVersion);
            }
            return result;

        } catch (Exception e) {
            logger.error("Failed to validate version text due to: {}", e.getMessage());
            return false;
        }
    }
}