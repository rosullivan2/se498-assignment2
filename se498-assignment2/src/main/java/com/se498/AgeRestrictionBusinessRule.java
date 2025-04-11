package com.se498;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class AgeRestrictionBusinessRule implements BusinessRule {

    //Implement method(s) for determining age restriction rule violation
    
    private static final int MIN_AGE = 18;
    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );

    @Override
    public boolean apply(Object objectToCheck) {
        if (!(objectToCheck instanceof Participant)) {
            return false;
        }
        
        Participant participant = (Participant) objectToCheck;
        String dob = participant.getDateOfBirth();

        LocalDate birth_date = parseDate(dob);

        if (birth_date == null) {
            return false;
        }

        int age = calc_age(birth_date);
        return age >= MIN_AGE;
    }

    private LocalDate parseDate(String date_str) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(date_str, formatter);
            } catch (DateTimeParseException e) {
                // next format
            }
        }
        return null;
    }

    private int calc_age(LocalDate birth_date) {
        return Period.between(birth_date, LocalDate.now()).getYears();
    }

}
