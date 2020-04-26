package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.helper.BaseValueFactory;
import de.rumford.tradingsystem.helper.ValueDateTupel;

class EWMACTest {

	EWMAC ewmac;
	int shortHorizon;
	int longHorizon;
	static BaseValue baseValue;

	static final String BASE_VALUE_NAME = "Base value name";
	static final int BASE_SCALE = 10;

	static LocalDateTime localDateTimeJan01220000;
	static LocalDateTime localDateTimeJan02220000;
	static LocalDateTime localDateTimeJan03220000;
	static LocalDateTime localDateTimeJan04220000;
	static LocalDateTime localDateTimeJan05220000;
	static LocalDateTime localDateTimeJan06220000;
	static LocalDateTime localDateTimeJan07220000;
	static LocalDateTime localDateTimeJan08220000;
	static LocalDateTime localDateTimeJan09220000;
	static LocalDateTime localDateTimeJan10220000;
	static LocalDateTime localDateTimeJan11220000;
	static LocalDateTime localDateTimeJan12220000;
	static LocalDateTime localDateTimeJan13220000;
	static LocalDateTime localDateTimeJan14220000;
	static LocalDateTime localDateTimeJan15220000;
	static LocalDateTime localDateTimeJan16220000;
	static LocalDateTime localDateTimeJan17220000;
	static LocalDateTime localDateTimeJan18220000;
	static LocalDateTime localDateTimeJan19220000;
	static LocalDateTime localDateTimeJan20220000;
	static LocalDateTime localDateTimeJan21220000;
	static LocalDateTime localDateTimeJan22220000;
	static LocalDateTime localDateTimeJan23220000;
	static LocalDateTime localDateTimeJan24220000;
	static LocalDateTime localDateTimeJan25220000;
	static LocalDateTime localDateTimeJan26220000;
	static LocalDateTime localDateTimeJan27220000;
	static LocalDateTime localDateTimeJan28220000;
	static LocalDateTime localDateTimeJan29220000;
	static LocalDateTime localDateTimeJan30220000;
	static LocalDateTime localDateTimeJan31220000;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		baseValue = BaseValueFactory.jan1Jan31calcShort(BASE_VALUE_NAME);

		localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		localDateTimeJan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));
		localDateTimeJan06220000 = LocalDateTime.of(LocalDate.of(2020, 1, 6), LocalTime.of(22, 0));
		localDateTimeJan07220000 = LocalDateTime.of(LocalDate.of(2020, 1, 7), LocalTime.of(22, 0));
		localDateTimeJan08220000 = LocalDateTime.of(LocalDate.of(2020, 1, 8), LocalTime.of(22, 0));
		localDateTimeJan09220000 = LocalDateTime.of(LocalDate.of(2020, 1, 9), LocalTime.of(22, 0));
		localDateTimeJan10220000 = LocalDateTime.of(LocalDate.of(2020, 1, 10), LocalTime.of(22, 0));
		localDateTimeJan11220000 = LocalDateTime.of(LocalDate.of(2020, 1, 11), LocalTime.of(22, 0));
		localDateTimeJan12220000 = LocalDateTime.of(LocalDate.of(2020, 1, 12), LocalTime.of(22, 0));
		localDateTimeJan13220000 = LocalDateTime.of(LocalDate.of(2020, 1, 13), LocalTime.of(22, 0));
		localDateTimeJan14220000 = LocalDateTime.of(LocalDate.of(2020, 1, 14), LocalTime.of(22, 0));
		localDateTimeJan15220000 = LocalDateTime.of(LocalDate.of(2020, 1, 15), LocalTime.of(22, 0));
		localDateTimeJan16220000 = LocalDateTime.of(LocalDate.of(2020, 1, 16), LocalTime.of(22, 0));
		localDateTimeJan17220000 = LocalDateTime.of(LocalDate.of(2020, 1, 17), LocalTime.of(22, 0));
		localDateTimeJan18220000 = LocalDateTime.of(LocalDate.of(2020, 1, 18), LocalTime.of(22, 0));
		localDateTimeJan19220000 = LocalDateTime.of(LocalDate.of(2020, 1, 19), LocalTime.of(22, 0));
		localDateTimeJan20220000 = LocalDateTime.of(LocalDate.of(2020, 1, 20), LocalTime.of(22, 0));
		localDateTimeJan21220000 = LocalDateTime.of(LocalDate.of(2020, 1, 21), LocalTime.of(22, 0));
		localDateTimeJan22220000 = LocalDateTime.of(LocalDate.of(2020, 1, 22), LocalTime.of(22, 0));
		localDateTimeJan23220000 = LocalDateTime.of(LocalDate.of(2020, 1, 23), LocalTime.of(22, 0));
		localDateTimeJan24220000 = LocalDateTime.of(LocalDate.of(2020, 1, 24), LocalTime.of(22, 0));
		localDateTimeJan25220000 = LocalDateTime.of(LocalDate.of(2020, 1, 25), LocalTime.of(22, 0));
		localDateTimeJan26220000 = LocalDateTime.of(LocalDate.of(2020, 1, 26), LocalTime.of(22, 0));
		localDateTimeJan27220000 = LocalDateTime.of(LocalDate.of(2020, 1, 27), LocalTime.of(22, 0));
		localDateTimeJan28220000 = LocalDateTime.of(LocalDate.of(2020, 1, 28), LocalTime.of(22, 0));
		localDateTimeJan29220000 = LocalDateTime.of(LocalDate.of(2020, 1, 29), LocalTime.of(22, 0));
		localDateTimeJan30220000 = LocalDateTime.of(LocalDate.of(2020, 1, 30), LocalTime.of(22, 0));
		localDateTimeJan31220000 = LocalDateTime.of(LocalDate.of(2020, 1, 31), LocalTime.of(22, 0));
	}

	@BeforeEach
	void setUp() throws Exception {
		shortHorizon = 2;
		longHorizon = 8;
		ewmac = new EWMAC(baseValue, null, localDateTimeJan08220000, localDateTimeJan10220000, longHorizon,
				shortHorizon, BASE_SCALE);
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.EWMAC#EWMAC(BaseValue, Rule[], LocalDateTime, LocalDateTime, int, int, double)}.
	 */
	@Test
	void testEWMAC() {
		EWMAC ewmac2 = new EWMAC(baseValue, null, localDateTimeJan08220000, localDateTimeJan10220000, longHorizon,
				shortHorizon, BASE_SCALE);

		assertEquals(ewmac, ewmac2, "Two identical instances do not equal");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.EWMAC#calculateRawForecast(LocalDateTime)}.
	 */
	@Test
	void testCalculateRawForecast_negativeRawForecast() {
		double expectedValue = -13.177732526197076; // Excel: -13.1777325261971

		double actualValue = ewmac.calculateRawForecast(localDateTimeJan13220000);

		assertEquals(expectedValue, actualValue, "Negative raw Forecast is not correctly calculated");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.EWMAC#calculateRawForecast(LocalDateTime)}.
	 */
	@Test
	void testCalculateRawForecast_positiveRawForecast() {
		double expectedValue = 32.171834876807424; // Excel: 32.1718348768074

		double actualValue = ewmac.calculateRawForecast(localDateTimeJan08220000);

		assertEquals(expectedValue, actualValue, "Positive raw Forecast is not correctly calculated");
	}

	/**
	 * Test method for
	 * 
	 * {@link de.rumford.tradingsystem.EWMAC#calculateForecastScalar()}.
	 */
	@Test
	void testCalculateForecastScalar() {
		double expectedValue = 26.972575992335333; // Excel: 26.9725759923353

		double actualValue = ewmac.getForecastScalar();

		assertEquals(expectedValue, actualValue, "Forecast scalar is not correctly calculated");
	}

	/**
	 * Test method for
	 * 
	 * {@link de.rumford.tradingsystem.EWMAC#calculateForecastScalar()}.
	 */
	@Test
	void testCalculateForecastScalar_FC0() {
		double expectedValue = 0;

		EWMAC ewmac = new EWMAC(BaseValueFactory.jan1Jan31allVal0calcShort(BASE_VALUE_NAME), null,
				localDateTimeJan15220000, localDateTimeJan21220000, 8, 2, BASE_SCALE);

		double actualValue = ewmac.getForecastScalar();

		assertEquals(expectedValue, actualValue, "Forecast scalar of zero is not correctly calculated");
	}

	/**
	 * Test method for
	 * 
	 * {@link de.rumford.tradingsystem.EWMAC#calculateScaledForecasts()}.
	 */
	@Test
	void testCalculateForecasts() {
		double expectedValue = 4.28213669891743; // Excel: 4.28213669891743

		double actualValue = ValueDateTupel.getElement(ewmac.getForecasts(), localDateTimeJan10220000).getValue();

		assertEquals(expectedValue, actualValue, "Forecasts are not correctly calculated");
	}

	/**
	 * Test method for
	 * 
	 * {@link de.rumford.tradingsystem.EWMAC#calculateScaledForecasts()}.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	void testCalculateForecasts_unchangedOverTime() throws InterruptedException {
		double expectedValue = 4.28213669891743; // Excel: 4.28213669891743

		ValueDateTupel.getElement(ewmac.getForecasts(), localDateTimeJan10220000).getValue();
		double actualValue = ValueDateTupel.getElement(ewmac.getForecasts(), localDateTimeJan10220000).getValue();

		assertEquals(expectedValue, actualValue, "Forecasts are not correctly calculated");
	}

}
