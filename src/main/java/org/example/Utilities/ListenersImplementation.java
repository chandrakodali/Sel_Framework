package org.example.Utilities;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class ListenersImplementation implements ITestListener {

public static ExtentTest test;


public void onTestStart(ITestResult result) {
    test = ExtentReportManager.report.createTest(result.getMethod().getMethodName());
}


    public void onTestSuccess(ITestResult result) {
        System.out.println("Success of test case and its details are: " + result.getName());
    }

    public void onTestFailure(ITestResult result) {
        System.out.println("Failure of test case and its details are: " + result.getName());
    }

    public void onTestSkipped(ITestResult result) {
    test.log(Status.SKIP, result.getThrowable());
    test.log(Status.SKIP, result.getMethod().getMethodName());
    test.log(Status.SKIP, MarkupHelper.createLabel(result.getName(), ExtentColor.YELLOW));
    }

    public void onTestFailureWithSuccessPercenatge(ITestResult result) {
        System.out.println("Failure of test case and its details are: " + result.getName());
    }

    public void onStart(ITestContext context){
        ExtentReportManager.report= ExtentReportManager.getReport();
    }
    public void onFinish(ITestContext context){
    ExtentReportManager.report.flush();
    }

}
