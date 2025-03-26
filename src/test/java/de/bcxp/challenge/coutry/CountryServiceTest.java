package de.bcxp.challenge.coutry;

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

class CountryServiceTest {

	@Mock
	DataParser<CountryModel> parser;

	CountryService underTest = new CountryService() {
		@Override
		DataParser<CountryModel> getParser() {
			// necessary due to the lack of proper DI and to avoid the usage of Spy
			return CountryServiceTest.this.parser;
		}
	};

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCountryWithHighestPopulationDensity_Successful() {
		// given
		final CountryModel c1 = mockData("country1", 10, 100);
		final CountryModel c2 = mockData("country2", 50, 100);
		final CountryModel c3 = mockData("country3", 20, 100);
		when(this.parser.parseData(Mockito.anyString())).thenReturn(List.of(c1, c2, c3));
		// when
		final var result = this.underTest.countryWithHighestPopulationDensity();
		// then
		assertEquals("country2", result);
	}

	@Test
	void testCountryWithHighestPopulationDensity_WithEmptyList() {
		// given
		when(this.parser.parseData(Mockito.anyString())).thenReturn(List.of());

		// when
		// then
		assertThrows(InvalidDataException.class, () -> this.underTest.countryWithHighestPopulationDensity());
	}

	@Test
	void testCountryWithHighestPopulationDensity_WithZeroCountrySize() {
		// given
		final CountryModel c1 = mockData("country1", 10, 0);
		when(this.parser.parseData(Mockito.anyString())).thenReturn(List.of(c1));

		// when
		// then
		assertThrows(InvalidDataException.class, () -> this.underTest.countryWithHighestPopulationDensity());
	}

	@Test
	void testCountryWithHighestPopulationDensity_WithNegativeCountrySize() {
		// given
		final CountryModel c1 = mockData("country1", 10, -10);
		when(this.parser.parseData(Mockito.anyString())).thenReturn(List.of(c1));

		// when
		// then
		assertThrows(InvalidDataException.class, () -> this.underTest.countryWithHighestPopulationDensity());
	}

	@Test
	void testCountryWithHighestPopulationDensity_WithNegativePopulationSize() {
		// given
		final CountryModel c1 = mockData("country1", -10, 10);
		when(this.parser.parseData(Mockito.anyString())).thenReturn(List.of(c1));

		// when
		// then
		assertThrows(InvalidDataException.class, () -> this.underTest.countryWithHighestPopulationDensity());
	}

	@Test
	void testCountryWithHighestPopulationDensity_WithoutMandatoryFields() {
		// given
		final CountryModel c1 = mockData(null, 10, 100);
		when(this.parser.parseData(Mockito.anyString())).thenReturn(List.of(c1));

		// when
		// then
		assertThrows(InvalidDataException.class, () -> this.underTest.countryWithHighestPopulationDensity());
	}

	// FIXME for more sophisticated test cases ObjectMother would be appropriate
	private static CountryModel mockData(final String name, final long population, final double size) {
		return new CountryModel(name, null, null, population, size, 0, 0, 0);
	}

}
