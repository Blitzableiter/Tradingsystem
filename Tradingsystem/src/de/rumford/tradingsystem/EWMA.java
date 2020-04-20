package de.rumford.tradingsystem;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * 
 * @author Max Rumford
 *
 */
public class EWMA {

	private int horizon;
	private double decay;
	private ValueDateTupel[] baseValues;
	private ValueDateTupel[] ewmaValues;

	/**
	 * Constructor for the {@link EWMA} class
	 * 
	 * @param horizon {@code int} horizon this EWMA is to be over
	 */
	public EWMA(ValueDateTupel[] baseValues, int horizon) {
		// TODO INPUT SANITATION
		this.setHorizon(horizon);
		this.setDecay(this.calculateDecay(this.getHorizon()));
		this.setBaseValues(baseValues);
		this.setEwmaValues(this.calculateEwmaValues(this.getBaseValues()));
	}

	/**
	 * Calculate the EWMA-value for given previous value and base value
	 * 
	 * @param previousEWMA {@code double} EWMA of the previous time period
	 * @param baseValue    {@code double} base value of the current time period
	 * @return {@code double} EWMA for the current time period
	 */
	public double calculateEWMA(double previousEWMA, double baseValue) {
		/* E_t = A * P_t + [E_t-1 * ( 1 - A ) ] */
		double _ewma = this.getDecay() * baseValue + (previousEWMA * (1d - this.getDecay()));
		return _ewma;
	}

	/**
	 * Calculate the decay value based on the given horizon.
	 * 
	 * @param horizon {@code int} Horizon of this EWMA.
	 * @return {@code double} the decay used to calculate the importance of the
	 *         previous EWMA.
	 */
	private double calculateDecay(int horizon) {
		return 2d / (horizon + 1d);
	}

	/**
	 * Calculate the EWMA values based on the given base values.
	 * 
	 * @param baseValues {@code ValueDateTupel[]} The base values of the given
	 *                   asset.
	 * @return {@code ValueDateTupel[]} An array of calculated EWMA values.
	 */
	private ValueDateTupel[] calculateEwmaValues(ValueDateTupel[] baseValues) {
		ValueDateTupel[] ewmaValues = ValueDateTupel.createEmptyArray();
		double previousEwma = 0;
		/* Calculate all EWMA-Values */
		for (ValueDateTupel baseValue : baseValues) {
			/* Calculate the new values */
			double newValue = this.calculateEWMA(previousEwma, baseValue.getValue());
			/* Add the new value to the array of EWMA values */
			ewmaValues = ArrayUtils.add(ewmaValues, new ValueDateTupel(baseValue.getDate(), newValue));
			/* Set previousEwma to be that value for calculation of next value */
			previousEwma = newValue;
		}
		return ewmaValues;
	}

	/**
	 * ======================================================================
	 * OVERRIDES
	 * ======================================================================
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + horizon;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EWMA other = (EWMA) obj;
		if (horizon != other.horizon)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EWMA [horizon=" + horizon + "]";
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */

	/**
	 * Get the horizon of an EWMA
	 * 
	 * @return {@code int} horizon of the EWMA
	 */
	public int getHorizon() {
		return horizon;
	}

	/**
	 * Set the horizon of an EWMA
	 * 
	 * @param horizon {@code int} horizon to be set
	 */
	public void setHorizon(int horizon) {
		this.horizon = horizon;
	}

	/**
	 * Get the decay of an EWMA
	 * 
	 * @return {@code double} decay of the EWMA
	 */
	public double getDecay() {
		return this.decay;
	}

	/**
	 * Set the decay of an EWMA Is only called upon creation of a new instance,
	 * hence private
	 * 
	 * @param horizon {@code int} horizon on which the decay is derived from
	 */
	private void setDecay(double decay) {
		this.decay = decay;
	}

	/**
	 * @return baseValues EWMA
	 */
	public ValueDateTupel[] getBaseValues() {
		return baseValues;
	}

	/**
	 * @param baseValues the baseValues to set
	 */
	private void setBaseValues(ValueDateTupel[] baseValues) {
		this.baseValues = baseValues;
	}

	/**
	 * @return ewmaValues EWMA
	 */
	public ValueDateTupel[] getEwmaValues() {
		return ewmaValues;
	}

	/**
	 * @param ewmaValues the ewmaValues to set
	 */
	private void setEwmaValues(ValueDateTupel[] ewmaValues) {
		this.ewmaValues = ewmaValues;
	}

}
