package practsise;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTestWithNoPOM {
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
    public void validLogin() {
        driver.get("https://www.saucedemo.com");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("inventory"));
        Assert.assertTrue(driver.findElement(By.className("title")).isDisplayed());
    }

    @Test
    public void invalidLogin() {
        driver.get("https://www.saucedemo.com");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test='error']")));
        Assert.assertTrue(error.getText().contains("do not match"));
    }
    @Test
    public void lockedOutUser() {
        driver.get("https://www.saucedemo.com");
        driver.findElement(By.id("user-name")).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test='error']")));
        Assert.assertTrue(error.getText().contains("Sorry, this user has been locked out."));
    }

    @Test
    public void invalidLoginUsingPOM() {
        loginPage.open();
        loginPage.login("standard_user", "wrong_password");
        Assert.assertTrue(loginPage.getErrorText().contains("do not match"));
    }

    @Test
    public void lockedOutUserUsingPOM() {
        loginPage.open();
        loginPage.login("locked_out_user", "secret_sauce");

        Assert.assertTrue(loginPage.getErrorText().contains("Sorry, this user has been locked out."));
    }

    @Test
    public void validLoginUsingPOM() {
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