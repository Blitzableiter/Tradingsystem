package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.helper.ValueDateTupel;

class BaseValueTest {

	final static String NAME_OF_TEST_BASE_VALUES = "Test Base Value";

	final static int NUMBER_OF_VALUES = 4;
	ValueDateTupel valuedatetupel1;
	ValueDateTupel valuedatetupel2;
	ValueDateTupel valuedatetupel3;
	ValueDateTupel valuedatetupel4;

	ValueDateTupel valuedatetupel5;
	ValueDateTupel valuedatetupel6;
	ValueDateTupel valuedatetupel7;
	ValueDateTupel valuedatetupel8;

	ValueDateTupel[] values;
	ValueDateTupel[] shortValues;

	BaseValue baseValue;
	BaseValue baseValue2;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		valuedatetupel1 = new ValueDateTupel(LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0)), 200d);
		valuedatetupel2 = new ValueDateTupel(LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0)), 400d);
		valuedatetupel3 = new ValueDateTupel(LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0)), 500d);
		valuedatetupel4 = new ValueDateTupel(LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0)), 400d);

		valuedatetupel5 = new ValueDateTupel(LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0)), 1000d);
		valuedatetupel6 = new ValueDateTupel(LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0)), 500d);
		valuedatetupel7 = new ValueDateTupel(LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0)), 375d);
		valuedatetupel8 = new ValueDateTupel(LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0)), 450d);

		values = ValueDateTupel.createEmptyArray();

		shortValues = ValueDateTupel.createEmptyArray();
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.BaseValue#BaseValue(String, ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_name_values() {
		values = ValueDateTupel.appendElement(values, valuedatetupel1);
		values = ValueDateTupel.appendElement(values, valuedatetupel2);
		values = ValueDateTupel.appendElement(values, valuedatetupel3);
		values = ValueDateTupel.appendElement(values, valuedatetupel4);

		baseValue = new BaseValue(NAME_OF_TEST_BASE_VALUES, values);
		baseValue2 = new BaseValue(NAME_OF_TEST_BASE_VALUES, values);

		assertEquals(baseValue, baseValue2, "Two instances with the same contents are not equal");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.BaseValue#BaseValue(String, ValueDateTupel[], ValueDateTupel[])}.
	 */
	@Test
	void testBaseValue_name_values_shortIndexValues() {
		values = ValueDateTupel.appendElement(values, valuedatetupel1);
		values = ValueDateTupel.appendElement(values, valuedatetupel2);
		values = ValueDateTupel.appendElement(values, valuedatetupel3);
		values = ValueDateTupel.appendElement(values, valuedatetupel4);

		shortValues = ValueDateTupel.appendElement(shortValues, valuedatetupel5);
		shortValues = ValueDateTupel.appendElement(shortValues, valuedatetupel6);
		shortValues = ValueDateTupel.appendElement(shortValues, valuedatetupel7);
		shortValues = ValueDateTupel.appendElement(shortValues, valuedatetupel6);

		baseValue = new BaseValue(NAME_OF_TEST_BASE_VALUES, values, shortValues);
		baseValue2 = new BaseValue(NAME_OF_TEST_BASE_VALUES, values, shortValues);

		assertEquals(baseValue, baseValue2, "Two instances with the same contents are not equal");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.BaseValue#calculateShortIndexValues(ValueDateTupel[])}.
	 */
	@Test
	void testCalculateShortIndexValues() {
		values = ValueDateTupel.appendElement(values, valuedatetupel1);
		values = ValueDateTupel.appendElement(values, valuedatetupel2);
		values = ValueDateTupel.appendElement(values, valuedatetupel3);
		values = ValueDateTupel.appendElement(values, valuedatetupel4);

		shortValues = ValueDateTupel.appendElement(shortValues, valuedatetupel5);
		shortValues = ValueDateTupel.appendElement(shortValues, valuedatetupel6);
		shortValues = ValueDateTupel.appendElement(shortValues, valuedatetupel7);
		shortValues = ValueDateTupel.appendElement(shortValues, valuedatetupel8);
		
		baseValue = new BaseValue(NAME_OF_TEST_BASE_VALUES, values);

		ValueDateTupel[] actualValue = baseValue.getShortIndexValues();
		
		assertArrayEquals(shortValues, actualValue, "The calculated short index values are not as expected");
	}

}
