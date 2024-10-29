package org.example.Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager{

    static ExtentSparkReporter extentSparkReporter;
    public static ExtentReports report;


    public static ExtentReports getReport(){
        if(report == null ){
            extentSparkReporter = new ExtentSparkReporter("Spark.html");
            report = new ExtentReports();
            extentSparkReporter.config().setDocumentTitle("FrameWork Automation");
            extentSparkReporter.config().setReportName("FrameWork Automation Reports");
            report.attachReporter(extentSparkReporter);
        }
        return report;
    }

    public static void endReport()
    {
        report.flush();

    }
}
