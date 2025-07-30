package config;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.qameta.allure.Attachment;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.PropertyReader;
import utils.TestNGListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@Listeners({AllureTestNg.class, TestNGListener.class})
public abstract class AppiumConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppiumConfig.class);
    private static final ThreadLocal<AppiumDriver> driverThread = new ThreadLocal<>();

    public AppiumDriver getDriver() {
        return driverThread.get();
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({
            "platformName",
            "deviceName",
            "platformVersion",
            "appPackage",
            "appActivity",
            "automationName",
            "appiumServerURL"
    })
    public void setup() {
        UiAutomator2Options options = new UiAutomator2Options();

        options.setPlatformName(PropertyReader.get("platformName"));
        options.setDeviceName(PropertyReader.get("deviceName"));
        options.setPlatformVersion(PropertyReader.get("platformVersion"));
        options.setAppPackage(PropertyReader.get("appPackage"));
        options.setAppActivity(PropertyReader.get("appActivity"));
        options.setAutomationName(PropertyReader.get("automationName"));

        // troubleshooting logcat/ADB issues
        options.setNoReset(false);
        options.setFullReset(false);
        options.setNewCommandTimeout(Duration.ofSeconds(120));

        // Fix Logcat 255 + Stability
        options.setCapability("clearDeviceLogsOnStart", true);
        options.setCapability("ignoreHiddenApiPolicyError", true);
        options.setCapability("disableWindowAnimation", true);
        options.setCapability("autoGrantPermissions", true);

        try {
            logger.info("Driver started on thread: {}", Thread.currentThread().getId());
            AppiumDriver driver = new AndroidDriver(new URL(PropertyReader.get("appiumServerURL")), options);
            driverThread.set(driver);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed Appium server URL", e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        AppiumDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver quit on thread: {}", Thread.currentThread().getId());
            } catch (Exception e) {
                logger.error("Driver quit failed: app may have crashed", e);
            } finally {
                driverThread.remove();
            }
        }
    }
}