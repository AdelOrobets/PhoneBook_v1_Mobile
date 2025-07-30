package screens;

import dto.ContactLombok;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditContactScreen extends BaseScreen {

    public static final Logger logger = LoggerFactory.getLogger(EditContactScreen.class);

    public EditContactScreen(AppiumDriver driver) {
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

    @FindBy(id = "com.sheygam.contactapp:id/updateBtn")
    WebElement btnUpdate;

    public void editContact(ContactLombok contact) {
        logger.info("Name: {}", contact.getName());
        waitUntilElementIsVisible(inputName);
        enterText(inputName, contact.getName());
        logger.info("LastName: {}", contact.getLastName());
        enterText(inputLastName, contact.getLastName());
        logger.info("Email: {}", contact.getEmail());
        enterText(inputEmail, contact.getEmail());
        logger.info("Phone: {}", contact.getPhone());
        enterText(inputPhone, contact.getPhone());
        logger.info("Address: {}", contact.getAddress());
        enterText(inputAddress, contact.getAddress());
        logger.info("Description: {}", contact.getDescription());
        enterText(inputDescription, contact.getDescription());
        clickWhenReady(btnUpdate);
        new ContactListScreen(driver);
    }
}

