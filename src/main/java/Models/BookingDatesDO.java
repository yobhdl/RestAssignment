package Models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder(setterPrefix = "set")
public class BookingDatesDO {

    private Date checkin;
    private Date checkout;

}
