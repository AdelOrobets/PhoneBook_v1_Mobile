package ui_mobile_tests;

import config.AppiumConfig;
import io.qameta.allure.AllureId;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import screens.SplashScreen;
import utils.TestNGListener;


@Listeners(TestNGListener.class)
public class SplashTests extends AppiumConfig {

    @AllureId("VV_01")
    @Story("Version validation")
    @Test(groups = {"smoke", "regression"})
    public void splashVersionTest() {
        String expectedVersion = "Version 1.0.0";
        boolean result = new SplashScreen(getDriver()).validateVersionApp(expectedVersion);
        Assert.assertTrue(result, "Version on Splash Screen is not as expected");
    }
}