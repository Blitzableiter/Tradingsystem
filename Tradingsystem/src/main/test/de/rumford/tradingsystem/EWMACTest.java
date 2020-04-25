package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EWMACTest {

	EWMAC ewmac;
	int shortHorizon;
	int longHorizon;

	static final String BASE_VALUE_NAME = "Base value name";
	static final int BASE_SCALE = 10;

	static LocalDateTime localDateTimeJan01_22_00_00;
	static LocalDateTime localDateTimeJan02_22_00_00;
	static LocalDateTime localDateTimeJan03_22_00_00;
	static LocalDateTime localDateTimeJan04_22_00_00;
	static LocalDateTime localDateTimeJan05_22_00_00;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		localDateTimeJan01_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		localDateTimeJan02_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		localDateTimeJan03_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		localDateTimeJan04_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		localDateTimeJan05_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));
	}

	void setUp() throws Exception {
		shortHorizon = 2;
		longHorizon = 8;
		ewmac = new EWMAC(BaseValue.jan1_jan4_22_00_00_val200_400_500_200_calc_short(BASE_VALUE_NAME), null,
				localDateTimeJan01_22_00_00, localDateTimeJan04_22_00_00, longHorizon, shortHorizon, BASE_SCALE);
	}

//	@Test
//	void testCalculateRawForecast_negativeRawForecast() {
//		double shortHorizonForecast = 12d;
//		double longHorizonForecast = 15d;
//		double expectedValue = -3d;
//
//		double actualValue = ewmac.calculateRawForecast(shortHorizonForecast, longHorizonForecast);
//
//		assertEquals(expectedValue, actualValue, "Negative raw Forecast is not correctly calculated");
//	}

//	@Test
//	void testCalculateRawForecast_positiveRawForecast() {
//		double shortHorizonForecast = 15d;
//		double longHorizonForecast = 12d;
//		double expectedValue = 3d;
//
//		double actualValue = ewmac.calculateRawForecast(shortHorizonForecast, longHorizonForecast);
//
//		assertEquals(expectedValue, actualValue, "Positive raw Forecast is not correctly calculated");
//	}

//	@Test
//	void testCalculateRawForecast_rawForecast0() {
//		double shortHorizonForecast = 15d;
//		double longHorizonForecast = 15d;
//		double expectedValue = 0d;
//
//		double actualValue = ewmac.calculateRawForecast(shortHorizonForecast, longHorizonForecast);
//
//		assertEquals(expectedValue, actualValue, "Zero raw Forecast is not correctly calculated");
//	}

}
