import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import practsise.InventoryPage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;

public class BaseTest {
    protected WebDriver driver;
    protected LoginPage loginPage;
    protected InventoryPage inventoryPage;
    protected static ExtentReports extent = ExtentManager.getInstance();
    protected ExtentTest test;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        test = extent.createTest(method.getName());
        String browser = ConfigReader.get("browser");
        switch (browser.toLowerCase()) {
            case "firefox": driver = new FirefoxDriver(); break;
            case "edge":    driver = new EdgeDriver();    break;
            default: {
                ChromeOptions options = new ChromeOptions();
                if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
                    options.addArguments("--headless=new", "--window-size=1920,1080");
                }
                driver = new ChromeDriver(options);
            }
        }
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String path = takeScreenshot(result.getName());
            test.fail(result.getThrowable());
            if (path != null) {
                test.addScreenCaptureFromPath(path);
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test passed");
        } else {
            test.skip("Test skipped");
        }
        driver.quit();
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        extent.flush();
    }

    private String takeScreenshot(String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File("screenshots/" + testName + "_" + System.currentTimeMillis() + ".png");
            dest.getParentFile().mkdirs();
            Files.copy(src.toPath(), dest.toPath());
            return dest.getAbsolutePath();
        } catch (IOException e) {
            System.out.println("Could not save screenshot: " + e.getMessage());
            return null;
        }
    }
}