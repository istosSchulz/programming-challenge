package de.bcxp.challenge.coutry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.bcxp.challenge.common.InvalidDataException;
import lombok.Value;

@Value
class CountryModel {

	@JsonProperty("Name")
	private String countryName;

	@JsonProperty("Capital")
	private String capitalCity;

	@JsonProperty("Accession")
	private String accessionDate;

	@JsonProperty("Population")
	private long totalPopulation;

	@JsonProperty("Area (km²)")
	private double areaInSquareKilometers;

	@JsonProperty("GDP (US$ M)")
	private double gdpInMillionUSD;

	@JsonProperty("HDI")
	private double humanDevelopmentIndex;

	@JsonProperty("MEPs")
	private int europeanParliamentMembers;

	@JsonIgnore
	public double getPopulationDensity() { // FIXME prone to unprecise calculation, BigDecimal would be more robust
		if (!isValid()) { // TODO theoretically possible to toss name validation here
			throw new InvalidDataException(
					"Invalid data to calculate population density on. Must have 'Name', 'Area (km²)' > 0 and 'Population' > 0");
		}
		return getTotalPopulation() / getAreaInSquareKilometers();
	}

	@JsonIgnore
	public boolean isValid() {
		if (getCountryName() == null) {
			System.out.println("Filtered out input entry. Mandatory field 'Name' was missing. See: " + toString());
			return false;
		}
		if (getAreaInSquareKilometers() <= 0) { // FIXME prone to precision loss comparison
			System.out.println("Filtered out input entry. 'Area (km²)' must be greater than 0. See: " + toString());
			return false;
		}
		if (getTotalPopulation() < 0) {
			System.out.println("Filtered out input entry. 'Population' must be greater than 0. See: " + toString());
			return false;
		}
		return true;
	}

}
