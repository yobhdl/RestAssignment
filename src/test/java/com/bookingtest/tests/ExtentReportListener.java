package com.bookingtest.tests;

import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener extends TestBase implements ITestListener {

    public static ExtentTest test;

    public void onTestStart(ITestResult result) {

        test = extentReport.createTest(result.getMethod().getMethodName());
    }
    public void onTestSuccess(ITestResult result) {
        test.pass(result.getMethod().getMethodName());
    }

    public void onTestFailure(ITestResult result) {
        test.fail(result.getMethod().getMethodName());
    }

    public void onTestSkipped(ITestResult result) {
        test.skip(result.getMethod().getMethodName());
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // TODO Auto-generated method stub
    }
}
