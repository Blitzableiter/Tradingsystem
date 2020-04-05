package de.rumford.tradingsystem.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValueDateTupelTest {

	float value;
	LocalDateTime date;
	ValueDateTupel vdt;
	ValueDateTupel vdt2;
	
	@BeforeEach
	void setUp() throws Exception {
		value = 500f;
		date = LocalDateTime.of(2020, 1, 1, 0, 0);
	}

	@Test
	void testEqualsObject() {
		vdt = new ValueDateTupel(value, date);
		vdt2 = new ValueDateTupel(value, date);
		
		assertEquals(vdt, vdt2, "Two equal instances of ValueDateTupel are not equal");
	}

}
