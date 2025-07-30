package screens;

import dto.ContactLombok;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AddContactScreen extends BaseScreen {

    public AddContactScreen(AppiumDriver driver){
        super(driver);
    }

    @FindBy(id = "com.sheygam.contactapp:id/inputName")
    WebElement inputName;

    @FindBy(id = "com.sheygam.contactapp:id/inputLastName")
    WebElement inputLastName;

    @FindBy(id = "com.sheygam.contactapp:id/inputEmail")
    WebElement inputEmail;

    @FindBy(id = "com.sheygam.contactapp:id/inputPhone")
    WebElement inputPhone;

    @FindBy(id = "com.sheygam.contactapp:id/inputAddress")
    WebElement inputAddress;

    @FindBy(id = "com.sheygam.contactapp:id/inputDesc")
    WebElement inputDescription;

    @FindBy(xpath = "//*[@text='CREATE']")
    WebElement btnCreate;

    public AddContactScreen fillContactForm(ContactLombok contact) {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOf(inputName));
        inputName.sendKeys(contact.getName());
        inputLastName.sendKeys(contact.getLastName());
        inputEmail.sendKeys(contact.getEmail());
        inputPhone.sendKeys(contact.getPhone());
        inputAddress.sendKeys(contact.getAddress());
        inputDescription.sendKeys(contact.getDescription());
        return this;
    }

    public void clickCreateContact() {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.elementToBeClickable(btnCreate));
        btnCreate.click();
    }
}
