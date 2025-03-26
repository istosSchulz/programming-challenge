package de.bcxp.challenge;

import de.bcxp.challenge.coutry.CountryService;
import de.bcxp.challenge.weather.WeatherService;

/**
 * The entry class for your solution. This class is only aimed as starting point and not intended as baseline for your software
 * design. Read: create your own classes and packages as appropriate.
 */
public final class App {

    /**
     * This is the main entry method of your program.
     * @param args The CLI arguments passed
     */
    public static void main(String... args) {

        // Your preparation code …
    	// FIXME would benefit from DI and/or service locator
    	WeatherService weatherService = new WeatherService();
    	CountryService countryService = new CountryService(); 

        String dayWithSmallestTempSpread = String.valueOf(weatherService.dayWithSmallestTemperatureSpread());     // Your day analysis function call …
        System.out.printf("Day with smallest temperature spread: %s%n", dayWithSmallestTempSpread);

        String countryWithHighestPopulationDensity = countryService.countryWithHighestPopulationDensity(); // Your population density analysis function call …
        System.out.printf("Country with highest population density: %s%n", countryWithHighestPopulationDensity);
    }
}
