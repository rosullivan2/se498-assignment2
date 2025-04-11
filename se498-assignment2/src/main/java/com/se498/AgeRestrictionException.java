package com.se498;

public class AgeRestrictionException extends Exception {

    //Implement method(s) for age restriction exception

    private final String locale;
    private final String reason;

    public AgeRestrictionException(String locale, String reason) {
        this.locale = locale;
        this.reason = reason;
    }

    public String getLocale() {
        return locale;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "AgeRestrictionException [locale=" + locale + ", reason=" + reason + "]";
    }
}