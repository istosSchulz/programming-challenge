package de.bcxp.challenge.coutry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
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
	public double getPopulationDensity() {
		return getTotalPopulation() / getAreaInSquareKilometers();
	}

}
