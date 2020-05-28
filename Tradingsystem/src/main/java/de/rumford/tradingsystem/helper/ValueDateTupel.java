/**
 * 
 */
package de.rumford.tradingsystem.helper;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.BaseValue;

/**
 * A ValueDateTupel represents a decimal value at a given point in time.
 * <p>
 * The ValueDateTupel is the most used helper class in this library. It consists
 * of a LocalDateTime value representing a point in time, and of a double,
 * representing any kind of decimal value associated with the aforementioned
 * point in time. By using LocalDateTime (an not just a class representing Date
 * values) intraday usage is possible.
 * <p>
 * The ValueDateTupel brings a lot of static method used throughout the entire
 * library. Most of these methods deal with arrays or larger structures of
 * ValueDateTupel, as the instance of ValueDateTupel by itself does not have
 * many limitations.
 * 
 * @author Max Rumford
 *
 */
public class ValueDateTupel {

	/* The value to be represented. */
	private double value;
	/* The datetime to be represented. */
	private LocalDateTime date;

	static final String MESSAGE_ARRAY_MUST_NOT_BE_NULL = "Given array must not be null";
	static final String MESSAGE_VALUE_MUST_NOT_BE_NULL = "Given value must not be null";

	/**
	 * Creates a new {@link ValueDateTupel} instance using the given
	 * LocaDateTime and double.
	 * 
	 * @param date  {@link LocalDateTime} The dateTime to be set for this
	 *              {@link ValueDateTupel}
	 * @param value {@code double} The value to be set for this
	 *              {@link ValueDateTupel}
	 */
	public ValueDateTupel(LocalDateTime date, double value) {
		this.setDate(date);
		this.setValue(value);
	}

	/**
	 * Adds a given {@link ValueDateTupel} to the given array of
	 * {@link ValueDateTupel} at the given position. Returns the given array
	 * extended with the given single value.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} Array to be extended.
	 * @param vdtToBeAdded    {@link ValueDateTupel} Value to be added.
	 * @param position        {@code int} Position the given value shall be put
	 *                        into.
	 * @return {@code ValueDateTupel[]} The extended array.
	 * @throws IllegalArgumentException If the given array is null.
	 * @throws IllegalArgumentException If the given value to be added is null.
	 * @throws IllegalArgumentException If the position is negative.
	 * @throws IllegalArgumentException If the given position is greater than
	 *                                  the length of the given array.
	 */
	public static ValueDateTupel[] addOneAt(ValueDateTupel[] valueDateTupels,
			ValueDateTupel vdtToBeAdded, int position) {
		if (valueDateTupels == null)
			throw new IllegalArgumentException(MESSAGE_ARRAY_MUST_NOT_BE_NULL);
		if (vdtToBeAdded == null)
			throw new IllegalArgumentException(MESSAGE_VALUE_MUST_NOT_BE_NULL);
		if (position < 0)
			throw new IllegalArgumentException(
					"Cannot not add a value at position < 0. Given position is "
							+ position);
		if (position > valueDateTupels.length)
			throw new IllegalArgumentException(
					"Cannot add a value at position > " + valueDateTupels.length
							+ ". Given position is " + position + ".");

		ValueDateTupel[] extendedArray = ValueDateTupel
				.createEmptyArray(valueDateTupels.length + 1);

		/* Add new ValueDateTupel at the beginning of the given array. */
		if (position == 0) {
			extendedArray[position] = vdtToBeAdded;
			System.arraycopy(valueDateTupels, 0, extendedArray, 1,
					valueDateTupels.length);
			return extendedArray;
		}
		/* Add new ValueDateTupel at the end of the given array. */
		if (position == valueDateTupels.length) {
			System.arraycopy(valueDateTupels, 0, extendedArray, 0,
					valueDateTupels.length);
			extendedArray[position] = vdtToBeAdded;
			return extendedArray;
		}
		/*
		 * This code is only reached, when the new ValueDateTupel shall not be
		 * added at end or at beginning.
		 */
		/* Add all values prior to the new ValueDateTupel */
		System.arraycopy(valueDateTupels, 0, extendedArray, 0, position);
		/* Add new ValueDateTupel at the given position. */
		extendedArray[position] = vdtToBeAdded;
		/* Add all values subsequent to the new ValueDateTupel */
		System.arraycopy(valueDateTupels, position, extendedArray, position + 1,
				valueDateTupels.length - position);
		return extendedArray;
	}

	/**
	 * Add missing {@link LocalDateTime} values to all given
	 * {@code ValueDateTupel[]}. The corresponding value will be set to average
	 * the values of its direct predecessor and successor.
	 * <p>
	 * If there are multiple {@link LocalDateTime} missing in a row, all of
	 * those will get the average value of the last position before and the
	 * first position after all missing values.
	 * <p>
	 * If the missing {@link LocalDateTime} would be the first value in the new
	 * array, its value will be set to match the previously first one.
	 * <p>
	 * If the missing {@link LocalDateTime} would be the last value in the new
	 * array, its value will be set to match the previously last one.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[][]} Array of arrays of
	 *                        {@link ValueDateTupel} whose {@link LocalDateTime}
	 *                        shall be aligned.
	 * @return {@code ValueDateTupel[][]} Array of arrays of
	 *         {@link ValueDateTupel} with now aligned {@link LocalDateTime}
	 *         values.
	 * @throws IllegalArgumentException If the given array of arrays is null.
	 * @throws IllegalArgumentException If the given array of arrays contains
	 *                                  null.
	 * @throws IllegalArgumentException If any array of the given array of
	 *                                  arrays contains null.
	 * @throws IllegalArgumentException If the given array contains an array of
	 *                                  {@link ValueDateTupel} not sorted in
	 *                                  ascending order.
	 * @throws IllegalArgumentException If the one of the given arrays contains
	 *                                  only {@link Double#NaN}.
	 */
	public static ValueDateTupel[][] alignDates(
			ValueDateTupel[][] valueDateTupels) {
		if (valueDateTupels == null)
			throw new IllegalArgumentException(
					"Given array of arrays must not be null");

		/* TreeSet (unique and sorted) of all dates in all valueDateTupel[] */
		TreeSet<LocalDateTime> uniqueSortedDates = getUniqueDates(
				valueDateTupels);

		/* Loop over all rows */
		for (int rowIndex = 0; rowIndex < valueDateTupels.length; rowIndex++) {
			/*
			 * If the row's length equals the length of uniqueSortedDates no
			 * Value has to be added as it already contains all dateTimes.
			 */
			if (valueDateTupels[rowIndex].length == uniqueSortedDates.size())
				continue;

			try {
				/* Validate if the row contains at least one suitable value. */
				Validator.validateRow(valueDateTupels[rowIndex]);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(
						"Row at position " + rowIndex + " is not valid.");
			}

			/* Enhance current row by missing LocalDateTime values. */
			valueDateTupels[rowIndex] = enhanceRowByNaNs(
					valueDateTupels[rowIndex], uniqueSortedDates);

			/* Replace all values of Double.NaN by real values. */
			valueDateTupels[rowIndex] = replaceNansByValues(
					valueDateTupels[rowIndex]);

		}
		return valueDateTupels;
	}

	/**
	 * Finds all {@link LocalDateTime} present in uniqueSortedDates but not in
	 * valueDateTupels and add them to the latter with a value of Double.NaN.
	 * 
	 * @param valueDateTupels   {@code ValueDateTupel[]} The array of
	 *                          {@link ValueDateTupel} to be enhanced.
	 * @param uniqueSortedDates {@code TreeSet<LocalDateTime>} A {@link TreeSet}
	 *                          of {@link LocalDateTime} containing all unique
	 *                          LocalDateTimes.
	 * @return {@code ValueDateTupel[]} valueDateTupels + all LocalDateTime
	 *         additionally given by uniqueSortedDates. Array is sorted as by
	 *         {@link #isSortedAscending(ValueDateTupel[])}.
	 */
	private static ValueDateTupel[] enhanceRowByNaNs(
			ValueDateTupel[] valueDateTupels,
			TreeSet<LocalDateTime> uniqueSortedDates) {

		/*
		 * Load unique sorted dates into an ArrayList to have access to an
		 * index.
		 */
		List<LocalDateTime> uniqueSortedDatesList = new ArrayList<>(
				uniqueSortedDates);

		/*
		 * Loop over all unique dateTimes to assess if they are in the current
		 * row. If not, missing dateTimes are added into the original arrays.
		 * Their value is set to Double.NaN
		 */
		for (int fieldIndex = 0; fieldIndex < uniqueSortedDates
				.size(); fieldIndex++) {
			ValueDateTupel valueDateTupelToBeAdded = new ValueDateTupel(
					uniqueSortedDatesList.get(fieldIndex), Double.NaN);

			if (fieldIndex < valueDateTupels.length
					&& uniqueSortedDatesList.get(fieldIndex)
							.isEqual(valueDateTupels[fieldIndex].getDate())) {

				/*
				 * Nothing has to be done, as we're not at the end of the list
				 * and the current LocalDateTime out of the list of unique
				 * values is already in the given row.
				 */
				continue;
			}

			valueDateTupels = ValueDateTupel.addOneAt(valueDateTupels,
					valueDateTupelToBeAdded, fieldIndex);
		}

		return valueDateTupels;
	}

	/**
	 * Fills up a gap of NaN-values in a given array of {@link ValueDateTupel}
	 * with the average of the previously last and first next available
	 * non-NaN-value.
	 * 
	 * @param valueDateTupels   {@code ValueDateTupel} Array of
	 *                          {@link ValueDateTupel} holding the values.
	 * @param previousAvailable {@code int} Index of the last available
	 *                          non-NaN-value before the gap to be filled.
	 * @param nextAvailable     {@code int} Index of the first available
	 *                          non-NaN-value after the gap to be filled.
	 * @return {@code ValueDateTupel} Array of {@link ValueDateTupel} with the
	 *         gap filled.
	 */
	private static ValueDateTupel[] fillCenterValues(
			ValueDateTupel[] valueDateTupels, int previousAvailable,
			int nextAvailable) {
		/*
		 * The value to be set to all missing values is the average of the last
		 * non-NaN before the NaNs and the first non-NaN after the NaNs. This is
		 * the case when the missing NaNs are not at the beginning or the end of
		 * the given array.
		 */
		double valueToBeSet = (valueDateTupels[previousAvailable].getValue()
				+ valueDateTupels[nextAvailable].getValue()) / 2;

		int localIndex = previousAvailable + 1;
		/* Fill all values up to the next NaN with the calculated value. */
		while (localIndex < nextAvailable) {
			valueDateTupels[localIndex].setValue(valueToBeSet);
			localIndex++;
		}
		return valueDateTupels;
	}

	/**
	 * Fills the values at the beginning of the array. If the first
	 * valueDateTupel contains Double.NaN, its value will be set to match the
	 * next non-NaN-value. If the following values are also Double.NaN, iterate
	 * through the array until a value != Double.NaN is found.
	 *
	 * @param valueDateTupels {@code ValueDateTupel[]} An array of
	 *                        {@link ValueDateTupel} to be filled.
	 * @return {@code ValueDateTupel[]} The same array of {@link ValueDateTupel}
	 *         but with starting values filled.
	 * @throws IllegalArgumentException if the row only contains Double.NaN.
	 */
	private static ValueDateTupel[] fillStartingValues(
			ValueDateTupel[] valueDateTupels) {
		int localFieldIndex = 1;
		/* Iterate through the array until a value != Double.NaN is found */
		while (Double.isNaN(valueDateTupels[localFieldIndex].getValue())) {
			localFieldIndex++;
		}

		/*
		 * If only one value has to be set execution can continue with the next
		 * loop iteration
		 */
		if (localFieldIndex == 1) {
			valueDateTupels[localFieldIndex - 1]
					.setValue(valueDateTupels[localFieldIndex].getValue());
		}

		/*
		 * If multiple values have to be set iterate over them an fill them
		 * subsequently, starting from the last NaN before the first valid
		 * value.
		 */
		while (localFieldIndex >= 1) {
			valueDateTupels[localFieldIndex - 1]
					.setValue(valueDateTupels[localFieldIndex].getValue());
			localFieldIndex--;
		}

		return valueDateTupels;
	}

	/**
	 * Get unique dates from an array of arrays of {@link ValueDateTupel}.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[][]} The array of arrays of
	 *                        {@link ValueDateTupel} the get all unique dates
	 *                        from.
	 * @return {@code TreeSet<LocalDateTime>} A TreeSet of all unique dates.
	 */
	private static TreeSet<LocalDateTime> getUniqueDates(
			ValueDateTupel[][] valueDateTupels) {
		TreeSet<LocalDateTime> uniqueSortedDates = new TreeSet<>();

		/* For each array in ValueDateTupels ... */
		for (int rowIndex = 0; rowIndex < valueDateTupels.length; rowIndex++) {

			try {
				Validator.validateValues(valueDateTupels[rowIndex]);
				Validator.validateDates(valueDateTupels[rowIndex]);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("The array at position "
						+ rowIndex + " does not meet specifications.", e);
			}

			/* ... add all values into uniqueSortedDates */
			uniqueSortedDates.addAll(Arrays.asList(
					ValueDateTupel.getDates(valueDateTupels[rowIndex])));
		}
		return uniqueSortedDates;
	}

	/**
	 * Check if the given {@link ValueDateTupel} can be found in the given array
	 * of {@link ValueDateTupel}. Will only find exact matches.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} Array to be searched in.
	 * @param vdtToBeFound    {@link ValueDateTupel} Value to be searched for.
	 * @return {@code boolean} True, if the given value can be found in the
	 *         given array, false otherwise.
	 */
	public static boolean contains(ValueDateTupel[] valueDateTupels,
			ValueDateTupel vdtToBeFound) {
		if (valueDateTupels == null)
			throw new IllegalArgumentException(MESSAGE_ARRAY_MUST_NOT_BE_NULL);
		List<ValueDateTupel> list = new ArrayList<>(
				Arrays.asList(valueDateTupels));
		return list.contains(vdtToBeFound);
	}

	/**
	 * Check if the given array of {@link ValueDateTupel} contains the given
	 * {@link LocalDateTime}.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} Array to be searched in.
	 * @param dtToBeFound     {@link LocalDateTime} Value to be searched for.
	 * @return {@code boolean} True, if the given value can be found inside the
	 *         given array, false otherwise.
	 * @throws IllegalArgumentException If the given array of
	 *                                  {@link ValueDateTupel} is null.
	 * @throws IllegalArgumentException If the given {@link LocalDateTime} is
	 *                                  null.
	 * 
	 */
	public static boolean containsDate(ValueDateTupel[] valueDateTupels,
			LocalDateTime dtToBeFound) {
		if (valueDateTupels == null)
			throw new IllegalArgumentException(MESSAGE_ARRAY_MUST_NOT_BE_NULL);
		if (dtToBeFound == null)
			throw new IllegalArgumentException(MESSAGE_VALUE_MUST_NOT_BE_NULL);
		/* Load all values from the given array into an ArrayList. */
		List<LocalDateTime> list = new ArrayList<>(
				Arrays.asList(ValueDateTupel.getDates(valueDateTupels)));
		/* Utilize the generic contains method on ArrayList. */
		return list.contains(dtToBeFound);
	}

	/**
	 * Creates an empty array of {@link ValueDateTupel}.
	 * 
	 * @return {@code ValueDateTupel[]} An Empty array of
	 *         {@link ValueDateTupel}.
	 */
	public static ValueDateTupel[] createEmptyArray() {
		return ValueDateTupel.createEmptyArray(0);
	}

	/**
	 * Creates an empty array of {@link ValueDateTupel} with the given length.
	 * 
	 * @param length {@code int} Length the new array should have.
	 * @return {@code ValueDateTupel[]} An Empty array of
	 *         {@link ValueDateTupel}.
	 */
	public static ValueDateTupel[] createEmptyArray(int length) {
		return new ValueDateTupel[length];
	}

	/**
	 * Check if the {@link BaseValue} instance contains the given
	 * {@link LocalDateTime} in its values. Returns the containing
	 * {@link ValueDateTupel} if so, returns {@code null} otherwise.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} The array of
	 *                        {@link ValueDateTupel} to be searched through.
	 * @param dtToBeFound     {@link LocalDateTime} Value to be found inside the
	 *                        {@link BaseValue} values.
	 * @return {@link ValueDateTupel} containing the given
	 *         {@link LocalDateTime}. {@code null} if the given
	 *         {@link LocalDateTime} cannot be found.
	 */
	public static ValueDateTupel getElement(ValueDateTupel[] valueDateTupels,
			LocalDateTime dtToBeFound) {
		/* Check if given array is null */
		if (valueDateTupels == null)
			throw new IllegalArgumentException(MESSAGE_ARRAY_MUST_NOT_BE_NULL);

		if (dtToBeFound == null)
			throw new IllegalArgumentException(MESSAGE_VALUE_MUST_NOT_BE_NULL);

		for (ValueDateTupel value : valueDateTupels) {
			if (value.getDate().equals(dtToBeFound))
				return value;
		}
		return null;
	}

	/**
	 * Get all elements between two given DateTimes (inclusive) from the given
	 * array. If null is passed for either LocalDateTime, the representing
	 * border will be set to the boundaries of the given array.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} The base array.
	 * @param dtFrom          {@link LocalDateTime} The first DateTime to be
	 *                        included. If null, all values up until dtTo will
	 *                        be given.
	 * @param dtTo            {@link LocalDateTime} The last DateTime to be
	 *                        included. If null, all values starting from dtFrom
	 *                        will be given.
	 * @return {@code ValueDateTupel[]} The found elements. null, if dtFrom or
	 *         dtTo cannot be found in the given array.
	 */
	public static ValueDateTupel[] getElements(ValueDateTupel[] valueDateTupels,
			LocalDateTime dtFrom, LocalDateTime dtTo) {
		int positionFrom;
		int positionTo;
		/*
		 * Get the position indices of the given LocalDateTime values. If null
		 * is passed, set the positions to be the respective boundaries of the
		 * given array.
		 */
		if (dtFrom == null) {
			positionFrom = 0;
		} else {
			positionFrom = ValueDateTupel.getPosition(valueDateTupels, dtFrom);
		}
		if (dtTo == null) {
			positionTo = valueDateTupels.length - 1;
		} else {
			positionTo = ValueDateTupel.getPosition(valueDateTupels, dtTo);
		}

		/*
		 * If the given LocalDateTime values cannot be found in the given array
		 * return null.
		 */
		if (positionFrom == Integer.MIN_VALUE
				|| positionTo == Integer.MIN_VALUE)
			return null;

		ValueDateTupel[] elements = {};
		/* Add all elements between the two found positions ... */
		for (int i = positionFrom; i <= positionTo; i++) {
			elements = ArrayUtils.add(elements, valueDateTupels[i]);
		}
		/* ... and return them. */
		return elements;
	}

	/**
	 * Get all {@link LocalDateTime} from an array of {@link ValueDateTupel}.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} An array of
	 *                        {@link ValueDateTupel}}.
	 * @return {@code LocalDateTime[]} An array of {@link LocalDateTime} of the
	 *         given {@code ValueDateTupel[]}. Returns an empty array if the
	 *         given array is empty.
	 * @throws IllegalArgumentException if the given array is null.
	 */
	public static LocalDateTime[] getDates(ValueDateTupel[] valueDateTupels) {
		if (valueDateTupels == null)
			throw new IllegalArgumentException(MESSAGE_ARRAY_MUST_NOT_BE_NULL);

		LocalDateTime[] values = {};
		for (ValueDateTupel tupel : valueDateTupels) {
			values = ArrayUtils.add(values, tupel.getDate());
		}
		return values;
	}

	/**
	 * Finds the position of a given {@link LocalDateTime} in a given array of
	 * {@link ValueDateTupel}.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} The array to be searched.
	 * @param dtToBeFound     {@link LocalDateTime} The value to be found.
	 * @return {@code int} The position the given LocalDateTime was found. If
	 *         the given LocalDateTime cannot be found, Integer.MIN_VALUE is
	 *         returned. If the given array's length is 0 Integer.MIN_VALUE is
	 *         returned.
	 */
	public static int getPosition(ValueDateTupel[] valueDateTupels,
			LocalDateTime dtToBeFound) {
		final int defaultReturnValue = Integer.MIN_VALUE;

		/* Check if given array is null */
		if (valueDateTupels == null)
			throw new IllegalArgumentException(MESSAGE_ARRAY_MUST_NOT_BE_NULL);

		/*
		 * If there are no values in the given array return the default return
		 * value.
		 */
		if (valueDateTupels.length == 0)
			return defaultReturnValue;

		/* Check if given LocalDateTime is null */
		if (dtToBeFound == null)
			throw new IllegalArgumentException(MESSAGE_VALUE_MUST_NOT_BE_NULL);

		/*
		 * if the given LocalDateTime is in the given array, return its
		 * position.
		 */
		for (int i = 0; i < valueDateTupels.length; i++) {
			if (valueDateTupels[i].getDate().equals(dtToBeFound))
				return i;
		}

		/* Otherwise, return Integer.MIN_VALUE */
		return defaultReturnValue;
	}

	/**
	 * Get all values from an array of {@link ValueDateTupel}.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} An array of
	 *                        {@link ValueDateTupel}}.
	 * @return {@code double[]} An array of values of the given
	 *         {@code ValueDateTupel[]}. Returns an empty array if the given
	 *         array is empty.
	 * @throws IllegalArgumentException if the given array is null.
	 */
	public static double[] getValues(ValueDateTupel[] valueDateTupels) {
		if (valueDateTupels == null)
			throw new IllegalArgumentException(MESSAGE_ARRAY_MUST_NOT_BE_NULL);

		double[] values = {};
		for (ValueDateTupel tupel : valueDateTupels)
			values = ArrayUtils.add(values, tupel.getValue());
		return values;
	}

	/**
	 * Evaluate if the given array of {@link ValueDateTupel} is sorted in
	 * ascending order, i.e., if the value at position 0 has the lowest
	 * {@link LocalDateTime} value (implicit check) and all subsequent
	 * {@link ValueDateTupel} each have a {@link LocalDateTime} after
	 * ({@link LocalDateTime#isAfter(ChronoLocalDateTime)}) the previous one.
	 * <p>
	 * If two values have the same {@link LocalDateTime} false will be returned.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} array of
	 *                        {@link ValueDateTupel} to be checked for ascending
	 *                        order.
	 * @return {@code boolean} False, if any date is not chronologically after
	 *         its predecessor, true if otherwise.
	 * @throws IllegalArgumentException If the given array is null.
	 * @throws IllegalArgumentException If the given array contains null values.
	 */
	public static boolean isSortedAscending(ValueDateTupel[] valueDateTupels) {
		if (valueDateTupels == null)
			throw new IllegalArgumentException(MESSAGE_ARRAY_MUST_NOT_BE_NULL);
		if (ValueDateTupel.contains(valueDateTupels, null))
			throw new IllegalArgumentException(
					"The given array must not contain any nulls");

		for (int i = 1; i < valueDateTupels.length; i++) {
			if (!valueDateTupels[i].getDate()
					.isAfter(valueDateTupels[i - 1].getDate())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Evaluate if the given array of {@link ValueDateTupel} is sorted in
	 * descending order, i.e., if the value at position 0 has the highest
	 * {@link LocalDateTime} value (implicit check) and all subsequent
	 * {@link ValueDateTupel} each have a {@link LocalDateTime} before
	 * ({@link LocalDateTime#isBefore(ChronoLocalDateTime)}) the previous one.
	 * <p>
	 * If two values have the same {@link LocalDateTime} false will be returned.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} array of
	 *                        {@link ValueDateTupel} to be checked for
	 *                        descending order.
	 * @return {@code boolean} False, if any date is not chronologically before
	 *         its predecessor, true if otherwise.
	 * @throws IllegalArgumentException If the given array is null.
	 * @throws IllegalArgumentException If the given array contains null values.
	 */
	public static boolean isSortedDescending(ValueDateTupel[] valueDateTupels) {
		if (valueDateTupels == null)
			throw new IllegalArgumentException(MESSAGE_ARRAY_MUST_NOT_BE_NULL);
		if (ValueDateTupel.contains(valueDateTupels, null))
			throw new IllegalArgumentException(
					"The given array must not contain any null LocalDateTime");

		for (int i = 1; i < valueDateTupels.length; i++) {
			if (!valueDateTupels[i].getDate()
					.isBefore(valueDateTupels[i - 1].getDate())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Replace Double.NaN in a given row by real values values.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} An array of
	 *                        {@link ValueDateTupel} to be enhanced.
	 * @return {@code ValueDateTupel[]} The same array of {@link ValueDateTupel}
	 *         but with real values instead of Double.NaN.
	 */
	private static ValueDateTupel[] replaceNansByValues(
			ValueDateTupel[] valueDateTupels) {
		/*
		 * Loop over all dateTimes for each row to assess if they are
		 * Double.NaN.
		 */
		for (int fieldIndex = 0; fieldIndex < valueDateTupels.length; fieldIndex++) {
			/*
			 * If the ValueDateTupel contains a value other than Double.NaN
			 * continue with the next iteration.
			 */
			if (!Double.isNaN(valueDateTupels[fieldIndex].getValue()))
				continue;

			/*
			 * If the first valueDateTupel contains Double.NaN, its value will
			 * be set to match the next non-NaN-value. If the following values
			 * are also Double.NaN, iterate through the array until a value !=
			 * Double.NaN is found.
			 */
			if (fieldIndex == 0) {
				valueDateTupels = fillStartingValues(valueDateTupels);
				continue;
			}

			/*
			 * The missing value will be set to average the values of its direct
			 * predecessor and successor.
			 * 
			 * If there are multiple values missing in a row, all of those will
			 * get the average value of the last position before and the first
			 * position after all missing values.
			 * 
			 * If all values until the last one are missing, all consecutive NaN
			 * values will be set to the last position before all NaNs.
			 */
			int limitIndex = fieldIndex;

			double valueToBeSet = Double.NaN;

			while (Double.isNaN(valueDateTupels[limitIndex].getValue())) {
				limitIndex++;
				/*
				 * If there are no values in the remaining array set all values
				 * to be the last non-NaN, which is at fieldIndex-1.
				 */
				if (limitIndex == valueDateTupels.length) {
					limitIndex--;
					valueToBeSet = valueDateTupels[fieldIndex - 1].getValue();
					break;
				}

			}

			/*
			 * If the value to be set has already been calculated then there is
			 * NaNs left in the array, no more real values, until the very last
			 * position. All remaining values can be set to this value then.
			 * After that the loop can be left.
			 */
			if (!Double.isNaN(valueToBeSet)) {
				int localIndex = fieldIndex;
				while (localIndex < valueDateTupels.length) {
					valueDateTupels[localIndex].setValue(valueToBeSet);
					localIndex++;
				}
				break;
			}

			/* Fill in a "gap" of Double.NaN-values */
			valueDateTupels = fillCenterValues(valueDateTupels, fieldIndex - 1,
					limitIndex);

		}
		return valueDateTupels;
	}

	/**
	 * ======================================================================
	 * OVERRIDES
	 * ======================================================================
	 */

	/**
	 * A hash code for this ValueDateTupel.
	 */
	@GeneratedCode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Checks if this ValueDateTupel is equal to another ValueDateTupel.
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
		ValueDateTupel other = (ValueDateTupel) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}

	/**
	 * Outputs the fields of this ValueDateTupel as a {@code String}.
	 */
	@GeneratedCode
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ValueDateTupel [value=");
		builder.append(value);
		builder.append(", date=");
		builder.append(date);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */

	/**
	 * Get the value of a {@link ValueDateTupel}
	 * 
	 * @return {@code double} value of the {@link ValueDateTupel}
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Set the value of a {@link ValueDateTupel}
	 * 
	 * @param value {@code double} value to be set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Get the date of a {@link ValueDateTupel}
	 * 
	 * @return {@link LocalDateTime} date for the {@link ValueDateTupel}
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * Set the date for a {@link ValueDateTupel}
	 * 
	 * @param date {@link LocalDateTime} date to be set for the
	 *             {@link ValueDateTupel}
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

}
