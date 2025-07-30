package screens;

import dto.ContactDto;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ContactListScreen extends BaseScreen {

    public static final Logger logger = LoggerFactory.getLogger(ContactListScreen.class);

    private static final By CONTACTS_LIST_LOCATOR = By.id("com.sheygam.contactapp:id/rowContainer");

    @FindBy(id = "com.sheygam.contactapp:id/add_contact_btn")
    WebElement btnAddContact;

    @FindBy(id = "android:id/button1")
    WebElement btnRemoveYes;

    @FindBy(id = "android:id/button2")
    WebElement btnCancel;

    @FindBy(xpath = "//android.widget.ImageView[@content-desc='More options']")
    WebElement btnMenu;

    @FindBy(xpath = "//android.widget.TextView[@text='Logout']")
    WebElement btnLogout;

    @FindBy(id = "com.sheygam.contactapp:id/rowContainer")
    List<WebElement> contactsList;

    public ContactListScreen(AppiumDriver driver) {
        super(driver);
    }

    public List<WebElement> getContacts() {
        return driver.findElements(CONTACTS_LIST_LOCATOR);
    }

    public boolean isContactListScreenDisplayed() {
        waitUntilElementIsVisible(btnAddContact);
        return isElementDisplayed(btnAddContact);
    }

    public void clickAddContact() {
        clickWhenReady(btnAddContact);
    }

    public void clickDeleteContact() {
        clickWhenReady(btnRemoveYes);
    }

    public void clickDeleteCancel() {
        clickWhenReady(btnCancel);
    }

    public boolean isContactPresent(String name, String phone) {
        try {
            wait.withTimeout(Duration.ofSeconds(30))
                    .until(driver -> {
                        List<WebElement> contacts = getContacts();
                        for (WebElement contact : contacts) {
                            String contactFullName = contact.findElement
                                    (By.id("com.sheygam.contactapp:id/rowName")).getText();
                            String contactPhone = contact.findElement
                                    (By.id("com.sheygam.contactapp:id/rowPhone")).getText();
                            logger.info("New Contact Name: {}, Phone: {}", contactFullName, contactPhone);

                            if (contactFullName.equals(name) && contactPhone.equals(phone)) {
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

    public List<ContactDto> getContactDtoList() {
        List<ContactDto> contactList = new ArrayList<>();

        try {
            wait.withTimeout(Duration.ofSeconds(30))
                    .until(driver -> !getContacts().isEmpty());
        } catch (TimeoutException e) {
            logger.error("Contacts not visible");
            return contactList;
        }

        List<WebElement> contacts = getContacts();
        for (WebElement contact : contacts) {
            String fullName = contact.findElement(By.id("com.sheygam.contactapp:id/rowName")).getText().trim();
            String phone = contact.findElement(By.id("com.sheygam.contactapp:id/rowPhone")).getText().trim();

            contactList.add(ContactDto.builder()
                    .fullName(fullName)
                    .phone(phone)
                    .build());
        }
        return contactList;
    }

    public void swipeToLeftForEditFirstContact() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(contactsList));
        } catch (TimeoutException e) {
            logger.error("Contacts not visible for edit");
        }
        if (contactsList.isEmpty()) {
            logger.info("No contacts to swipe for edit");
        }
        WebElement contact = getContacts().get(0);
        int contactStartX = contact.getLocation().getX();
        int contactWidth = contact.getSize().getWidth();
        int startX = contactStartX + contactWidth - 20;
        int endX = contactStartX + 20;
        int y = contact.getLocation().getY() + contact.getSize().getHeight() / 2;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, y));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, y));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(swipe));
    }

    public void swipeToRightForDeleteFirstContact() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(contactsList));
        } catch (TimeoutException e) {
            logger.error("Contacts not visible for delete");
        }
        if (contactsList.isEmpty()) {
            logger.info("No contacts to swipe for delete");
        }
        WebElement contact = getContacts().get(0);
        int contactStartX = contact.getLocation().getX();
        int contactWidth = contact.getSize().getWidth();
        int startX = contactStartX + 20;
        int endX = contactStartX + contactWidth - 20;
        int y = contact.getLocation().getY() + contact.getSize().getHeight() / 2;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, y));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, y));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(swipe));
    }

    public void logout() {
        clickWhenReady(btnMenu);
        clickWhenReady(btnLogout);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        wait.until(ExpectedConditions.visibilityOf(authenticationScreen.inputEmail));
    }
}