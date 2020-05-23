package de.rumford.tradingsystem.helper;

import java.time.LocalDateTime;

import de.rumford.tradingsystem.BaseValue;
import de.rumford.tradingsystem.Rule;

/**
 * Validator class as used throughout the library containing solely of static
 * methods representing validation of input values.
 * 
 * @author Max Rumford
 *
 */
public class Validator {

	/**
	 * Constructor for the {@link Validator} class<br/>
	 * Only supports static methods, hence no instance shall be created, hence a
	 * private constructor
	 */
	private Validator() {
	}

	/**
	 * Validates if the given array of ValueDateTupel is sorted in an ascending
	 * order.
	 * 
	 * @param values {@code ValueDateTupel[]} The given array of values.
	 * @throws IllegalArgumentException if the specifications above are not met.
	 */
	public static void validateDates(ValueDateTupel[] values) {
		/*
		 * The values cannot be used if they are not in ascending order.
		 */
		if (!ValueDateTupel.isSortedAscending(values))
			throw new IllegalArgumentException("Given values are not properly sorted or there are non-unique values.");
	}

	/**
	 * Validates the given array of ValueDateTupel. The given array must fulfill the
	 * following specifications:
	 * <ul>
	 * <li>Must not be null</li>
	 * <li>Must be of length greater than 0</li>
	 * <li>Must not contain null</li>
	 * <li>Must not contain NaNs as values</li>
	 * </ul>
	 * 
	 * @param values {@code ValueDateTupel[]} The values to be validated.
	 * @throws IllegalArgumentException if the given array does not meet the above
	 *                                  specifications.
	 */
	public static void validateValues(ValueDateTupel[] values) {
		/* Check if passed values array is null */
		if (values == null)
			throw new IllegalArgumentException("The given values array must not be null");
		/* Check if passed values array contains elements */
		if (values.length == 0)
			throw new IllegalArgumentException("Values must not be an empty array");

		for (ValueDateTupel value : values) {
			/* Validate if there are null values in the given values array. */
			if (value == null)
				throw new IllegalArgumentException("Given values must not contain null.");

			/* Validate if there are NaN values in the given values array. */
			if (Double.isNaN(value.getValue()))
				throw new IllegalArgumentException("Given values must not contain NaN.");
		}
	}

	/**
	 * Validates the given Array of {@link Rule}. Must meet the following
	 * specifications:
	 * <ul>
	 * <li>Must not be null.</li>
	 * <li>Must not be an empty array.</li>
	 * </ul>
	 * 
	 * @param rules {@code Rule[]} The Array of {@link Rule} to be validated.
	 * @throws IllegalArgumentException if the above specifications are not met.
	 */
	public static void validateRules(Rule[] rules) {
		if (rules == null)
			throw new IllegalArgumentException("Rules must not be null");

		if (rules.length == 0)
			throw new IllegalArgumentException("Rules must not be an empty array");
	}

	/**
	 * Validates the given time window values. Values are needed for checking of
	 * containment. Must meet the following specifications:
	 * <ul>
	 * <li>startOfTimeWindow must not be null.</li>
	 * <li>endOfTimeWindow must not be null.</li>
	 * <li>endOfTimeWindow must be after startOfTimeWindow. See
	 * {@link LocalDateTime#isAfter(java.time.chrono.ChronoLocalDateTime)}.</li>
	 * <li>values must contain startOfTimeWindow.</li>
	 * <li>values must contain endOfTimeWindow.</li>
	 * </ul>
	 * 
	 * @param startOfTimeWindow {@link LocalDateTime} The start of Time window to be
	 *                          checked.
	 * @param endOfTimeWindow   {@link LocalDateTime} The end of Time window to be
	 *                          checked.
	 * @param values            {@code ValueDateTupel[]} The array of
	 *                          {@link ValueDateTupel} to be checked.
	 * @throws IllegalArgumentException if any of the above specifications are not
	 *                                  met.
	 */
	public static void validateTimeWindow(LocalDateTime startOfTimeWindow, LocalDateTime endOfTimeWindow,
			ValueDateTupel[] values) {
		/* Check if LocalDateTimes are null */
		if (startOfTimeWindow == null)
			throw new IllegalArgumentException("Start of time window value must not be null");
		if (endOfTimeWindow == null)
			throw new IllegalArgumentException("End of time window value must not be null");
		/* Check if time window is properly defined: end must be after start */
		if (!endOfTimeWindow.isAfter(startOfTimeWindow))
			throw new IllegalArgumentException("End of time window value must be after start of time window value");

		/* The given startOfTimeWindow must be included in the given base values. */
		if (!ValueDateTupel.containsDate(values, startOfTimeWindow))
			throw new IllegalArgumentException("Given values do not include given start value for time window");
		/* The given endOfTimeWindow must be included in the given base values. */
		if (!ValueDateTupel.containsDate(values, endOfTimeWindow))
			throw new IllegalArgumentException("Given values do not include given end value for time window");
	}

	/**
	 * Validates the given {@link BaseValue}.
	 * <ul>
	 * <li>Must not be null.</li>
	 * </ul>
	 * 
	 * @param baseValue {@link BaseValue} The base value to be validated.
	 */
	public static void validateBaseValue(BaseValue baseValue) {
		if (baseValue == null)
			throw new IllegalArgumentException("Base value must not be null");
	}

	/**
	 * Validates the given double value. The value must meet the following
	 * specifications:
	 * <ul>
	 * <li>Must not be Double.NaN.</li>
	 * <li>Must be a positive decimal.</li>
	 * </ul>
	 * 
	 * @param value {@code double} The value to validate.
	 * @throws IllegalArgumentException if the above specifications are not met.
	 */
	public static void validatePositiveDouble(double value) {
		if (Double.isNaN(value))
			throw new IllegalArgumentException("Value must not be Double.NaN");

		if (value <= 0)
			throw new IllegalArgumentException("Value must be a positive decimal");

	}

	/**
	 * Validates the given array of {@link Rule} against the given
	 * {@link BaseValue}.
	 * <ul>
	 * <li>All rules must have the given {@link BaseValue} as their own
	 * {@link BaseValue}.</li>
	 * </ul>
	 * 
	 * @param rules     {@code Rule[]} The array of {@link Rule} to be validated.
	 * @param baseValue {@link BaseValue} The {@link BaseValue} to be validated.
	 * @throws IllegalArgumentException if any of the above specifications are not
	 *                                  met.
	 */
	public static void validateRulesVsBaseValue(Rule[] rules, BaseValue baseValue) {
		for (int i = 0; i < rules.length; i++)
			if (!rules[i].getBaseValue().equals(baseValue))
				throw new IllegalArgumentException(
						"The base value of all rules must be equal to given base value but the rule at position " + i
								+ " does not comply.");
	}

	/**
	 * Validates the given array of arrays.
	 * <ul>
	 * <li>Must not be null.</li>
	 * </ul>
	 * 
	 * @param values {@code double[]} The array to be validated.
	 * @throws IllegalArgumentException if any of the above specifications are not
	 *                                  met.
	 */
	public static void validateArrayOfDoubles(double[] values) {
		if (values == null)
			throw new IllegalArgumentException("Given array must not be null");
	}

}
