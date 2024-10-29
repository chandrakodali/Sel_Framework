package org.example.Magento;

import org.example.Maps.Magento.HomePageMap;
import org.example.Pages.Magento.HomePage;
import org.example.Utilities.BaseCommands;
import org.example.Utilities.BaseTest;
import org.testng.annotations.Test;

public class Registration extends BaseTest {

    HomePage homePage = new HomePage();

    @Test
    public void registration() throws InterruptedException {
        BaseCommands.getURL(URL);
        homePage.createAccount();

    }
}
