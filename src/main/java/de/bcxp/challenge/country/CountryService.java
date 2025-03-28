package de.bcxp.challenge.country;

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
public class CountryService {

	private static final String DEFAULT_LOCATION = "de/bcxp/challenge/countries.csv";

	// FIXME would benefit from DI and relying on DataParser interface instead
	private DataParser<CountryModel> parser;

	// defaults
	private String inputFile = DEFAULT_LOCATION;
	private final CsvSchema schema = CsvSchema.emptySchema()
		.withUseHeader(true)
		.withColumnSeparator(';');

	public CountryService(final String inputFile) {
		this.inputFile = Objects.toString(inputFile, DEFAULT_LOCATION);
	}

	/**
	 * Reads the country data input file from classpath and determines the country
	 * with the highest population density within the parsed data set. Skips
	 * semantically invalid import entries.
	 *
	 * @return The name of the country with the highest population density within
	 *         the imported dataset.</br>
	 *         </br>
	 *         <b>"error"</b> in case of any error happening like an empty data set
	 *         was imported. This can be the case if there isn't any data provided
	 *         within the input or all the imported entries are skipped due to
	 *         validation errors. Also see {@link CSVDataParser#parseData(String).}
	 */
	public String countryWithHighestPopulationDensity() {
		try {
			return getParser().parseData(this.inputFile) // TODO data loading can be done once and cached
				.stream()
				.filter(CountryModel::isValid)
				.max(Comparator.comparingDouble(CountryModel::getPopulationDensity))
				.map(CountryModel::getCountryName)
				.orElseThrow(() -> new InvalidDataException("Maximum population density could not be calculated"));
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
	DataParser<CountryModel> getParser() {
		if (this.parser == null) {
			this.parser = new CSVDataParser<>(DataType.COUNTRY, this.schema, new TypeReference<List<CountryModel>>() {
			});
		}
		return this.parser;
	}

}
