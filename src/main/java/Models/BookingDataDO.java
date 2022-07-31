package Models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "set")
public class BookingDataDO {

    private String firstname;
    private String lastname;
    private Number totalprice;
    private Boolean depositpaid;
    private BookingDatesDO bookingdates;
    private String  additionalneeds;
}
