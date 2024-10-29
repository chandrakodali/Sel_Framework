package org.example.Pages.Reddit;

import org.example.Maps.Reddit.HomePageMap;
import org.example.Utilities.BaseCommands;
import org.example.Utilities.BaseTest;

import java.awt.*;
import java.io.IOException;

import static org.example.Utilities.ListenersImplementation.test;

public class HomePage {
    HomePageMap homePageMap = new HomePageMap();

    public void validateUserLogin() throws InterruptedException {
        BaseCommands.click(homePageMap.userAvatar(),"User Avatar");
        BaseCommands.waitForElement(homePageMap.viewProfile());
        BaseCommands.addScreenShotInReport("Validated User logged In");
        BaseCommands.findElement(homePageMap.viewProfile());
    }

    public void logoutAndValidate() throws InterruptedException, IOException, AWTException {
        BaseCommands.click(homePageMap.logOut(),"Log Out");
        BaseTest.setVeryShortWait();
        boolean logout=BaseCommands.isElementDisplayed(homePageMap.userAvatar());
        System.out.println(logout);
        if(logout){
            test.fail("Logout failed");
            BaseCommands.addDesktopScreenShotInReport("Logout failed");
        }else {
            BaseCommands.addScreenShotInReport("Logout successful");
        }

    }
}
