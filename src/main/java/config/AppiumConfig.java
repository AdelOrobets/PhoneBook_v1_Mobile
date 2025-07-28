package config;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class AppiumConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppiumConfig.class);

    protected static AppiumDriver driver;

    @BeforeMethod
    public void setup() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "Pixel_5");
        desiredCapabilities.setCapability("platformVersion", "8.0");
        desiredCapabilities.setCapability("appPackage", "com.sheygam.contactapp");
        desiredCapabilities.setCapability("appActivity", ".SplashActivity");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        String url = "http://localhost:4723";

        try {
            driver = new AndroidDriver(new URL(url), desiredCapabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                logger.error("Driver quit failed: app may have crashed", e);
            }
        }
    }
}