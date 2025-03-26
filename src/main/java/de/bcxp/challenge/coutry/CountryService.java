package de.bcxp.challenge.coutry;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Locale;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import de.bcxp.challenge.common.InvalidDataException;

public class CountryService {

	private static final String DEFAULT_LOCATION = "de/bcxp/challenge/countries.csv";

	public String countryWithHighestPopulationDensity() throws IOException {

		final ObjectMapper mapper = new CsvMapper().addHandler(new DeserializationProblemHandler() {
			@Override
			public Object handleWeirdStringValue(final DeserializationContext ctxt, final Class<?> targetType,
					final String valueToConvert, final String failureMsg) throws IOException {
				if (targetType == long.class) {
					try {
						return NumberFormat.getInstance(Locale.GERMANY)
							.parse(valueToConvert)
							.longValue();
					} catch (final ParseException e) {
						return NOT_HANDLED;
					}
				}
				return super.handleWeirdStringValue(ctxt, targetType, valueToConvert, failureMsg);
			}
		});
		final CsvSchema schema = CsvSchema.emptySchema()
			.withColumnSeparator(';')
			.withUseHeader(true);

		return mapper.readerFor(CountryModel.class)
			.with(schema)
			.<CountryModel>readValues(getClass().getClassLoader()
				.getResourceAsStream(DEFAULT_LOCATION))
			.readAll()
			.stream()
			.max(Comparator.comparingDouble(CountryModel::getPopulationDensity))
			.map(CountryModel::getCountryName)
			.orElseThrow(() -> new InvalidDataException("Maximal population density could not be calculated"));
	}

}
