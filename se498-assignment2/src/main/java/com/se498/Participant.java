package com.se498;

public class Participant {

    //Implement Participant attributes and methods
    String dateOfBirth;
    String locale;

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getLocale() {
        return locale;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Participant (String dateOfBirth, String locale) {
        this.dateOfBirth = dateOfBirth;
        this.locale = locale;

    }
}
