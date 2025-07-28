package screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ContactListScreen extends BaseScreen {

    public ContactListScreen(AppiumDriver driver) {
        super(driver);
    }

    @FindBy(id = "com.sheygam.contactapp:id/add_contact_btn")
    WebElement btnAddContact;

    @FindBy(xpath = "//android.widget.ImageView[@content-desc='More options']")
    WebElement btnMenu;

    @FindBy(xpath = "//android.widget.TextView[@text='Logout']")
    WebElement btnLogout;

    @FindBy(id = "com.sheygam.contactapp:id/recyclerView")
    List<WebElement> contacts;


    public boolean isContactListDisplayed() {
        waitUntilElementIsVisible(btnAddContact);
        return isElementDisplayed(btnAddContact);
    }

    public void clickAddContact() {
        btnAddContact.click();
    }

    public boolean isContactPresent(String name, String phone) {
        for (WebElement contact : contacts) {
            String contactName = contact.findElement(By.id("com.sheygam.contactapp:id/rowName")).getText();
            String contactPhone = contact.findElement(By.id("com.sheygam.contactapp:id/rowPhone")).getText();

            if (contactName.equals(name) && contactPhone.equals(phone)) {
                return true;
            }
        }
        return false;
    }

    public AuthenticationScreen logout() {
        clickWhenReady(btnMenu);
        clickWhenReady(btnLogout);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        wait.until(ExpectedConditions.visibilityOf(authenticationScreen.inputEmail));
        return authenticationScreen;
    }
}