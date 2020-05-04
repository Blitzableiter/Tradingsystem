package de.rumford.tradingsystem;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.GeneratedCode;
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
		this.validateBaseValues(baseValues);
		this.validateHorizon(horizon);

		this.setBaseValues(baseValues);
		this.setHorizon(horizon);
		this.setDecay(this.calculateDecay(this.getHorizon()));
		this.setEwmaValues(this.calculateEwmaValues(this.getBaseValues()));
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
	 * Calculate the EWMA-value for given previous value and base value
	 * 
	 * @param previousEWMA {@code double} EWMA of the previous time period
	 * @param baseValue    {@code double} base value of the current time period
	 * @return {@code double} EWMA for the current time period
	 */
	public double calculateEWMA(double previousEWMA, double baseValue) {
		/* E_t = A * P_t + [E_t-1 * ( 1 - A ) ] */
		return this.getDecay() * baseValue + (previousEWMA * (1d - this.getDecay()));
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
			double newValue = 0;
			if (Double.isNaN(baseValue.getValue())) {
				newValue = Double.NaN;
				previousEwma = 0;
			} else {
				/* Calculate the new values */
				newValue = this.calculateEWMA(previousEwma, baseValue.getValue());
				previousEwma = newValue;
			}
			/* Add the new value to the array of EWMA values */
			ewmaValues = ArrayUtils.add(ewmaValues, new ValueDateTupel(baseValue.getDate(), newValue));
		}
		return ewmaValues;
	}

	/**
	 * Validates the given horizon.
	 * 
	 * @param horizon {@code int} the horizon to be validated.
	 * @throws IllegalArgumentException if the given horizon is < 2.
	 */
	private void validateHorizon(int horizon) {
		if (horizon < 2)
			throw new IllegalArgumentException("The horizon must not be < 2");
	}

	/**
	 * Validates the given base values.
	 * 
	 * @param baseValues {@code ValueDateTupel[]} the base values the EWMA is to be
	 *                   calculated on.
	 * @throws IllegalArgumentException if the length of the base values array is 0.
	 * @throws IllegalArgumentException if {@code validateDates(ValueDateTupel[])}
	 *                                  in {@code BaseValue} throws an
	 *                                  IllegalArgumentException.
	 */
	private void validateBaseValues(ValueDateTupel[] baseValues) {
		/* Check if passed values array contains elements */
		if (baseValues.length == 0)
			throw new IllegalArgumentException("Base values must not be an empty array");

		BaseValue.validateDates(baseValues);
	}

	/**
	 * ======================================================================
	 * OVERRIDES
	 * ======================================================================
	 */

	@GeneratedCode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + horizon;
		return result;
	}

	@GeneratedCode
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

	@GeneratedCode
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
