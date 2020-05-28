package de.rumford.tradingsystem;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Validator;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * The EWMA class represents the mathematical concept of an exponentially
 * weighted moving average. In an EWMA, the "older" a given base value is (in
 * proportion to the "current" base value) the less it influences the current
 * EWMA value. This influence deteriorates exponentially by the given horizon to
 * the power of 2, thus the name.
 * 
 * @author Max Rumford
 *
 */
public class EWMA {

	/* The horizon this EWMA shall cover. */
	private int horizon;
	/* The decay factor for recent values calculated from the given horizon. */
	private double decay;
	/* The values this EWMA shall be based upon. */
	private ValueDateTupel[] baseValues;
	/* The calculated EWMA values. */
	private ValueDateTupel[] ewmaValues;

	/**
	 * Constructor for the {@link EWMA} class
	 *
	 * @param baseValues {@code ValueDateTupel[]} The values this EWMA shall is
	 *                   to be based on.
	 * @param horizon    {@code int} horizon this EWMA is to be over
	 */
	public EWMA(ValueDateTupel[] baseValues, int horizon) {
		validateBaseValues(baseValues);
		validateHorizon(horizon);

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
		return this.getDecay() * baseValue
				+ (previousEWMA * (1d - this.getDecay()));
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
				newValue = this.calculateEWMA(previousEwma,
						baseValue.getValue());
				previousEwma = newValue;
			}
			/* Add the new value to the array of EWMA values */
			ewmaValues = ArrayUtils.add(ewmaValues,
					new ValueDateTupel(baseValue.getDate(), newValue));
		}
		return ewmaValues;
	}

	/**
	 * Validates the given base values.
	 * 
	 * @param baseValues {@code ValueDateTupel[]} the base values the EWMA is to
	 *                   be calculated on. Must pass
	 *                   {@link Validator#validateValues(ValueDateTupel[])} and
	 *                   {@link Validator#validateDates(ValueDateTupel[])}.
	 * @throws IllegalArgumentException if the above specifications are not met.
	 */
	private static void validateBaseValues(ValueDateTupel[] baseValues) {
		try {
			Validator.validateValues(baseValues);
			Validator.validateDates(baseValues);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"The given values do not meet the specifications.", e);
		}
	}

	/**
	 * Validates the given horizon.
	 * 
	 * @param horizon {@code int} the horizon to be validated.
	 * @throws IllegalArgumentException if the given horizon is < 2.
	 */
	private static void validateHorizon(int horizon) {
		if (horizon < 2)
			throw new IllegalArgumentException("The horizon must not be < 2");
	}

	/**
	 * ======================================================================
	 * OVERRIDES
	 * ======================================================================
	 */

	/**
	 * A hash code for this EWMA.
	 */
	@GeneratedCode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + horizon;
		return result;
	}

	/**
	 * Checks if this EWMA is equal to another EWMA.
	 */
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

	/**
	 * Outputs the fields of this EWMA as a {@code String}.
	 */
	@GeneratedCode
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EWMA [horizon=");
		builder.append(horizon);
		builder.append(", decay=");
		builder.append(decay);
		builder.append(", baseValues=");
		builder.append(Arrays.toString(baseValues));
		builder.append(", ewmaValues=");
		builder.append(Arrays.toString(ewmaValues));
		builder.append("]");
		return builder.toString();
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */

	/**
	 * Get the horizon of this EWMA.
	 * 
	 * @return {@code int} horizon of the EWMA
	 */
	public int getHorizon() {
		return horizon;
	}

	/**
	 * Set the horizon of this EWMA.
	 * 
	 * @param horizon {@code int} horizon to be set
	 */
	public void setHorizon(int horizon) {
		this.horizon = horizon;
	}

	/**
	 * Get the decay of this EWMA.
	 * 
	 * @return {@code double} decay of the EWMA
	 */
	public double getDecay() {
		return this.decay;
	}

	/**
	 * Set the decay of this EWMA.
	 * 
	 * @param horizon {@code int} horizon on which the decay is derived from
	 */
	private void setDecay(double decay) {
		this.decay = decay;
	}

	/**
	 * Get the base values of this EWMA.
	 * 
	 * @return baseValues EWMA
	 */
	public ValueDateTupel[] getBaseValues() {
		return baseValues;
	}

	/**
	 * Set the base values of this EWMA.
	 * 
	 * @param baseValues the baseValues to set
	 */
	private void setBaseValues(ValueDateTupel[] baseValues) {
		this.baseValues = baseValues;
	}

	/**
	 * Get the EWMA values of this EWMA.
	 * 
	 * @return ewmaValues EWMA
	 */
	public ValueDateTupel[] getEwmaValues() {
		return ewmaValues;
	}

	/**
	 * Set the EWMA values of this EWMA.
	 * 
	 * @param ewmaValues the ewmaValues to set
	 */
	private void setEwmaValues(ValueDateTupel[] ewmaValues) {
		this.ewmaValues = ewmaValues;
	}

}
