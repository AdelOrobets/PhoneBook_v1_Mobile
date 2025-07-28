package screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class ContactListScreen extends BaseScreen {

    public static final Logger logger = LoggerFactory.getLogger(ContactListScreen.class);

    public ContactListScreen(AppiumDriver driver) {
        super(driver);
    }

    @FindBy(id = "com.sheygam.contactapp:id/add_contact_btn")
    WebElement btnAddContact;

    @FindBy(xpath = "//android.widget.ImageView[@content-desc='More options']")
    WebElement btnMenu;

    @FindBy(xpath = "//android.widget.TextView[@text='Logout']")
    WebElement btnLogout;

    @FindBy(id = "com.sheygam.contactapp:id/rowContainer")
    List<WebElement> contacts;

    private static final By CONTACTS_LOCATOR = By.id("com.sheygam.contactapp:id/rowContainer");

    public List<WebElement> getContacts() {
        return driver.findElements(CONTACTS_LOCATOR);
    }

    public boolean isContactListScreenDisplayed() {
        waitUntilElementIsVisible(btnAddContact);
        return isElementDisplayed(btnAddContact);
    }

    public void clickAddContact() {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.elementToBeClickable(btnAddContact));
        btnAddContact.click();
    }

    public boolean isContactPresent(String name, String phone) {
        try {
            wait.withTimeout(Duration.ofSeconds(10))
                    .until(driver -> {
                        List<WebElement> contacts = getContacts();
                        for (WebElement contact : contacts) {
                            String contactName = contact.findElement
                                    (By.id("com.sheygam.contactapp:id/rowName")).getText();
                            String contactPhone = contact.findElement
                                    (By.id("com.sheygam.contactapp:id/rowPhone")).getText();
                            logger.info("Update Contact Name: " + contactName + ", Phone: " + contactPhone);

                            if (contactName.equals(name) && contactPhone.equals(phone)) {
                                return true;
                            }
                        }
                        return false;
                    });
        } catch (TimeoutException e) {
            logger.error("Contact with name '{}' and phone '{}' not visible", name, phone);
            return false;
        }
        return true;
    }

    public AuthenticationScreen logout() {
        clickWhenReady(btnMenu);
        clickWhenReady(btnLogout);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        wait.until(ExpectedConditions.visibilityOf(authenticationScreen.inputEmail));
        return authenticationScreen;
    }

    public ContactListScreen swipeToLeftForEditFirstContact() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(contacts));
        } catch (TimeoutException e) {
            logger.error("Contacts not visible after timeout");
            return this;
        }
        if (contacts.isEmpty()) {
            logger.warn("No contacts to swipe");
            return this;
        }
        WebElement contact = getContacts().get(0);
        int contactStartX = contact.getLocation().getX();
        int contactWidth = contact.getSize().getWidth();
        int startX = contactStartX + contactWidth - 20;
        int endX = contactStartX + 20;
        int y = contact.getLocation().getY() + contact.getSize().getHeight() / 2;

        logger.info("Swiping from (" + startX + "," + y + ") to (" + endX + "," + y + ")");

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, y));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, y));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(swipe));
        return this;
    }
}