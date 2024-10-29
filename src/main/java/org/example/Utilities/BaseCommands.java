package org.example.Utilities;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.example.Utilities.BaseTest.driver;
import static org.example.Utilities.ListenersImplementation.test;
//import static org.example.utilities.BaseTest.driver;
//import static org.example.utilities.ListenersImplementation.test;

public class BaseCommands {

    static WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));


    public static void failureScenario(String failureMessage) {
        // Log failure message to the console
        System.out.println(failureMessage);

        // Log failure to the Extent Report
        if (test != null) {
            try {
                // Add failure message
                test.fail(failureMessage);

                // Capture and add a screenshot to the report
                addDesktopScreenShotInReport(failureMessage);

            } catch (IOException | AWTException | InterruptedException e) {
                System.out.println("Failed to add screenshot to report: " + e.getMessage());
                test.fail("Failed to capture screenshot for failure.");
            }
        }

        // Optionally throw an exception to fail the test
        Assert.fail(failureMessage);
    }

    public static void getURL(String inputURL) {
        try {
            driver.get(inputURL);
            test.pass("Entered application URL successfully");
            test.info("Application URL is: " + inputURL);
        } catch (Exception var4) {
            failureScenario("Failed to enter application URL. Exception: " + var4);
        }
    }


    public static boolean findElement(By locator) {
        try {
            driver.findElement(locator);
        } catch (Exception var4) {
            failureScenario("finding element failed: " + var4);
        }
        return false;
    }

    public static WebElement waitForElement(By locator) {
        WebElement element = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            // test.pass("Able to wait for an element");
        } catch (Exception var3) {
            failureScenario("waiting for an element failed " + var3);
        }
        return element;
    }

    public static WebElement waitForClickable(By locator) {
        WebElement element = null;
        while (true) {
            try {
                waitForVisibilityOfElement(locator);
                element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                // test.pass("Able to wait for an element");
                break;
            } catch (StaleElementReferenceException e) {
                waitForElement(locator);
            } catch (Exception var3) {
                failureScenario("waiting for clickable element failed " + var3);
                return element;
            }
        }
        return element;
    }

    public static String getAttribute(By locator, String attribute) {
        try {
            waitForElement(locator);
            return driver.findElement(locator).getAttribute(attribute);
        } catch (Exception var4) {
            failureScenario("Failed to retrieve the getAttribute from an element: " + var4);
            return null;
        }
    }

    public static void assertEquals(String actual, String expected) {
        try {
            Assert.assertEquals(actual, expected);
            test.pass("Verified expected and actual message successfully");
        } catch (AssertionError var4) {
            failureScenario("Assertion failed: " + var4);
        }
    }

    public static void sendKeys(By element, String value, String elementName) {
        int retry = 0;
        while (retry < 3) {
            try {
                //waitForClickable(element).sendKeys(value);
                waitForVisibilityOfElement(element);
                driver.findElement(element).sendKeys(value);
                test.pass("Entered " + value + " for input field " + elementName + " successfully");
                // Thread.sleep(2000); // Uncomment if needed
//                result = true;
                break;
            } catch (StaleElementReferenceException e) {
                driver.findElement(element).sendKeys(value);
                retry++;
            } catch (Exception var4) {
                failureScenario("Failed to send the value for input field " + elementName + ": " + var4);
                retry++;
            }
        }
    }

    public static void sendAndClick(By element, String value) {
        try {
            waitForClickable(element).sendKeys(value);
            driver.findElement(element).click();
            test.pass("Entered " + value + " for input field " + driver.findElement(element).getText() + " successfully");
        } catch (Exception var4) {
            failureScenario("Failed to send the value for input field: " + var4);
        }
    }

    public static void waitForElementToBeClickable(By locator) {
        try {
            int counter = 0;
            // Wait until the element is clickable or max tries reached
            while (counter < 5) {
                WebElement element = waitForVisibilityOfElement(locator); // Wait for the element to be visible
                if (element.isDisplayed() && element.isEnabled()) { // Check if the element is displayed and enabled
                    break; // Element is clickable
                }
                Thread.sleep(1000); // Sleep for 1 second
                counter++;
            }
//            test.pass("Element is now clickable: " + locator); // Log success
        } catch (Exception e) {
            failureScenario("Failed to wait for the element to be clickable: " + e.getMessage()); // Log failure with the exception message
        }
    }


    public static void click(By locator, String elementName) throws InterruptedException {
        int retry = 0;
        while (retry <= 3) {
            try {
                waitForVisibilityOfElement(locator);
                waitForElementToBeClickable(locator);
                try {
                    driver.findElement(locator).click();
                    test.pass("Able to click on the " + elementName + " successfully");
                    break; // Exit loop if click is successful
                } catch (ElementClickInterceptedException e) {
                    try {
                        Actions action = new Actions(driver);
                        WebElement element = driver.findElement(locator);
                        action.moveToElement(element).click().perform();
                        test.pass("Able to click the " + elementName + " successfully");
                        break; // Exit loop if action click is successful
                    } catch (ElementClickInterceptedException d) {
                        Thread.sleep(1500);
                        clickUsingJS(locator, elementName);
                        break; // Exit loop if JS click is successful
                    }
                }
            } catch (StaleElementReferenceException e) {
                Thread.sleep(1000);
            }
            retry++;
        }
    }


    public static String takeScreenshot(WebDriver driver) {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot takeScreenShot = (TakesScreenshot) driver;
        File sourceFile = takeScreenShot.getScreenshotAs(OutputType.FILE);

        // Set the destination file path
        String destFile = "./screenshots/" + dateName + System.currentTimeMillis() + ".png";
        File destn = new File(destFile);

        try {
            Files.copy(sourceFile.toPath(), destn.toPath()); // Copy the screenshot to the destination
        } catch (IOException e) {
            System.out.println("Screenshot failed: " + e.getMessage()); // Log failure
        }

        return destFile; // Return the path of the saved screenshot
    }
    public static void addScreenShotInReport(String message) {
        String temp = BaseCommands.takeScreenshot(driver); // Take screenshot and get the file path
        test.info(message, MediaEntityBuilder.createScreenCaptureFromPath(temp).build()); // Add screenshot to the report
    }

    public static String desktopScreenshot() {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String destFile = "./screenshots/" + dateName + System.currentTimeMillis() + ".png"; // Set the destination file path

        try {
            // Create a File object to hold the screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Copy the screenshot to the specified file path
            FileUtils.copyFile(screenshot, new File(destFile));
//            test.pass("Screenshot taken successfully: " + destFile); // Log success
        } catch (IOException e) {
            failureScenario("Failed to take screenshot: " + e.getMessage()); // Log failure with error message
            e.printStackTrace(); // Print stack trace for debugging
        } catch (Exception e) {
            failureScenario("An unexpected error occurred while taking the screenshot: " + e.getMessage()); // Log unexpected error
            e.printStackTrace(); // Print stack trace for debugging
        }

        return destFile; // Return the path of the saved screenshot
    }

    // This method adds a screenshot of the entire desktop to the report
    public static void addDesktopScreenShotInReport(String message) throws IOException, AWTException, InterruptedException {
        Thread.sleep(1000); // Optional delay before taking the screenshot
        String tempScreenshotPath = BaseCommands.desktopScreenshot(); // Capture the desktop screenshot
        test.info(message, MediaEntityBuilder.createScreenCaptureFromPath(tempScreenshotPath).build()); // Add screenshot to report
    }


    public static void clickUsingJS(By locator) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", driver.findElement(locator));
            test.pass("Able to click on element using JavaScript executor successfully");
        } catch (Exception e) {
            failureScenario("Failed to click on element using JavaScript executor: " + e);
        }
    }

    public static void clickUsingJS(By locator, String element) {
        try {
            WebElement ele = driver.findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", ele);
            test.pass("Able to click on " + element + " successfully");
        } catch (Exception e) {
            failureScenario("Failed to click on element: " + e);
        }
    }


    public static void switchToFrame(By locator) {
        try {
            waitForElement(locator);
            WebElement frame = driver.findElement(locator);
            driver.switchTo().frame(frame);
            test.pass("Able to switch into frame successfully");
        } catch (Exception var4) {
            failureScenario("Failed to switch into frame: " + var4);
        }
    }


    public static void switchToFrame(int frameNumber) {
        try {
            driver.switchTo().frame(frameNumber);
            test.pass("Able to switch into the frame successfully");
        } catch (Exception var4) {
            failureScenario("Failed to switch into frame: " + var4);
        }
    }

    public static void switchToFrame(String frameName) {
        try {
            driver.switchTo().frame(frameName);
            test.pass("Able to switch into the frame successfully");
        } catch (Exception var4) {
            failureScenario("Failed to switch into frame: " + var4);
        }
    }


    public static void switchToParentFrame() {
        try {
            driver.switchTo().parentFrame();
            test.pass("Able to switch back into parent frame");
        } catch (Exception var4) {
            failureScenario("Failed to switch back into parent frame: " + var4);
        }
    }


    public static void switchBackFromFrame() {
        driver.switchTo().defaultContent();
    }

    public static void switchToNewTab() {
        try {
            for (String childWindow : driver.getWindowHandles()) {
                driver.switchTo().window(childWindow);
            }
            Thread.sleep(5000);
            test.pass("Switched to new window successfully");
        } catch (Exception var3) {
            failureScenario("Failed to switch to new window: " + var3);
        }
    }
    public static void switchToNewWindow() {
        try {
            for (String childWindow : driver.getWindowHandles()) {
                driver.switchTo().window(childWindow);
            }
            Thread.sleep(5000);
            test.pass("Switched to new window successfully");
        } catch (Exception var3) {
            failureScenario("Failed to switch to new window: " + var3);
        }
    }

    public static void scrollToCenter(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", element);
            test.pass("Able to scroll to the element in the center successfully");
        } catch (Exception e) {
            failureScenario("Failed to scroll to the center: " + e);
        }
    }

    public static void scrollIntoView(By locator) {
        try {
            WebElement element = BaseCommands.waitForVisibilityOfElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            test.pass("Able to scroll into view to the element successfully");
        } catch (Exception e) {
            failureScenario("Scroll into view on the element failed: " + e);
        }
    }

    public static WebElement waitForVisibilityOfElement(By locator) {
        WebElement element = null;
        try {
            waitForElement(locator);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            element = wait.ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
//            test.pass("Able to wait until the element is visible");
        } catch (Exception e) {
            failureScenario("Failed to find the visible element: " + e);
        }
        return element;
    }


    public static void dropDownSelectByText(By locator, String visibleText, String elementName) {
        try {
            waitForClickable(locator);
            Select option = new Select(driver.findElement(locator));
            option.selectByVisibleText(visibleText);
//            cursorWait();
            test.pass("Able to select the '" + visibleText + "' in the '" + elementName + "' dropdown successfully");
        } catch (Exception e) {
            failureScenario("Selecting value from dropdown by visible text failed: " + e);
        }
    }


    public static void dropDownSelectByValue(By locator, String value) {
        try {
            waitForVisibilityOfElement(locator); // Wait for the dropdown to be visible
            Select dropdown = new Select(driver.findElement(locator)); // Locate the dropdown
            dropdown.selectByValue(value); // Select the option by its value
            test.pass("Successfully selected '" + value + "' from the dropdown."); // Log success message
        } catch (NoSuchElementException e) {
            failureScenario("Failed to find the dropdown element: " + e.getMessage()); // Handle case where dropdown is not found
        } catch (ElementNotInteractableException e) {
            failureScenario("Dropdown is not interactable: " + e.getMessage()); // Handle case where dropdown is not interactable
        } catch (Exception e) {
            failureScenario("Failed to select the value: " + e.getMessage()); // Catch-all for any other exceptions
        }
    }

    public static void mouseHover(By locator) {
        try {
            WebElement hoverTo = driver.findElement(locator); // Locate the element to hover over
            Actions action = new Actions(driver); // Create an Actions object
            action.moveToElement(hoverTo).perform(); // Perform the hover action
            test.pass("Successfully hovered over the element."); // Log success message
        } catch (NoSuchElementException e) {
            failureScenario("Failed to find the element to hover: " + e.getMessage()); // Handle case where element is not found
        } catch (Exception e) {
            failureScenario("Mouse hovering on the element failed: " + e.getMessage()); // Catch-all for any other exceptions
        }
    }


    public static String getText(By locator) {
        String text = null; // Initialize text variable
        try {
            text = waitForElement(locator).getText(); // Wait for the element and get its text
            test.pass("Successfully retrieved text: " + text); // Log success message
        } catch (NoSuchElementException e) {
            failureScenario("Failed to find the element to get text: " + e.getMessage()); // Handle case where element is not found
        } catch (Exception e) {
            failureScenario("Failed to get text from the element: " + e.getMessage()); // Catch-all for any other exceptions
        }
        return text; // Return the retrieved text
    }


    public static void refresh() {
        try {
            driver.navigate().refresh(); // Refresh the current page
            test.pass("Successfully refreshed the page."); // Log success message
        } catch (Exception e) {
            failureScenario("Failed to refresh the page: " + e.getMessage()); // Handle any exceptions during refresh
        }
    }


    public static String getElementAttribute(By locator, String attributeName) {
        String elementValue = null; // Initialize variable to hold the attribute value
        try {
            waitForElement(locator); // Wait for the element to be present
            elementValue = driver.findElement(locator).getAttribute(attributeName); // Retrieve the attribute value
            test.pass("Successfully retrieved the attribute '" + attributeName + "' from the element."); // Log success message
        } catch (NoSuchElementException e) {
            failureScenario("Element not found while trying to retrieve attribute: " + e.getMessage()); // Handle case where element is not found
        } catch (Exception e) {
            failureScenario("Failed to retrieve the attribute from the element: " + e.getMessage()); // Catch-all for any other exceptions
        }
        return elementValue; // Return the attribute value
    }


    public static void assertElementAttribute(By locator, String attributeName, String expected) {
        String actual = null; // Initialize actual variable
        try {
            actual = getElementAttribute(locator, attributeName); // Retrieve the actual attribute value
            Assert.assertEquals("Attribute value mismatch for " + attributeName, expected, actual); // Assert the actual value matches the expected
            test.pass("Successfully asserted the attribute '" + attributeName + "' with expected value: " + expected); // Log success message
        } catch (AssertionError e) {
            failureScenario("Assertion failed for attribute '" + attributeName + "': expected '" + expected + "', but found '" + actual + "'. Error: " + e.getMessage()); // Log assertion failure
        } catch (Exception e) {
            failureScenario("Failed to assert element attribute: " + e.getMessage()); // Handle other exceptions
        }
    }




    public static String getPageTitle() {
        String title = null; // Initialize title variable
        try {
            title = driver.getTitle(); // Get the page title
            // test.pass("Able to get the page title successfully"); // Uncomment if you have a logging mechanism
        } catch (Exception e) {
            failureScenario("Failed to get the page title: " + e.getMessage()); // Log failure with the exception message
        }
        return title; // Return the page title
    }


    public static void waitForElementInvisible(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator)); // Wait until the element is invisible
            // test.pass("Wait for element invisible was successful"); // Uncomment if you have a logging mechanism
        } catch (Exception e) {
            failureScenario("Failed to wait for invisibility of an element: " + e.getMessage()); // Log failure with the exception message
        }
    }


    public static void enter(By locator) {
        try {
            driver.findElement(locator).sendKeys(Keys.ENTER); // Send ENTER key to the specified locator
            // test.pass("Able to enter successfully"); // Uncomment if you have a logging mechanism
        } catch (Exception e) {
            failureScenario("Failed to enter: " + e.getMessage()); // Log failure with the exception message
        }
    }


    public static String[] splitString(String account) {
        String[] accountDetails = null; // Initialize accountDetails variable
        try {
            accountDetails = account.split("[|]"); // Split the account string using regex
        } catch (Exception e) {
            failureScenario("Failed to split the string: " + e.getMessage()); // Log failure with the exception message
        }
        return accountDetails; // Return the split string array
    }


    public static void waitForAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent()); // Wait until the alert is present
            // test.pass("Able to wait until the alert is present"); // Uncomment if you have a logging mechanism
        } catch (Exception e) {
            failureScenario("Failed to find the alert: " + e.getMessage()); // Log failure with the exception message
        }
    }

    public static boolean isElementDisplayed(By locator) {
        try {
            boolean isDisplayed = driver.findElement(locator).isDisplayed(); // Check if the element is displayed
            // test.pass("Able to display an element successfully"); // Uncomment if you have a logging mechanism
            return isDisplayed; // Return the visibility status of the element
        } catch (Exception e) {
            // failureScenario("Failed to check if the element is displayed: " + e.getMessage()); // Uncomment if you want to log failures
            return false; // Return false if an exception occurs
        }
    }

    public static void scrollDown() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver; // Cast driver to JavascriptExecutor
            js.executeScript("window.scrollBy(0, 500);"); // Scroll down by 500 pixels
        } catch (Exception e) {
            failureScenario("Failed to scroll down: " + e.getMessage()); // Log failure with the exception message
        }
    }


    public static void scrollUp() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver; // Cast driver to JavascriptExecutor
            js.executeScript("window.scrollBy(0, -500);"); // Scroll up by 500 pixels
        } catch (Exception e) {
            failureScenario("Failed to scroll up: " + e.getMessage()); // Log failure with the exception message
        }
    }

    public static void switchToWindow(int indexOfWindow) {
        try {
            int tries = 0;
            // Wait until at least 2 windows are available or max tries reached
            while (driver.getWindowHandles().size() < 2 && tries < 10) {
                Thread.sleep(2000); // Sleep for 2 seconds
                tries++;
            }

            // Get all window handles
            Set<String> windowHandles = driver.getWindowHandles();
            List<String> windowHandlesList = new ArrayList<>(windowHandles);

            // Ensure the index is valid
            if (indexOfWindow < 0 || indexOfWindow >= windowHandlesList.size()) {
                throw new IndexOutOfBoundsException("Invalid window index: " + indexOfWindow);
            }

            // Switch to the specified window
            driver.switchTo().window((String) ((ArrayList<?>) windowHandlesList).get(indexOfWindow));
            driver.manage().window().maximize(); // Maximize the window
            test.pass("Able to switch to child window successfully."); // Log success
        } catch (Exception e) {
            failureScenario("Failed to switch window: " + e.getMessage()); // Log failure with the exception message
        }
    }

    public static void scrollToElement(By locator) {
        try {
            waitForElement(locator); // Wait for the element to be present
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            WebElement elem = driver.findElement(locator); // Locate the element
            executor.executeScript("arguments[0].scrollIntoView(true);", elem); // Scroll to the element
            // test.pass("Able to scroll on the element"); // Uncomment if you want to log success
        } catch (Exception e) {
            failureScenario("Mouse scroll on the element failed: " + e.getMessage()); // Log failure with the exception message
        }
    }


    public static void waitForElementToBeEnabled(By locator) {
        try {
            Thread.sleep(2000); // Initial wait before checking the element
            int counter = 0;

            // Wait until the element is enabled or until the counter reaches 5
            while (!driver.findElement(locator).isEnabled() && counter < 5) {
                Thread.sleep(1000); // Wait for 1 second before re-checking
                counter++;
            }
        } catch (Exception e) {
            failureScenario("Failed to wait for the element to be enabled: " + e.getMessage()); // Log failure with the exception message
        }
    }

    // This method is used to close the current browser instance
    public static void closeCurrentBrowser() {
        try {
            Thread.sleep(1000); // Wait for a moment before closing
            driver.close(); // Close the current browser window
        } catch (Exception e) {
            failureScenario("Failed to close the current browser: " + e.getMessage()); // Log failure with the exception message
        }
    }


    public static void sendKeys(By element, String value) {
        int retry = 0; // Initialize retry counter
        boolean result = false; // To track success

        while (retry < 3) { // Retry up to 3 times
            try {
                waitForVisibilityOfElement(element); // Wait until the element is visible
                driver.findElement(element).sendKeys(value); // Send the keys to the element
                 test.pass("Entered " + value + " for input field successfully");
                result = true; // Mark as successful
                break; // Exit the loop if successful
            } catch (Exception e) {
                retry++; // Increment retry counter
                if (retry == 3) {
                    failureScenario("Failed to send the value for input field after 3 attempts: " + e.getMessage()); // Log failure after max retries
                }
            }
        }
    }
    public static void secureSendKeys(By element, String value) {
        int retry = 0; // Initialize retry counter
        boolean result = false; // To track success
        String maskedValue = "*".repeat(value.length()); // Mask the password with stars

        while (retry < 3) { // Retry up to 3 times
            try {
                waitForVisibilityOfElement(element); // Wait until the element is visible
                driver.findElement(element).sendKeys(value); // Send the actual password to the element
                test.pass("Entered " + maskedValue + " for password field successfully"); // Log masked value
                result = true; // Mark as successful
                break; // Exit the loop if successful
            } catch (Exception e) {
                retry++; // Increment retry counter
                if (retry == 3) {
                    failureScenario("Failed to send the password value after 3 attempts: " + e.getMessage()); // Log failure after max retries
                }
            }
        }
    }
    public static boolean isFileDownloaded(String downloadPath, String fileName) throws InterruptedException {
        Thread.sleep(4000); // Wait for the download to complete
        File dir = new File(downloadPath);
        File[] dirContents = dir.listFiles(); // Use proper naming convention

        // Check if directory contents are not null
        if (dirContents != null) {
            for (File dirContent : dirContents) {
                if (dirContent.getName().equals(fileName)) { // Corrected syntax
                    // test.pass("File downloaded successfully"); // Uncomment for logging
                    return true; // Return true if the file is found
                }
            }
        }

        failureScenario("Failed to download the file: " + fileName); // Log failure if not found
        return false; // Return false if the file was not found
    }


    public static void clearDir(String path) {
        try {
            // Walk through the directory, filtering for regular files
            Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile) // Filter for regular files
                    .map(Path::toFile) // Convert Path to File
                    .forEach(File::delete); // Delete each file

            // Optionally log success
            // test.pass("Successfully cleared the directory: " + path);
        } catch (IOException e) {
            // Log failure with a detailed message
            failureScenario("Failed to clear the directory: " + path + ". Error: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    public static void createDirectory(String dirName) {
        // Define the path for the directory
        Path directoryPath = Paths.get(dirName);

        try {
            // Create the directory if it does not exist
            if (!Files.exists(directoryPath)) {
                Files.createDirectory(directoryPath);
                System.out.println("Directory created: " + dirName);
            } else {
                System.out.println("Directory already exists: " + dirName);
            }
        } catch (IOException e) {
            // Handle any IO exceptions that occur
            System.err.println("Failed to create directory: " + dirName);
            e.printStackTrace();
        }
    }



}







