package com.bookingtest.tests;

import Models.*;
import com.bookingtest.Endpoints.EndPoints;
import com.bookingtest.utils.HelperUtility;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import providers.DataProviders;

import java.util.Date;

import static io.restassured.RestAssured.given;

@Listeners(ExtentReportListener.class)
public class TestHotelBooking extends TestBase{


    @Test(dataProvider = "bookingdata", dataProviderClass = DataProviders.class)
    public void TestBookHotel(String fName,String lName, String totalPrice, String depositPaid, String checkinDate, String checkoutDate, String additionalNeeds) {

        BookingDatesDO bookingDates = BookingDatesDO.builder()
                .setCheckin(HelperUtility.createDate(checkinDate))
                .setCheckout(HelperUtility.createDate(checkoutDate)).build();

        BookingDataDO bookingData = BookingDataDO.builder()
                .setBookingdates(bookingDates)
                .setFirstname(fName)
                .setLastname(lName)
                .setTotalprice(Integer.parseInt(totalPrice))
                .setDepositpaid(Boolean.parseBoolean(depositPaid))
                .setAdditionalneeds(additionalNeeds).build();

        BookingResponseDO Response = given().contentType("application/json")
                .body(bookingData)
                .when().post(EndPoints.CREATE_BOOKING)
                .then().statusCode(HttpStatus.SC_OK).log().all()
                .extract().response().as(BookingResponseDO.class);

        System.out.println("Booking ID is :" + Response.getBookingid());
        Assert.assertTrue(Response.getBookingid() > 0);

        BookingDataDO bookingDataResponse = Response.getBooking();
        BookingDatesDO bookingDatesResponse = bookingDataResponse.getBookingdates();

        System.out.println(String.format(String.format("Checkin date is :" + bookingDatesResponse.getCheckin())));
        System.out.println("Checkout date is :" + bookingDatesResponse.getCheckout());

        Assert.assertEquals(bookingDataResponse.getFirstname(),fName);
        Assert.assertEquals(bookingDataResponse.getLastname(),lName);
        Assert.assertEquals(bookingDataResponse.getTotalprice().intValue(),Integer.parseInt(totalPrice));
        Assert.assertEquals(bookingDataResponse.getDepositpaid(),Boolean.parseBoolean(depositPaid));
        Assert.assertEquals(bookingDataResponse.getAdditionalneeds(),additionalNeeds);
        Assert.assertEquals(bookingDatesResponse.getCheckin(),HelperUtility.createDate(checkinDate));
        Assert.assertEquals(bookingDatesResponse.getCheckout(),HelperUtility.createDate(checkoutDate));

    }

    @Test(dataProvider = "bookingdatabyid", dataProviderClass = DataProviders.class)
    public void TestGetBookingById(String fName,String lName, String totalPrice, String depositPaid, String checkinDate, String checkoutDate, String additionalNeeds){

        BookingDatesDO bookingDates = BookingDatesDO.builder()
                .setCheckin(HelperUtility.createDate(checkinDate))
                .setCheckout(HelperUtility.createDate(checkoutDate)).build();

        BookingDataDO bookingData = BookingDataDO.builder()
                .setBookingdates(bookingDates)
                .setFirstname(fName)
                .setLastname(lName)
                .setTotalprice(Integer.parseInt(totalPrice))
                .setDepositpaid(Boolean.parseBoolean(depositPaid))
                .setAdditionalneeds(additionalNeeds).build();

        BookingResponseDO createBookingResponse = given().contentType("application/json")
                .body(bookingData)
                .when().post(EndPoints.CREATE_BOOKING)
                .then().statusCode(HttpStatus.SC_OK).log().all()
                .extract().response().as(BookingResponseDO.class);

        int id = createBookingResponse.getBookingid();
        System.out.println("ID is :" + id);
        BookingDataDO getBookingByIdResponse = given()
                .pathParam("id",id)
                .log().all()
                .when()
                .get(EndPoints.GET_BOOKING_BY_ID)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response().as(BookingDataDO.class);

        Assert.assertEquals(getBookingByIdResponse.getFirstname(),fName);
        Assert.assertEquals(getBookingByIdResponse.getLastname(),lName);
        Assert.assertEquals(getBookingByIdResponse.getAdditionalneeds(),additionalNeeds);
        Assert.assertEquals(getBookingByIdResponse.getTotalprice().intValue(),Integer.parseInt(totalPrice));
        Assert.assertEquals(getBookingByIdResponse.getDepositpaid(),Boolean.parseBoolean(depositPaid));

        BookingDatesDO getBookingByIdDates = getBookingByIdResponse.getBookingdates();

        Assert.assertEquals(getBookingByIdDates.getCheckin(),HelperUtility.createDate(checkinDate));
        Assert.assertEquals(getBookingByIdDates.getCheckout(),HelperUtility.createDate(checkoutDate));
    }

    @Test
    public void TestPartialUpdateBooking(){

        String lName = "testlname";
        String checkoutDate = "2022-08-20";
        BookingDatesDO bookingDate = BookingDatesDO.builder()
                .setCheckout(HelperUtility.createDate(checkoutDate)).build();

        BookingDataDO bookingDataPartialUpdate = BookingDataDO.builder().setLastname(lName).setBookingdates(bookingDate).build();

        BookingId[] bookingIds  =  given().accept(ContentType.JSON)
                .when().get(EndPoints.GET_BOOKINGS)
                .then().statusCode(HttpStatus.SC_OK)
                .log().all()
                .extract().response().as(BookingId[].class);

        int id = bookingIds[0].getBookingid();

       BookingDataDO updatedBookingData =  given().header("Cookie", "token=" + authToken)
                .body(bookingDataPartialUpdate).contentType(ContentType.JSON)
                .pathParam("id",id)
                .when().patch(EndPoints.PARTIAL_UPDATE_BOOKING)
                .then().statusCode(HttpStatus.SC_OK)
                .log().all()
                .extract().response().as(BookingDataDO.class);

       Assert.assertEquals(updatedBookingData.getLastname(),lName);
       Assert.assertEquals(updatedBookingData.getBookingdates().getCheckout(),HelperUtility.createDate(checkoutDate));
    }

    @Test
    public void TestGetBookings()
    {
        BookingId[] bookingIds = given().accept(ContentType.JSON)
                .when().get(EndPoints.GET_BOOKINGS)
                .then().statusCode(HttpStatus.SC_OK)
                .log().all()
                .extract().response().as(BookingId[].class);
        Assert.assertTrue(bookingIds.length > 0);

    }
    @Test
    public void DeleteBookingByID(){

       BookingId[] bookingIds = given().accept(ContentType.JSON)
                .when().get(EndPoints.GET_BOOKINGS)
                .then().statusCode(HttpStatus.SC_OK)
                .log().all()
                .extract().response().as(BookingId[].class);

        int id = bookingIds[0].getBookingid();

       System.out.println("Id in test is :" + id);
       System.out.println("Token in test is :" + authToken);
       given().header("Cookie", "token=" + authToken)
               .pathParam("id",id)
               .log().all()
               .when()
               .delete(EndPoints.DELETE_BOOKING)
               .then().log().all()
               .statusCode(HttpStatus.SC_CREATED);

       given().pathParam("id",id)
               .when().get(EndPoints.GET_BOOKING_BY_ID)
               .then().statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void getBookingsfilteredWithDate(){


           Date checkindate =  HelperUtility.createDate("2021-07-20");
           Date checkoutdate = HelperUtility.createDate("2022-07-30");

      BookingId[] bookingWithDateRange =  given()
                .queryParam("checkin",checkindate)
                .queryParam("checkout",checkoutdate)
                .log().all()
                .when()
              .accept(ContentType.JSON)
              .get(EndPoints.GET_BOOKINGS)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response().as(BookingId[].class);

      for(BookingId bookingId : bookingWithDateRange)
      {
             int id = bookingId.getBookingid();

             BookingDataDO getBookingByIdResponse = given()
                  .pathParam("id",id)
                  .log().all()
                  .when()
                  .get(EndPoints.GET_BOOKING_BY_ID)
                  .then().log().all()
                  .statusCode(HttpStatus.SC_OK)
                  .extract()
                  .response().as(BookingDataDO.class);

          Date checkind = getBookingByIdResponse.getBookingdates().getCheckin();
          Date checkoutd = getBookingByIdResponse.getBookingdates().getCheckout();

          Assert.assertTrue(checkind.compareTo(checkindate) >= 0);
          Assert.assertTrue(checkoutd.compareTo(checkoutdate) <= 0);
      }

    }
}
