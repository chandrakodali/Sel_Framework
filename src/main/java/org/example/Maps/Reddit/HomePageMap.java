package org.example.Maps.Reddit;

import org.openqa.selenium.By;

public class HomePageMap {

    public By userAvatar(){
        return By.xpath("//*[@alt='User Avatar']");
    }

    public By viewProfile(){
        return By.xpath("//span[text()='View Profile']");
    }

    public By logOut(){
        return By.xpath("//span[text()='Log Out']");
    }


}
