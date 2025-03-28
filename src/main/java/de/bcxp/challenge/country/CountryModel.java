package de.bcxp.challenge.country;

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
		if (!hasValidPopulation() || !hasValidSize()) {
			throw new InvalidDataException(
					"Invalid data to calculate population density on. Must have 'Area (km²)' > 0 and 'Population' >= 0");
		}
		return getTotalPopulation() / getAreaInSquareKilometers();
	}

	/**
	 * Validating the CountryModel by mandatory fields Name and ranges for Area
	 * (km²) > 0 and Population >= 0.
	 *
	 * @return true if restrictions are met.
	 */
	@JsonIgnore
	public boolean isValid() {
		if (!hasValidName()) {
			System.err.println("Filtered out input entry. Mandatory field 'Name' was missing. See: " + toString());
			return false;
		}
		if (!hasValidSize()) { // FIXME prone to precision loss comparison
			System.err.println("Filtered out input entry. 'Area (km²)' must be greater than 0. See: " + toString());
			return false;
		}
		if (!hasValidPopulation()) {
			System.err.println("Filtered out input entry. 'Population' must be greater than 0. See: " + toString());
			return false;
		}
		return true;
	}

	private boolean hasValidPopulation() {
		return getTotalPopulation() >= 0;
	}

	private boolean hasValidSize() {
		return getAreaInSquareKilometers() > 0;
	}

	private boolean hasValidName() {
		return getCountryName() != null;
	}

}
