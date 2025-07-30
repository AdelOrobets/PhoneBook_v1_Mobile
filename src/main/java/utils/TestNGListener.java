package utils;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestNGListener implements ITestListener {

    Logger logger = LoggerFactory.getLogger(TestNGListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("[TEST START] {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("[TEST PASSED] {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.info("[TEST SKIPPED] {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.info("[TEST FAILED WITHIN SUCCESS PERCENTAGE] {}", result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("[SUITE START] {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("[SUITE END] {}", context.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.info("[TEST FAILED] {}", result.getMethod().getMethodName(), result.getThrowable());

        Object testInstance = result.getInstance();
        try {
            AppiumDriver driver = (AppiumDriver) testInstance
                    .getClass()
                    .getMethod("getDriver")
                    .invoke(testInstance);
            if (driver != null) {
                attachScreenshot(driver);
            } else {
                logger.warn("Driver is null on test failure: {}", result.getMethod().getMethodName());
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot on test failure: {}", result.getName(), e);
        }
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    public byte[] attachScreenshot(AppiumDriver driver) {
        return driver.getScreenshotAs(OutputType.BYTES);
    }
}