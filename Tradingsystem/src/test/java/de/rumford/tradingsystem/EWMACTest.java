package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.helper.BaseValueFactory;

/**
 * Test class for {@link EWMAC}.
 * 
 * @author Max Rumford
 *
 */
class EWMACTest {

	static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

	EWMAC ewmac;
	int shortHorizon;
	int longHorizon;
	static BaseValue baseValue;

	static final String BASE_VALUE_NAME = "Base value name";
	static final int BASE_SCALE = 10;

	static LocalDateTime localDateTimeJan01220000;
	static LocalDateTime localDateTimeJan02220000;
	static LocalDateTime localDateTimeJan03220000;
	static LocalDateTime localDateTimeJan04220000;
	static LocalDateTime localDateTimeJan05220000;
	static LocalDateTime localDateTimeJan06220000;
	static LocalDateTime localDateTimeJan07220000;
	static LocalDateTime localDateTimeJan08220000;
	static LocalDateTime localDateTimeJan09220000;
	static LocalDateTime localDateTimeJan10220000;
	static LocalDateTime localDateTimeJan11220000;
	static LocalDateTime localDateTimeJan12220000;
	static LocalDateTime localDateTimeJan13220000;
	static LocalDateTime localDateTimeJan14220000;
	static LocalDateTime localDateTimeJan15220000;
	static LocalDateTime localDateTimeJan16220000;
	static LocalDateTime localDateTimeJan17220000;
	static LocalDateTime localDateTimeJan18220000;
	static LocalDateTime localDateTimeJan19220000;
	static LocalDateTime localDateTimeJan20220000;
	static LocalDateTime localDateTimeJan21220000;
	static LocalDateTime localDateTimeJan22220000;
	static LocalDateTime localDateTimeJan23220000;
	static LocalDateTime localDateTimeJan24220000;
	static LocalDateTime localDateTimeJan25220000;
	static LocalDateTime localDateTimeJan26220000;
	static LocalDateTime localDateTimeJan27220000;
	static LocalDateTime localDateTimeJan28220000;
	static LocalDateTime localDateTimeJan29220000;
	static LocalDateTime localDateTimeJan30220000;
	static LocalDateTime localDateTimeJan31220000;

	@BeforeAll
	static void setUpBeforeClass() {
		baseValue = BaseValueFactory.jan1Jan31calcShort(BASE_VALUE_NAME);

		localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1),
				LocalTime.of(22, 0));
		localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2),
				LocalTime.of(22, 0));
		localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3),
				LocalTime.of(22, 0));
		localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4),
				LocalTime.of(22, 0));
		localDateTimeJan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5),
				LocalTime.of(22, 0));
		localDateTimeJan06220000 = LocalDateTime.of(LocalDate.of(2020, 1, 6),
				LocalTime.of(22, 0));
		localDateTimeJan07220000 = LocalDateTime.of(LocalDate.of(2020, 1, 7),
				LocalTime.of(22, 0));
		localDateTimeJan08220000 = LocalDateTime.of(LocalDate.of(2020, 1, 8),
				LocalTime.of(22, 0));
		localDateTimeJan09220000 = LocalDateTime.of(LocalDate.of(2020, 1, 9),
				LocalTime.of(22, 0));
		localDateTimeJan10220000 = LocalDateTime.of(LocalDate.of(2020, 1, 10),
				LocalTime.of(22, 0));
		localDateTimeJan11220000 = LocalDateTime.of(LocalDate.of(2020, 1, 11),
				LocalTime.of(22, 0));
		localDateTimeJan12220000 = LocalDateTime.of(LocalDate.of(2020, 1, 12),
				LocalTime.of(22, 0));
		localDateTimeJan13220000 = LocalDateTime.of(LocalDate.of(2020, 1, 13),
				LocalTime.of(22, 0));
		localDateTimeJan14220000 = LocalDateTime.of(LocalDate.of(2020, 1, 14),
				LocalTime.of(22, 0));
		localDateTimeJan15220000 = LocalDateTime.of(LocalDate.of(2020, 1, 15),
				LocalTime.of(22, 0));
		localDateTimeJan16220000 = LocalDateTime.of(LocalDate.of(2020, 1, 16),
				LocalTime.of(22, 0));
		localDateTimeJan17220000 = LocalDateTime.of(LocalDate.of(2020, 1, 17),
				LocalTime.of(22, 0));
		localDateTimeJan18220000 = LocalDateTime.of(LocalDate.of(2020, 1, 18),
				LocalTime.of(22, 0));
		localDateTimeJan19220000 = LocalDateTime.of(LocalDate.of(2020, 1, 19),
				LocalTime.of(22, 0));
		localDateTimeJan20220000 = LocalDateTime.of(LocalDate.of(2020, 1, 20),
				LocalTime.of(22, 0));
		localDateTimeJan21220000 = LocalDateTime.of(LocalDate.of(2020, 1, 21),
				LocalTime.of(22, 0));
		localDateTimeJan22220000 = LocalDateTime.of(LocalDate.of(2020, 1, 22),
				LocalTime.of(22, 0));
		localDateTimeJan23220000 = LocalDateTime.of(LocalDate.of(2020, 1, 23),
				LocalTime.of(22, 0));
		localDateTimeJan24220000 = LocalDateTime.of(LocalDate.of(2020, 1, 24),
				LocalTime.of(22, 0));
		localDateTimeJan25220000 = LocalDateTime.of(LocalDate.of(2020, 1, 25),
				LocalTime.of(22, 0));
		localDateTimeJan26220000 = LocalDateTime.of(LocalDate.of(2020, 1, 26),
				LocalTime.of(22, 0));
		localDateTimeJan27220000 = LocalDateTime.of(LocalDate.of(2020, 1, 27),
				LocalTime.of(22, 0));
		localDateTimeJan28220000 = LocalDateTime.of(LocalDate.of(2020, 1, 28),
				LocalTime.of(22, 0));
		localDateTimeJan29220000 = LocalDateTime.of(LocalDate.of(2020, 1, 29),
				LocalTime.of(22, 0));
		localDateTimeJan30220000 = LocalDateTime.of(LocalDate.of(2020, 1, 30),
				LocalTime.of(22, 0));
		localDateTimeJan31220000 = LocalDateTime.of(LocalDate.of(2020, 1, 31),
				LocalTime.of(22, 0));
	}

	@BeforeEach
	void setUp() {
		shortHorizon = 2;
		longHorizon = 8;
		ewmac = new EWMAC(baseValue, null, localDateTimeJan08220000,
				localDateTimeJan10220000, longHorizon, shortHorizon, BASE_SCALE);
	}

	/**
	 * Test method for
	 * {@link EWMAC#EWMAC(BaseValue, Rule[], LocalDateTime, LocalDateTime, int, int, double)}.
	 */
	@Test
	void testEWMAC() {
		EWMAC ewmac2 = new EWMAC(baseValue, null, localDateTimeJan08220000,
				localDateTimeJan10220000, longHorizon, shortHorizon, BASE_SCALE);

		assertEquals(ewmac, ewmac2, "Two identical instances do not equal");
	}

	/**
	 * Test method for {@link EWMAC#validateHorizonValues(LocalDateTime)}.
	 */
	@Test
	void testValidateHorizonValues_longSmallerThanShort_noVariations() {
		int shortHorizonValue = 8;
		int longHorizonValue = 4;
		String expectedMessage = "The long horizon must be greater than the short horizon";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new EWMAC(baseValue, null, localDateTimeJan08220000,
						localDateTimeJan10220000, longHorizonValue, shortHorizonValue,
						BASE_SCALE),
				"Short horizon greater than long horizon is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link EWMAC#validateHorizonValues(LocalDateTime)}.
	 */
	@Test
	void testValidateHorizonValues_longSmallerThanShort_withVariations() {
		EWMAC[] variations = {
				new EWMAC(baseValue, null, localDateTimeJan08220000,
						localDateTimeJan10220000, 8, 4, BASE_SCALE) };
		int shortHorizonValue = 8;
		int longHorizonValue = 4;

		assertTrue(
				new EWMAC(baseValue, variations, localDateTimeJan08220000,
						localDateTimeJan10220000, longHorizonValue, shortHorizonValue,
						BASE_SCALE) instanceof EWMAC,
				"horizon values are not properly ignored when rule has variations");
	}

	/**
	 * Test method for {@link EWMAC#validateHorizonValues(LocalDateTime)}.
	 */
	@Test
	void testValidateHorizonValues_longEqualsShort() {
		int shortHorizonValue = 4;
		int longHorizonValue = 4;
		String expectedMessage = "The long horizon must be greater than the short horizon";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new EWMAC(baseValue, null, localDateTimeJan08220000,
						localDateTimeJan10220000, longHorizonValue, shortHorizonValue,
						BASE_SCALE),
				"Short horizon equal to long horizon is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link EWMAC#validateHorizonValues(LocalDateTime)}.
	 */
	@Test
	void testValidateHorizonValues_shortLessThan2_noVariations() {
		int shortHorizonValue = 1;
		int longHorizonValue = 4;
		String expectedMessage = "The short horizon must not be < 2";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new EWMAC(baseValue, null, localDateTimeJan08220000,
						localDateTimeJan10220000, longHorizonValue, shortHorizonValue,
						BASE_SCALE),
				"Short horizon < 2 is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link EWMAC#validateHorizonValues(LocalDateTime)}.
	 */
	@Test
	void testValidateHorizonValues_shortLessThan2_withVariations() {
		EWMAC[] variations = {
				new EWMAC(baseValue, null, localDateTimeJan08220000,
						localDateTimeJan10220000, 8, 4, BASE_SCALE) };
		int shortHorizonValue = 1;
		int longHorizonValue = 4;

		assertTrue(
				new EWMAC(baseValue, variations, localDateTimeJan08220000,
						localDateTimeJan10220000, longHorizonValue, shortHorizonValue,
						BASE_SCALE) instanceof EWMAC,
				"horizon values are not properly ignored when rule has variations");
	}

	/**
	 * Test method for {@link EWMAC#calculateRawForecast(LocalDateTime)}.
	 */
	@Test
	void testCalculateRawForecast_negativeRawForecast() {
		double expectedValue = -13.177732526197076; // Excel: -13.1777325261971

		double actualValue = ewmac
				.calculateRawForecast(localDateTimeJan13220000);

		assertEquals(expectedValue, actualValue,
				"Negative raw Forecast is not correctly calculated");
	}

	/**
	 * Test method for {@link EWMAC#calculateRawForecast(LocalDateTime)}.
	 */
	@Test
	void testCalculateRawForecast_positiveRawForecast() {
		double expectedValue = 32.171834876807424; // Excel: 32.1718348768074

		double actualValue = ewmac
				.calculateRawForecast(localDateTimeJan08220000);

		assertEquals(expectedValue, actualValue,
				"Positive raw Forecast is not correctly calculated");
	}

}
