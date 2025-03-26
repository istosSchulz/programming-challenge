package de.bcxp.challenge.coutry;

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
import lombok.SneakyThrows;

@NoArgsConstructor
public class CountryService {

	private static final String DEFAULT_LOCATION = "de/bcxp/challenge/countries.csv";

	// FIXME would benefit from DI and relying on DataParser interface instead
	private CSVDataParser<CountryModel> parser;

	// defaults
	private String inputFile = DEFAULT_LOCATION;
	private final CsvSchema schema = CsvSchema.emptySchema()
		.withUseHeader(true)
		.withColumnSeparator(';');

	public CountryService(final String inputFile) {
		this.inputFile = Objects.toString(inputFile, DEFAULT_LOCATION);
	}

	@SneakyThrows // TODO exception handling
	public String countryWithHighestPopulationDensity() {

		return getParser().parseData(inputFile) // TODO data loading can be done once and cached
			.stream()
			.filter(CountryModel::isValid)
			.max(Comparator.comparingDouble(CountryModel::getPopulationDensity))
			.map(CountryModel::getCountryName)
			.orElseThrow(() -> new InvalidDataException("Maximum population density could not be calculated"));
	}

	// accessible for test reasons
	DataParser<CountryModel> getParser() {
		if (this.parser == null) {
			this.parser = new CSVDataParser<>(DataType.COUNTRY, this.schema, new TypeReference<List<CountryModel>>() {
			});
		}
		return this.parser;
	}

}
