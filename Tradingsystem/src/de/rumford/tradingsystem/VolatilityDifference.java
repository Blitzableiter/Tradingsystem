/**
 * 
 */
package de.rumford.tradingsystem;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * @author Max Rumford
 *
 */
public class VolatilityDifference extends Rule {

	private ValueDateTupel[] volatilityIndices;
	private double averageVolatility;
	private BaseValue baseValue;
	private int lookbackWindow;

	/**
	 * 
	 */
	public VolatilityDifference() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates a new {@link VolatilityDifference} instance using the passed
	 * {@link BaseValue} to calculate the {@code volatilityIndices} and the
	 * {@code averageVolatility}.
	 * 
	 * @param baseValue      {@link BaseValue} The base value to be used for this
	 *                       {@link VolatilityDifference}. Must be of length >
	 *                       {@code 0}.
	 * @param lookbackWindow {@code int} The lookback window to be used for this
	 *                       {@link VolatilityDifference}.
	 */
	public VolatilityDifference(BaseValue baseValue, int lookbackWindow) {
		this.setBaseValue(baseValue);
		this.setLookbackWindow(lookbackWindow);
		this.setVolatilityIndices(this.calculateVolatilityIndices());
		this.setAverageVolatility();
	}

	public double calculateRawForecast(double currentVolatilty) {
		return this.getAverageVolatility() - currentVolatilty;
	}

	/**
	 * Calculate the volatility index values for its {@link VolatilityDifference}.
	 * If the lookback window is longer than there are base values, an empty
	 * {@link ValueDateTupel[]} is returned.
	 * 
	 * @return
	 */
	private ValueDateTupel[] calculateVolatilityIndices() {
		ValueDateTupel[] baseValues = this.baseValue.getValues();
		int lookbackWindow = this.getLookbackWindow();

		/**
		 * If there are less base values than the lookback window is long no volatility
		 * values can be calculated. The volatility values only have any true meaning,
		 * when the lookback window is used in its entirety.
		 */
		if (baseValues.length < lookbackWindow)
			return new ValueDateTupel[baseValues.length];

		ValueDateTupel[] volatilityIndices = ValueDateTupel.createEmptyArray();

		/**
		 * Fill the spaces before reaching lookbackWindow with NaN
		 */
		for (int i = 0; i < lookbackWindow - 1; i++)
			ArrayUtils.add(volatilityIndices, Double.NaN);
		/**
		 * Start calculation with first adequate time value (after lookback window is
		 * reached), e.g. lookbackWindow = 4, start with index 3 (4th element)
		 */
		for (int i = lookbackWindow - 1; i < baseValues.length; i++) {
			// Copy the relevant values into a temporary array
			ValueDateTupel[] tempBaseValues = new ValueDateTupel[lookbackWindow];
			System.arraycopy(baseValues, // src
					i - (lookbackWindow - 1), // srcPos
					tempBaseValues, // dest
					0, // destPos
					tempBaseValues.length // length
			);

			// Extract relevant values to be used in standard deviation
			double[] tempDoubleValues = {};
			for (ValueDateTupel tempBaseValue : tempBaseValues)
				ArrayUtils.add(tempDoubleValues, tempBaseValue.getValue());

			// Calculate standard deviation and save into local variable
			StandardDeviation sd = new StandardDeviation();
			double volatilityIndexValue = sd.evaluate(tempDoubleValues);

			// Add calculated standard deviation to volatility indices
			ArrayUtils.add(volatilityIndices, volatilityIndexValue);
		}

		return volatilityIndices;
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */
	/**
	 * Get the volatility indices for this {@link VolatilityDifference}.
	 * 
	 * @return {@link ValueDateTupel[]} The volatility indices for this
	 *         {@link VolatilityDifference}
	 */
	public ValueDateTupel[] getVolatilityIndices() {
		return volatilityIndices;
	}

	/**
	 * Set the volatilityIndices
	 * 
	 * @param volatilityIndices {@link ValueDateTupel[]} the volatilityIndices to
	 *                          set
	 */
	private void setVolatilityIndices(ValueDateTupel[] volatilityIndices) {
		this.volatilityIndices = volatilityIndices;
	}

	/**
	 * Get the average volatility for this {@link VolatilityDifference}.
	 * 
	 * @return {@code double} the average volatility of this
	 *         {@link VolatilityDifference}
	 */
	public double getAverageVolatility() {
		return averageVolatility;
	}

	/**
	 * Set the average volatility for this {@link VolatilityDifference}.
	 * 
	 * @param averageVolatility {@code double} The {@code averageVolatility} to be
	 *                          set
	 */
	private void setAverageVolatility() {
//		TODO testme
//		TODO testme
//		TODO testme
//		TODO testme
		ValueDateTupel[] allVolatilityIndices = this.getVolatilityIndices();
		int lookbackWindow = this.getLookbackWindow();
		ValueDateTupel[] relevantVolatilityIndices = {};
		System.arraycopy(allVolatilityIndices, // src
				lookbackWindow - 1, // srcPos
				relevantVolatilityIndices, // dest
				0, // destPos
				allVolatilityIndices.length - lookbackWindow // length
		);

		this.averageVolatility = averageVolatility;
	}

	/**
	 * Get the base value for this {@code VolatilityDifference}.
	 * 
	 * @return {@link BaseValue} The base value for this
	 *         {@link VolatilityDifference}.
	 */
	public BaseValue getBaseValue() {
		return baseValue;
	}

	/**
	 * Set the base value for this {@link VolatilityDifference}.
	 * 
	 * @param baseValue {@link BaseValue} The baseValue to be set for this
	 *                  {@link VolatilityDifference}.
	 */
	private void setBaseValue(BaseValue baseValue) {
		this.baseValue = baseValue;
	}

	/**
	 * Get the lookbackWindow for this {@link VolatilityDifference}.
	 * 
	 * @return {@code int} The lookback window for this {@link VolatilityDifference}
	 */
	public int getLookbackWindow() {
		return lookbackWindow;
	}

	/**
	 * Set the lookback window for this {@link VolatilityDifference}.
	 * 
	 * @param lookbackWindow {@code int} The lookback window to be set for this
	 *                       {@link VolatilityDifference}
	 */
	private void setLookbackWindow(int lookbackWindow) {
		this.lookbackWindow = lookbackWindow;
	}

}
