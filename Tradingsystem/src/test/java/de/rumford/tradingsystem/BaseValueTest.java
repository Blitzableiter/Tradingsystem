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

import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * Test class for {@link BaseValue}.
 * 
 * @author Max Rumford
 *
 */
class BaseValueTest {

	static final String NAME_OF_TEST_BASE_VALUES = "Test Base Value";
	static final String EMPTY_STRING = "";
	static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

	final static int NUMBER_OF_VALUES = 4;
	static ValueDateTupel valuedatetupel1;
	static ValueDateTupel valuedatetupel2;
	static ValueDateTupel valuedatetupel3;
	static ValueDateTupel valuedatetupel4;
	static ValueDateTupel valuedatetupel5;
	static ValueDateTupel valuedatetupel6;
	static ValueDateTupel valuedatetupel7;
	static ValueDateTupel valuedatetupel8;

	static ValueDateTupel[] values;
	static ValueDateTupel[] shortValues;
	static ValueDateTupel[] emptyValues;

	BaseValue baseValue;
	BaseValue baseValue2;

	static LocalDateTime localDateTimeJan01_22_00_00;
	static LocalDateTime localDateTimeJan02_22_00_00;
	static LocalDateTime localDateTimeJan03_22_00_00;
	static LocalDateTime localDateTimeJan04_22_00_00;
	static LocalDateTime localDateTimeJan05_22_00_00;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		localDateTimeJan01_22_00_00 = LocalDateTime
				.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		localDateTimeJan02_22_00_00 = LocalDateTime
				.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		localDateTimeJan03_22_00_00 = LocalDateTime
				.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		localDateTimeJan04_22_00_00 = LocalDateTime
				.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		localDateTimeJan05_22_00_00 = LocalDateTime
				.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));

		valuedatetupel1 = new ValueDateTupel(localDateTimeJan01_22_00_00,
				200d);
		valuedatetupel2 = new ValueDateTupel(localDateTimeJan02_22_00_00,
				400d);
		valuedatetupel3 = new ValueDateTupel(localDateTimeJan03_22_00_00,
				500d);
		valuedatetupel4 = new ValueDateTupel(localDateTimeJan04_22_00_00,
				400d);
		valuedatetupel5 = new ValueDateTupel(localDateTimeJan01_22_00_00,
				1000d);
		valuedatetupel6 = new ValueDateTupel(localDateTimeJan02_22_00_00,
				500d);
		valuedatetupel7 = new ValueDateTupel(localDateTimeJan03_22_00_00,
				375d);
		valuedatetupel8 = new ValueDateTupel(localDateTimeJan04_22_00_00,
				450d);

		shortValues = ValueDateTupel.createEmptyArray();
		shortValues = ArrayUtils.add(shortValues, valuedatetupel5);
		shortValues = ArrayUtils.add(shortValues, valuedatetupel6);
		shortValues = ArrayUtils.add(shortValues, valuedatetupel7);
		shortValues = ArrayUtils.add(shortValues, valuedatetupel8);

		emptyValues = ValueDateTupel.createEmptyArray();
	}

	@BeforeEach
	void setUp() throws Exception {
		values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);

		shortValues = ValueDateTupel.createEmptyArray();
		shortValues = ArrayUtils.add(shortValues, valuedatetupel5);
		shortValues = ArrayUtils.add(shortValues, valuedatetupel6);
		shortValues = ArrayUtils.add(shortValues, valuedatetupel7);
		shortValues = ArrayUtils.add(shortValues, valuedatetupel8);
	}

	/**
	 * Test method for {@link BaseValue#BaseValue(String, ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_name_values() {
		baseValue = new BaseValue(NAME_OF_TEST_BASE_VALUES, values);
		baseValue2 = new BaseValue(NAME_OF_TEST_BASE_VALUES, values);

		assertEquals(baseValue, baseValue2,
				"Two instances with the same contents are not equal");
	}

	/**
	 * Test method for {@link BaseValue#BaseValue(String, ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_nullName() {
		String nullName = null;
		String expectedMessage = "The given name must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(nullName, values),
				"Empty name not properly rejected");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link BaseValue#BaseValue(String, ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_nullValues() {
		ValueDateTupel[] nullValues = null;
		String expectedMessage = "The given values array must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(NAME_OF_TEST_BASE_VALUES, nullValues),
				"Empty name not properly rejected");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link BaseValue#BaseValue(String, ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_emptyName() {
		String expectedMessage = "Name must not be an empty String";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(EMPTY_STRING, values),
				"Empty name not properly rejected");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link BaseValue#BaseValue(String, ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_emptyValues() {
		String expectedMessage = "Values must not be an empty array";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(NAME_OF_TEST_BASE_VALUES, emptyValues),
				"Empty values not properly rejected");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link BaseValue#BaseValue(String, ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_duplicateDatesInValues() {
		String expectedMessage = "Given values are not properly sorted or there are non-unique values.";
		values = ArrayUtils.add(values, valuedatetupel1);

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(NAME_OF_TEST_BASE_VALUES, values),
				"Duplicate date/time values are not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link BaseValue#BaseValue(String, ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_datesInIncorrectOrder() {
		String expectedMessage = "Given values are not properly sorted or there are non-unique values.";
		values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel4);

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(NAME_OF_TEST_BASE_VALUES, values),
				"Date/time values in incorrect order are not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link BaseValue#BaseValue(String, ValueDateTupel[], ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_name_values_shortIndexValues() {
		baseValue = new BaseValue(NAME_OF_TEST_BASE_VALUES, values,
				shortValues);
		baseValue2 = new BaseValue(NAME_OF_TEST_BASE_VALUES, values,
				shortValues);

		assertEquals(baseValue, baseValue2,
				"Two instances with the same contents are not equal");
	}

	/**
	 * Test method for
	 * {@link BaseValue#BaseValue(String, ValueDateTupel[], ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_emptyName_values_shortIndexValues() {
		String expectedMessage = "Name must not be an empty String";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(EMPTY_STRING, values, shortValues),
				"Empty name not properly rejected");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link BaseValue#BaseValue(String, ValueDateTupel[], ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_name_emptyValues_shortIndexValues() {
		String expectedMessage = "Values must not be an empty array";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(NAME_OF_TEST_BASE_VALUES, emptyValues,
						shortValues),
				"Empty values not properly rejected");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link BaseValue#BaseValue(String, ValueDateTupel[], ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_name_values_shortIndexValues_null() {
		String expectedMessage = "Given short index values do not meet the specifications.";
		String expectedCauseMessage = "The given values array must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(NAME_OF_TEST_BASE_VALUES, values, null),
				"Empty short index values not properly rejected");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCauseMessage, thrown.getCause().getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link BaseValue#BaseValue(String, ValueDateTupel[], ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_name_values_emptyShortIndexValues() {
		String expectedMessage = "Given short index values do not meet the specifications.";
		String expectedCauseMessage = "Values must not be an empty array";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(NAME_OF_TEST_BASE_VALUES, values, emptyValues),
				"Empty short index values not properly rejected");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCauseMessage, thrown.getCause().getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link BaseValue#BaseValue(String, ValueDateTupel[], ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_name_values_duplicateShortIndexValues() {
		String expectedMessage = "Given short index values do not meet the specifications.";
		String expectedCauseMessage = "Given values are not properly sorted or there are non-unique values.";
		shortValues = ArrayUtils.add(shortValues, valuedatetupel1);

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(NAME_OF_TEST_BASE_VALUES, values, shortValues),
				"Duplicate date/time values in short index values are not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCauseMessage, thrown.getCause().getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link BaseValue#validateInput(ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_name_values_nanInShortIndexValues() {
		String expectedMessage = "Given short index values do not meet the specifications.";
		String expectedCauseMessage = "Given values must not contain NaN.";
		shortValues = ArrayUtils.add(shortValues,
				new ValueDateTupel(localDateTimeJan05_22_00_00, Double.NaN));

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(NAME_OF_TEST_BASE_VALUES, values, shortValues),
				"NaN values in short index values are not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCauseMessage, thrown.getCause().getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link BaseValue#validateInput(ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_name_values_nullInShortIndexValues() {
		String expectedMessage = "Given short index values do not meet the specifications.";
		String expectedCauseMessage = "Given values must not contain null.";
		shortValues = ArrayUtils.add(shortValues, null);

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new BaseValue(NAME_OF_TEST_BASE_VALUES, values, shortValues),
				"nulls in short index values are not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCauseMessage, thrown.getCause().getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link BaseValue#calculateShortIndexValues(ValueDateTupel[])}.
	 */
	@Test
	void testCalculateShortIndexValues() {
		baseValue = new BaseValue(NAME_OF_TEST_BASE_VALUES, values);

		ValueDateTupel[] actualValues = baseValue.getShortIndexValues();

		assertArrayEquals(shortValues, actualValues,
				"The calculated short index values are not as expected");
	}

	/**
	 * Test method for
	 * {@link BaseValue#calculateShortIndexValues(ValueDateTupel[])}.
	 */
	@Test
	void testCalculateShortIndexValues_2valuesInBaseValue() {
		values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		shortValues = ValueDateTupel.createEmptyArray();
		shortValues = ArrayUtils.add(shortValues, valuedatetupel5);
		shortValues = ArrayUtils.add(shortValues, valuedatetupel6);

		baseValue = new BaseValue(NAME_OF_TEST_BASE_VALUES, values);
		ValueDateTupel[] actualValues = baseValue.getShortIndexValues();

		assertArrayEquals(shortValues, actualValues,
				"The calculated short index values are not as expected");
	}
}
