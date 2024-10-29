package org.example.Pages.Magento;

import org.example.Maps.Magento.HomePageMap;
import org.example.Utilities.BaseCommands;

public class HomePage {

    HomePageMap homePageMap = new HomePageMap();
    public void createAccount() throws InterruptedException {
        String pageTitle= BaseCommands.getPageTitle();
        BaseCommands.assertEquals(pageTitle,"Magento 2 Commerce (Enterprise) Demo - Magebit");
        BaseCommands.click(homePageMap.createAnAccount(),"Create An Account");
    }
}
