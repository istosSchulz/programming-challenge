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

	/**
	 * Calculates the temperature spread by {@link #getMaxTemperature()} -
	 * {@link #getMinTemperature()}.
	 *
	 * @return The temperature spread of this weather date
	 * @throws {@link
	 *             InvalidDataException} in case {@link #getMaxTemperature()} <
	 *             {@link #getMinTemperature()} or those aren't set
	 */
	@JsonIgnore
	public double getTemperatureSpread() {
		if (!hasValidTemperatures() || !hasValidTemperatureBoundaries()) {
			throw new InvalidDataException("Invalid data to calculate temperature spread on. Must have 'MnT' < 'MxT'");
		}
		return Math.abs(getMaxTemperature() - getMinTemperature());
	}

	/**
	 * Validating the WeatherModel by mandatory fields Day, Mxt and MnT and ranges 1
	 * <= Day <= 31, MnT <= MxT.
	 *
	 * @return true if restrictions are met.
	 */
	@JsonIgnore
	public boolean isValid() {
		if (!hasValidTemperatures()) {
			System.err.println(
					"Filtered out input entry. At least one of the mandatory fields 'MxT' and 'MnT' were missing. See: "
							+ toString());
			return false;
		}
		if (!hasValidDay()) {
			System.err.println("Filtered out input entry. 'Day' must be between 1 and 31. See: " + toString());
			return false;
		}
		if (!hasValidTemperatureBoundaries()) {
			System.err.println("Filtered out input entry. 'MnT' must not be greater than 'MxT'. See: " + toString());
			return false;
		}
		return true;
	}

	private boolean hasValidTemperatureBoundaries() {
		return getMinTemperature() <= getMaxTemperature();
	}

	private boolean hasValidDay() {
		return 1 <= getDay() && getDay() <= 31;
	}

	private boolean hasValidTemperatures() {
		return getMaxTemperature() != null && getMinTemperature() != null;
	}

}
