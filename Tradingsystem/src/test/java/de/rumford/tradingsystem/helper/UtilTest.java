package de.rumford.tradingsystem.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.BaseValue;
import de.rumford.tradingsystem.Rule;
import de.rumford.tradingsystem.RuleTest.RealRule;

/**
 * Test class for {@link Util}.
 * 
 * @author Max Rumford
 *
 */
class UtilTest {

	static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

	static final String BASE_VALUE_NAME = "Base value name";
	static BaseValue baseValue;

	static LocalDateTime localDateTimeJan09220000;
	static LocalDateTime localDateTimeJan10220000;
	static LocalDateTime localDateTimeJan11220000;
	static LocalDateTime localDateTimeJan12220000;

	static final double VARIATOR = 1;
	static final double BASE_SCALE = 10;

	static Rule r1;
	static Rule r2;
	static Rule r3;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		baseValue = BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME);
		localDateTimeJan09220000 = LocalDateTime.of(2020, 01, 9, 22, 0);
		localDateTimeJan10220000 = LocalDateTime.of(2020, 01, 10, 22, 0);
		localDateTimeJan11220000 = LocalDateTime.of(2020, 01, 11, 22, 0);
		localDateTimeJan12220000 = LocalDateTime.of(2020, 01, 12, 22, 0);
	}

	@BeforeEach
	void setUp() throws Exception {
		r1 = RealRule.from(baseValue, null, localDateTimeJan10220000,
				localDateTimeJan12220000, BASE_SCALE, VARIATOR);
		r2 = RealRule.from(baseValue, null, localDateTimeJan10220000,
				localDateTimeJan12220000, BASE_SCALE, 2);
		r3 = RealRule.from(baseValue, null, localDateTimeJan10220000,
				localDateTimeJan12220000, BASE_SCALE, 3);
	}

	/**
	 * Test method for {@link Util#adjustForStandardDeviation(double, double)}.
	 */
	@Test
	void testAdjustForStandardDeviation() {
		double value = 100d;
		double standardDeviation = 2.5d;
		double expectedValue = 40d;

		double actualValue = Util.adjustForStandardDeviation(value,
				standardDeviation);

		assertEquals(expectedValue, actualValue,
				"Standard deviation adjusted value is not correctly calculated");
	}

	/**
	 * Test method for {@link Util#adjustForStandardDeviation(double, double)}.
	 */
	@Test
	void testAdjustForStandardDeviation_sd0() {
		double value = 100d;
		double standardDeviation = 0d;

		double actualValue = Util.adjustForStandardDeviation(value,
				standardDeviation);

		assertTrue(Double.isNaN(actualValue),
				"Stanard deviation of zero is not properly handled");
	}

	/**
	 * Test method for {@link Util#areRulesUnique(Rule[])}.
	 */
	@Test
	void testAreRulesUnique_identicalRules() {
		r1 = RealRule.from(baseValue, null, localDateTimeJan10220000,
				localDateTimeJan12220000, BASE_SCALE, VARIATOR);
		r2 = RealRule.from(baseValue, null, localDateTimeJan10220000,
				localDateTimeJan12220000, BASE_SCALE, VARIATOR);
		Rule[] rules = { r1, r2 };

		assertFalse(Util.areRulesUnique(rules),
				"Identical rules are not identified.");
	}

	/**
	 * Test method for {@link Util#areRulesUnique(Rule[])}.
	 */
	@Test
	void testAreRulesUnique_uniqueRules() {
		Rule[] variations = { r3 };
		r2 = RealRule.from(baseValue, variations, localDateTimeJan10220000,
				localDateTimeJan12220000, BASE_SCALE, VARIATOR);
		Rule[] rules = { r1, r2 };
		assertTrue(Util.areRulesUnique(rules),
				"Unique rules are not identified.");

		r2 = RealRule.from(baseValue, null, localDateTimeJan09220000,
				localDateTimeJan12220000, BASE_SCALE, VARIATOR);
		Rule[] rules2 = { r1, r2 };
		assertTrue(Util.areRulesUnique(rules2),
				"Unique rules are not identified.");

		r2 = RealRule.from(baseValue, null, localDateTimeJan10220000,
				localDateTimeJan11220000, BASE_SCALE, VARIATOR);
		Rule[] rules3 = { r1, r2 };
		assertTrue(Util.areRulesUnique(rules3),
				"Unique rules are not identified.");

		@SuppressWarnings("unused")
		double diffBaseScale = (BASE_SCALE - 1 <= 0 ? BASE_SCALE + 1
				: BASE_SCALE - 1);
		r2 = RealRule.from(baseValue, null, localDateTimeJan10220000,
				localDateTimeJan12220000, diffBaseScale, VARIATOR);
		Rule[] rules4 = { r1, r2 };
		assertTrue(Util.areRulesUnique(rules4),
				"Unique rules are not identified.");

		double diffVariator = VARIATOR - 1;
		r2 = RealRule.from(baseValue, null, localDateTimeJan10220000,
				localDateTimeJan12220000, BASE_SCALE, diffVariator);
		Rule[] rules5 = { r1, r2 };
		assertTrue(Util.areRulesUnique(rules5),
				"Unique rules are not identified.");
	}

	/**
	 * Test method for {@link Util#areRulesUnique(Rule[])}.
	 */
	@Test
	void testAreRulesUnique_rules_null() {
		String expectedMessage = "The given rules must not be null";
		Rule[] rules = null;

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Util.areRulesUnique(rules),
				"Rules array of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Util#areRulesUnique(Rule[])}.
	 */
	@Test
	void testAreRulesUnique_rulesContains_null() {
		String expectedMessage = "The given array must not contain nulls";
		Rule[] rules = { null };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Util.areRulesUnique(rules),
				"Rules array containing null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Util#areRulesUnique(Rule[])}.
	 */
	@Test
	void testAreRulesUnique_rules_empty() {
		String expectedMessage = "The given array of rules must not be empty.";
		Rule[] rules = {};

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Util.areRulesUnique(rules),
				"Empty rules array is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
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

		assertEquals(expectedValue, actualValue,
				"Forecast is not correctly calculated");
	}

	/**
	 * Test method for {@link Util#calculateAverage(double[])}.
	 */
	@Test
	void testCalculateAverage() {
		double[] values = { 1, 2, 3 };
		double expectedValue = 2;

		double actualValue = Util.calculateAverage(values);

		assertEquals(expectedValue, actualValue,
				"Average value is not properly calculated");
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

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Util.calculateAverage(values),
				"Array of null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
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

		assertEquals(expectedValue, actualValue,
				"Forecast scalar is not correctly calculated");
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
				() -> Util.calculateForecastScalar(values, baseScale),
				"Empty values array is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Util#calculateForecastScalar(double[], double)}.
	 */
	@Test
	void testCalculateForecastScalar_baseScale0() {
		String expectedMessage = "Given base scale does not meet specifications.";
		String expectedCause = "Value must be a positive decimal";
		double[] values = { 10d, 4d, -1d, 6d, -4d };
		double baseScale = 0;

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Util.calculateForecastScalar(values, baseScale),
				"Base scale of 0 is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
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

		assertEquals(expectedValue, actualValue,
				"Positive return is not correctly calculated");
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

		assertEquals(expectedValue, actualValue,
				"Negative return is not correctly calculated");
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

		assertEquals(expectedValue, actualValue,
				"Former value of 0 is not properly handled");
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
		double[] expectedValue = { 0.8577367845186973, 0.6885007663072988,
				0.6564051762162094 };

		double[] actualValue = Util.calculateCorrelationOfRows(values);

		assertArrayEquals(expectedValue, actualValue,
				"Correlations between three rows are not correctly calculated");
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

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> Util.calculateCorrelationOfRows(values),
				"Arrays containing all equal values are not properly handled.");

		assertEquals(expectedmessage, thrown.getMessage(),
				MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link Util#calculateWeightsForThreeCorrelations(double[])}.
	 */
	@Test
	void testCalculateWeightsForThreeCorrelations() {
		// Excel: 0.3, 0.366666666666667, 0.333333333333333
		double[] expectedValue = { .3, 0.3666666666666667, 1d / 3d };
		double[] correlations = { .5, .6, .4 };

		double[] actualValue = Util
				.calculateWeightsForThreeCorrelations(correlations);

		assertArrayEquals(expectedValue, actualValue,
				"Weights for 3 correlations are not correctly calculated");
	}

	/**
	 * Test method for
	 * {@link Util#calculateWeightsForThreeCorrelations(double[])}.
	 */
	@Test
	void testCalculateWeightsForThreeCorrelations_negativeCorrelations() {
		double[] correlations1 = { .5, .6, -.4 };
		double[] correlations2 = { .5, .6, 0 };

		double[] actualValue1 = Util
				.calculateWeightsForThreeCorrelations(correlations1);
		double[] actualValue2 = Util
				.calculateWeightsForThreeCorrelations(correlations2);

		assertArrayEquals(actualValue1, actualValue2,
				"Weights for negative correlations are not correctly calculated");
	}

	/**
	 * Test method for
	 * {@link Util#calculateWeightsForThreeCorrelations(double[])}.
	 */
	@Test
	void testCalculateWeightsForThreeCorrelations_threeEqualWeights() {
		double[] expectedValue = { 1d / 3d, 1d / 3d, 1d / 3d };
		double[] correlations = { 1, 1, 1 };

		double[] actualValue = Util
				.calculateWeightsForThreeCorrelations(correlations);

		assertArrayEquals(expectedValue, actualValue,
				"Weights for 3 equal correlations are not correctly calculated");
	}

}