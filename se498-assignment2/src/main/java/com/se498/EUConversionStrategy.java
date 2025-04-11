package com.se498;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EUConversionStrategy implements ConversionStrategy {

    //Implement method(s) for the EU format time and date conversion
    @Override
    public Date convert(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        return formatter.parse(date);
    }
}
