package de.bcxp.challenge.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.bcxp.challenge.common.DataParser;
import de.bcxp.challenge.common.InvalidDataException;

public class WeatherServiceTest {

	@Mock
	DataParser<WeatherModel> parser;

	WeatherService underTest = new WeatherService() {
		@Override
		DataParser<WeatherModel> getParser() {
			return WeatherServiceTest.this.parser; // necessary due to the lack of proper DI and to avoid the usage of
													// Spy
		}
	};

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testDayWithSmallestTemperatureSpread_Successful() {
		// given
		final WeatherModel w1 = mockData(1, 10d, 100d);
		final WeatherModel w2 = mockData(2, 20d, 90d);
		final WeatherModel w3 = mockData(3, 40d, 50d);
		when(this.parser.parseData(Mockito.anyString())).thenReturn(List.of(w1, w2, w3));
		// when
		final var result = this.underTest.dayWithSmallestTemperatureSpread();
		// then
		assertEquals(3, result);
	}

	@Test
	void testDayWithSmallestTemperatureSpread_WithEmptyList() {
		// given
		when(this.parser.parseData(Mockito.anyString())).thenReturn(List.of());

		// when
		// then
		assertThrows(InvalidDataException.class, () -> this.underTest.dayWithSmallestTemperatureSpread());
	}

	@Test
	void testDayWithSmallestTemperatureSpread_WithInvalidTemperatureRange() {
		// given
		final WeatherModel w1 = mockData(1, 100d, 10d);
		when(this.parser.parseData(Mockito.anyString())).thenReturn(List.of(w1));

		// when
		// then
		assertThrows(InvalidDataException.class, () -> this.underTest.dayWithSmallestTemperatureSpread());
	}

	@Test
	void testDayWithSmallestTemperatureSpread_WithDegreeBelowZeroSuccessulf() {
		// given
		final WeatherModel w1 = mockData(1, -15d, 10d);
		final WeatherModel w2 = mockData(2, -10d, 10d);
		final WeatherModel w3 = mockData(3, -10d, -5d);
		when(this.parser.parseData(Mockito.anyString())).thenReturn(List.of(w1, w2, w3));

		// when
		// when
		final var result = this.underTest.dayWithSmallestTemperatureSpread();
		// then
		assertEquals(3, result);
	}

	@Test
	void testDayWithSmallestTemperatureSpread_WithoutMandatoryFields() {
		// given
		final WeatherModel w1 = mockData(2, null, 10d);
		final WeatherModel w2 = mockData(3, 100d, null);
		when(this.parser.parseData(Mockito.anyString())).thenReturn(List.of(w1, w2));

		// when
		// then
		assertThrows(InvalidDataException.class, () -> this.underTest.dayWithSmallestTemperatureSpread());
	}

	// FIXME for more sophisticated test cases ObjectMother would be appropriate
	private static WeatherModel mockData(final int day, final Double tempMin, final Double tempMax) {
		return new WeatherModel(day, tempMax, tempMin, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

}
