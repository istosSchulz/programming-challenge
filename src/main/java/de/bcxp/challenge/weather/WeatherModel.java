package de.bcxp.challenge.weather;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.bcxp.challenge.common.InvalidDataException;
import lombok.Value;

@Value
class WeatherModel {

	@JsonProperty(value = "Day")
	private int day;

	@JsonProperty("MxT")
	private Double maxTemperature;

	@JsonProperty("MnT")
	private Double minTemperature;

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

	@JsonProperty("R AvSLP") // FIXME most likely a typo with the prev. column "MnR,AvSLP"
	private double averageSeaLevelPressure;

	@JsonIgnore
	public double getTemperatureSpread() {
		if (!isValid()) {
			throw new InvalidDataException(
					"Invalid data to calculate temperature spread on. Must have 0 < 'Day' < 32 and 'MnT' < 'MxT'");
		}
		return Math.abs(getMaxTemperature() - getMinTemperature());
	}

	@JsonIgnore
	public boolean isValid() {
		if (getMaxTemperature() == null || getMinTemperature() == null) {
			System.out.println(
					"Filtered out input entry. At least one of the mandatory fields 'MxT' and 'MnT' were missing. See: "
							+ toString());
			return false;
		}
		if (getDay() < 1 || getDay() > 31) {
			System.out.println("Filtered out input entry. 'Day' must be between 1 and 31. See: " + toString());
			return false;
		}
		if (getMaxTemperature() < getMinTemperature()) {
			System.out.println("Filtered out input entry. 'MnT' must not be greater than 'MxT'. See: " + toString());
			return false;
		}
		return true;
	}

}
