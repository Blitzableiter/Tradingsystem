/**
 * 
 */
package de.rumford.tradingsystem;

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
	}

	public double calculateRawForecast(double currentVolatilty) {
		return this.getAverageVolatility() - currentVolatilty;
	}

	/**
	 * Calculate the volatility index values for its {@code VolatilityDifference}.
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

		for (int i = 0; i < lookbackWindow; i++) {
//			TODO calculate volatility values
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
	public void setVolatilityIndices(ValueDateTupel[] volatilityIndices) {
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
	public void setAverageVolatility(double averageVolatility) {
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
