/**
 * 
 */
package de.rumford.tradingsystem;

import java.util.DoubleSummaryStatistics;

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
	 * Creates a new {@link VolatilityDifference} instance using the passed
	 * {@link BaseValue} to calculate the volatility indices and the average
	 * volatility.
	 * 
	 * @param baseValue      {@link BaseValue} The base value to be used for this
	 *                       {@link VolatilityDifference}. Must not be null. Values
	 *                       array must be of of length > {@code 0}.
	 * @param lookbackWindow {@code int} The lookback window to be used for this
	 *                       {@link VolatilityDifference}. Must be > {@code 1}.
	 */
	public VolatilityDifference(BaseValue baseValue, int lookbackWindow) throws IllegalArgumentException {
		/* Check if base value fulfills requirements. If yes, set it */
		if (baseValue == null)
			throw new IllegalArgumentException("Base value must not be null");
		if (baseValue.getValues().length == 0)
			throw new IllegalArgumentException("Base value must contain at least one value");
		this.setBaseValue(baseValue);

		/* Check if lookback window fulfills requirements. If yes, set it */
		if (lookbackWindow <= 1)
			throw new IllegalArgumentException("Lookback window must be at least 2");
		this.setLookbackWindow(lookbackWindow);

		/* Calculate volatility index values based on the base value and set it */
		this.setVolatilityIndices(this.calculateVolatilityIndices());

		/*
		 * Calculate the average volatility for the base value over all available
		 * volatility index values
		 */
		this.calculateAverageVolatility();
	}

	public double calculateRawForecast(double currentVolatilty) {
		return this.getAverageVolatility() - currentVolatilty;
	}

	/**
	 * Calculate the volatility index values for its {@link VolatilityDifference}.
	 * If the lookback window is longer than there are base values, an empty
	 * {@link ValueDateTupel[]} is returned.
	 * 
	 * @return {@link ValueDateTupel[]} calculated volatility indices. If the number
	 *         of values in the instance base value is smaller than the lookback
	 *         window the returned array only contains {@code Double.NaN}. Else, all
	 *         values until the lookback window is reached contain
	 *         {@code Double.NaN}, the rest contains real volatility index values.
	 */
	private ValueDateTupel[] calculateVolatilityIndices() {
		ValueDateTupel[] baseValues = this.baseValue.getValues();
		int lookbackWindow = this.getLookbackWindow();

		ValueDateTupel[] volatilityIndices = ValueDateTupel.createEmptyArray();

		/**
		 * Fill the spaces before reaching lookbackWindow with NaN
		 */
		for (int i = 0; i < lookbackWindow - 1; i++) {
			ValueDateTupel volatilityIndexNaN = new ValueDateTupel(baseValues[i].getDate(), Double.NaN);
			ArrayUtils.add(volatilityIndices, volatilityIndexNaN);
		}

		/**
		 * If there are less base values than the lookback window is long no volatility
		 * values can be calculated. The volatility values only have any true meaning,
		 * when the lookback window is used in its entirety.
		 */
		if (baseValues.length < lookbackWindow)
			return volatilityIndices;

		/**
		 * Start calculation with first adequate time value (after lookback window is
		 * reached), e.g. lookbackWindow = 4, start with index 3 (4th element)
		 */
		for (int i = lookbackWindow - 1; i < baseValues.length; i++) {
			/* Copy the relevant values into a temporary array */
			ValueDateTupel[] tempBaseValues = new ValueDateTupel[lookbackWindow];
			System.arraycopy(baseValues, /* source array */
					i - (lookbackWindow - 1), /* source array position (starting position for copy) */
					tempBaseValues, /* destination array */
					0, /* destination position (starting position for paste) */
					tempBaseValues.length /* length (number of values to copy */
			);

			/* Extract relevant values to be used in standard deviation */
			double[] tempDoubleValues = {};
			for (ValueDateTupel tempBaseValue : tempBaseValues)
				ArrayUtils.add(tempDoubleValues, tempBaseValue.getValue());

			/* Calculate standard deviation and save into local variable */
			StandardDeviation sd = new StandardDeviation();
			double volatilityIndexValue = sd.evaluate(tempDoubleValues);

			ValueDateTupel volatilityIndexValueDateTupel = new ValueDateTupel(baseValues[i].getDate(),
					volatilityIndexValue);

			/* Add calculated standard deviation to volatility indices */
			ArrayUtils.add(volatilityIndices, volatilityIndexValueDateTupel);
		}

		return volatilityIndices;
	}

	/**
	 * Set the average volatility for this {@link VolatilityDifference}. Calculates
	 * the value based on the lookback window and the previously set volatility
	 * index values.
	 * 
	 * @param averageVolatility {@code double} The average volatility} to be set
	 */
	private void calculateAverageVolatility() {
		/* Get all volatility index values */
		ValueDateTupel[] allVolatilityIndices = this.getVolatilityIndices();

		/*
		 * If the last volatility index value is NaN then no volatility index values
		 * were calculated due to there not being enough values. If this is the case,
		 * the average volatility is set to be Double.NaN
		 */
		if (allVolatilityIndices[allVolatilityIndices.length - 1].getValue() == Double.NaN) {
			this.averageVolatility = Double.NaN;
			return;
		}

		/* Get lookbackWindow to evaluate the values relevant for the calculation */
		int lookbackWindow = this.getLookbackWindow();

		ValueDateTupel[] relevantVolatilityIndices = {};
		/* Copy the relevant volatility index values into a temporary array */
		System.arraycopy(allVolatilityIndices, /* source array */
				lookbackWindow - 1, /* source array position (starting position for copy) */
				relevantVolatilityIndices, /* destination array */
				0, /* destination position (starting position for paste) */
				allVolatilityIndices.length - lookbackWindow + 1 /* length (number of values to copy */
		);

		DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
		/* Extract all relevant values into statistics object */
		for (ValueDateTupel volatilityIndex : relevantVolatilityIndices)
			stats.accept(volatilityIndex.getValue());

		/* Put average value of relevant values into class variable */
		this.averageVolatility = stats.getAverage();
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
	 * Get the base value for this {@link VolatilityDifference}.
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
