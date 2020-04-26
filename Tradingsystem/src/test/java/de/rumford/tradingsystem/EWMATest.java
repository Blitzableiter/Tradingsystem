package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.helper.BaseValueFactory;

class EWMATest {

	EWMA ewma2;
	EWMA ewma2_1;
	EWMA ewma4;
	EWMA ewma8;

	BaseValue baseValue = BaseValueFactory.jan1Jan5calcShort("My base value");

	@BeforeEach
	void setUp() throws Exception {
		ewma2 = new EWMA(baseValue.getValues(), 2);
		ewma2_1 = new EWMA(baseValue.getValues(), 2);
		ewma4 = new EWMA(baseValue.getValues(), 4);
		ewma8 = new EWMA(baseValue.getValues(), 8);
	}

	@Test
	void testEWMA_ewma_instanceof_EWMA() {
		assertTrue(ewma2 instanceof EWMA, "ewma2 is instanceof EWMA");
		assertEquals(ewma2, ewma2_1, "Two EWMAs with the same horizon are equal");
	}

	@Test
	void testCalculateEWMA_Horizon2_baseValue786point75_Is_524point5() {
		double calculatedValue = ewma2.calculateEWMA(0d, 786.75d);
		double expectedValue = 524.5d;
		assertEquals(expectedValue, calculatedValue, "Calculated EWMA is expected EWMA");
	}

	@Test
	void testCalculateEWMA_Horizon8_baseValue786point75_Is_174point833() {
		double calculatedValue = ewma2.calculateEWMA(0d, 100d);
		double expectedValue = (0d + 2d / 3d) * 100d;
		assertEquals(expectedValue, calculatedValue, "Calculated EWMA is expected EWMA");
	}

	@Test
	void testGetDecay_DecayForHorizon2_Is_point67() {
		double expectedValue = 2d / 3d;
		assertEquals(expectedValue, ewma2.getDecay(), "Calculated Decay is expected Decay");
	}

}
