package de.rumford.tradingsystem.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link ValueDateTupel}.
 * 
 * @author Max Rumford
 *
 */
class ValueDateTupelTest {

	static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";
	static final String MESSAGE_ARRAY_MUST_NOT_BE_NULL = "Given array must not be null";
	static final String MESSAGE_VALUE_MUST_NOT_BE_NULL = "Given value must not be null";

	static double value1;
	static double value2;
	static double value3;
	static double value4;
	static double value5;
	static LocalDateTime date_20200101;
	static LocalDateTime date_20200102;
	static LocalDateTime date_20200103;
	static LocalDateTime date_20200104;
	static LocalDateTime date_20200105;
	static ValueDateTupel valueDateTupel1;
	static ValueDateTupel valueDateTupel1_;
	static ValueDateTupel valueDateTupel2;
	static ValueDateTupel valueDateTupel3;
	static ValueDateTupel valueDateTupel4;
	static ValueDateTupel valueDateTupel5;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		date_20200101 = LocalDateTime.of(2020, 1, 1, 0, 0);
		date_20200102 = LocalDateTime.of(2020, 1, 2, 0, 0);
		date_20200103 = LocalDateTime.of(2020, 1, 3, 0, 0);
		date_20200104 = LocalDateTime.of(2020, 1, 4, 0, 0);
		date_20200105 = LocalDateTime.of(2020, 1, 5, 0, 0);
		value1 = 100d;
		value2 = 200d;
		value3 = 300d;
		value4 = 400d;
		value5 = 500d;

		valueDateTupel1 = new ValueDateTupel(date_20200101, value1);
		valueDateTupel2 = new ValueDateTupel(date_20200102, value2);
		valueDateTupel3 = new ValueDateTupel(date_20200103, value3);
		valueDateTupel4 = new ValueDateTupel(date_20200104, value4);
		valueDateTupel5 = new ValueDateTupel(date_20200105, value5);
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * Test method for {@link ValueDateTupel#equals(Object)}.
	 */
	@Test
	void testEqualsObject() {
		valueDateTupel1_ = new ValueDateTupel(date_20200101, value1);

		assertEquals(valueDateTupel1, valueDateTupel1_, "Two equal instances of ValueDateTupel are not equal");
	}

	/**
	 * Test method for {@link ValueDateTupel#createEmptyArray()}.
	 */
	@Test
	void testCreateEmptyArray() {
		ValueDateTupel[] expectedArray = new ValueDateTupel[0];

		ValueDateTupel[] actualArray = ValueDateTupel.createEmptyArray();

		assertArrayEquals(expectedArray, actualArray, "A non empty array is created");
	}

	/**
	 * Test method for {@link ValueDateTupel#createEmptyArray(int)}.
	 */
	@Test
	void testCreateEmptyArrayInt() {
		ValueDateTupel[] expectedArray = new ValueDateTupel[2];

		ValueDateTupel[] actualArray = ValueDateTupel.createEmptyArray(2);

		assertArrayEquals(expectedArray, actualArray, "The created array is not as expected");
	}

	/**
	 * Test method for {@link ValueDateTupel#isSortedAscending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedAscending() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };

		assertTrue(ValueDateTupel.isSortedAscending(valueDateTupelArray),
				"The given array is falsly marked as not in ascending order");
	}

	/**
	 * Test method for {@link ValueDateTupel#isSortedAscending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedAscending_notInOrder() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel2 };

		assertFalse(ValueDateTupel.isSortedAscending(valueDateTupelArray),
				"The given array is falsly marked as in ascending order");
	}

	/**
	 * Test method for {@link ValueDateTupel#isSortedAscending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedAscending_twoEqualDates() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel2 };

		assertFalse(ValueDateTupel.isSortedAscending(valueDateTupelArray),
				"The given array is falsly marked as in ascending order");
	}

	/**
	 * Test method for {@link ValueDateTupel#isSortedAscending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedAscending_arrayNull() {
		ValueDateTupel[] valueDateTupelArray = null;
		String expectedMessage = MESSAGE_ARRAY_MUST_NOT_BE_NULL;

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.isSortedAscending(valueDateTupelArray), "A null array is not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link ValueDateTupel#isSortedAscending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedAscending_arrayContainsNull() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, null, valueDateTupel2 };
		String expectedMessage = "The given array must not contain any nulls";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.isSortedAscending(valueDateTupelArray),
				"An array containing null is not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link ValueDateTupel#isSortedDescending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedDescending() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel3, valueDateTupel2, valueDateTupel1 };

		assertTrue(ValueDateTupel.isSortedDescending(valueDateTupelArray),
				"The given array is falsly marked as not in descending order");
	}

	/**
	 * Test method for {@link ValueDateTupel#isSortedDescending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedDescending_notInOrder() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel3, valueDateTupel1, valueDateTupel2 };

		assertFalse(ValueDateTupel.isSortedDescending(valueDateTupelArray),
				"The given array is falsly marked as in descending order");
	}

	/**
	 * Test method for {@link ValueDateTupel#isSortedDescending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedDescending_twoEqualDates() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel2, valueDateTupel2, valueDateTupel1 };

		assertFalse(ValueDateTupel.isSortedDescending(valueDateTupelArray),
				"The given array is falsly marked as in descending order");
	}

	/**
	 * Test method for {@link ValueDateTupel#isSortedDescending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedDescending_arrayNull() {
		ValueDateTupel[] valueDateTupelArray = null;
		String expectedMessage = MESSAGE_ARRAY_MUST_NOT_BE_NULL;

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.isSortedDescending(valueDateTupelArray), "A null array is not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link ValueDateTupel#isSortedDescending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedDescending_arrayContainsNull() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel2, null, valueDateTupel1 };
		String expectedMessage = "The given array must not contain any null LocalDateTime";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.isSortedDescending(valueDateTupelArray),
				"An array containing null is not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link ValueDateTupel#alignDates(ValueDateTupel[][])}.
	 */
	@Test
	void testAlignDates() {
		ValueDateTupel vdtCalculated20200102_200 = new ValueDateTupel(date_20200102, 200d);
		ValueDateTupel vdtCalculated20200104_400 = new ValueDateTupel(date_20200104, 400d);
		ValueDateTupel vdtCalculated20200101_200 = new ValueDateTupel(date_20200101, 200d);
		ValueDateTupel vdtCalculated20200105_400 = new ValueDateTupel(date_20200105, 400d);
		ValueDateTupel vdtCalculated20200102_250 = new ValueDateTupel(date_20200102, 250d);
		ValueDateTupel vdtCalculated20200103_250 = new ValueDateTupel(date_20200103, 250d);
		ValueDateTupel vdtCalculated20200101_300 = new ValueDateTupel(date_20200101, 300d);
		ValueDateTupel vdtCalculated20200102_300 = new ValueDateTupel(date_20200102, 300d);
		ValueDateTupel vdtCalculated20200104_300 = new ValueDateTupel(date_20200104, 300d);
		ValueDateTupel vdtCalculated20200105_300 = new ValueDateTupel(date_20200105, 300d);
		ValueDateTupel[] expectedVdtArray1 = { //
				valueDateTupel1, //
				vdtCalculated20200102_200, //
				valueDateTupel3, //
				vdtCalculated20200104_400, //
				valueDateTupel5 };
		ValueDateTupel[] expectedVdtArray2 = { //
				vdtCalculated20200101_200, //
				valueDateTupel2, //
				valueDateTupel3, //
				valueDateTupel4, //
				vdtCalculated20200105_400 };
		ValueDateTupel[] expectedVdtArray3 = { //
				valueDateTupel1, //
				vdtCalculated20200102_250, //
				vdtCalculated20200103_250, //
				valueDateTupel4, //
				valueDateTupel5 };
		ValueDateTupel[] expectedVdtArray4 = { //
				vdtCalculated20200101_300, //
				vdtCalculated20200102_300, //
				valueDateTupel3, //
				valueDateTupel4, //
				valueDateTupel5 };
		ValueDateTupel[] expectedVdtArray5 = { //
				valueDateTupel1, //
				valueDateTupel2, //
				valueDateTupel3, //
				vdtCalculated20200104_300, //
				vdtCalculated20200105_300 };
		ValueDateTupel[] expectedVdtArray6 = { //
				valueDateTupel1, //
				valueDateTupel2, //
				valueDateTupel3, //
				valueDateTupel4, //
				valueDateTupel5 };
		ValueDateTupel[][] expectedValue = { //
				expectedVdtArray1, //
				expectedVdtArray2, //
				expectedVdtArray3, //
				expectedVdtArray4, //
				expectedVdtArray5, //
				expectedVdtArray6 };

		ValueDateTupel[] vdtArray1 = { valueDateTupel1, valueDateTupel3, valueDateTupel5 };
		ValueDateTupel[] vdtArray2 = { valueDateTupel2, valueDateTupel3, valueDateTupel4 };
		ValueDateTupel[] vdtArray3 = { valueDateTupel1, valueDateTupel4, valueDateTupel5 };
		ValueDateTupel[] vdtArray4 = { valueDateTupel3, valueDateTupel4, valueDateTupel5 };
		ValueDateTupel[] vdtArray5 = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };
		ValueDateTupel[] vdtArray6 = { valueDateTupel1, valueDateTupel2, valueDateTupel3, valueDateTupel4,
				valueDateTupel5 };
		ValueDateTupel[][] vdtArraysArray = { vdtArray1, vdtArray2, vdtArray3, vdtArray4, vdtArray5, vdtArray6 };
		ValueDateTupel[][] actualValue = ValueDateTupel.alignDates(vdtArraysArray);

		assertArrayEquals(expectedValue, actualValue, "Dates aren't correct after aligning ValueDateTuples");
	}

	/**
	 * Test method for {@link ValueDateTupel#alignDates(ValueDateTupel[][])}.
	 */
	@Test
	void testAlignDates_middleMissing() {
		date_20200101 = LocalDateTime.of(2020, 1, 1, 0, 0);
		date_20200102 = LocalDateTime.of(2020, 1, 2, 0, 0);
		date_20200103 = LocalDateTime.of(2020, 1, 3, 0, 0);
		valueDateTupel1 = new ValueDateTupel(date_20200101, value1);
		valueDateTupel2 = new ValueDateTupel(date_20200102, value2);
		valueDateTupel3 = new ValueDateTupel(date_20200103, value3);

		ValueDateTupel[] expectedVdtArray1 = { //
				valueDateTupel1, //
				valueDateTupel2, //
				valueDateTupel3 };
		ValueDateTupel[] expectedVdtArray2 = { //
				new ValueDateTupel(LocalDateTime.of(2020, 1, 1, 0, 0), 999), //
				new ValueDateTupel(LocalDateTime.of(2020, 1, 2, 0, 0), 1099.6), //
				new ValueDateTupel(LocalDateTime.of(2020, 1, 3, 0, 0), 1200.2) };
		ValueDateTupel[][] expectedValue = { //
				expectedVdtArray1, //
				expectedVdtArray2 };

		ValueDateTupel[] vdtArray1 = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };
		ValueDateTupel[] vdtArray2 = { new ValueDateTupel(LocalDateTime.of(2020, 1, 1, 0, 0), 999),
				new ValueDateTupel(LocalDateTime.of(2020, 1, 3, 0, 0), 1200.2) };
		ValueDateTupel[][] vdtArraysArray = { vdtArray1, vdtArray2 };
		ValueDateTupel[][] actualValue = ValueDateTupel.alignDates(vdtArraysArray);

		assertArrayEquals(expectedValue[0], actualValue[0], "Dates aren't correct after aligning ValueDateTuples");
		assertArrayEquals(expectedValue[1], actualValue[1], "Dates aren't correct after aligning ValueDateTuples");
	}

	/**
	 * Test method for {@link ValueDateTupel#alignDates(ValueDateTupel[][])}.
	 */
	@Test
	void testAlignDates_arrayOfArraysNull() {
		String expectedMessage = "Given array of arrays must not be null";

		ValueDateTupel[][] vdtArraysArray = null;
		Exception thrown = assertThrows( //
				IllegalArgumentException.class, //
				() -> ValueDateTupel.alignDates(vdtArraysArray), //
				"Array of arrays = null is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link ValueDateTupel#alignDates(ValueDateTupel[][])}.
	 */
	@Test
	void testAlignDates_arrayNull() {
		String expectedMessage = "The array at position 0 does not meet specifications.";
		String expectedCause = "The given values array must not be null";

		ValueDateTupel[] vdtArray1 = null;
		ValueDateTupel[] vdtArray2 = { valueDateTupel2, valueDateTupel3, valueDateTupel4 };
		ValueDateTupel[][] vdtArraysArray = { vdtArray1, vdtArray2 };
		Exception thrown = assertThrows( //
				IllegalArgumentException.class, //
				() -> ValueDateTupel.alignDates(vdtArraysArray), //
				"null in array of arrays is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_ARRAY_MUST_NOT_BE_NULL);
	}

	/**
	 * Test method for {@link ValueDateTupel#alignDates(ValueDateTupel[][])}.
	 */
	@Test
	void testAlignDates_arrayContainsNull() {
		String expectedMessage = "The array at position 0 does not meet specifications.";
		String expectedCause = "Given values must not contain null.";

		ValueDateTupel[] vdtArray1 = { valueDateTupel1, null, valueDateTupel5 };
		ValueDateTupel[] vdtArray2 = { valueDateTupel2, valueDateTupel3, valueDateTupel4 };
		ValueDateTupel[][] vdtArraysArray = { vdtArray1, vdtArray2 };
		Exception thrown = assertThrows( //
				IllegalArgumentException.class, //
				() -> ValueDateTupel.alignDates(vdtArraysArray), //
				"null in array is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link ValueDateTupel#alignDates(ValueDateTupel[][])}.
	 */
	@Test
	void testAlignDates_arrayNotSortedAscending() {
		String expectedMessage = "The array at position 0 does not meet specifications.";
		String expectedCause = "Given values are not properly sorted or there are non-unique values.";

		ValueDateTupel[] vdtArray1 = { valueDateTupel1, valueDateTupel5, valueDateTupel3 };
		ValueDateTupel[] vdtArray2 = { valueDateTupel2, valueDateTupel3, valueDateTupel4 };
		ValueDateTupel[][] vdtArraysArray = { vdtArray1, vdtArray2 };
		Exception thrown = assertThrows( //
				IllegalArgumentException.class, //
				() -> ValueDateTupel.alignDates(vdtArraysArray), //
				"Unsorted array is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link ValueDateTupel#alignDates(ValueDateTupel[][])}.
	 */
	@Test
	void testAlignDates_onlyNaN() {
		String expectedMessage = "The array at position 0 does not meet specifications.";
		String expectedCause = "Given values must not contain NaN.";

		ValueDateTupel vdt1NaN = new ValueDateTupel(date_20200101, Double.NaN);
		ValueDateTupel vdt3NaN = new ValueDateTupel(date_20200103, Double.NaN);
		ValueDateTupel vdt5NaN = new ValueDateTupel(date_20200105, Double.NaN);

		ValueDateTupel[] vdtArray1 = { vdt1NaN, vdt3NaN, vdt5NaN };
		ValueDateTupel[] vdtArray2 = { valueDateTupel2, valueDateTupel3, valueDateTupel4 };
		ValueDateTupel[][] vdtArraysArray = { vdtArray1, vdtArray2 };
		Exception thrown = assertThrows( //
				IllegalArgumentException.class, //
				() -> ValueDateTupel.alignDates(vdtArraysArray), //
				"Only NaN values in ValueDateTupel is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#contains(ValueDateTupel[], ValueDateTupel)}.
	 */
	@Test
	void testContains() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };

		assertTrue(ValueDateTupel.contains(valueDateTupelArray, valueDateTupel1),
				"An element which is in the array falsely cannot be identified");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#contains(ValueDateTupel[], ValueDateTupel)}.
	 */
	@Test
	void testContains_unknownElement() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };

		assertFalse(ValueDateTupel.contains(valueDateTupelArray, valueDateTupel4),
				"An unknown element is falsely marked as being in the given array");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#contains(ValueDateTupel[], ValueDateTupel)}.
	 */
	@Test
	void testContains_arrayNull() {
		String expectedMessage = MESSAGE_ARRAY_MUST_NOT_BE_NULL;

		ValueDateTupel[] valueDateTupelArray = null;
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.contains(valueDateTupelArray, valueDateTupel4),
				"Array of null is not properly handled.");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#containsDate(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsDate() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };

		assertTrue(ValueDateTupel.containsDate(valueDateTupelArray, date_20200101),
				"A DateTime which is in the array falsely cannot be identified");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#containsDate(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsDate_unknownDate() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };

		assertFalse(ValueDateTupel.containsDate(valueDateTupelArray, date_20200104),
				"An unknown DateTime is falsely marked as being in the given array");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#containsDate(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsDate_arrayNull() {
		String expectedMessage = "Given array must not be null";

		ValueDateTupel[] valueDateTupelArray = null;
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.containsDate(valueDateTupelArray, date_20200101),
				"Null array is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#containsDate(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsDate_dateTimeNull() {
		String expectedMessage = MESSAGE_VALUE_MUST_NOT_BE_NULL;

		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.containsDate(valueDateTupelArray, null),
				"LocalDateTime of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
	 */
	@Test
	void testAddOneAt_position_0() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel2, valueDateTupel3, valueDateTupel4 };
		ValueDateTupel[] expectedValue = { valueDateTupel1, valueDateTupel2, valueDateTupel3, valueDateTupel4 };

		ValueDateTupel[] actualValue = ValueDateTupel.addOneAt(valueDateTupelArray, valueDateTupel1, 0);

		assertArrayEquals(expectedValue, actualValue, "Value cannot be added correctly at position 0");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
	 */
	@Test
	void testAddOneAt_position_end() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };
		ValueDateTupel[] expectedValue = { valueDateTupel1, valueDateTupel2, valueDateTupel3, valueDateTupel4 };

		ValueDateTupel[] actualValue = ValueDateTupel.addOneAt(valueDateTupelArray, valueDateTupel4, 3);

		assertArrayEquals(expectedValue, actualValue, "Value cannot be added correctly at last position");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
	 */
	@Test
	void testAddOneAt_position_between() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };
		ValueDateTupel[] expectedValue = { valueDateTupel1, valueDateTupel2, valueDateTupel3, valueDateTupel4 };

		ValueDateTupel[] actualValue = ValueDateTupel.addOneAt(valueDateTupelArray, valueDateTupel2, 1);

		assertArrayEquals(expectedValue, actualValue, "Value cannot be added correctly at inbetween position");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
	 */
	@Test
	void testAddOneAt_arrayNull() {
		String expectedMessage = "Given array must not be null";

		ValueDateTupel[] valueDateTupelArray = null;
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.addOneAt(valueDateTupelArray, valueDateTupel1, 0),
				"Array of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
	 */
	@Test
	void testAddOneAt_valueNull() {
		String expectedMessage = MESSAGE_VALUE_MUST_NOT_BE_NULL;

		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };
		ValueDateTupel vdtNull = null;
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.addOneAt(valueDateTupelArray, vdtNull, 0),
				"New value of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
	 */
	@Test
	void testAddOneAt_position_negative() {
		String expectedMessage = "Cannot not add a value at position < 0. Given position is -1";

		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };
		int negativePosition = -1;
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.addOneAt(valueDateTupelArray, valueDateTupel1, negativePosition),
				"Position < 0 is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
	 */
	@Test
	void testAddOneAt_position_greater_arrayLength() {
		String expectedMessage = "Cannot add a value at position > 3. Given position is 4.";

		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };
		int tooLargeAPosition = 4;
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.addOneAt(valueDateTupelArray, valueDateTupel1, tooLargeAPosition),
				"Position > array.length is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getElement(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsLocalDateTime() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };
		ValueDateTupel expectedValue = valueDateTupel1;

		ValueDateTupel actualValue = ValueDateTupel.getElement(valueDateTupelArray, date_20200101);

		assertEquals(expectedValue, actualValue, "Value cannot be properly found");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getElement(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsLocalDateTime_dateNotFound() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };

		ValueDateTupel actualValue = ValueDateTupel.getElement(valueDateTupelArray, date_20200102);

		assertNull(actualValue, "Date non existant in array is not properly handled");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getElement(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsLocalDateTime_arrayNull() {
		String expectedMessage = MESSAGE_ARRAY_MUST_NOT_BE_NULL;

		ValueDateTupel[] valueDateTupelArray = null;
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.getElement(valueDateTupelArray, date_20200101),
				"Array of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getElement(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsLocalDateTime_dateToBeFoundNull() {
		String expectedMessage = MESSAGE_VALUE_MUST_NOT_BE_NULL;

		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };
		LocalDateTime dateNull = null;
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.getElement(valueDateTupelArray, dateNull),
				"Date to be found of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link ValueDateTupel#getValues(ValueDateTupel[])}.
	 */
	@Test
	void testGetValues() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };
		double[] expectedValues = { //
				valueDateTupel1.getValue(), // 100
				valueDateTupel3.getValue(), // 300
				valueDateTupel4.getValue() // 400
		};

		double[] actualValues = ValueDateTupel.getValues(valueDateTupelArray);

		assertArrayEquals(expectedValues, actualValues, "Incorrect values are gotten from array");
	}

	/**
	 * Test method for {@link ValueDateTupel#getValues(ValueDateTupel[])}.
	 */
	@Test
	void testGetValues_arrayNull() {
		String expectedMessage = MESSAGE_ARRAY_MUST_NOT_BE_NULL;

		ValueDateTupel[] valueDateTupelArray = null;
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.getValues(valueDateTupelArray), "Null array is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link ValueDateTupel#getDates(ValueDateTupel[])}.
	 */
	@Test
	void testGetDates() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };
		LocalDateTime[] expectedValues = { //
				valueDateTupel1.getDate(), // date_20200101
				valueDateTupel3.getDate(), // date_20200102
				valueDateTupel4.getDate() // date_20200103
		};

		LocalDateTime[] actualValues = ValueDateTupel.getDates(valueDateTupelArray);

		assertArrayEquals(expectedValues, actualValues, "Incorrect values are gotten from array");
	}

	/**
	 * Test method for {@link ValueDateTupel#getDates(ValueDateTupel[])}.
	 */
	@Test
	void testGetDates_arrayNull() {
		String expectedMessage = MESSAGE_ARRAY_MUST_NOT_BE_NULL;

		ValueDateTupel[] valueDateTupelArray = null;
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.getDates(valueDateTupelArray), "Null array is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getElements(ValueDateTupel[], LocalDateTime, LocalDateTime)}.
	 */
	@Test
	void testGetElements() {
		ValueDateTupel[] vdtArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3, valueDateTupel4,
				valueDateTupel5 };
		ValueDateTupel[] expectedValue = { valueDateTupel2, valueDateTupel3, valueDateTupel4 };

		ValueDateTupel[] actualValue = ValueDateTupel.getElements(vdtArray, date_20200102, date_20200104);

		assertArrayEquals(expectedValue, actualValue, "Elements cannot be correctly retrieved.");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getElements(ValueDateTupel[], LocalDateTime, LocalDateTime)}.
	 */
	@Test
	void testGetElements_dtFromNull() {
		ValueDateTupel[] expectedValue = { valueDateTupel1, valueDateTupel2, valueDateTupel3, valueDateTupel4 };

		ValueDateTupel[] vdtArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3, valueDateTupel4,
				valueDateTupel5 };
		ValueDateTupel[] actualValue = ValueDateTupel.getElements(vdtArray, null, date_20200104);

		assertArrayEquals(expectedValue, actualValue, "Elements cannot be correctly retrieved when dtFrom is null.");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getElements(ValueDateTupel[], LocalDateTime, LocalDateTime)}.
	 */
	@Test
	void testGetElements_dtToNull() {
		ValueDateTupel[] expectedValue = { valueDateTupel2, valueDateTupel3, valueDateTupel4, valueDateTupel5 };

		ValueDateTupel[] vdtArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3, valueDateTupel4,
				valueDateTupel5 };
		ValueDateTupel[] actualValue = ValueDateTupel.getElements(vdtArray, date_20200102, null);

		assertArrayEquals(expectedValue, actualValue, "Elements cannot be correctly retrieved when dtTo is null.");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getElements(ValueDateTupel[], LocalDateTime, LocalDateTime)}.
	 */
	@Test
	void testGetElements_dtFromEqualsDtTo() {
		ValueDateTupel[] expectedValue = { valueDateTupel2 };

		ValueDateTupel[] vdtArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3, valueDateTupel4,
				valueDateTupel5 };
		ValueDateTupel[] actualValue = ValueDateTupel.getElements(vdtArray, date_20200102, date_20200102);

		assertArrayEquals(expectedValue, actualValue,
				"Elements cannot be correctly retrieved when dtFrom equals dtTo.");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getElements(ValueDateTupel[], LocalDateTime, LocalDateTime)}.
	 */
	@Test
	void testGetElements_dtFromNotInArray() {
		ValueDateTupel[] vdtArray = { valueDateTupel4, valueDateTupel5 };

		assertNull(ValueDateTupel.getElements(vdtArray, date_20200102, date_20200105),
				"dtFrom not in array is not properly handled.");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getElements(ValueDateTupel[], LocalDateTime, LocalDateTime)}.
	 */
	@Test
	void testGetElements_dtToNotInArray() {
		ValueDateTupel[] vdtArray = { valueDateTupel1, valueDateTupel2 };

		assertNull(ValueDateTupel.getElements(vdtArray, date_20200102, date_20200105),
				"dtTo not in array is not properly handled.");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getPosition(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testGetPosition() {
		int expectedValueFirstPosition = 0;
		int expectedValueLastPosition = 4;

		ValueDateTupel[] vdtArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3, valueDateTupel4,
				valueDateTupel5 };
		int actualValueFirstPosition = ValueDateTupel.getPosition(vdtArray, date_20200101);
		int actualValueLastPosition = ValueDateTupel.getPosition(vdtArray, date_20200105);

		assertEquals(expectedValueLastPosition, actualValueLastPosition, "Position cannot be correctly retrieved");
		assertEquals(expectedValueFirstPosition, actualValueFirstPosition, "Position cannot be correctly retrieved");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getPosition(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testGetPosition_arrayNull() {
		String expectedMessage = MESSAGE_ARRAY_MUST_NOT_BE_NULL;

		ValueDateTupel[] vdtArray = null;
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.getPosition(vdtArray, date_20200102), "Array of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getPosition(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testGetPosition_arrayEmpty() {
		int expectedValue = Integer.MIN_VALUE;

		ValueDateTupel[] vdtArray = {};
		assertEquals(expectedValue, ValueDateTupel.getPosition(vdtArray, date_20200102),
				"Empty array is not properly handled");
	}

	/**
	 * Test method for
	 * {@link ValueDateTupel#getPosition(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testGetPosition_dtToBeFoundNull() {
		String expectedMessage = MESSAGE_VALUE_MUST_NOT_BE_NULL;

		ValueDateTupel[] vdtArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3, valueDateTupel4,
				valueDateTupel5 };
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.getPosition(vdtArray, null), "Date to be found of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}
}
