package com.bookingtest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperUtility {

    public static Date createDate(String dateString)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
