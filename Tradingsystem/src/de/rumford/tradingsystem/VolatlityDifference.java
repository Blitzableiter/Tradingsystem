/**
 * 
 */
package de.rumford.tradingsystem;

import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * @author Max Rumford
 *
 */
public class VolatlityDifference extends Rule {

	private ValueDateTupel[] volatilityIndices;
	private double averageVolatility;
//	TODO
//	public BaseValue baseValue;

	/**
	 * 
	 */
	public VolatlityDifference() {
		// TODO Auto-generated constructor stub
	}

	public double calculateRawForecast(double currentVolatilty) {
		return this.getAverageVolatility() - currentVolatilty;
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */
	/**
	 * Get the volatility indices
	 * 
	 * @return {@code ValueDateTupel[]} the volatilityIndices
	 */
	public ValueDateTupel[] getVolatilityIndices() {
		return volatilityIndices;
	}

	/**
	 * Set the volatilityIndices
	 * 
	 * @param volatilityIndices {@code ValueDateTupel} the volatilityIndices to set
	 */
	public void setVolatilityIndices(ValueDateTupel[] volatilityIndices) {
		this.volatilityIndices = volatilityIndices;
	}

	/**
	 * @return the averageVolatility
	 */
	public double getAverageVolatility() {
		return averageVolatility;
	}

	/**
	 * @param averageVolatility the averageVolatility to set
	 */
	public void setAverageVolatility(double averageVolatility) {
		this.averageVolatility = averageVolatility;
	}

}
