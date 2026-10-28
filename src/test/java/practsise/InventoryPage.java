package practsise;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {
    private WebDriver driver;

    private By title = By.className("title");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isLoaded() {
        return driver.findElement(title).isDisplayed();
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }
}