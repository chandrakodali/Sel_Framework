package org.example.Utilities;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

@Listeners({ListenersImplementation.class})
public class BaseTest {




    public static WebDriver driver;
    public static EdgeDriver edgeOptions;
    public static ChromeOptions chromeOptions;
    public static FirefoxOptions firefoxOptions;

    public static final ReadProperty readProperty = ReadProperty.getInstance();
    public static final String URL = readProperty.readProperties("Url");

    public static final String testDataExcel = System.getProperty("user.dir") + readProperty.readProperties("testDataPath");
    public static String downloadFolder = System.getProperty("user.dir")+ "\\downloads\\";
    public static final String gridURL = readProperty.readProperties("gridUrl");
    public static final String browser = readProperty.readProperties("browserName");
    public static String testCycleKey = "";
    public static final String remoteFlag = readProperty.readProperties("remoteExecution");
    public static final String jiraURL = readProperty.readProperties("jiraUrl");
    public static final String jiraUserName = readProperty.readProperties("jiraUserName");
    public static final String jiraApiToken = readProperty.readProperties("jiraApiToken");
    public static final String jiraFlag = readProperty.readProperties("jiraFlag");
    public static final String zephyrProjectKey = readProperty.readProperties("zephyrProjectKey");
    public static final String cycleKey = readProperty.readProperties("cycleKey");
    public static final String userName = readProperty.readProperties("userName");
    public static final String password = readProperty.readProperties("password");

    File msOfficeExtension = new File(System.getProperty("user.dir") + "\\extension\\" + "sample.crx");


    private static int veryShortWait = 1000;
    private static int shortWait = 2000;
    private static int smallWait = 5000;
    private static int longWait = 8000;

@BeforeSuite(alwaysRun = true)
    public void aCreateDirectory(){
    BaseCommands.createDirectory("Screenshots");
    BaseCommands.createDirectory("Downloads");
    }


    @BeforeMethod
    public void initBrowser() throws MalformedURLException {
        String[] browsers = {"chrome", "firefox"};

        for (String br : browsers) {
            if (browser.equalsIgnoreCase(br)) {
                if (br.equals("chrome")) {
                    WebDriverManager.chromedriver().setup();
                    getChromeBrowserOptions();
                    if (remoteFlag.equalsIgnoreCase("Yes")) {
                        driver = new RemoteWebDriver(new URL(gridURL), chromeOptions);
                    } else {
                        driver = new ChromeDriver(chromeOptions);
                    }
                } else if (br.equals("firefox")) {
                    WebDriverManager.firefoxdriver().setup();
                    getFirefoxBrowserOptions();
                    if (remoteFlag.equalsIgnoreCase("Yes")) {
                        driver = new RemoteWebDriver(new URL(gridURL), firefoxOptions);
                    } else {
                        driver = new FirefoxDriver(firefoxOptions);
                    }
                }
                break; // Exit the loop once a matching browser is set up
            }
        }
    }


    public static int getwait(){
        int wait = 5;
        return wait;
    }

    protected void setWindowSize(int height, int width) {
        Dimension dimension = new Dimension(width, height);
        driver.manage().window().setSize(dimension);
    }

    public void getChromeBrowserOptions(){
        chromeOptions = new ChromeOptions();
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.default_directory", downloadFolder);
        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--disable-application-cache");
        chromeOptions.addArguments("--disable-session-crashed-bubble");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--no-first-run");

        // Add these CI-specific options
        if (System.getProperty("headless", "false").equals("true") ||
                System.getenv("CI") != null ||
                System.getenv("GITHUB_ACTIONS") != null) {

            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--window-size=1920,1080");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("--no-first-run");
            chromeOptions.addArguments("--disable-default-apps");
            chromeOptions.addArguments("--disable-web-security");
            chromeOptions.addArguments("--user-data-dir=/tmp/chrome-user-data-" + System.currentTimeMillis());

        } else {
            chromeOptions.addArguments("--start-maximized");
        }
    }
    public void getFirefoxBrowserOptions(){
    firefoxOptions = new FirefoxOptions();
    HashMap<String, Object >fireFoxPrefs = new HashMap<String, Object>();
    fireFoxPrefs.put("download.default_directory", downloadFolder);
    fireFoxPrefs.put("download.prompt.for_download", true);
    fireFoxPrefs.put("plugins.always_open_pdf_externally", true);
    firefoxOptions.setAcceptInsecureCerts(true);
    }

    public static void setSmallWait() throws InterruptedException{
    Thread.sleep(smallWait);
    }

    public static void setLongWait() throws InterruptedException{
        Thread.sleep(longWait);
    }

    public static void setShortWaitWait() throws InterruptedException{
        Thread.sleep(shortWait);
    }

    public static void setVeryShortWait() throws InterruptedException{
        Thread.sleep(veryShortWait);
    }
}
