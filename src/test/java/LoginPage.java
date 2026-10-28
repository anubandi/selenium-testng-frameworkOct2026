import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;
    private CommonActions actions;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new CommonActions(driver);
    }

    private By username = By.id("user-name");
    private By password = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.cssSelector("[data-test='error']");


    public void open() {
        driver.get(ConfigReader.get("base.url"));
    }

    public void login(String user, String pass) {
        actions.type(username,user);
        actions.type(password,pass);
        actions.click(loginButton);
    }

    public String getErrorText() {

        return actions.getText(errorMessage);

    }
}