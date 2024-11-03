package org.example.Magento;

import org.example.Maps.Magento.HomePageMap;
import org.example.Pages.Magento.HomePage;
import org.example.Pages.Magento.RegistrationPage;
import org.example.Utilities.BaseCommands;
import org.example.Utilities.BaseTest;
import org.example.Utilities.ExcelDataProvider;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class Registration extends BaseTest {

    HomePage homePage = new HomePage();
    RegistrationPage registrationPage = new RegistrationPage();


    @DataProvider(name = "registration")
    public Object[][] getRegistration()  {
        Map<Integer, Map<String,String>> registration = ExcelDataProvider.excelDataMap(testDataExcel+"/testData.xlsx","Sheet1");
        return new Object[][]{{registration}};
    }



    @Test(dataProvider = "registration")
    public void registration(Map<Integer,Map<String, String>> data  ) throws InterruptedException, IOException, AWTException {
        BaseCommands.getURL(URL);
        homePage.createAccount();
        registrationPage.createNewCustomer(data);
    }
}
