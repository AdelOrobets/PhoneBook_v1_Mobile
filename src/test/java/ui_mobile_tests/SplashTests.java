package ui_mobile_tests;

import config.AppiumConfig;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import screens.SplashScreen;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class SplashTests extends AppiumConfig {

    @Test
    public void splashVersionTest() {
        String expectedVersion = "Version 1.0.0";
        boolean result = new SplashScreen(driver).validateVersionApp(expectedVersion);
        Assert.assertTrue(result, "Version on splash screen is not as expected");
    }
}
