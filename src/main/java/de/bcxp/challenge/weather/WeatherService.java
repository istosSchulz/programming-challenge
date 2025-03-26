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
	private CSVDataParser<WeatherModel> parser;

	// defaults
	private String inputFile = DEFAULT_LOCATION;
	private final CsvSchema schema = CsvSchema.emptySchema()
		.withUseHeader(true);

	public WeatherService(final String inputFile) {
		this.inputFile = Objects.toString(inputFile, DEFAULT_LOCATION);
	}

	public int dayWithSmallestTemperatureSpread() {

		return getParser().parseData(this.inputFile) // TODO data loading can be done once and cached
			.stream()
			.filter(WeatherModel::isValid)
			.min(Comparator.comparingDouble(WeatherModel::getTemperatureSpread))
			.map(WeatherModel::getDay)
			.orElseThrow(() -> new InvalidDataException("Minimal temperature spread could not be calculated"));
	}

	// accessible for test reasons
	DataParser<WeatherModel> getParser() {
		if (this.parser == null) {
			this.parser = new CSVDataParser<>(DataType.WEATHER, this.schema, new TypeReference<List<WeatherModel>>() {
			});
		}
		return this.parser;
	}

}
