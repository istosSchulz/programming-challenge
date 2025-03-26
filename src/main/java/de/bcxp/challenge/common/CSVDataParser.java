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

@RequiredArgsConstructor
public class CSVDataParser<T> implements DataParser<T> {

	// FIXME would profit from proper DI
	private static final ObjectMapper MAPPER = new CsvMapper();

	// FIXME prone to concurrent access issues, ok for this tiny usecase
	private static final Map<DataType, ObjectReader> READER_CACHE = new EnumMap<>(DataType.class);

	private final DataType type;
	private final CsvSchema schema;
	private final TypeReference<List<T>> typeReference;

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

	@Override
	public List<T> parseData(final String source) {
		// @formatter:off
		try {
			final var temp = getDataReader(this.schema != null ? this.schema : defaultSchema())
					.readValues(getClass().getClassLoader()
							.getResourceAsStream(source))
					.readAll();
			return MAPPER.convertValue(temp, this.typeReference); // necessary workaround due to type erasure with Java Generics
		} catch (final IOException | NullPointerException e) {
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
