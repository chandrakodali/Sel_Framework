package org.example.Maps.Magento;

import org.openqa.selenium.By;

public class RegistrationMap {


    public By firstName(){
        return By.xpath("//input[@id='firstname']");
    }

    public By lastName(){
        return By.xpath("//input[@id='lastname']");
    }

    public By newsletter(){
        return By.xpath("//input[@id='is_subscribed']");
    }

    public By email(){
        return By.xpath("//input[@id='email_address']");
    }

    public By password(){
        return By.xpath("//input[@id='password']");
    }

    public By passwordConfirmation(){
        return By.xpath("//input[@id='password-confirmation']");
    }

    public By createAccount(){
        return By.xpath("//button[@title='Create an Account']");
    }

    //div[text()='Thank you for registering with Main Website Store.']

    public By validateRegistration(){
        return By.xpath("//div[text()='Thank you for registering with Main Website Store.']");
    }


}
