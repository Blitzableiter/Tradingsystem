/**
 * 
 */
package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * de.rumford.tradingsystem
 * 
 * @author Max Rumford
 *
 */
class VolatilityDifferenceTest {

	final static String BASE_VALUE_NAME = "Base Value";

	BaseValue baseValue;
	int lookbackWindow;
	ValueDateTupel[] values;

	static ValueDateTupel valuedatetupel1;
	static ValueDateTupel valuedatetupel2;
	static ValueDateTupel valuedatetupel3;
	static ValueDateTupel valuedatetupel4;

	static LocalDateTime localDateTime2019Dec31_22_00_00;
	static LocalDateTime localDateTime2020Jan01_22_00_00;
	static LocalDateTime localDateTime2020Jan02_22_00_00;
	static LocalDateTime localDateTime2020Jan03_22_00_00;
	static LocalDateTime localDateTime2020Jan04_22_00_00;
	static LocalDateTime localDateTime2020Jan05_22_00_00;

	VolatilityDifference volatilityDifference;
	VolatilityDifference volatilityDifference2;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		localDateTime2019Dec31_22_00_00 = LocalDateTime.of(LocalDate.of(2019, 12, 31), LocalTime.of(22, 0));
		localDateTime2020Jan01_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		localDateTime2020Jan02_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		localDateTime2020Jan03_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		localDateTime2020Jan04_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		localDateTime2020Jan05_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));

		valuedatetupel1 = new ValueDateTupel(localDateTime2020Jan01_22_00_00, 200d);
		valuedatetupel2 = new ValueDateTupel(localDateTime2020Jan02_22_00_00, 400d);
		valuedatetupel3 = new ValueDateTupel(localDateTime2020Jan03_22_00_00, 500d);
		valuedatetupel4 = new ValueDateTupel(localDateTime2020Jan04_22_00_00, 200d);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);

		baseValue = new BaseValue(BASE_VALUE_NAME, values);

		lookbackWindow = 2;
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference() {
		volatilityDifference = new VolatilityDifference(baseValue, lookbackWindow, localDateTime2020Jan02_22_00_00,
				localDateTime2020Jan04_22_00_00);
		volatilityDifference2 = new VolatilityDifference(baseValue, lookbackWindow, localDateTime2020Jan02_22_00_00,
				localDateTime2020Jan04_22_00_00);

		assertEquals(volatilityDifference, volatilityDifference2,
				"Two identical instances are not considered identical");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference_baseValue_null() {
		BaseValue nullBaseValue = null;
		String expectedMessage = "Base value must not be null";

		Exception thrown = assertThrows(
				IllegalArgumentException.class, () -> new VolatilityDifference(nullBaseValue, lookbackWindow,
						localDateTime2020Jan02_22_00_00, localDateTime2020Jan04_22_00_00),
				"Base value of null is not correctly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference_lookbackWindow_1() {
		int lookbackWindowOne = 1;
		String expectedMessage = "Lookback window must be at least 2";

		Exception thrown = assertThrows(
				IllegalArgumentException.class, () -> new VolatilityDifference(baseValue, lookbackWindowOne,
						localDateTime2020Jan02_22_00_00, localDateTime2020Jan04_22_00_00),
				"Lookback window <= 1 is not correctly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference_startOfReferenceWindow_null() {
		String expectedMessage = "Start of reference window value must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, lookbackWindow, null, localDateTime2020Jan04_22_00_00),
				"startOfReferenceWindow of null is not correctly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference_endOfReferenceWindow_null() {
		String expectedMessage = "End of reference window value must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, lookbackWindow, localDateTime2020Jan02_22_00_00, null),
				"endOfReferenceWindow of null is not correctly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference_endOfReferenceWindow_before_startOfReferenceWindow() {
		String expectedMessage = "End of reference window value must not be before start of reference window value";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, lookbackWindow, localDateTime2020Jan04_22_00_00,
						localDateTime2020Jan02_22_00_00),
				"endOfReferenceWindow before startOfReferenceWindow is not correctly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference_illegalStartOfReferenceWindow() {
		String expectedMessage = "Base values do not include given start value for reference window";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, lookbackWindow, localDateTime2019Dec31_22_00_00,
						localDateTime2020Jan04_22_00_00),
				"Not included startOfReferenceWindow is not correctly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference_illegalEndOfReferenceWindow() {
		String expectedMessage = "Base values do not include given end value for reference window";

		Exception thrown = assertThrows(
				IllegalArgumentException.class, () -> new VolatilityDifference(baseValue, lookbackWindow,
						localDateTime2020Jan02_22_00_00, localDateTime2020Jan05_22_00_00),
				"Not included endOfReferenceWindow is not correctly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#calculateRawForecast(double)}.
	 */
	@Test
	void testCalculateRawForecast() {
		double[] sdValues = { 200d, 400d };
		StandardDeviation sd = new StandardDeviation();
		double expectedValue = sd.evaluate(sdValues) - 100d; /* ~41.42136 */
		/* TODO */
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#calculateVolatilityIndices()}.
	 */
	@Test
	void testGetVolatilityIndices() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#calculateAverageVolatility()}.
	 */
	@Test
	void testCalculateAverageVolatility() {
		double[] sdValues = { 200d, 400d };
		StandardDeviation sd = new StandardDeviation();
		double expectedValue = sd.evaluate(sdValues); /* ~141.42136 */

		VolatilityDifference volDif = new VolatilityDifference(baseValue, lookbackWindow,
				localDateTime2020Jan02_22_00_00, localDateTime2020Jan03_22_00_00);
		double actualValue = volDif.getAverageVolatility();

		assertEquals(expectedValue, actualValue, "The average volatilty is not correctly calculated");
	}

}
