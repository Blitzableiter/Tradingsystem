package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.helper.BaseValueFactory;
import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * Test class for {@link Rule}.
 * 
 * @author Max Rumford
 *
 */
public class RuleTest {

	public static class RealRule extends Rule {
		private double variator;

		public RealRule(BaseValue baseValue, Rule[] variations, LocalDateTime startOfReferenceWindow,
				LocalDateTime endOfReferenceWindow, double baseScale, double variator) {
			super(baseValue, variations, startOfReferenceWindow, endOfReferenceWindow, baseScale);
			this.variator = variator;
		}

		public static RealRule from(BaseValue baseValue, Rule[] variations, LocalDateTime startOfReferenceWindow,
				LocalDateTime endOfReferenceWindow, double baseScale, double variator) {
			return new RealRule(baseValue, variations, startOfReferenceWindow, endOfReferenceWindow, baseScale,
					variator);
		}

		@Override
		double calculateRawForecast(LocalDateTime forecastDateTime) {
			return ValueDateTupel.getElement(this.getBaseValue().getValues(), forecastDateTime).getValue()
					+ this.variator * 100;
		}

		@GeneratedCode
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			long temp;
			temp = Double.doubleToLongBits(variator);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		@GeneratedCode
		@Override
		public boolean equals(Object obj) {

			if (this == obj)
				return true;

			if (!super.equals(obj))
				return false;

			if (getClass() != obj.getClass())
				return false;
			RealRule other = (RealRule) obj;
			if (Double.doubleToLongBits(variator) != Double.doubleToLongBits(other.variator))
				return false;
			return true;
		}

		@GeneratedCode
		@Override
		public String toString() {
			return "RealRule [variator=" + variator + "]";
		}

	}

	static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

	RealRule realRule;
	static BaseValue baseValue;
	static double variator;

	static final String BASE_VALUE_NAME = "Base value name";
	static final int BASE_SCALE = 10;

	static LocalDateTime localDateTime2019Dec31220000;
	static LocalDateTime localDateTimeJan01220000;
	static LocalDateTime localDateTimeJan02220000;
	static LocalDateTime localDateTimeJan03220000;
	static LocalDateTime localDateTimeJan04220000;
	static LocalDateTime localDateTimeJan05220000;
	static LocalDateTime localDateTimeJan07220000;
	static LocalDateTime localDateTimeJan10220000;
	static LocalDateTime localDateTimeJan12220000;
	static LocalDateTime localDateTimeFeb05220000;
	static LocalDateTime localDateTime2020Dec31220000;

	@BeforeAll
	static void setUpBeforeClass() {
		baseValue = BaseValueFactory.jan1Jan31calcShort(BASE_VALUE_NAME);

		localDateTime2019Dec31220000 = LocalDateTime.of(LocalDate.of(2019, 12, 31), LocalTime.of(22, 0));
		localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		localDateTimeJan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));
		localDateTimeJan07220000 = LocalDateTime.of(LocalDate.of(2020, 1, 7), LocalTime.of(22, 0));
		localDateTimeJan10220000 = LocalDateTime.of(LocalDate.of(2020, 1, 10), LocalTime.of(22, 0));
		localDateTimeJan12220000 = LocalDateTime.of(LocalDate.of(2020, 1, 12), LocalTime.of(22, 0));
		localDateTimeFeb05220000 = LocalDateTime.of(LocalDate.of(2020, 2, 5), LocalTime.of(22, 0));
		localDateTime2020Dec31220000 = LocalDateTime.of(LocalDate.of(2020, 12, 31), LocalTime.of(22, 0));
	}

	@BeforeEach
	void setUp() {
		variator = 1;
		realRule = RealRule.from(baseValue, null, localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE,
				variator);
	}

	/**
	 * Test method for {@link Rule#calculateForecastScalar()}.
	 */
	@Test
	void testCalculateForecastScalar() {
		double expectedValue = 2.793618728459556; // Excel: 2.79361872845956

		double actualValue = realRule.getForecastScalar();

		assertEquals(expectedValue, actualValue, "Forecast scalar is not correctly calculated");
	}

	/**
	 * Test method for {@link Rule#calculateForecastScalar()}.
	 */
	@Test
	void testCalculateForecastScalar_FcScalarDiv0() {
		String expectedMessage = "Illegal values in calulated forecast values. Adjust reference window.";

		double variator = -1d;
		realRule = RealRule.from(BaseValueFactory.jan1Jan5calcShort_sameValuesOn2To5(BASE_VALUE_NAME), null,
				localDateTimeJan02220000, localDateTimeJan05220000, BASE_SCALE, variator);

		Exception thrown = assertThrows(IllegalArgumentException.class, () -> realRule.getForecastScalar(),
				"Illegal values in reference window are not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#calculateScaledForecasts()}.
	 */
	@Test
	void testCalculateForecasts() {
		double expectedValue = 12.66459427439483; // Excel: 12.6645942743948

		double actualValue = ValueDateTupel.getElement(realRule.getForecasts(), localDateTimeJan10220000).getValue();

		assertEquals(expectedValue, actualValue, "Forecasts are not correctly calculated");
	}

	/**
	 * Test method for {@link Rule#calculateScaledForecasts()}.
	 */
	@Test
	void testCalculateForecasts_unchangedOverTime() {
		double expectedValue = 12.66459427439483; // Excel: 12.6645942743948

		ValueDateTupel.getElement(realRule.getForecasts(), localDateTimeJan10220000).getValue();
		double actualValue = ValueDateTupel.getElement(realRule.getForecasts(), localDateTimeJan10220000).getValue();

		assertEquals(expectedValue, actualValue, "Forecasts are not correctly calculated");
	}

	/**
	 * Test method for {@link Rule#calculateScaledForecast(double)}.
	 */
	@Test
	void testCalculateScaledForecast_FcNegative20() {
		double expectedValue = -20d;
		variator = -0.1d;
		realRule = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null, localDateTimeJan10220000,
				localDateTimeJan12220000, BASE_SCALE, variator);

		ValueDateTupel.getElement(realRule.getForecasts(), localDateTimeFeb05220000).getValue();
		double actualValue = ValueDateTupel.getElement(realRule.getForecasts(), localDateTimeFeb05220000).getValue();

		assertEquals(expectedValue, actualValue, "Forecasts < -20 are not correctly calculated");
	}

	/**
	 * Test method for
	 * {@link Rule#validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 */
	@Test
	void testCalculateVolatilityIndices_baseValue_null() {
		BaseValue nullBaseValue = null;
		String expectedMessage = "Base value must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class, () -> RealRule.from(nullBaseValue, null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator),
				"Base value of null is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#validateInputs(BaseValue, LocalDateTime,
	 * LocalDateTime, double}.
	 */
	@Test
	void testValidateInputs_startOfReferenceWindow_null() {
		String expectedMessage = "The given reference window does not meet specifications.";
		String expectedCause = "Start of time window value must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> RealRule.from(baseValue, null, null, localDateTimeJan12220000, BASE_SCALE, variator),
				"startOfReferenceWindow of null is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#validateInputs(BaseValue, LocalDateTime,
	 * LocalDateTime, double}.
	 */
	@Test
	void testValidateInputs_endOfReferenceWindow_null() {
		String expectedMessage = "The given reference window does not meet specifications.";
		String expectedCause = "End of time window value must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> RealRule.from(baseValue, null, localDateTimeJan10220000, null, BASE_SCALE, variator),
				"endOfReferenceWindow of null is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#validateInputs(BaseValue, LocalDateTime,
	 * LocalDateTime, double}.
	 */
	@Test
	void testValidateInputs_baseScale_0() {
		String expectedMessage = "The given base scale does not meet specifications.";
		String expectedCause = "Value must be a positive decimal";
		double zeroBaseScale = 0;

		Exception thrown = assertThrows(
				IllegalArgumentException.class, () -> RealRule.from(baseValue, null, localDateTimeJan10220000,
						localDateTimeJan12220000, zeroBaseScale, variator),
				"baseScale of zero is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#validateInputs(BaseValue, LocalDateTime,
	 * LocalDateTime, double}.
	 */
	@Test
	void testValidateInputs_baseScale_sub0() {
		String expectedMessage = "The given base scale does not meet specifications.";
		String expectedCause = "Value must be a positive decimal";
		double subZeroBaseScale = -1;

		Exception thrown = assertThrows(
				IllegalArgumentException.class, () -> RealRule.from(baseValue, null, localDateTimeJan10220000,
						localDateTimeJan12220000, subZeroBaseScale, variator),
				"baseScale of less than zero is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#validateInputs(BaseValue, LocalDateTime,
	 * LocalDateTime, double}.
	 */
	@Test
	void testValidateInputs_endOfReferenceWindow_before_startOfReferenceWindow() {
		String expectedMessage = "The given reference window does not meet specifications.";
		String expectedCause = "End of time window value must be after start of time window value";

		Exception thrown = assertThrows(
				IllegalArgumentException.class, () -> RealRule.from(baseValue, null, localDateTimeJan12220000,
						localDateTimeJan10220000, BASE_SCALE, variator),
				"endOfReferenceWindow before startOfReferenceWindow is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#validateInputs(BaseValue, LocalDateTime,
	 * LocalDateTime, double}.
	 */
	@Test
	void testValidateInputs_illegalStartOfReferenceWindow() {
		String expectedMessage = "Given base value and reference window do not fit.";
		String expectedCause = "Given values do not include given start value for time window";

		Exception thrown = assertThrows(
				IllegalArgumentException.class, () -> RealRule.from(baseValue, null, localDateTime2019Dec31220000,
						localDateTimeJan12220000, BASE_SCALE, variator),
				"Not included startOfReferenceWindow is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#validateInputs(BaseValue, LocalDateTime,
	 * LocalDateTime, double}.
	 */
	@Test
	void testValidateInputs_illegalEndOfReferenceWindow() {
		String expectedMessage = "Given base value and reference window do not fit.";
		String expectedCause = "Given values do not include given end value for time window";

		Exception thrown = assertThrows(
				IllegalArgumentException.class, () -> RealRule.from(baseValue, null, localDateTimeJan10220000,
						localDateTime2020Dec31220000, BASE_SCALE, variator),
				"Not included endOfReferenceWindow is not correctly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link Rule#validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 */
	@Test
	void testValidateInputs_moreThan3Variations() {
		String expectedMessage = "A rule must not contain more than 3 variations.";
		RealRule var1 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var2 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var3 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var4 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule[] variations = { var1, var2, var3, var4 };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
						localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator),
				"> 3 variations is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link Rule#validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 */
	@Test
	void testValidateInputs_emptyVariationsArray() {
		String expectedMessage = "The given variations array must not be empty.";
		RealRule[] variations = {};

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
						localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator),
				"Empty variations array is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link Rule#validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 */
	@Test
	void testValidateInputs_variationIsNull() {
		String expectedMessage = "The variation at position 2 in the given variations array is null.";
		RealRule var1 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var2 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var3 = null;
		RealRule[] variations = { var1, var2, var3 };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
						localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator),
				"Variation = null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link Rule#validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 */
	@Test
	void testValidateInputs_variationsStartOfReferenceWindowDoesNotMatchRules() {
		String expectedMessage = "The given reference window does not match the variation's at position 1. The given start of reference window is different.";
		RealRule var1 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var2 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan07220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var3 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule[] variations = { var1, var2, var3 };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
						localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator),
				"Unmatched start of reference window is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link Rule#validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 */
	@Test
	void testValidateInputs_variationsEndOfReferenceWindowDoesNotMatchRules() {
		String expectedMessage = "The given reference window does not match the variation's at position 1. The given end of reference window is different.";
		RealRule var1 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var2 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeFeb05220000, BASE_SCALE, variator);
		RealRule var3 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule[] variations = { var1, var2, var3 };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
						localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator),
				"Unmatched end of reference window is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for
	 * {@link Rule#validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 */
	@Test
	void testValidateInputs_variationsDoNotHaveRulesBaseValues() {
		String expectedMessage = "The given variations do not meet specifications.";
		String expectedCause = "The base value of all rules must be equal to given base value but the rule at position 0 does not comply.";
		RealRule var1 = RealRule.from(BaseValueFactory.jan1Jan31calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule[] variations = { var1 };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
						localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator),
				"Incorrect BaseValue in variations is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
		assertEquals(expectedCause, thrown.getCause().getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#weighVariations()}.
	 */
	@Test
	void testWeighVariations_1Variation() {
		double expectedValue = 1;
		RealRule var1 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule[] variations = { var1 };

		RealRule realRule = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		double actualValue = realRule.getVariations()[0].getWeight();

		assertEquals(expectedValue, actualValue, "Weight for 1 variation is not correctly calculated");
	}

	/**
	 * Test method for {@link Rule#weighVariations()}.
	 */
	@Test
	void testWeighVariations_2Variations() {
		double[] expectedValue = { 0.5, 0.5 };
		RealRule var1 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var2 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule[] variations = { var1, var2 };

		RealRule realRule = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		double[] actualValue = { realRule.getVariations()[0].getWeight(), realRule.getVariations()[1].getWeight() };

		assertArrayEquals(expectedValue, actualValue, "Weights for 2 variation are not correctly calculated");
	}

	/**
	 * Test method for {@link Rule#weighVariations()}.
	 */
	@Test
	void testWeighVariations_3EqualVariations() {
		double[] expectedValue = { 1d / 3d, 1d / 3d, 1d / 3d };
		RealRule var1 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var2 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var3 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule[] variations = { var1, var2, var3 };

		RealRule realRule = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		double[] actualValue = { realRule.getVariations()[0].getWeight(), realRule.getVariations()[1].getWeight(),
				realRule.getVariations()[2].getWeight() };

		assertArrayEquals(expectedValue, actualValue, "Weights for 3 equal variations are not correctly calculated");
	}

	/**
	 * Test method for {@link Rule#weighVariations()}.
	 */
	@Test
	void testWeighVariations_3Variations() {
		double[] expectedValue = { //
				0.19285828960561063, // Excel: 0.192858289605609
				0.3259968088978676, // Excel: 0.325996808897865
				0.4811449014965218, // Excel: 0.481144901496526
		};
		double variator1 = 1;
		double variator2 = 3.19;
		double variator3 = -0.1;
		RealRule var1 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator1);
		RealRule var2 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator2);
		RealRule var3 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator3);
		RealRule[] variations = { var1, var2, var3 };

		RealRule realRule = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		double[] actualValue = { realRule.getVariations()[0].getWeight(), realRule.getVariations()[1].getWeight(),
				realRule.getVariations()[2].getWeight() };

		assertArrayEquals(expectedValue, actualValue, "Weights for 3 inequal variations are not correctly calculated");
	}

	/**
	 * Test method for {@link Rule#calculateForecastScalar()}.
	 */
	@Test
	void testCalculateForecastScalar_referenceWindowOnFirstDayOfBaseValues() {
		String expectedMessage = "Reference window must not start on first time interval of base value data.";
		double variator3 = 1;

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
						localDateTimeJan01220000, localDateTimeJan03220000, BASE_SCALE, variator3),
				"Illegal start of reference window is not properly handled.");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#calculateWeights(double[])}.
	 */
	@Test
	void testCalculateWeights_negativeCorrelations() {
		double[] expectedValue = { //
				0.4999239558356957, // Excel: 0.499923955835696
				0.2500380220821522, // Excel: 0.250038022082152
				0.2500380220821522, // Excel: 0.250038022082152
		};
		double variator1 = -1;
		double variator2 = 0.5;
		double variator3 = 1;
		RealRule var1 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator1);
		RealRule var2 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator2);
		RealRule var3 = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator3);
		RealRule[] variations = { var1, var2, var3 };

		RealRule realRule = RealRule.from(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		double[] actualValue = { realRule.getVariations()[0].getWeight(), realRule.getVariations()[1].getWeight(),
				realRule.getVariations()[2].getWeight() };

		assertArrayEquals(expectedValue, actualValue,
				"Weights for variations with negative correlations are not correctly calculated");
	}

	/**
	 * Test method for {@link Rule#extractRelevantForecastValues()}.
	 */
	@Test
	void testGetRelevantForecastValues() {
		double[] expectedValues = { //
				12.66459427439483, // Excel: 12.6645942743948
				8.277321595406873, // Excel: 8.27732159540687
				9.058084130198298, // Excel: 9.0580841301983
		};

		double[] actualValues = realRule.extractRelevantForecastValues();

		assertArrayEquals(expectedValues, actualValues, "Relevant forecasts are not properly extracted");
	}
}
