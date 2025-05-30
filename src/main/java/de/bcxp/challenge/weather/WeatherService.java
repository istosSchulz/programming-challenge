package de.bcxp.challenge.weather;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import de.bcxp.challenge.common.CSVDataParser;
import de.bcxp.challenge.common.DataParser;
import de.bcxp.challenge.common.DataParser.DataType;
import de.bcxp.challenge.common.InvalidDataException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WeatherService {

	private static final String DEFAULT_LOCATION = "de/bcxp/challenge/weather.csv";

	// FIXME would benefit from DI and relying on DataParser interface instead
	private DataParser<WeatherModel> parser;

	// defaults
	private String inputFile = DEFAULT_LOCATION;
	private final CsvSchema schema = CsvSchema.emptySchema()
		.withUseHeader(true);

	public WeatherService(final String inputFile) {
		this.inputFile = Objects.toString(inputFile, DEFAULT_LOCATION);
	}

	/**
	 * Reads the weather data input file from classpath and calculates the minimal
	 * temperature spread within the parsed dataset. Skips semantically invalid
	 * import entries.
	 *
	 * @return The index of the day with the minimal temperature spread within the
	 *         imported dataset expressed in the range between 1 and 31.</br>
	 *         </br>
	 *         <b>"error"</b> in case of any error happening like an empty data set
	 *         was imported. This can be the case if there isn't any data provided
	 *         within the input or all the imported entries are skipped due to
	 *         validation errors. Also see {@link CSVDataParser#parseData(String).}
	 */
	public String dayWithSmallestTemperatureSpread() {
		try {
			return getParser().parseData(this.inputFile) // TODO data loading can be done once and cached
				.stream()
				.filter(WeatherModel::isValid)
				.min(Comparator.comparingDouble(WeatherModel::getTemperatureSpread))
				.map(WeatherModel::getDay)
				.map(String::valueOf) // FIXME clumsy choice of return type due to error-support && existing app
				.orElseThrow(() -> new InvalidDataException("Minimal temperature spread could not be calculated"));
		} catch (final InvalidDataException e) {
			System.err.println(e.getMessage());
			return "error"; // FIXME clumsy choice, exit codes would be better
		}
	}

	/**
	 * Lazily creates the data parser in case it wasn't created yet.
	 *
	 * @return The {@link DataParser} to use.
	 */
	// accessible for test reasons
	DataParser<WeatherModel> getParser() {
		if (this.parser == null) {
			this.parser = new CSVDataParser<>(DataType.WEATHER, this.schema, new TypeReference<List<WeatherModel>>() {
			});
		}
		return this.parser;
	}

}
