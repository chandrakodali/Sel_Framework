package org.example.Maps.Reddit;

import org.openqa.selenium.By;

public class LoginMap {

    public By logInButton(){
        return By.xpath("//span[text()='Log In']");
    }

    public By userName(){
        return By.xpath("//input[@name='username']");
    }


    public By password(){
        return By.xpath("//input[@name='password']");
    }
}
