package org.example.Pages.Magento;

import org.example.Maps.Magento.RegistrationMap;
import org.example.Utilities.BaseCommands;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class RegistrationPage {

    RegistrationMap registrationMap = new RegistrationMap();
public void createNewCustomer(Map<Integer, Map<String, String>> data ) throws InterruptedException, IOException, AWTException {

    BaseCommands.sendKeys(registrationMap.firstName(),data.get(1).get("First Name")+BaseCommands.getUniqueDateTime(),"First Name");
    BaseCommands.sendKeys(registrationMap.lastName(),data.get(1).get("Last Name")+BaseCommands.getUniqueDateTime(),"Last Name");
    if(data.get(1).get("NewsLetter").equalsIgnoreCase("yes")) {
        BaseCommands.click(registrationMap.newsletter(), "NewsLetter");
    }
    BaseCommands.sendKeys(registrationMap.email(),data.get(1).get("Email")+BaseCommands.getUniqueDateTime()+"@email.com","Email");
    BaseCommands.sendKeys(registrationMap.password(),data.get(1).get("Password"),"Password");
    BaseCommands.sendKeys(registrationMap.passwordConfirmation(),data.get(1).get("Password confirmation"),"Password Confirmation");
    BaseCommands.addDesktopScreenShotInReport("Customer Account Details");
    BaseCommands.click(registrationMap.createAccount(),"CreateAccount");
    BaseCommands.assertEquals(BaseCommands.getText(registrationMap.validateRegistration()),"Thank you for registering with Main Website Store.");
    BaseCommands.addDesktopScreenShotInReport("Successfully Registered");
}


}
