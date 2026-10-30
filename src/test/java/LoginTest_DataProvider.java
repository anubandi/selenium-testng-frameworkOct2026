import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import practsise.InventoryPage;

public class LoginTest_DataProvider extends BaseTest {

        private LoginPage loginPage;
        private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
        public void initPages() {
            loginPage = new LoginPage(driver);
            inventoryPage = new InventoryPage(driver);
        }


    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][] {
                {"standard_user", "wrong_password", "Do not match"},
                {"wrong_user", "secret_sauce", "do not match"},
                {"", "", "Username is required"},
                {"standard_user", "", "Password is required"}
        };
    }

    @Test(groups = "regression", dataProvider = "invalidCredentials")
    public void invalidLoginCombos(String user, String pass, String expectedError) {
        loginPage.open();
        loginPage.login(user, pass);
        Assert.assertTrue(loginPage.getErrorText().contains(expectedError));
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

    @Test(groups = "smoke")
    public void validLogin() {
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        String actualURL = inventoryPage.getUrl();
        String expectedURL = "https://www.saucedemo.com/inventory.html";
        Assert.assertEquals(actualURL, expectedURL);
        Assert.assertTrue(inventoryPage.isLoaded());
    }


}