package de.rumford.tradingsystem.helper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@link Util}.
 * 
 * @author Max Rumford
 *
 */
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

		double actualValue = Util.adjustForStandardDeviation(value, standardDeviation);

		assertTrue(Double.isNaN(actualValue), "Stanard deviation of zero is not properly handled");
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
	 * Test method for {@link Util#calculateAverage(double[])}.
	 */
	@Test
	void testCalculateAverage() {
		double[] values = { 1, 2, 3 };
		double expectedValue = 2;

		double actualValue = Util.calculateAverage(values);

		assertEquals(expectedValue, actualValue, "Average value is not properly calculated");
	}

	/**
	 * Test method for {@link Util#calculateAverage(double[])}.
	 */
	@Test
	void testCalculateAverage_withNegatives() {
		double[] values = { 1, 2, -3, 4 };
		double expectedValue = 1;

		double actualValue = Util.calculateAverage(values);

		assertEquals(expectedValue, actualValue,
				"Average value of values containing negatives is not properly calculated");
	}

	/**
	 * Test method for {@link Util#calculateAverage(double[])}.
	 */
	@Test
	void testCalculateAverage_arrayNull() {
		double[] values = null;
		String expectedMessage = "Given array must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class, () -> Util.calculateAverage(values),
				"Array of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
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

	/**
	 * Test method for {@link Util#calculateCorrelationOfRows(double[][])}.
	 */
	@Test
	void testCalculateCorrelationsOfThreeRows() {
		double[] row1 = { -20, -20, -12.31, -5.34 };
		double[] row2 = { -20, -20, -20, -17.93 };
		double[] row3 = { -9.59, -10.62, -9.8, -9.23 };
		double[][] values = { row1, row2, row3 };
		// Excel: 0.857736784518697, 0.688500766307298, 0.656405176216209
		double[] expectedValue = { 0.8577367845186973, 0.6885007663072988, 0.6564051762162094 };

		double[] actualValue = Util.calculateCorrelationOfRows(values);

		assertArrayEquals(expectedValue, actualValue, "Correlations between three rows are not correctly calculated");
	}

	/**
	 * Test method for {@link Util#calculateCorrelationOfRows(double[][])}.
	 */
	@Test
	void testCalculateCorrelationsOfThreeRows_valuesContainsArrayContainingNan() {
		double[][] values = { { 0, 4 }, { 2, Double.NaN }, { 1, 0.2 } };
		double[] expectedCorrelations = { Double.NaN, -1, Double.NaN };

		double[] actualCorrelations = Util.calculateCorrelationOfRows(values);

		assertArrayEquals(expectedCorrelations, actualCorrelations,
				"Correlations containing NaN are not correctly calculated");
	}

	/**
	 * Test method for {@link Util#calculateCorrelationOfRows(double[][])}.
	 */
	@Test
	void testCalculateCorrelationsOfThreeRows_allEqaulValues() {
		double[] row1 = { 1, 1, 1 };
		double[] row2 = { 2, 3, 4 };
		double[] row3 = { 3, 3, 5 };
		double[][] values = { row1, row2, row3 };
		String expectedmessage = "Correlations cannot be calculated caused by all identical values in row at position 0.";

		Exception thrown = assertThrows(IllegalArgumentException.class, () -> Util.calculateCorrelationOfRows(values),
				"Arrays containing all equal values are not properly handled.");

		assertEquals(expectedmessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}
}