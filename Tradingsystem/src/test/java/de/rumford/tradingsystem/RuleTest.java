/**
 * 
 */
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
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * de.rumford.tradingsystem
 * 
 * @author Max Rumford
 *
 */
class RuleTest {

	private class RealRule extends Rule {

		private double variator;

		public RealRule(BaseValue baseValue, Rule[] variations, LocalDateTime startOfReferenceWindow,
				LocalDateTime endOfReferenceWindow, double baseScale, double variator) {
			super(baseValue, variations, startOfReferenceWindow, endOfReferenceWindow, baseScale);
			this.variator = variator;
		}

		@Override
		double calculateRawForecast(LocalDateTime forecastDateTime) {
			return ValueDateTupel.getElement(this.getBaseValue().getValues(), forecastDateTime).getValue()
					+ this.variator * 100;
		}
	}

	static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

	RealRule realRule;
	static BaseValue baseValue;
	static double variator;

	static final String BASE_VALUE_NAME = "Base value name";
	static final int BASE_SCALE = 10;

	static LocalDateTime localDateTimeJan10220000;
	static LocalDateTime localDateTimeJan12220000;
	static LocalDateTime localDateTimeFeb05220000;

	@BeforeAll
	static void setUpBeforeClass() {
		baseValue = BaseValueFactory.jan1Jan31calcShort(BASE_VALUE_NAME);

		localDateTimeJan10220000 = LocalDateTime.of(LocalDate.of(2020, 1, 10), LocalTime.of(22, 0));
		localDateTimeJan12220000 = LocalDateTime.of(LocalDate.of(2020, 1, 12), LocalTime.of(22, 0));
		localDateTimeFeb05220000 = LocalDateTime.of(LocalDate.of(2020, 2, 5), LocalTime.of(22, 0));
	}

	@BeforeEach
	void setUp() {
		variator = 1;
		realRule = new RealRule(baseValue, null, localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE,
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
	void testCalculateForecastScalar_FC0() {
		double expectedValue = 0;

		realRule = new RealRule(BaseValueFactory.jan1Jan31allVal0calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		double actualValue = realRule.getForecastScalar();

		assertEquals(expectedValue, actualValue, "Forecast scalar of zero is not correctly calculated");
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
		realRule = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null, localDateTimeJan10220000,
				localDateTimeJan12220000, BASE_SCALE, variator);

		ValueDateTupel.getElement(realRule.getForecasts(), localDateTimeFeb05220000).getValue();
		double actualValue = ValueDateTupel.getElement(realRule.getForecasts(), localDateTimeFeb05220000).getValue();

		assertEquals(expectedValue, actualValue, "Forecasts < -20 are not correctly calculated");
	}

	/**
	 * Test method for {@link Rule#validateSetAndWeighVariations(Rule[])}.
	 */
	@Test
	void testValidateSetAndWeighVariations_moreThan3Variations() {
		String expectedMessage = "Each layer must not contain more than 3 rules/variations";
		RealRule var1 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var2 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var3 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var4 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule[] variations = { var1, var2, var3, var4 };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
						localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator),
				"> 3 variations is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#validateSetAndWeighVariations(Rule[])}.
	 */
	@Test
	void testValidateSetAndWeighVariations_variationIsNull() {
		String expectedMessage = "The variation at position 2 in the given variations array is null.";
		RealRule var1 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var2 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var3 = null;
		RealRule[] variations = { var1, var2, var3 };

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
						localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator),
				"Variation = null is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link Rule#weighVariations()}.
	 */
	@Test
	void testWeighVariations_1Variation() {
		double expectedValue = 1;
		RealRule var1 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule[] variations = { var1 };

		RealRule realRule = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
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
		RealRule var1 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var2 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule[] variations = { var1, var2 };

		RealRule realRule = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
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
		RealRule var1 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var2 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule var3 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		RealRule[] variations = { var1, var2, var3 };

		RealRule realRule = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
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
		RealRule var1 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator1);
		RealRule var2 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator2);
		RealRule var3 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator3);
		RealRule[] variations = { var1, var2, var3 };

		RealRule realRule = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		double[] actualValue = { realRule.getVariations()[0].getWeight(), realRule.getVariations()[1].getWeight(),
				realRule.getVariations()[2].getWeight() };

		assertArrayEquals(expectedValue, actualValue, "Weights for 3 inequal variations are not correctly calculated");
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
		RealRule var1 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator1);
		RealRule var2 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator2);
		RealRule var3 = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator3);
		RealRule[] variations = { var1, var2, var3 };

		RealRule realRule = new RealRule(BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME), variations,
				localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, variator);
		double[] actualValue = { realRule.getVariations()[0].getWeight(), realRule.getVariations()[1].getWeight(),
				realRule.getVariations()[2].getWeight() };

		assertArrayEquals(expectedValue, actualValue,
				"Weights for variations with negative correlations are not correctly calculated");
	}

}