package org.example.Pages.Reddit;

import org.example.Maps.Reddit.LoginMap;
import org.example.Utilities.BaseCommands;
import org.example.Utilities.BaseTest;

import static org.example.Utilities.BaseTest.password;
import static org.example.Utilities.BaseTest.userName;

public class LoginPage {

    LoginMap loginMap = new LoginMap();

    public void logIn() throws InterruptedException {
        BaseCommands.click(loginMap.logInButton(),"Login Button");
        BaseCommands.sendKeys(loginMap.userName(),userName);
        BaseCommands.secureSendKeys(loginMap.password(),password);
        BaseTest.setVeryShortWait();
        BaseCommands.enter(loginMap.password());

    }


}
