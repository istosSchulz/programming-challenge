package de.bcxp.challenge.common;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link DataParser} providing data import in CSV format from
 * local file system.
 *
 * @param <T>
 *            The data type of the data to import.
 */
@RequiredArgsConstructor
public class CSVDataParser<T> implements DataParser<T> {

	// FIXME would profit from proper DI
	private static final ObjectMapper MAPPER = new CsvMapper();

	/*
	 * Local cache of each ObectReader. Almost obsolete for this usecase since it
	 * needs one service that calls one data import per data problem exactly once
	 * but would become beneficial for more complex situations.
	 */
	// FIXME prone to concurrent access issues, ok for this tiny usecase
	private static final Map<DataType, ObjectReader> READER_CACHE = new EnumMap<>(DataType.class);

	/**
	 * Type of the parser. Used for identifying it in consecutive access attempts
	 * from the same problem domain.
	 */
	private final DataType type;
	/**
	 * Individual parser configuration for each problem domain. Default to
	 * {@link #defaultSchema()} if not provided.
	 */
	private final CsvSchema schema; // FIXME should be hidden from the outside as well and fed with by a custom
									// class
	/**
	 * Mapping target class to parse the input data to.
	 */
	private final TypeReference<List<T>> typeReference; // FIXME makeshift solution

	// static Jackson configuration allowing for German and English decimal number
	// formats as well as lenience in data structure by ignoring additional/unknown
	// columns
	static {
		MAPPER.addHandler(new DeserializationProblemHandler() {
			@Override
			public Object handleWeirdStringValue(final DeserializationContext ctxt, final Class<?> targetType,
					final String valueToConvert, final String failureMsg) throws IOException {
				if (targetType == long.class || targetType == Long.class) {
					try {
						return NumberFormat.getInstance(Locale.GERMANY)
							.parse(valueToConvert)
							.longValue();
					} catch (final ParseException e) {
						return NOT_HANDLED;
					}
				} else if (targetType == double.class || targetType == Double.class) {
					try {
						return NumberFormat.getInstance(Locale.GERMANY)
							.parse(valueToConvert)
							.doubleValue();
					} catch (final ParseException e) {
						return NOT_HANDLED;
					}
				}
				return super.handleWeirdStringValue(ctxt, targetType, valueToConvert, failureMsg);
			}
		});
		MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * Imports CSV data from a file addressed by {@code source} parameter and parses
	 * it into a list of provided data type {@link T}.
	 *
	 * @param source
	 *            Parameter addressing the file to be imported from within the
	 *            application's classpath. Path has to be defined relative to the
	 *            applications's <i>src/main/resources</i> folder.
	 *
	 * @return The list of parsed data of type {@link T} or am empty list in case
	 *         import was empty.
	 *
	 * @throws InvalidDataException
	 *             In case of any unsuccessful import attempt. Can happen if the
	 *             import file was not found/not readable, the
	 *             {@link #typeReference} was invalid/null, the provided
	 *             {@code source} parameter is null, the input file is syntactically
	 *             broken or the input data was not compatible with the provided
	 *             type to be parsed to.
	 */
	@Override
	public List<T> parseData(final String source) throws InvalidDataException {
		// @formatter:off
		try {
			if(this.typeReference == null) {
				throw new InvalidDataException("Unable to import file due to provided type reference being null");
			}
			if(source == null) {
				throw new InvalidDataException("Unable to import file due to provided file path being null");
			}
			final var temp = getDataReader(this.schema != null ? this.schema : defaultSchema())
					.readValues(getClass().getClassLoader()
							.getResourceAsStream(source))
					.readAll();
			return MAPPER.convertValue(temp, this.typeReference); // necessary workaround due to type erasure with Java Generics
		} catch (IOException | IllegalArgumentException e) {
			// FIXME the Exception contains valid information about why Jackson isn't able to parse the data, this be important for debugging and should be put into some persistent log ideally
			throw new InvalidDataException("Unable to import file " + source, e);
		}
		// @formatter:on
	}

	private CsvSchema defaultSchema() {
		return CsvSchema.emptySchema()
			.withHeader();
	}

	private ObjectReader getDataReader(final CsvSchema schema) {
		return READER_CACHE.computeIfAbsent(this.type, k -> MAPPER.readerFor(new TypeReference<T>() {
		})
			.with(schema));
	}

}
