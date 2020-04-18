/**
 * 
 */
package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.DoubleSummaryStatistics;

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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		baseValue = BaseValue.jan1_jan4_val200_400_500_200_calc_short(BASE_VALUE_NAME);

		lookbackWindow = 2;
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference() {
		volatilityDifference = new VolatilityDifference(baseValue, null, localDateTime2020Jan02_22_00_00,
				localDateTime2020Jan04_22_00_00, lookbackWindow);
		volatilityDifference2 = new VolatilityDifference(baseValue, null, localDateTime2020Jan02_22_00_00,
				localDateTime2020Jan04_22_00_00, lookbackWindow);

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

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(nullBaseValue, null, localDateTime2020Jan02_22_00_00,
						localDateTime2020Jan04_22_00_00, lookbackWindow),
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

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02_22_00_00,
						localDateTime2020Jan04_22_00_00, lookbackWindowOne),
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
				() -> new VolatilityDifference(baseValue, null, null, localDateTime2020Jan04_22_00_00, lookbackWindow),
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
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02_22_00_00, null, lookbackWindow),
				"endOfReferenceWindow of null is not correctly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#VolatilityDifference(de.rumford.tradingsystem.BaseValue, int)}.
	 */
	@Test
	void testVolatilityDifference_endOfReferenceWindow_before_startOfReferenceWindow() {
		String expectedMessage = "End of reference window value must be after start of reference window value";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan04_22_00_00,
						localDateTime2020Jan02_22_00_00, lookbackWindow),
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
				() -> new VolatilityDifference(baseValue, null, localDateTime2019Dec31_22_00_00,
						localDateTime2020Jan04_22_00_00, lookbackWindow),
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

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02_22_00_00,
						localDateTime2020Jan05_22_00_00, lookbackWindow),
				"Not included endOfReferenceWindow is not correctly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#calculateVolatilityIndices()}.
	 */
	@Test
	void testCalculateVolatilityIndices() {
		StandardDeviation sd = new StandardDeviation();
		double[] values1 = { 200d, 400d };
		double expectedVolatilityValue1 = sd.evaluate(values1);
		double[] values2 = { 400d, 500d };
		double expectedVolatilityValue2 = sd.evaluate(values2);
		double[] values3 = { 500d, 200d };
		double expectedVolatilityValue3 = sd.evaluate(values3);
		ValueDateTupel volatilityIndex1 = new ValueDateTupel(localDateTime2020Jan01_22_00_00, Double.NaN);
		ValueDateTupel volatilityIndex2 = new ValueDateTupel(localDateTime2020Jan02_22_00_00, expectedVolatilityValue1);
		ValueDateTupel volatilityIndex3 = new ValueDateTupel(localDateTime2020Jan03_22_00_00, expectedVolatilityValue2);
		ValueDateTupel volatilityIndex4 = new ValueDateTupel(localDateTime2020Jan04_22_00_00, expectedVolatilityValue3);
		ValueDateTupel[] expectedValues = ValueDateTupel.createEmptyArray();
		expectedValues = ArrayUtils.add(expectedValues, volatilityIndex1);
		expectedValues = ArrayUtils.add(expectedValues, volatilityIndex2);
		expectedValues = ArrayUtils.add(expectedValues, volatilityIndex3);
		expectedValues = ArrayUtils.add(expectedValues, volatilityIndex4);

		volatilityDifference = new VolatilityDifference(baseValue, null, localDateTime2020Jan02_22_00_00,
				localDateTime2020Jan04_22_00_00, lookbackWindow);
		ValueDateTupel[] actualValues = volatilityDifference.getVolatilityIndices();

		assertArrayEquals(expectedValues, actualValues, "Volatility index values are not properly calculated");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#calculateAverageVolatility()}.
	 */
	@Test
	void testCalculateAverageVolatility() {
		StandardDeviation sd = new StandardDeviation();
		DoubleSummaryStatistics stats = new DoubleSummaryStatistics();

		double[] sdValues1 = { 200d, 400d };
		double volatilityValue1 = sd.evaluate(sdValues1); /* ~ 141.42136 */
		stats.accept(volatilityValue1);

		double[] sdValues2 = { 400d, 500d };
		double volatilityValue2 = sd.evaluate(sdValues2); /* ~ 70.71068 */
		stats.accept(volatilityValue2);

		double expectedValue = stats.getAverage(); /* ~ 106.066017 */

		VolatilityDifference volDif = new VolatilityDifference(baseValue, null, localDateTime2020Jan02_22_00_00,
				localDateTime2020Jan03_22_00_00, lookbackWindow);
		double actualValue = volDif.getAverageVolatility();

		assertEquals(expectedValue, actualValue, "The average volatilty is not correctly calculated");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.VolatilityDifference#calculateRawForecast(double)}.
	 */
	@Test
	void testCalculateRawForecast() {
		double currentVolatility = 100d;
		StandardDeviation sd = new StandardDeviation();
		DoubleSummaryStatistics stats = new DoubleSummaryStatistics();

		double[] sdValues1 = { 200d, 400d };
		double volatilityValue1 = sd.evaluate(sdValues1); /* ~ 141.42136 */
		stats.accept(volatilityValue1);

		double[] sdValues2 = { 400d, 500d };
		double volatilityValue2 = sd.evaluate(sdValues2); /* ~ 70.71068 */
		stats.accept(volatilityValue2);

		double expectedValue = stats.getAverage() - currentVolatility; /* ~ 6.066017 */

		VolatilityDifference volDif = new VolatilityDifference(baseValue, null, localDateTime2020Jan02_22_00_00,
				localDateTime2020Jan03_22_00_00, lookbackWindow);
		double actualValue = volDif.calculateRawForecast(currentVolatility);

		assertEquals(expectedValue, actualValue, "Raw Forecast is not correctly calculated");
	}

}
