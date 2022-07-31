package com.bookingtest.tests;

import Models.AuthTokenRequest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.bookingtest.Endpoints.EndPoints;
import com.bookingtest.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;


import static io.restassured.RestAssured.given;

@Listeners(ExtentReportListener.class)
public class TestBase {

    protected  String authToken;
    protected static ExtentReports extentReport;
    protected static ExtentSparkReporter sparkReporter;

    @BeforeClass
    public void configSetup() {

        RestAssured.baseURI = ConfigReader.getProperty("BASE_URL");

        extentReport = new ExtentReports();
        sparkReporter = new ExtentSparkReporter("target/extentreport.html");
        extentReport.attachReporter(sparkReporter);

        AuthTokenRequest authTokenRequest = AuthTokenRequest.builder()
                .setUsername("admin").setPassword("password123").build();

        authToken = given().body(authTokenRequest).contentType(ContentType.JSON)
                .log().all()
                .when().post(EndPoints.AUTH_TOKEN)
                .then().statusCode(HttpStatus.SC_OK)
                .log().all()
                .extract().response().jsonPath().getString("token");
    }

    @AfterClass
    public void teardown()
    {
        extentReport.flush();
        System.out.println("Inside teardown");

    }
}
