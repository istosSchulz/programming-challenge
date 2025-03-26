package de.bcxp.challenge.weather;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherModel {

	@JsonProperty("Day")
	private int day;

	@JsonProperty("MxT")
	private double maxTemperature;

	@JsonProperty("MnT")
	private double minTemperature;

	@JsonProperty("AvT")
	private double averageTemperature;

	@JsonProperty("AvDP")
	private double averageDewPoint;

	@JsonProperty("1HrP TPcpn")
	private double oneHourPrecipitation;

	@JsonProperty("PDir")
	private int primaryWindDirection;

	@JsonProperty("AvSp")
	private double averageWindSpeed;

	@JsonProperty("Dir")
	private int mostFrequentWindDirection;

	@JsonProperty("MxS")
	private double maxWindSpeed;

	@JsonProperty("SkyC")
	private double skyCoverage;

	@JsonProperty("MxR")
	private int maxRelativeHumidity;

	@JsonProperty("Mn")
	private int minRelativeHumidity;

	@JsonProperty("R AvSLP")
	private double averageSeaLevelPressure;

	@JsonIgnore
	public double getTemperatureSpread() {
		return getMaxTemperature() - getMinTemperature();
	}

}
