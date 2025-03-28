package de.bcxp.challenge.common;

import java.util.List;

/**
 * Generic data importer interface to import {@link T} data from file system.
 *
 * @param <T>
 *            The data type of the data to import.
 */
@FunctionalInterface
public interface DataParser<T> {

	// data type can be used for decoupling and service location. let's say with
	// maven submodule and different parser implementations (not just csv) that can
	// be plugged in e.g. via maven profiles
	/**
	 * Identifier for problem domains supported by imports
	 */
	enum DataType {
		WEATHER, COUNTRY
	}

	/**
	 *
	 * @param source
	 *            The path for file to import and have its data parsed. Depending on
	 *            {@link DataType} different problem domains can be addressed.
	 * @return The parsed data as type {@link List} or empty if there isn't any data
	 * @throws InvalidDataException
	 *             in any error case like import file not found or unparseable data
	 *             within the file.
	 */
	List<T> parseData(final String source) throws InvalidDataException;

}
