package de.rumford.tradingsystem.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@link Validator}.
 * 
 * @author Max Rumford
 *
 */
class ValidatorTest {

	static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

	/**
	 * Test method for {@link Validator#validateCorrelations(double[])}.
	 */
	@Test
	void testValidateCorrelations_arrayNull() {
		String expectedMessage = "Correlations array must not be null";
		double[] correlations = null;

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Validator.validateCorrelations(correlations),
				"Array of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Validator#validateCorrelations(double[])}.
	 */
	@Test
	void testValidateCorrelations_arrayOfLengthNotThree() {
		String expectedMessage = "There must be exactly three correlation values in the given array";
		double[] correlations = { 0, 0 };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Validator.validateCorrelations(correlations),
				"Array of length != 3 is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Validator#validateCorrelations(double[])}.
	 */
	@Test
	void testValidateCorrelations_arrayContainsNan() {
		String expectedMessage = "NaN-values are not allowed. Correlation at position 1 is NaN.";
		double[] correlations = { 0, Double.NaN, 0 };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Validator.validateCorrelations(correlations),
				"Array containing Double.NaN is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Validator#validateCorrelations(double[])}.
	 */
	@Test
	void testValidateCorrelations_arrayContainsValueGreaterThan1() {
		String expectedMessage = "Correlation at position 1 is greater than 1";
		double[] correlations = { 0, 2, 0 };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Validator.validateCorrelations(correlations),
				"Array containing values greater than 1 is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Validator#validateCorrelations(double[])}.
	 */
	@Test
	void testValidateCorrelations_arrayContainsValueLessThanNegative1() {
		String expectedMessage = "Correlation at position 1 is less than -1";
		double[] correlations = { 0, -2, 0 };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Validator.validateCorrelations(correlations),
				"Array containing values less than -1 is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

}
