package providers;

import com.bookingtest.utils.ConfigReader;
import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name = "bookingdata")
    public Object[][] getBookingData(){

        return  ConfigReader.getData("BookingData","bookingdata");
    }

    @DataProvider(name = "bookingdatabyid")
    public Object[][] getBookingDataById(){

        return  ConfigReader.getData("BookingData","bookingdatabyid");
    }
}
