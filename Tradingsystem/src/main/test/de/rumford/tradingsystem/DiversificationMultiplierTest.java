/**
 * 
 */
package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Max Rumford
 *
 */
class DiversificationMultiplierTest {

	final double[] weights = { 0.5d, 0.5d };
	final double[][] correlations = { { 1d, 0.75d }, { 0.75d, 1d } };
	DiversificationMultiplier dm;

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#DiversificationMultiplier(double[], double[][])}.
	 */
	@Test
	void testDiversificationMultiplier() {
		dm = new DiversificationMultiplier(weights, correlations);

		assertTrue(dm instanceof DiversificationMultiplier, "Instance of DiversificationMultiplier not recognized");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#DiversificationMultiplier(double[], double[][])}.
	 */
	@Test
	void testDiversificationMultiplier_noCorrelations() {
		double[][] correlations = { {}, {} };

		assertThrows(IllegalArgumentException.class, () -> new DiversificationMultiplier(weights, correlations),
				"No correlations are not being correctly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#DiversificationMultiplier(double[], double[][])}.
	 */
	@Test
	void testDiversificationMultiplier_noWeights() {
		double[] weights = {};

		assertThrows(IllegalArgumentException.class, () -> new DiversificationMultiplier(weights, correlations),
				"No weights are not being correctly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#DiversificationMultiplier(double[], double[][])}.
	 */
	@Test
	void testDiversificationMultiplier_numberOfCorrelationRowsNotEqualColumns() {
		double[][] correlations = { { 1d, 0.75d }, { 0.75d } };

		assertThrows(IllegalArgumentException.class, () -> new DiversificationMultiplier(weights, correlations),
				"Inequal number of correlation rows and columns are not being correctly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#DiversificationMultiplier(double[], double[][])}.
	 */
	@Test
	void testDiversificationMultiplier_numberOfWeightsNotEqualNumberOfCorrelations() {
		double[] weights = { 0.75d };

		assertThrows(IllegalArgumentException.class, () -> new DiversificationMultiplier(weights, correlations),
				"Inequal number of correlations and weights is not being correctly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#DiversificationMultiplier(double[], double[][])}.
	 */
	@Test
	void testDiversificationMultiplier_negativeWeights() {
		double[] weights = { -1d, 1d };

		assertThrows(IllegalArgumentException.class, () -> new DiversificationMultiplier(weights, correlations),
				"Negative weights are not being correctly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#DiversificationMultiplier(double[], double[][])}.
	 */
	@Test
	void testDiversificationMultiplier_weightsDontAddUpTo1() {
		double[] weights = { 0.75d, 0.75d };

		assertThrows(IllegalArgumentException.class, () -> new DiversificationMultiplier(weights, correlations),
				"Weights not adding up to 1 is not being correctly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#DiversificationMultiplier(double[], double[][])}.
	 */
	@Test
	void testDiversificationMultiplier_assymetricalCorrelationsMatrix() {
		double[][] correlations = { { 1d, 0.75d }, { 0.6d, 1d } };

		assertThrows(IllegalArgumentException.class, () -> new DiversificationMultiplier(weights, correlations),
				"Assymetrical correlations matrix is not being correctly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#DiversificationMultiplier(double[], double[][])}.
	 */
	@Test
	void testDiversificationMultiplier_invalidSelfCorrelations() {
		double[][] correlations = { { 1d, 0.75d }, { 0.75d, 0.8d } };

		assertThrows(IllegalArgumentException.class, () -> new DiversificationMultiplier(weights, correlations),
				"Invalid self correlations are not being correctly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#getValue()}.
	 */
	@Test
	void testGetValue() {
		dm = new DiversificationMultiplier(weights, correlations);
		double expectedValue = 1 / Math.sqrt(0.875d);

		double actualValue = dm.getValue();

		assertEquals(expectedValue, actualValue, "Value is not correctly calculated");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#getValue()}.
	 */
	@Test
	void testGetValue_incorrectPopulationOfWeights() {
		double[] weights = { 0.5d, 0.6d };

		try {
			dm = new DiversificationMultiplier(weights, correlations);
		} catch (Exception e) {
			/**
			 * See testDiversificationMultiplier_weightsDontAddUpTo1()
			 */
		}

		assertThrows(NullPointerException.class, () -> dm.getValue(),
				"No error is thrown although instanciation variables were not in spec");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#getValue()}.
	 */
	@Test
	void testGetValue_incorrectPopulationOfCorrelations() {
		double[][] correlations = { { 1d, 0.5d }, { 0.6d, 1d } };

		try {
			dm = new DiversificationMultiplier(weights, correlations);
		} catch (Exception e) {
			/**
			 * See testDiversificationMultiplier_assymetricalCorrelationsMatrix()
			 */
		}

		assertThrows(NullPointerException.class, () -> dm.getValue(),
				"No error is thrown although instanciation variables were not in spec");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#getWeights()}.
	 */
	@Test
	void testGetWeights() {
		dm = new DiversificationMultiplier(weights, correlations);

		double[] localWeights = dm.getWeights();

		assertEquals(weights, localWeights,
				"Weights were either not correctly set or could not be correctly retrieved");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.DiversificationMultiplier#getCorrelations()}.
	 */
	@Test
	void testGetCorrelations() {
		dm = new DiversificationMultiplier(weights, correlations);

		double[][] localCorrelations = dm.getCorrelations();

		assertEquals(correlations, localCorrelations,
				"Correlations were either not correctly set or could not be correctly retrieved");
	}

}
