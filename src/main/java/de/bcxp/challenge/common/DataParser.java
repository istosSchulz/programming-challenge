package de.bcxp.challenge.common;

import java.util.List;

public interface DataParser<T> {

	// data type can be used for decoupling and service location. let's say with
	// maven submodule and different parser implementations (not just csv) that can
	// be plugged in e.g. via maven profiles
	enum DataType {
		WEATHER, COUNTRY
	}

	List<T> parseData(final String source);

}
