package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.helper.BaseValueFactory;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * Test class for {@link VolatilityDifference}.
 * 
 * @author Max Rumford
 * 
 */
class VolatilityDifferenceTest {

	static final String BASE_VALUE_NAME = "Base Value";
	static final int BASE_SCALE = 10;

	static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

	BaseValue baseValue;
	int lookbackWindow;
	static ValueDateTupel[] volatilityIndicesArray;

	static LocalDateTime localDateTime2019Dec31220000;
	static LocalDateTime localDateTime2020Jan01220000;
	static LocalDateTime localDateTime2020Jan02220000;
	static LocalDateTime localDateTime2020Jan03220000;
	static LocalDateTime localDateTime2020Jan04220000;
	static LocalDateTime localDateTime2020Jan05220000;
	static LocalDateTime localDateTime2020Jan08220000;
	static LocalDateTime localDateTime2020Jan09220000;
	static LocalDateTime localDateTime2020Jan10220000;
	static LocalDateTime localDateTime2020Jan11220000;
	static LocalDateTime localDateTime2020Jan12220000;
	static LocalDateTime localDateTime2020Jan31220000;

	VolatilityDifference volatilityDifference;
	VolatilityDifference volatilityDifference2;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		localDateTime2019Dec31220000 = LocalDateTime.of(LocalDate.of(2019, 12, 31),
				LocalTime.of(22, 0));
		localDateTime2020Jan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1),
				LocalTime.of(22, 0));
		localDateTime2020Jan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2),
				LocalTime.of(22, 0));
		localDateTime2020Jan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3),
				LocalTime.of(22, 0));
		localDateTime2020Jan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4),
				LocalTime.of(22, 0));
		localDateTime2020Jan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5),
				LocalTime.of(22, 0));
		localDateTime2020Jan08220000 = LocalDateTime.of(LocalDate.of(2020, 1, 8),
				LocalTime.of(22, 0));
		localDateTime2020Jan09220000 = LocalDateTime.of(LocalDate.of(2020, 1, 9),
				LocalTime.of(22, 0));
		localDateTime2020Jan10220000 = LocalDateTime.of(LocalDate.of(2020, 1, 10),
				LocalTime.of(22, 0));
		localDateTime2020Jan11220000 = LocalDateTime.of(LocalDate.of(2020, 1, 11),
				LocalTime.of(22, 0));
		localDateTime2020Jan12220000 = LocalDateTime.of(LocalDate.of(2020, 1, 12),
				LocalTime.of(22, 0));
		localDateTime2020Jan31220000 = LocalDateTime.of(LocalDate.of(2020, 1, 31),
				LocalTime.of(22, 0));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		baseValue = BaseValueFactory.jan1Jan4calcShort(BASE_VALUE_NAME);

		lookbackWindow = 2;
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#VolatilityDifference(BaseValue, VolatilityDifference[], LocalDateTime, LocalDateTime, int, double)}.
	 */
	@Test
	void testVolatilityDifference() {
		volatilityDifference = new VolatilityDifference(baseValue, null,
				localDateTime2020Jan03220000, localDateTime2020Jan04220000, lookbackWindow,
				BASE_SCALE);
		volatilityDifference2 = new VolatilityDifference(baseValue, null,
				localDateTime2020Jan03220000, localDateTime2020Jan04220000, lookbackWindow,
				BASE_SCALE);

		assertEquals(volatilityDifference, volatilityDifference2,
				"Two identical instances are not considered identical");
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#VolatilityDifference(BaseValue, VolatilityDifference[], LocalDateTime, LocalDateTime, int, double)}.
	 */
	@Test
	void testVolatilityDifference_volatilityIndicesGiven() {
		ValueDateTupel volatilityIndex1 = new ValueDateTupel(localDateTime2020Jan01220000,
				Double.NaN);
		ValueDateTupel volatilityIndex2 = new ValueDateTupel(localDateTime2020Jan02220000, 100d);
		ValueDateTupel volatilityIndex3 = new ValueDateTupel(localDateTime2020Jan03220000, 5d);
		ValueDateTupel volatilityIndex4 = new ValueDateTupel(localDateTime2020Jan04220000, 10d);
		volatilityIndicesArray = ValueDateTupel.createEmptyArray();
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex1);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex2);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex3);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex4);

		VolatilityDifference volDif = new VolatilityDifference(baseValue, null,
				localDateTime2020Jan02220000, localDateTime2020Jan04220000, lookbackWindow,
				BASE_SCALE, volatilityIndicesArray);
		VolatilityDifference volDif2 = new VolatilityDifference(baseValue, null,
				localDateTime2020Jan02220000, localDateTime2020Jan04220000, lookbackWindow,
				BASE_SCALE, volatilityIndicesArray);

		assertEquals(volDif, volDif2, "Two identical instances are not considered identical");
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#calculateVolatilityIndices(BaseValue, int)}.
	 */
	@Test
	void testCalculateVolatilityIndices() {
		double expectedVolatilityValue2 = 0.5303300858899106; // Excel: 0.530330085889911
		double expectedVolatilityValue3 = 0.6010407640085653; // Excel: 0.601040764008565
		ValueDateTupel volatilityIndex1 = new ValueDateTupel(localDateTime2020Jan01220000,
				Double.NaN);
		ValueDateTupel volatilityIndex2 = new ValueDateTupel(localDateTime2020Jan02220000,
				Double.NaN);
		ValueDateTupel volatilityIndex3 = new ValueDateTupel(localDateTime2020Jan03220000,
				expectedVolatilityValue2);
		ValueDateTupel volatilityIndex4 = new ValueDateTupel(localDateTime2020Jan04220000,
				expectedVolatilityValue3);
		ValueDateTupel[] expectedValues = ValueDateTupel.createEmptyArray();
		expectedValues = ArrayUtils.add(expectedValues, volatilityIndex1);
		expectedValues = ArrayUtils.add(expectedValues, volatilityIndex2);
		expectedValues = ArrayUtils.add(expectedValues, volatilityIndex3);
		expectedValues = ArrayUtils.add(expectedValues, volatilityIndex4);

		volatilityDifference = new VolatilityDifference(baseValue, null,
				localDateTime2020Jan03220000, localDateTime2020Jan04220000, lookbackWindow,
				BASE_SCALE);
		ValueDateTupel[] actualValues = volatilityDifference.getVolatilityIndices();

		assertArrayEquals(expectedValues, actualValues,
				"Volatility index values are not properly calculated");
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#calculateVolatilityIndices(BaseValue, int)}.
	 */
	@Test
	void testCalculateVolatilityIndices_lessBaseValuesThanLookbackWindow() {
		int lookbackWindowTooGreat = 10;
		String expectedMessage = "The amount of base values must not be smaller than the lookback window. Number of base values: 4, lookback window: 10.";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02220000,
						localDateTime2020Jan04220000, lookbackWindowTooGreat, BASE_SCALE),
				"Too great of a lookback window is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				"Too great of a lookback window is not correctly handled.");
	}

	/**
	 * Test method for {@link VolatilityDifference#validateLookbackWindow(int)}.
	 */
	@Test
	void testValidateLookbackWindow_lookbackWindow_1() {
		int lookbackWindowOne = 1;
		String expectedMessage = "Lookback window must be at least 2";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan03220000,
						localDateTime2020Jan04220000, lookbackWindowOne, BASE_SCALE),
				"Lookback window <= 1 is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#validateVolatilityIndices(ValueDateTupel[])}.
	 */
	@Test
	void testValidateVolatilityIndices_volatilityIndicesArrayNull() {
		String expectedMessage = "Volatility indices must not be null.";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02220000,
						localDateTime2020Jan04220000, lookbackWindow, BASE_SCALE, null),
				"Empty array of volatlilty indices is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#validateVolatilityIndices(ValueDateTupel[])}.
	 */
	@Test
	void testValidateVolatilityIndices_emptyVolatilityIndicesArray() {
		String expectedMessage = "Volatility indices must not be an empty array";

		ValueDateTupel[] emptyVolatilityIndicesArray = ValueDateTupel.createEmptyArray();

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02220000,
						localDateTime2020Jan04220000, lookbackWindow, BASE_SCALE,
						emptyVolatilityIndicesArray),
				"Empty array of volatlilty indices is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#validateVolatilityIndices(ValueDateTupel[])}.
	 */
	@Test
	void testValidateVolatilityIndices_unsortedVolatilityIndicesArray() {
		String expectedMessage = "Given volatility indices are not properly sorted or there are duplicate LocalDateTime values";

		ValueDateTupel volatilityIndex1 = new ValueDateTupel(localDateTime2020Jan01220000,
				Double.NaN);
		ValueDateTupel volatilityIndex2 = new ValueDateTupel(localDateTime2020Jan02220000, 100d);
		ValueDateTupel volatilityIndex3 = new ValueDateTupel(localDateTime2020Jan04220000, 5d);
		ValueDateTupel volatilityIndex4 = new ValueDateTupel(localDateTime2020Jan03220000, 10d);
		volatilityIndicesArray = ValueDateTupel.createEmptyArray();
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex1);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex2);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex3);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex4);

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02220000,
						localDateTime2020Jan04220000, lookbackWindow, BASE_SCALE,
						volatilityIndicesArray),
				"Improperly sorted volatility indices are not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#validateVolatilityIndices(ValueDateTupel[])}.
	 */
	@Test
	void testValidateVolatilityIndices_duplicatesInVolatilityIndicesArray() {
		String expectedMessage = "Given volatility indices are not properly sorted or there are duplicate LocalDateTime values";

		ValueDateTupel volatilityIndex1 = new ValueDateTupel(localDateTime2020Jan01220000,
				Double.NaN);
		ValueDateTupel volatilityIndex2 = new ValueDateTupel(localDateTime2020Jan02220000, 100d);
		ValueDateTupel volatilityIndex3 = new ValueDateTupel(localDateTime2020Jan02220000, 5d);
		ValueDateTupel volatilityIndex4 = new ValueDateTupel(localDateTime2020Jan04220000, 10d);
		volatilityIndicesArray = ValueDateTupel.createEmptyArray();
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex1);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex2);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex3);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex4);

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02220000,
						localDateTime2020Jan04220000, lookbackWindow, BASE_SCALE,
						volatilityIndicesArray),
				"Duplicate volatility indices are not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#validateVolatilityIndices(ValueDateTupel[])}.
	 */
	@Test
	void testValidateVolatilityIndices_startOfReferenceWindowNotInVolatilityIndicesArray() {
		String expectedMessage = "Giving volatility indices do not meet specificiation.";
		String expectedCause = "Given values do not include given start value for time window";

		ValueDateTupel volatilityIndex2 = new ValueDateTupel(localDateTime2020Jan03220000,
				Double.NaN);
		ValueDateTupel volatilityIndex3 = new ValueDateTupel(localDateTime2020Jan04220000, 5d);
		ValueDateTupel volatilityIndex4 = new ValueDateTupel(localDateTime2020Jan05220000, 10d);
		volatilityIndicesArray = ValueDateTupel.createEmptyArray();
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex2);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex3);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex4);

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02220000,
						localDateTime2020Jan04220000, lookbackWindow, BASE_SCALE,
						volatilityIndicesArray),
				"Invalid start of reference window value is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#validateVolatilityIndices(ValueDateTupel[])}.
	 */
	@Test
	void testValidateVolatilityIndices_endOfReferenceWindowNotInVolatilityIndicesArray() {
		String expectedMessage = "Giving volatility indices do not meet specificiation.";
		String expectedCause = "Given values do not include given end value for time window";

		ValueDateTupel volatilityIndex1 = new ValueDateTupel(localDateTime2020Jan01220000,
				Double.NaN);
		ValueDateTupel volatilityIndex2 = new ValueDateTupel(localDateTime2020Jan02220000, 100d);
		ValueDateTupel volatilityIndex3 = new ValueDateTupel(localDateTime2020Jan03220000, 5d);
		volatilityIndicesArray = ValueDateTupel.createEmptyArray();
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex1);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex2);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex3);

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02220000,
						localDateTime2020Jan04220000, lookbackWindow, BASE_SCALE,
						volatilityIndicesArray),
				"Invalid end of reference window value is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#validateVolatilityIndices(ValueDateTupel[])}.
	 */
	@Test
	void testValidateVolatilityIndices_nansInRelevantAreaOfVolatilityIndicesArray() {
		String expectedMessage = "There must not be NaN-Values in the given volatility indices values in the area delimited by startOfReferenceWindow and endOfReferenceWindow";

		ValueDateTupel volatilityIndex1 = new ValueDateTupel(localDateTime2020Jan01220000, 100d);
		ValueDateTupel volatilityIndex2 = new ValueDateTupel(localDateTime2020Jan02220000, 100d);
		ValueDateTupel volatilityIndex3 = new ValueDateTupel(localDateTime2020Jan03220000,
				Double.NaN);
		ValueDateTupel volatilityIndex4 = new ValueDateTupel(localDateTime2020Jan04220000, 10d);
		volatilityIndicesArray = ValueDateTupel.createEmptyArray();
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex1);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex2);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex3);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex4);

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02220000,
						localDateTime2020Jan04220000, lookbackWindow, BASE_SCALE,
						volatilityIndicesArray),
				"NaNs in relevant area of volatility indices are not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link VolatilityDifference#validateVolatilityIndices(ValueDateTupel[])}.
	 */
	@Test
	void testValidateVolatilityIndices_baseValueVolatilityIndicesNotAligned() {
		String expectedMessage = "Base value and volatility index values are not properly aligned."
				+ " Utilize ValueDateTupel.alignDates(ValueDateTupel[][]) before creating a new VolatilityDifference.";

		ValueDateTupel volatilityIndex1 = new ValueDateTupel(localDateTime2020Jan01220000,
				Double.NaN);
		ValueDateTupel volatilityIndex2 = new ValueDateTupel(localDateTime2020Jan02220000, 100d);
		ValueDateTupel volatilityIndex3 = new ValueDateTupel(localDateTime2020Jan03220000, 5d);
		ValueDateTupel volatilityIndex4 = new ValueDateTupel(localDateTime2020Jan04220000, 10d);
		ValueDateTupel volatilityIndex5 = new ValueDateTupel(localDateTime2020Jan05220000, 99d);
		volatilityIndicesArray = ValueDateTupel.createEmptyArray();
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex1);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex2);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex3);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex4);
		volatilityIndicesArray = ArrayUtils.add(volatilityIndicesArray, volatilityIndex5);

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new VolatilityDifference(baseValue, null, localDateTime2020Jan02220000,
						localDateTime2020Jan04220000, lookbackWindow, BASE_SCALE,
						volatilityIndicesArray),
				"Not aligned base value and volatility indices are not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link VolatilityDifference#calculateRawForecast(double)}.
	 */
	@Test
	void testCalculateRawForecast() {
		baseValue = BaseValueFactory.jan1Jan31calcShort(BASE_VALUE_NAME);
		double expectedValue = -0.5604475969404489; /* Excel: -0.560447596940449 */

		VolatilityDifference volDif = new VolatilityDifference(baseValue, null,
				localDateTime2020Jan08220000, localDateTime2020Jan10220000, lookbackWindow,
				BASE_SCALE);
		double actualValue = volDif.calculateRawForecast(localDateTime2020Jan11220000);

		assertEquals(expectedValue, actualValue, "Raw Forecast is not correctly calculated");
	}
}
