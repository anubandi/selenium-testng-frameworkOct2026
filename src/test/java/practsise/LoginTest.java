package practsise;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {
    WebDriver driver;
    Loginpage loginPage;
    InventoryPage inventoryPage;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loginPage = new Loginpage(driver);
        inventoryPage = new InventoryPage(driver);
    }

    @Test
    public void invalidLogin() {
        loginPage.open();
        loginPage.login("standard_user", "wrong_password");
        Assert.assertTrue(loginPage.getErrorText().contains("do not match"));
    }

    @Test
    public void lockedOutUser() {
        loginPage.open();
        loginPage.login("locked_out_user", "secret_sauce");

        Assert.assertTrue(loginPage.getErrorText().contains("Sorry, this user has been locked out."));
    }

    @Test
    public void validLogin() {
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        String actualURL = inventoryPage.getUrl();
        String expectedURL = "https://www.saucedemo.com/inventory.html";
        Assert.assertEquals(actualURL, expectedURL);
        Assert.assertTrue(inventoryPage.isLoaded());
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}