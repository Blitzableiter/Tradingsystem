/**
 * 
 */
package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.commons.lang3.ArrayUtils;
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

	static LocalDateTime localDateTimeJan01_22_00_00;
	static LocalDateTime localDateTimeJan02_22_00_00;
	static LocalDateTime localDateTimeJan03_22_00_00;
	static LocalDateTime localDateTimeJan04_22_00_00;

	VolatilityDifference volatilityDifference;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		localDateTimeJan01_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		localDateTimeJan02_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		localDateTimeJan03_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		localDateTimeJan04_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));

		valuedatetupel1 = new ValueDateTupel(localDateTimeJan01_22_00_00, 200d);
		valuedatetupel2 = new ValueDateTupel(localDateTimeJan02_22_00_00, 400d);
		valuedatetupel3 = new ValueDateTupel(localDateTimeJan03_22_00_00, 500d);
		valuedatetupel4 = new ValueDateTupel(localDateTimeJan04_22_00_00, 200d);
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
		volatilityDifference = new VolatilityDifference(baseValue, lookbackWindow);
		VolatilityDifference volatilityDifference2 = new VolatilityDifference(baseValue, lookbackWindow);

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

		assertThrows(IllegalArgumentException.class, () -> new VolatilityDifference(nullBaseValue, lookbackWindow),
				"Base value of null is not correctly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference_baseValue_noValues() {
		ValueDateTupel[] emptyValueDateTupelArray = ValueDateTupel.createEmptyArray();
		BaseValue emptyBaseValue = new BaseValue(BASE_VALUE_NAME, emptyValueDateTupelArray);

		assertThrows(IllegalArgumentException.class, () -> new VolatilityDifference(emptyBaseValue, lookbackWindow),
				"Base value without values is not correctly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference_lookbackWindow_1() {
		int lookbackWindowOne = 1;

		assertThrows(IllegalArgumentException.class, () -> new VolatilityDifference(baseValue, lookbackWindowOne),
				"Lookback window <= 1 is not correctly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#calculateRawForecast(double)}.
	 */
	@Test
	void testCalculateRawForecast() {
		
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
		double expectedValue = 100d;

		double actualValue = new VolatilityDifference(baseValue, lookbackWindow).getAverageVolatility();

		assertEquals(expectedValue, actualValue, "The average volatilty is not correctly calculated");
	}

}
