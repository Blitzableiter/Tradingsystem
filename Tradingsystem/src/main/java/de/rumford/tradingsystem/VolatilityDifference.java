/**
 * 
 */
package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.Validator;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * Historically, times of higher volatility in a asset tended to be accompanied
 * by falls in course value. This rule exploits this behavior by comparing the
 * current volatility of an asset with its historic counterpart.
 * 
 * @author Max Rumford
 *
 */
public class VolatilityDifference extends Rule {

	private ValueDateTupel[] volatilityIndices;
	private int lookbackWindow;

	/**
	 * Creates a new {@link VolatilityDifference} instance using the passed
	 * {@link BaseValue} to calculate the volatility indices and the average
	 * volatility.
	 * 
	 * @param baseValue              Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param variations             {@code VolatilityDifference[]} An array of
	 *                               three or less rules. Represents the variations
	 *                               of this rule. Same limitations as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param startOfReferenceWindow Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param endOfReferenceWindow   Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param lookbackWindow         {@code int} The lookback window to be used for
	 *                               this {@link VolatilityDifference}. See
	 *                               {@link #validateLookbackWindow(int)} for
	 *                               limitations.
	 * @param baseScale              Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 */
	public VolatilityDifference(BaseValue baseValue, VolatilityDifference[] variations,
			LocalDateTime startOfReferenceWindow, LocalDateTime endOfReferenceWindow, int lookbackWindow,
			double baseScale) {
		super(baseValue, variations, startOfReferenceWindow, endOfReferenceWindow, baseScale);

		/* Check if lookback window fulfills requirements. */
		validateLookbackWindow(lookbackWindow);
		this.setLookbackWindow(lookbackWindow);

		/* Calculate volatility index values based on the base value and set it */
		ValueDateTupel[] calculatedVolatilityIndices = calculateVolatilityIndices(baseValue, lookbackWindow);
		this.validateVolatilityIndices(calculatedVolatilityIndices);
		this.setVolatilityIndices(calculatedVolatilityIndices);
	}

	/**
	 * Creates a new {@link VolatilityDifference} instance using the passed
	 * {@link BaseValue} to calculate the volatility indices and the average
	 * volatility.
	 * 
	 * @param baseValue              Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param variations             {@code VolatilityDifference[]} An array of
	 *                               three or less rules. Represents the variations
	 *                               of this rule. Same limitations as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param startOfReferenceWindow Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param endOfReferenceWindow   Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param lookbackWindow         {@code int} The lookback window to be used for
	 *                               this {@link VolatilityDifference}. See
	 *                               {@link #validateLookbackWindow(int)} for
	 *                               limitations.
	 * @param baseScale              Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param volatilityIndices      {@code ValueDateTupel[]} The volatility indices
	 *                               used for forecast calculations. See
	 *                               {@link VolatilityDifference#validateVolatilityIndices(ValueDateTupel[])}
	 *                               for limitations.
	 */
	public VolatilityDifference(BaseValue baseValue, VolatilityDifference[] variations,
			LocalDateTime startOfReferenceWindow, LocalDateTime endOfReferenceWindow, int lookbackWindow,
			double baseScale, ValueDateTupel[] volatilityIndices) {
		super(baseValue, variations, startOfReferenceWindow, endOfReferenceWindow, baseScale);

		/* Check if lookback window fulfills requirements. */
		validateLookbackWindow(lookbackWindow);
		this.setLookbackWindow(lookbackWindow);

		/* Calculate volatility index values based on the base value and set it */
		this.validateVolatilityIndices(volatilityIndices);
		this.setVolatilityIndices(volatilityIndices);
	}

	/**
	 * Calculates the raw forecast by subtracting the forecast for the given
	 * LocalDateTIme from the average volatility at that same given point in time.
	 * Positive results result in a positive forecast.
	 */
	@Override
	double calculateRawForecast(LocalDateTime forecastDateTime) {
		double currentVolatilty = ValueDateTupel.getElement(this.getVolatilityIndices(), forecastDateTime).getValue();
		return calculateAverageVolatility(forecastDateTime) - currentVolatilty;
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
	 * @throws IllegalArgumentException if the number of base values is smaller than
	 *                                  the given lookback window.
	 */
	private static ValueDateTupel[] calculateVolatilityIndices(BaseValue baseValue, int lookbackWindow) {
		ValueDateTupel[] baseValues = baseValue.getValues();

		ValueDateTupel[] volatilityIndices = null;

		/**
		 * If there are less base values than the lookback window is long no volatility
		 * values can be calculated. The volatility values only have any true meaning,
		 * when the lookback window is used in its entirety.
		 */
		if (baseValues.length < lookbackWindow)
			throw new IllegalArgumentException(
					"The amount of base values must not be smaller than the lookback window. Number of base values: "
							+ baseValues.length + ", lookback window: " + lookbackWindow + ".");

		/**
		 * Fill the spaces before reaching lookbackWindow with NaN
		 */
		for (int i = 0; i < lookbackWindow; i++) {
			ValueDateTupel volatilityIndexNaN = new ValueDateTupel(baseValues[i].getDate(), Double.NaN);
			volatilityIndices = ArrayUtils.add(volatilityIndices, volatilityIndexNaN);
		}

		/**
		 * Start calculation with first adequate time value (after lookback window is
		 * reached), e.g. lookbackWindow = 4, start with index 2 (4th element), as the
		 * returns of each day will be needed.
		 */
		for (int i = lookbackWindow; i < baseValues.length; i++) {
			/* Copy the relevant values into a temporary array */
			ValueDateTupel[] tempBaseValues = ValueDateTupel.createEmptyArray(lookbackWindow + 1);
			System.arraycopy(baseValues, /* source array */
					i - (lookbackWindow), /* source array position (starting position for copy) */
					tempBaseValues, /* destination array */
					0, /* destination position (starting position for paste) */
					lookbackWindow + 1/* length (number of values to copy */
			);

			/* Calculate relevant returns and store them into an array. */
			double[] tempDoubleValues = {};
			for (int j = 1; j < tempBaseValues.length; j++) {
				double returnForDayI = Util.calculateReturn(tempBaseValues[j - 1].getValue(),
						tempBaseValues[j].getValue());
				tempDoubleValues = ArrayUtils.add(tempDoubleValues, returnForDayI);
			}

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
	 * Calculate the average volatility for a given {@link LocalDateTime}.
	 * 
	 * @param dateToBeCalculatedFor {@link LocalDateTime} The LocalDateTime the
	 *                              average volatility is to be calculated for.
	 * @return {@code double} The average volatility up until the given
	 *         LocalDateTime.
	 */
	private double calculateAverageVolatility(LocalDateTime dateToBeCalculatedFor) {
		/* Starting point is the first DateTime that exceeds the lookback window. */
		LocalDateTime startingDateTime = this.getVolatilityIndices()[this.getLookbackWindow()].getDate();

		/* Get all relevant volatility index values */
		ValueDateTupel[] relevantVolatilityIndices = ValueDateTupel.getElements(this.getVolatilityIndices(),
				startingDateTime, dateToBeCalculatedFor);

		DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
		/* Extract all relevant values into statistics object */
		for (ValueDateTupel volatilityIndex : relevantVolatilityIndices)
			stats.accept(volatilityIndex.getValue());

		/* Put average value of relevant values into class variable */
		return stats.getAverage();
	}

	/**
	 * Validates the given lookback window. The lookback window must be >= 1.
	 * 
	 * @param lookbackWindow {@code int} The lookback window to be validated.
	 * @throws IllegalArgumentException if the given lookbackWindow does not meet
	 *                                  specifications
	 */
	public static void validateLookbackWindow(int lookbackWindow) {
		if (lookbackWindow <= 1)
			throw new IllegalArgumentException("Lookback window must be at least 2");
	}

	/**
	 * Validates the given volatility indices.
	 * 
	 * @param volatilityIndices {@code ValueDateTupel[]} the array of volatility
	 *                          indices to be validated. Must comply as follows:
	 *                          <ul>
	 *                          <li>Must not be null.</li>
	 *                          <li>Must not be an empty array.</li>
	 *                          <li>Must be sorted in ascending order.</li>
	 *                          <li>Must contain this instances
	 *                          startOfReferenceWindow and
	 *                          endOfReferenceWindow.</li>
	 *                          <li>Must not contain Double.NaN values</li>
	 *                          <li>Must be aligned with this instance's base
	 *                          value's values. See
	 *                          {@link ValueDateTupel#alignDates(ValueDateTupel[][])}.</li>
	 *                          </ul>
	 * @throws IllegalArgumentException if the given volatility indices do not meet
	 *                                  specifications.
	 */
	private void validateVolatilityIndices(ValueDateTupel[] volatilityIndices) {
		/* Check if passed volatility indices are null */
		if (volatilityIndices == null)
			throw new IllegalArgumentException("Volatility indices must not be null.");

		/* Check if passed values array contains elements */
		if (volatilityIndices.length == 0)
			throw new IllegalArgumentException("Volatility indices must not be an empty array");

		/*
		 * The values cannot be used if they are not in ascending order or if they
		 * contain duplicate LocalDateTimes.
		 */
		if (!ValueDateTupel.isSortedAscending(volatilityIndices))
			throw new IllegalArgumentException(
					"Given volatility indices are not properly sorted or there are duplicate LocalDateTime values");

		try {
			Validator.validateTimeWindow(this.getStartOfReferenceWindow(), this.getEndOfReferenceWindow(),
					volatilityIndices);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Giving volatility indices do not meet specificiation.", e);
		}

		/*
		 * The given startOfReferenceWindow must be included in the given
		 * volatilityIndices array.
		 */
		if (!ValueDateTupel.containsDate(volatilityIndices, this.getStartOfReferenceWindow()))
			throw new IllegalArgumentException(
					"The given startOfReferenceWindow is not included in the given volatilityIndices array.");
		/*
		 * The given startOfReferenceWindow must be included in the given
		 * volatilityIndices array.
		 */
		if (!ValueDateTupel.containsDate(volatilityIndices, this.getEndOfReferenceWindow()))
			throw new IllegalArgumentException(
					"The given endOfReferenceWindow is not included in the given volatilityIndices array.");

		/*
		 * The given volatility indices value must not contain NaNs in the area
		 * delimited by startOfReferenceWindow and endOfReferenceWindow.
		 */
		int startOfReferencePosition = ValueDateTupel.getPosition(volatilityIndices, this.getStartOfReferenceWindow());
		int endOfReferencePosition = ValueDateTupel.getPosition(volatilityIndices, this.getEndOfReferenceWindow());

		for (int i = startOfReferencePosition; i <= endOfReferencePosition; i++) {
			if (Double.isNaN(volatilityIndices[i].getValue()))
				throw new IllegalArgumentException(
						"There must not be NaN-Values in the given volatility indices values in the area delimited by startOfReferenceWindow and endOfReferenceWindow");
		}

		/* Extract dates out of the base value's values array and add it to a HashSet */
		Set<LocalDateTime> baseValueSet = new HashSet<>();
		for (ValueDateTupel value : this.getBaseValue().getValues())
			baseValueSet.add(value.getDate());
		/*
		 * Extract dates out of the volatility indices values array and add them to to a
		 * copy of the same HashSet
		 */
		Set<LocalDateTime> volatilityIndicesSet = new HashSet<>(baseValueSet);
		for (ValueDateTupel value : volatilityIndices)
			volatilityIndicesSet.add(value.getDate());
		/*
		 * If the Sets differ in length, the volatility indices added unique
		 * LocalDateTimes to the set. Therefore, the both are not properly aligned.
		 */
		if (baseValueSet.size() != volatilityIndicesSet.size())
			throw new IllegalArgumentException("Base value and volatility index values are not properly aligned."
					+ " Utilize ValueDateTupel.alignDates(ValueDateTupel[][]) before creating a new VolatilityDifference.");
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
		int result = super.hashCode();
		result = prime * result + lookbackWindow;
		result = prime * result + Arrays.hashCode(volatilityIndices);
		return result;
	}

	@GeneratedCode
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		VolatilityDifference other = (VolatilityDifference) obj;
		if (lookbackWindow != other.lookbackWindow)
			return false;
		if (!Arrays.equals(volatilityIndices, other.volatilityIndices))
			return false;
		return true;
	}

	@GeneratedCode
	@Override
	public String toString() {
		return "VolatilityDifference [volatilityIndices=" + Arrays.toString(volatilityIndices) + ", lookbackWindow="
				+ lookbackWindow + "]";
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
