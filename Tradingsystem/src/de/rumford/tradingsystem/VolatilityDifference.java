/**
 * 
 */
package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.util.Arrays;
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
	private int lookbackWindow;

	/**
	 * Creates a new {@link VolatilityDifference} instance using the passed
	 * {@link BaseValue} to calculate the volatility indices and the average
	 * volatility.
	 * 
	 * @param baseValue              {@link BaseValue} The base value to be used for
	 *                               this {@link VolatilityDifference}. Must not be
	 *                               null.
	 * @param variations             {@code VolatilityDifference[]} An array of
	 *                               three or less rules. Represents the variations
	 *                               of this rule.
	 * @param startOfReferenceWindow {@link LocalDateTime} First DateTime value to
	 *                               be considered in average calculation. Must be <
	 *                               {@code endOfReferenceWindow}. Must not be null.
	 * @param endOfReferenceWindow   {@link LocalDateTime} Last DateTime value to be
	 *                               considered in average calculation. Must be >
	 *                               {@code startOfReferenceWindow}. Must not be
	 *                               null.
	 * @param lookbackWindow         {@code int} The lookback window to be used for
	 *                               this {@link VolatilityDifference}. Must be >
	 *                               {@code 1}.
	 * @param baseScale              {@code double} The base scale used for the
	 *                               calculation of the forecast scalar and thus in
	 *                               scaled forecast calculations as well.
	 * @throws IllegalArgumentException If the given arguments to not satisfy
	 *                                  specifications.
	 */
	public VolatilityDifference(BaseValue baseValue, VolatilityDifference[] variations,
			LocalDateTime startOfReferenceWindow, LocalDateTime endOfReferenceWindow, int lookbackWindow,
			double baseScale) {
		this(baseValue, variations, startOfReferenceWindow, endOfReferenceWindow, lookbackWindow, baseScale,
				calculateVolatilityIndices(baseValue, lookbackWindow));
	}

	/**
	 * Creates a new {@link VolatilityDifference} instance using the passed
	 * {@link BaseValue} to calculate the volatility indices and the average
	 * volatility.
	 * 
	 * @param baseValue              {@link BaseValue} The base value to be used for
	 *                               this {@link VolatilityDifference}. Must not be
	 *                               null.
	 * @param variations             {@code VolatilityDifference[]} An array of
	 *                               three or less rules. Represents the variations
	 *                               of this rule.
	 * @param startOfReferenceWindow {@link LocalDateTime} First DateTime value to
	 *                               be considered in average calculation. Must be <
	 *                               {@code endOfReferenceWindow}. Must not be null.
	 * @param endOfReferenceWindow   {@link LocalDateTime} Last DateTime value to be
	 *                               considered in average calculation. Must be >
	 *                               {@code startOfReferenceWindow}. Must not be
	 *                               null.
	 * @param lookbackWindow         {@code int} The lookback window to be used for
	 *                               this {@link VolatilityDifference}. Must be >
	 *                               {@code 1}.
	 * @param baseScale              {@code double} The base scale used for the
	 *                               calculation of the forecast scalar and thus in
	 *                               scaled forecast calculations as well.
	 * @param volatilityIndices      {@code ValueDateTupel[]} The volatility indices
	 *                               used for forecast calculations.
	 * @throws IllegalArgumentException If the given arguments to not satisfy
	 *                                  specifications.
	 */
	public VolatilityDifference(BaseValue baseValue, VolatilityDifference[] variations,
			LocalDateTime startOfReferenceWindow, LocalDateTime endOfReferenceWindow, int lookbackWindow,
			double baseScale, ValueDateTupel[] volatilityIndices) {
		super(baseValue, variations, startOfReferenceWindow, endOfReferenceWindow, baseScale);
		// TODO CHECK CONSTRCTOR CALLS

		/* Check if lookback window fulfills requirements. */
		this.validateLookbackWindow(lookbackWindow);
		this.setLookbackWindow(lookbackWindow);

		/* Calculate volatility index values based on the base value and set it */
		// TODO validate volatilityIndices
		this.setVolatilityIndices(volatilityIndices);

		// TODO ALIGN volatilityIndices and baseValue

		/*
		 * Calculate the average volatility for the base value over all available
		 * volatility index values
		 */
		this.setAverageVolatility(this.calculateAverageVolatility(startOfReferenceWindow, endOfReferenceWindow));
	}

	/**
	 * Validates the given lookback window. The lookback window must be >= 1.
	 * 
	 * @param lookbackWindow {@code int} The lookback window to be validated.
	 */
	public void validateLookbackWindow(int lookbackWindow) {
		if (lookbackWindow <= 1)
			throw new IllegalArgumentException("Lookback window must be at least 2");
	}

	@Override
	double calculateRawForecast(LocalDateTime forecastDateTime) {
		double currentVolatilty = ValueDateTupel.getElement(this.getVolatilityIndices(), forecastDateTime).getValue();
		return calculateRawForecast(currentVolatilty);
	}

	private double calculateRawForecast(double currentVolatilty) {
		return this.getAverageVolatility() - currentVolatilty;
	}

	/**
	 * Calculate the volatility index values for this {@link VolatilityDifference}.
	 * If the lookback window is longer than there are base values, an empty
	 * {@link ValueDateTupel[]} is returned.
	 * 
	 * @return {@link ValueDateTupel[]} calculated volatility indices. If the number
	 *         of values in the instance base value is smaller than the lookback
	 *         window the returned array only contains {@code Double.NaN}. Else, all
	 *         values until the lookback window is reached contain
	 *         {@code Double.NaN}, the rest contains real volatility index values.
	 */
	private static ValueDateTupel[] calculateVolatilityIndices(BaseValue baseValue, int lookbackWindow) {
		ValueDateTupel[] baseValues = baseValue.getValues();

		ValueDateTupel[] volatilityIndices = ValueDateTupel.createEmptyArray();

		/**
		 * Fill the spaces before reaching lookbackWindow with NaN
		 */
		for (int i = 0; i < lookbackWindow - 1; i++) {
			ValueDateTupel volatilityIndexNaN = new ValueDateTupel(baseValues[i].getDate(), Double.NaN);
			volatilityIndices = ArrayUtils.add(volatilityIndices, volatilityIndexNaN);
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
				tempDoubleValues = ArrayUtils.add(tempDoubleValues, tempBaseValue.getValue());

			/* Calculate standard deviation and save into local variable */
			StandardDeviation sd = new StandardDeviation();
			double volatilityIndexValue = sd.evaluate(tempDoubleValues);

			ValueDateTupel volatilityIndexValueDateTupel = new ValueDateTupel(baseValues[i].getDate(),
					volatilityIndexValue);

			/* Add calculated standard deviation to volatility indices */
			volatilityIndices = ArrayUtils.add(volatilityIndices, volatilityIndexValueDateTupel);
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
	private double calculateAverageVolatility(LocalDateTime startOfReferenceWindow,
			LocalDateTime endOfReferenceWindow) {
		/* Get all volatility index values */
		ValueDateTupel[] allVolatilityIndices = this.getVolatilityIndices();

		/*
		 * If the last volatility index value is NaN then no volatility index values
		 * were calculated due to there not being enough values. If this is the case,
		 * the average volatility is set to be Double.NaN
		 */
		if (allVolatilityIndices[allVolatilityIndices.length - 1].getValue() == Double.NaN) {
			return Double.NaN;
		}

		int startIndex = 0;
		int endIndex = 0;

		for (int i = 0; i < allVolatilityIndices.length; i++) {
			if (startIndex == 0 && allVolatilityIndices[i].getDate().equals(startOfReferenceWindow)) {
				startIndex = i;
				continue;
			}
			if (startIndex != 0 && allVolatilityIndices[i].getDate().equals(endOfReferenceWindow)) {
				endIndex = i;
				/*
				 * When endOfReferenceWindow is found the loop can be exited, as
				 * startOfReferenceWindow must have already been found
				 */
				break;
			}
		}

		/* Get lookbackWindow to evaluate the values relevant for the calculation */
		int lookbackWindow = this.getLookbackWindow();

		/*
		 * If starting point lies before reaching lookback Window the volatility index
		 * value is Double.NaN
		 */
		if (startIndex < lookbackWindow - 1)
			throw new IllegalArgumentException("Start of reference window is set before lookback window is reached");

		int numOfValuesToBeCopied = endIndex - startIndex + 1;
		ValueDateTupel[] relevantVolatilityIndices = new ValueDateTupel[numOfValuesToBeCopied];

		/* Copy the relevant volatility index values into a temporary array */
		System.arraycopy(allVolatilityIndices, /* source array */
				startIndex, /* source array position (starting position for copy) */
				relevantVolatilityIndices, /* destination array */
				0, /* destination position (starting position for paste) */
				numOfValuesToBeCopied /* length (number of values to copy */
		);

		DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
		/* Extract all relevant values into statistics object */
		for (ValueDateTupel volatilityIndex : relevantVolatilityIndices)
			stats.accept(volatilityIndex.getValue());

		/* Put average value of relevant values into class variable */
		return stats.getAverage();
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
		long temp;
		temp = Double.doubleToLongBits(averageVolatility);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + lookbackWindow;
		result = prime * result + Arrays.hashCode(volatilityIndices);
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
		VolatilityDifference other = (VolatilityDifference) obj;
		if (Double.doubleToLongBits(averageVolatility) != Double.doubleToLongBits(other.averageVolatility))
			return false;
		if (lookbackWindow != other.lookbackWindow)
			return false;
		if (!Arrays.equals(volatilityIndices, other.volatilityIndices))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VolatilityDifference [volatilityIndices=" + Arrays.toString(volatilityIndices) + ", averageVolatility="
				+ averageVolatility + ", lookbackWindow=" + lookbackWindow + "]";
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
	 * @param averageVolatility {@code double} The average volatility to be set for
	 *                          this {@link VolatilityDifference}
	 */
	private void setAverageVolatility(double averageVolatility) {
		this.averageVolatility = averageVolatility;
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
