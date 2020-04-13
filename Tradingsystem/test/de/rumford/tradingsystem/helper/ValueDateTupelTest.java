package de.rumford.tradingsystem.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValueDateTupelTest {

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
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#equals(Object)}.
	 */
	@Test
	void testEqualsObject() {
		valueDateTupel1_ = new ValueDateTupel(date_20200101, value1);

		assertEquals(valueDateTupel1, valueDateTupel1_, "Two equal instances of ValueDateTupel are not equal");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#createEmptyArray()}.
	 */
	@Test
	void testCreateEmptyArray() {
		ValueDateTupel[] expectedArray = new ValueDateTupel[0];

		ValueDateTupel[] actualArray = ValueDateTupel.createEmptyArray();

		assertArrayEquals(expectedArray, actualArray, "A non empty array is created");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#createEmptyArray(int)}.
	 */
	@Test
	void testCreateEmptyArrayInt() {
		ValueDateTupel[] expectedArray = new ValueDateTupel[2];

		ValueDateTupel[] actualArray = ValueDateTupel.createEmptyArray(2);

		assertArrayEquals(expectedArray, actualArray, "The created array is not as expected");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#alignDates(ValueDateTupel[][])}.
	 */
	// FIXME
	@Test
	void testAlignDates() {
		ValueDateTupel[] expectedArray = new ValueDateTupel[0];

		ValueDateTupel[] actualArray = ValueDateTupel.createEmptyArray();

		assertArrayEquals(expectedArray, actualArray, "A non empty array is created");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#isSortedAscending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedAscending() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };

		assertTrue(ValueDateTupel.isSortedAscending(valueDateTupelArray),
				"The given array is falsly marked as not in ascending order");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#isSortedAscending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedAscending_notInOrder() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel2 };

		assertFalse(ValueDateTupel.isSortedAscending(valueDateTupelArray),
				"The given array is falsly marked as in ascending order");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#isSortedAscending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedAscending_twoEqualDates() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel2 };

		assertFalse(ValueDateTupel.isSortedAscending(valueDateTupelArray),
				"The given array is falsly marked as in ascending order");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#isSortedAscending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedAscending_arrayNull() {
		ValueDateTupel[] valueDateTupelArray = null;
		String expectedMessage = "The given array must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.isSortedAscending(valueDateTupelArray), "A null array is not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#isSortedAscending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedAscending_arrayContainsNull() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, null, valueDateTupel2 };
		String expectedMessage = "The given array must not contain any null values";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.isSortedAscending(valueDateTupelArray),
				"An array containing null is not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#isSortedDescending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedDescending() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel3, valueDateTupel2, valueDateTupel1 };

		assertTrue(ValueDateTupel.isSortedDescending(valueDateTupelArray),
				"The given array is falsly marked as not in descending order");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#isSortedDescending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedDescending_notInOrder() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel3, valueDateTupel1, valueDateTupel2 };

		assertFalse(ValueDateTupel.isSortedDescending(valueDateTupelArray),
				"The given array is falsly marked as in descending order");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#isSortedDescending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedDescending_twoEqualDates() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel2, valueDateTupel2, valueDateTupel1 };

		assertFalse(ValueDateTupel.isSortedDescending(valueDateTupelArray),
				"The given array is falsly marked as in descending order");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#isSortedDescending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedDescending_arrayNull() {
		ValueDateTupel[] valueDateTupelArray = null;
		String expectedMessage = "The given array must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.isSortedDescending(valueDateTupelArray), "A null array is not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#isSortedDescending(ValueDateTupel[])}.
	 */
	@Test
	void testIsSortedDescending_arrayContainsNull() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel2, null, valueDateTupel1 };
		String expectedMessage = "The given array must not contain any null values";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.isSortedDescending(valueDateTupelArray),
				"An array containing null is not properly handled");
		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#alignDates(ValueDateTupel[][])}.
	 */
	@Test
	void testalignDates() {
		ValueDateTupel vdt1NaN = new ValueDateTupel(date_20200101, Double.NaN);
		ValueDateTupel vdt2NaN = new ValueDateTupel(date_20200102, Double.NaN);
		ValueDateTupel vdt3NaN = new ValueDateTupel(date_20200103, Double.NaN);
		ValueDateTupel vdt4NaN = new ValueDateTupel(date_20200104, Double.NaN);
		ValueDateTupel vdt5NaN = new ValueDateTupel(date_20200105, Double.NaN);
		ValueDateTupel[] expectedVdtArray1 = { valueDateTupel1, vdt2NaN, valueDateTupel3, vdt4NaN, valueDateTupel5 };
		ValueDateTupel[] expectedVdtArray2 = { vdt1NaN, valueDateTupel2, valueDateTupel3, valueDateTupel4, vdt5NaN };
		ValueDateTupel[] expectedVdtArray3 = { valueDateTupel1, vdt2NaN, vdt3NaN, valueDateTupel4, valueDateTupel5 };
		ValueDateTupel[] expectedVdtArray4 = { vdt1NaN, vdt2NaN, valueDateTupel3, valueDateTupel4, valueDateTupel5 };
		ValueDateTupel[] expectedVdtArray5 = { valueDateTupel1, valueDateTupel2, valueDateTupel3, vdt4NaN, vdt5NaN };
		ValueDateTupel[][] expectedValue = { expectedVdtArray1, expectedVdtArray2, expectedVdtArray3, expectedVdtArray4,
				expectedVdtArray5 };

		ValueDateTupel[] vdtArray1 = { valueDateTupel1, valueDateTupel3, valueDateTupel5 };
		ValueDateTupel[] vdtArray2 = { valueDateTupel2, valueDateTupel3, valueDateTupel4 };
		ValueDateTupel[] vdtArray3 = { valueDateTupel1, valueDateTupel4, valueDateTupel5 };
		ValueDateTupel[] vdtArray4 = { valueDateTupel3, valueDateTupel4, valueDateTupel5 };
		ValueDateTupel[] vdtArray5 = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };
		ValueDateTupel[][] vdtArraysArray = { vdtArray1, vdtArray2, vdtArray3, vdtArray4, vdtArray5 };
		ValueDateTupel[][] actualValue = ValueDateTupel.alignDates(vdtArraysArray);

		assertArrayEquals(expectedValue, actualValue, "Dates aren't correct after aligning ValueDateTuples");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#contains(ValueDateTupel[], ValueDateTupel)}.
	 */
	@Test
	void testContains() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };

		assertTrue(ValueDateTupel.contains(valueDateTupelArray, valueDateTupel1),
				"An element which is in the array falsely cannot be identified");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#contains(ValueDateTupel[], ValueDateTupel)}.
	 */
	@Test
	void testContains_unknownElement() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };

		assertFalse(ValueDateTupel.contains(valueDateTupelArray, valueDateTupel4),
				"An unknown element is falsely marked as being in the given array");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#containsDate(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsDate() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };

		assertTrue(ValueDateTupel.containsDate(valueDateTupelArray, date_20200101),
				"A DateTime which is in the array falsely cannot be identified");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#containsDate(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsDate_unknownDate() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };

		assertFalse(ValueDateTupel.containsDate(valueDateTupelArray, date_20200104),
				"An unknown DateTime is falsely marked as being in the given array");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#containsDate(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsDate_arrayNull() {
		ValueDateTupel[] valueDateTupelArray = null;
		String expectedMessage = "Given array cannot be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.containsDate(valueDateTupelArray, date_20200101),
				"Null array is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage());
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#containsDate(ValueDateTupel[], LocalDateTime)}.
	 */
	@Test
	void testContainsDate_dateTimeNull() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel2, valueDateTupel3 };
		String expectedMessage = "Given LocalDateTime cannot be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.containsDate(valueDateTupelArray, null),
				"LocalDateTime of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage());
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
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
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
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
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
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
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
	 */
	@Test
	void testAddOneAt_arrayNull() {
		ValueDateTupel[] valueDateTupelArray = null;
		String expectedMessage = "Given array of ValueDateTupel must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.addOneAt(valueDateTupelArray, valueDateTupel1, 0),
				"Array of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
	 */
	@Test
	void testAddOneAt_valueNull() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };
		String expectedMessage = "Given valueDateTupelToBeAdded must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.addOneAt(valueDateTupelArray, null, 0),
				"New value of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
	 */
	@Test
	void testAddOneAt_position_negative() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };
		int negativePosition = -1;
		String expectedMessage = "Cannot not add a value at position < 0. Given position is " + negativePosition;

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.addOneAt(valueDateTupelArray, valueDateTupel1, negativePosition),
				"Position < 0 is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#addOneAt(ValueDateTupel[], ValueDateTupel, int)}.
	 */
	@Test
	void testAddOneAt_position_greater_arrayLength() {
		ValueDateTupel[] valueDateTupelArray = { valueDateTupel1, valueDateTupel3, valueDateTupel4 };
		int tooLargeAPosition = 4;
		String expectedMessage = "Cannot add a value at position > " + valueDateTupelArray.length;

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.addOneAt(valueDateTupelArray, valueDateTupel1, tooLargeAPosition),
				"Position > array.length is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#getValues(ValueDateTupel[])}.
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
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#getValues(ValueDateTupel[])}.
	 */
	@Test
	void testGetValues_arrayNull() {
		ValueDateTupel[] valueDateTupelArray = null;
		String expectedMessage = "Given array must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.getValues(valueDateTupelArray), "Null array is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#getDates(ValueDateTupel[])}.
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
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#getDates(ValueDateTupel[])}.
	 */
	@Test
	void testGetDates_arrayNull() {
		ValueDateTupel[] valueDateTupelArray = null;
		String expectedMessage = "Given array must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> ValueDateTupel.getDates(valueDateTupelArray), "Null array is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), "Incorrect Exception message");
	}
}
