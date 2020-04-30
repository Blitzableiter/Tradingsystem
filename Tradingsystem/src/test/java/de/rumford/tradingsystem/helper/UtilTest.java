package de.rumford.tradingsystem.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UtilTest {

	static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

	/**
	 * Test method for {@link Util#adjustForStandardDeviation(double, double)}.
	 */
	@Test
	void testAdjustForStandardDeviation() {
		double value = 100d;
		double standardDeviation = 2.5d;
		double expectedValue = 40d;

		double actualValue = Util.adjustForStandardDeviation(value, standardDeviation);

		assertEquals(expectedValue, actualValue, "Standard deviation adjusted value is not correctly calculated");
	}

	/**
	 * Test method for {@link Util#adjustForStandardDeviation(double, double)}.
	 */
	@Test
	void testAdjustForStandardDeviation_sd0() {
		double value = 100d;
		double standardDeviation = 0d;
		String expectedMessage = "Standard deviation must not be zero";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Util.adjustForStandardDeviation(value, standardDeviation),
				"Standard deviation of zero is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Util#calculateForecast(double, double)}.
	 */
	@Test
	void testCalculateForecast() {
		double unscaledForecast = 2.5d;
		double scalar = 4d;
		double expectedValue = 10d;

		double actualValue = Util.calculateForecast(unscaledForecast, scalar);

		assertEquals(expectedValue, actualValue, "Forecast is not correctly calculated");
	}

	/**
	 * Test method for {@link Util#calculateForecastScalar(double[], double)}.
	 */
	@Test
	void testCalculateForecastScalar() {
		double[] values = { 10d, 4d, -1d, 6d, -4d };
		double baseScale = 10d;
		double expectedValue = 2d;

		double actualValue = Util.calculateForecastScalar(values, baseScale);

		assertEquals(expectedValue, actualValue, "Forecast scalar is not correctly calculated");
	}

	/**
	 * Test method for {@link Util#calculateForecastScalar(double[], double)}.
	 */
	@Test
	void testCalculateForecastScalar_absoluteAverage0() {
		double[] values = { 0d, -0d };
		double baseScale = 10d;
		double expectedValue = Double.NaN;

		double actualValue = Util.calculateForecastScalar(values, baseScale);

		assertEquals(expectedValue, actualValue,
				"Forecast scalar is not correctly calculated when average of absolute values is zero");
	}

	/**
	 * Test method for {@link Util#calculateForecastScalar(double[], double)}.
	 */
	@Test
	void testCalculateForecastScalar_noValues() {
		double[] values = {};
		double baseScale = 10d;
		String expectedMessage = "Given array of values must not be empty";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Util.calculateForecastScalar(values, baseScale), "Empty values array is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Util#calculateForecastScalar(double[], double)}.
	 */
	@Test
	void testCalculateForecastScalar_baseScale0() {
		double[] values = { 10d, 4d, -1d, 6d, -4d };
		double baseScale = 0;
		String expectedMessage = "Base scale must not be 0.";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Util.calculateForecastScalar(values, baseScale), "Base scale of 0 is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Util#calculateReturn(double, double)}.
	 */
	@Test
	void testCalculateReturn_former200_latter300() {
		double formerValue = 200d;
		double latterValue = 300d;
		double expectedValue = 0.5d;

		double actualValue = Util.calculateReturn(formerValue, latterValue);

		assertEquals(expectedValue, actualValue, "Positive return is not correctly calculated");
	}

	/**
	 * Test method for {@link Util#calculateReturn(double, double)}.
	 */
	@Test
	void testCalculateReturn_former300_latter200() {
		double formerValue = 400d;
		double latterValue = 200d;
		double expectedValue = -0.5d;

		double actualValue = Util.calculateReturn(formerValue, latterValue);

		assertEquals(expectedValue, actualValue, "Negative return is not correctly calculated");
	}

	/**
	 * Test method for {@link Util#calculateReturn(double, double)}.
	 */
	@Test
	void testCalculateReturn_formerValue0() {
		double formerValue = 0d;
		double latterValue = 10d;
		double expectedValue = Double.NaN;

		double actualValue = Util.calculateReturn(formerValue, latterValue);

		assertEquals(expectedValue, actualValue, "Former value of 0 is not properly handled");
	}
}