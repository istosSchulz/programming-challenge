package de.bcxp.challenge.weather;

import java.io.IOException;
import java.util.Comparator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import de.bcxp.challenge.common.InvalidDataException;

public class WeatherService {

	private static final String DEFAULT_LOCATION = "de/bcxp/challenge/weather.csv";

	public int dayWithSmallestTemperatureSpread() throws IOException {

		final ObjectMapper mapper = new CsvMapper();
		final CsvSchema schema = CsvSchema.emptySchema()
			.withUseHeader(true);

		return mapper.readerFor(WeatherModel.class)
			.with(schema)
			.<WeatherModel>readValues(getClass().getClassLoader()
				.getResourceAsStream(DEFAULT_LOCATION))
			.readAll()
			.stream()
			.min(Comparator.comparingDouble(WeatherModel::getTemperatureSpread))
			.map(WeatherModel::getDay)
			.orElseThrow(() -> new InvalidDataException("Minimal temperature spread could not be calculated"));
	}

}
