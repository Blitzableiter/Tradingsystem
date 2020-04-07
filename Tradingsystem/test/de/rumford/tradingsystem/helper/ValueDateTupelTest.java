package de.rumford.tradingsystem.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValueDateTupelTest {

	float value;
	LocalDateTime date;
	ValueDateTupel vdt;
	ValueDateTupel vdt2;
	ValueDateTupel vdt3;
	ValueDateTupel vdt4;

	@BeforeEach
	void setUp() throws Exception {
		value = 500f;
		date = LocalDateTime.of(2020, 1, 1, 0, 0);
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#equals(Object)}.
	 */
	@Test
	void testEqualsObject() {
		vdt = new ValueDateTupel(date, value);
		vdt2 = new ValueDateTupel(date, value);

		assertEquals(vdt, vdt2, "Two equal instances of ValueDateTupel are not equal");
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
	 * {@link de.rumford.tradingsystem.helper.ValueDateTupel#appendElement(ValueDateTupel[], ValueDateTupel)}.
	 */
	@Test
	void testAppendElement() {
		vdt2 = new ValueDateTupel(LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0)), 400d);
		vdt3 = new ValueDateTupel(LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0)), 500d);
		ValueDateTupel[] values = ValueDateTupel.createEmptyArray();

		values = ValueDateTupel.appendElement(values, vdt2);
		values = ValueDateTupel.appendElement(values, vdt3);

		assertEquals(vdt2, values[0]);
		assertEquals(vdt3, values[1]);
	}

}
