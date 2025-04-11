package com.se498;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class USConversionStrategy implements ConversionStrategy {

    //Implement method(s) for the US format time and date conversion
    @Override
    public Date convert(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter.setLenient(false);
        return formatter.parse(date);
    }
}
