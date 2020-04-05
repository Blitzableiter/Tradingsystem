package de.rumford.tradingsystem.helper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilTest {
	@Test
	void testAdjustForStandardDeviation() {
		double value = 100d;
		double standardDeviation = 2.5d;
		double expectedValue = 40d;
		
		double actualValue = Util.adjustForStandardDeviation(value, standardDeviation);
		
		assertEquals(expectedValue, actualValue, "Standard deviation adjusted value is not correctly calculated");
	}
	@Test
	void testAdjustForStandardDeviation_sd0() {
		double value = 100d;
		double standardDeviation = 0d;
		
		assertThrows(
			IllegalArgumentException.class,
			() -> Util.adjustForStandardDeviation(value, standardDeviation),
			"IllegalArgumentException is not thrown when standardDeviation is zero"
		);
	}

	@Test
	void testCalculateAverage_Positives() {
		double[] values = {0d,0.5d,10d,25d,37d,100d,293d};
		double expectedValue = 66.5d;
		
		double actualValue = Util.calculateAverage(values);
		
		assertEquals(expectedValue, actualValue, "Positive average of given array is not correctly calculated");
	}
	@Test
	void testCalculateAverage_Negatives() {
		double[] values = {-0d,-0.5d,-10d,-25d,-37d,-100d,-293d};
		double expectedValue = -66.5d;
		
		double actualValue = Util.calculateAverage(values);
		
		assertEquals(expectedValue, actualValue, "Negative average of given array is not correctly calculated");
	}
	@Test
	void testCalculateAverage_Positives_and_Negatives() {
		double[] values = {-0d,0.5d,-10d,25d,-37d,-100d,293d};
		double expectedValue = 24.5d;
		
		double actualValue = Util.calculateAverage(values);
		
		assertEquals(expectedValue, actualValue, "Pos/neg average of given array is not correctly calculated");
	}
	@Test
	void testCalculateAverage_noValuesInArray() {
		double[] values = {};
		
		assertThrows(
			IllegalArgumentException.class,
			() -> Util.calculateAverage(values),
			"No values in array is not correctly handled"
		);
	}

	@Test
	void testCalculateForecast() {
		double unscaledForecast = 2.5d;
		double scalar = 4d;
		double expectedValue = 10d;
		
		double actualValue = Util.calculateForecast(unscaledForecast, scalar);
		
		assertEquals(expectedValue, actualValue, "Forecast is not correctly calculated");
	}
	@Test
	void testCalculateForecast_scalar0() {
		double unscaledForecast = 2.5d;
		double scalar = 0d;
		
		assertThrows(
			IllegalArgumentException.class,
			() -> Util.calculateForecast(unscaledForecast, scalar),
			"IllegalArgumentException is not thrown when scalar is zero"
		);
	}
	@Test
	void testCalculateForecastScalar() {
		double[] values = {10d, 4d, -1d, 6d, -4d};
		double baseScale = 10d;
		double expectedValue = 2d;
		
		double actualValue = Util.calculateForecastScalar(values, baseScale);
		
		assertEquals(expectedValue, actualValue, "Forecast scalar is not correctly calculated");
	}
	@Test
	void testCalculateForecastScalar_absoluteAverage0() {
		double[] values = {0d, -0d};
		double baseScale = 10d;
		
		assertThrows(
			IllegalArgumentException.class,
			() -> Util.calculateForecastScalar(values, baseScale),
			"IllegalArgumentException is not thrown when average of absolute values is zero"
		);
	}
	@Test
	void testCalculateForecastScalar_noValues() {
		double[] values = {};
		double baseScale = 10d;
		
		assertThrows(
			IllegalArgumentException.class,
			() -> Util.calculateForecastScalar(values, baseScale),
			"IllegalArgumentException is not thrown when no values are given"
		);
	}
	
	@Test
	void testCalculateReturn_former200_latter300() {
		double formerValue = 200d;
		double latterValue = 300d;
		double expectedValue = 0.5d;
		
		double actualValue = Util.calculateReturn(formerValue, latterValue);
		
		assertEquals(expectedValue, actualValue, "Positive return is not correctly calculated");
	}
	@Test
	void testCalculateReturn_former300_latter200() {
		double formerValue = 400d;
		double latterValue = 200d;
		double expectedValue = -0.5d;
		
		double actualValue = Util.calculateReturn(formerValue, latterValue);
		
		assertEquals(expectedValue, actualValue, "Negative return is not correctly calculated");
	}
	@Test
	void testCalculateReturn_formerValue0() {
		double formerValue = 0d;
		double latterValue = 10d;
		
		assertThrows(
			IllegalArgumentException.class,
			() -> Util.calculateReturn(formerValue, latterValue),
			"IllegalArgumentException is not thrown when formerValue is zero"
		);
		
	}
}