package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EWMACTest {

	EWMAC ewmac;
	int shortHorizon;
	int longHorizon;
	
	@BeforeEach
	void setUp() throws Exception {
		shortHorizon = 2;
		longHorizon = 8;
		ewmac = new EWMAC(shortHorizon, longHorizon);
	}

	@Test
	void testCalculateRawForecast_negativeRawForecast() {
		double shortHorizonForecast = 12d;
		double longHorizonForecast = 15d;
		double expectedValue = -3d;
		
		double actualValue = ewmac.calculateRawForecast(shortHorizonForecast, longHorizonForecast);
		
		assertEquals(expectedValue, actualValue, "Negative raw Forecast is not correctly calculated");
	}
	@Test
	void testCalculateRawForecast_positiveRawForecast() {
		double shortHorizonForecast = 15d;
		double longHorizonForecast = 12d;
		double expectedValue = 3d;
		
		double actualValue = ewmac.calculateRawForecast(shortHorizonForecast, longHorizonForecast);
		
		assertEquals(expectedValue, actualValue, "Positive raw Forecast is not correctly calculated");
	}
	@Test
	void testCalculateRawForecast_rawForecast0() {
		double shortHorizonForecast = 15d;
		double longHorizonForecast = 15d;
		double expectedValue = 0d;
		
		double actualValue = ewmac.calculateRawForecast(shortHorizonForecast, longHorizonForecast);
		
		assertEquals(expectedValue, actualValue, "Zero raw Forecast is not correctly calculated");
	}

}
