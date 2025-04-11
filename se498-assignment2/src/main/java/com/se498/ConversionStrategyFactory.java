package com.se498;

public class ConversionStrategyFactory {

    // Implement method(s) for the determining conversion strategy
    private static class InstanceHolder {
        private static final ConversionStrategyFactory INSTANCE = new ConversionStrategyFactory();
    }
    public static ConversionStrategyFactory getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ConversionStrategy getStrategy(String locale) throws ConversionStrategyException {
        if (locale == null) { 
            throw new ConversionStrategyException("unknown", "Locale can't be null.");
        }

        switch (locale.toUpperCase()) {
            case "US":
                return new USConversionStrategy();
            case "EU":
                return new EUConversionStrategy();
            case "UK":
                return new EUConversionStrategy();
            default: 
                throw new ConversionStrategyException(locale, "unsupported locale: " + locale);

        }
    }
}
