package org.example.Reddit;

import org.example.Pages.Reddit.HomePage;
import org.example.Pages.Reddit.LoginPage;
import org.example.Utilities.BaseCommands;
import org.example.Utilities.BaseTest;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;

public class Reddit extends BaseTest {

    LoginPage loginPage = new LoginPage();
    HomePage homePage = new HomePage();

    @Test(description = "Navigate to Apple website")
    public void simpleTest() throws InterruptedException, IOException, AWTException {
        BaseCommands.getURL(URL);
        loginPage.logIn();
        homePage.validateUserLogin();
        homePage.logoutAndValidate();
    }
}