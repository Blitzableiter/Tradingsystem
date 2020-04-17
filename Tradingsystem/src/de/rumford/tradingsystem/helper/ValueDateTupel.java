/**
 * 
 */
package de.rumford.tradingsystem.helper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Max Rumford
 *
 */
/**
 * de.rumford.tradingsystem.helper
 * 
 * @author Max Rumford
 *
 */
public class ValueDateTupel {

	private double value;
	private LocalDateTime date;

	/**
	 * Creates a new {@link ValueDateTupel} instance using... TODO
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
	 * Creates an empty array of {@link ValueDateTupel}.
	 * 
	 * @return {@link ValueDateTupel[]} An Empty array of {@link ValueDateTupel}.
	 */
	public static ValueDateTupel[] createEmptyArray() {
		return ValueDateTupel.createEmptyArray(0);
	}

	/**
	 * Creates an empty array of {@link ValueDateTupel} with the given length.
	 * 
	 * @param length {@code int} Length the new array should have.
	 * @return {@link ValueDateTupel[]} An Empty array of {@link ValueDateTupel}.
	 */
	public static ValueDateTupel[] createEmptyArray(int length) {
		return new ValueDateTupel[length];
	}

	/**
	 * Append a {@link ValueDateTupel} to an array of {@link ValueDateTupel}.
	 * 
	 * @param valueDateTupels {@link ValueDateTupel[]} Array of
	 *                        {@link ValueDateTupel} an element should be appended
	 *                        to.
	 * @param newElement      {@link ValueDateTupel} to be appended to the given
	 *                        array.
	 * @return {@link ValueDateTupel[]) Passed array with the passed Element in the
	 *         last index. @deprecated Use
	 *         {@link org.apache.commons.lang3.ArrayUtils#add(Object[], Object)}
	 *         instead
	 * @deprecated
	 */
	public static ValueDateTupel[] appendElement(ValueDateTupel[] valueDateTupels, ValueDateTupel newElement) {
		ValueDateTupel[] returnValueDateTupels = new ValueDateTupel[valueDateTupels.length + 1];

		for (int i = 0; i < valueDateTupels.length; i++)
			returnValueDateTupels[i] = valueDateTupels[i];

		returnValueDateTupels[returnValueDateTupels.length - 1] = newElement;

		return returnValueDateTupels;
	}

	/**
	 * Evaluate if the given {@link ValueDateTupel[]} is sorted in ascending order,
	 * i.e., if the value at position 0 has the lowest {@link LocalDateTime} value
	 * (implicit check) and all subsequent {@link ValueDateTupel} each have a
	 * {@link LocalDateTime} after
	 * ({@link LocalDateTime#isAfter(ChronoLocalDateTime)}) the previous one.
	 * <p>
	 * If two values have the same {@link LocalDateTime} false will be returned.
	 * 
	 * @param valueDateTupels {@link ValueDateTupel[]} array of
	 *                        {@link ValueDateTupel} to be checked for ascending
	 *                        order.
	 * @return {@code boolean} False, if any date is not chronologically after its
	 *         predecessor, true if otherwise.
	 * @throws IllegalArgumentException If the given array is null.
	 * @throws IllegalArgumentException If the given array contains null values.
	 */
	public static boolean isSortedAscending(ValueDateTupel[] valueDateTupels) throws IllegalArgumentException {
		if (valueDateTupels == null)
			throw new IllegalArgumentException("The given array must not be null");
		if (ValueDateTupel.contains(valueDateTupels, null))
			throw new IllegalArgumentException("The given array must not contain any null values");

		for (int i = 1; i < valueDateTupels.length; i++) {
			if (!valueDateTupels[i].getDate().isAfter(valueDateTupels[i - 1].getDate())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Evaluate if the given {@link ValueDateTupel[]} is sorted in descending order,
	 * i.e., if the value at position 0 has the highest {@link LocalDateTime} value
	 * (implicit check) and all subsequent {@link ValueDateTupel} each have a
	 * {@link LocalDateTime} before
	 * ({@link LocalDateTime#isBefore(ChronoLocalDateTime)}) the previous one.
	 * <p>
	 * If two values have the same {@link LocalDateTime} false will be returned.
	 * 
	 * @param valueDateTupels {@link ValueDateTupel[]} array of
	 *                        {@link ValueDateTupel} to be checked for descending
	 *                        order.
	 * @return {@code boolean} False, if any date is not chronologically before its
	 *         predecessor, true if otherwise.
	 * @throws IllegalArgumentException If the given array is null.
	 * @throws IllegalArgumentException If the given array contains null values.
	 */
	public static boolean isSortedDescending(ValueDateTupel[] valueDateTupels) throws IllegalArgumentException {
		if (valueDateTupels == null)
			throw new IllegalArgumentException("The given array must not be null");
		if (ValueDateTupel.contains(valueDateTupels, null))
			throw new IllegalArgumentException("The given array must not contain any null values");

		for (int i = 1; i < valueDateTupels.length; i++) {
			if (!valueDateTupels[i].getDate().isBefore(valueDateTupels[i - 1].getDate())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Add missing {@link LocalDateTime} values to all given
	 * {@code ValueDateTupel[]}. The corresponding value will be set to average the
	 * values of its direct predecessor and successor.
	 * <p>
	 * If there are multiple {@link LocalDateTime} missing in a row, all of those
	 * will get the average value of the last position before and the first position
	 * after all missing values.
	 * <p>
	 * If the missing {@link LocalDateTime} would be the first value in the new
	 * array, its value will be set to match the previously first one.
	 * <p>
	 * If the missing {@link LocalDateTime} would be the last value in the new
	 * array, its value will be set to match the previously last one.
	 * 
	 * @param valueDateTupel {@code ValueDateTupel[][]} Array of arrays of
	 *                       {@link ValueDateTupel} whose {@link LocalDateTime}
	 *                       shall be aligned.
	 * @return {@code ValueDateTupel[][]} Array of arrays of {@link ValueDateTupel}
	 *         with now aligned {@link LocalDateTime} values.
	 * @throws IllegalArgumentException If the given array of arrays is null.
	 * @throws IllegalArgumentException If the given array of arrays contains null.
	 * @throws IllegalArgumentException If any array of the given array of arrays
	 *                                  contains null.
	 * @throws IllegalArgumentException If the given array contains an array of
	 *                                  {@link ValueDateTupel} not sorted in
	 *                                  ascending order.
	 * @throws IllegalArgumentException If the one of the given arrays contains only
	 *                                  {@link Double#NaN}.
	 */
	public static ValueDateTupel[][] alignDates(ValueDateTupel[][] valueDateTupels) throws IllegalArgumentException {
		if (valueDateTupels == null)
			throw new IllegalArgumentException("Given array of arrays must not be null");

		/* TreeSet (unique and sorted) of all dates in all valueDateTupel[] */
		TreeSet<LocalDateTime> uniqueSortedDates = new TreeSet<>();

		/* For each array in ValueDateTupels ... */
		for (int rowIndex = 0; rowIndex < valueDateTupels.length; rowIndex++) {
			if (valueDateTupels[rowIndex] == null)
				throw new IllegalArgumentException("The array at position " + rowIndex + " is null.");
			if (ValueDateTupel.contains(valueDateTupels[rowIndex], null))
				throw new IllegalArgumentException(
						"The array at position " + rowIndex + " contains at least one null.");
			if (!ValueDateTupel.isSortedAscending(valueDateTupels[rowIndex]))
				throw new IllegalArgumentException(
						"The array at position " + rowIndex + " is not sorted in ascending order.");

			/* ... add all values into uniqueSortedDates */
			uniqueSortedDates.addAll(Arrays.asList(ValueDateTupel.getDates(valueDateTupels[rowIndex])));
		}

		/* Load unique sorted dates into an ArrayList to have access to an index. */
		List<LocalDateTime> uniqueSortedDatesList = new ArrayList<>(uniqueSortedDates);

		/* Loop over all rows */
		for (int rowIndex = 0; rowIndex < valueDateTupels.length; rowIndex++) {
			/*
			 * If the row's length equals the length of uniqueSortedDates no Value has to be
			 * added as it already contains all dateTimes.
			 */
			if (valueDateTupels[rowIndex].length == uniqueSortedDatesList.size())
				continue;

			ValueDateTupel valueDateTupelToBeAdded;

			/*
			 * Loop over all unique dateTimes to assess if they are in the current row. If
			 * not, missing dateTimes are added into the original arrays. Their value is set
			 * to Double.NaN
			 */
			for (int fieldIndex = 0; fieldIndex < uniqueSortedDatesList.size(); fieldIndex++) {

				/*
				 * If the index would access an element out of bounds for the valueDateTupels
				 * array add the new element to the end
				 */
				if (fieldIndex >= valueDateTupels[rowIndex].length) {
					valueDateTupelToBeAdded = new ValueDateTupel(uniqueSortedDatesList.get(fieldIndex), Double.NaN);
					valueDateTupels[rowIndex] = ValueDateTupel.addOneAt(valueDateTupels[rowIndex],
							valueDateTupelToBeAdded, fieldIndex);
					/*
					 * Since we're already operating on the very limits of the valueDateTupels array
					 * we don't need to do anything further
					 */
					continue;
				}

				/*
				 * If the currentDateTime out of the list of unique values is already in the
				 * given row nothing has to be done.
				 */
				if (uniqueSortedDatesList.get(fieldIndex) == valueDateTupels[rowIndex][fieldIndex].getDate())
					continue;
				valueDateTupelToBeAdded = new ValueDateTupel(uniqueSortedDatesList.get(fieldIndex), Double.NaN);
				valueDateTupels[rowIndex] = ValueDateTupel.addOneAt(valueDateTupels[rowIndex], valueDateTupelToBeAdded,
						fieldIndex);
			}

			/*
			 * Loop over all dateTimes for each row to assess if they are Double.NaN.
			 */
			for (int fieldIndex = 0; fieldIndex < valueDateTupels[rowIndex].length; fieldIndex++) {
				/*
				 * If the ValueDateTupel contains a value other than Double.NaN continue with
				 * the next iteration.
				 */
				if (!Double.isNaN(valueDateTupels[rowIndex][fieldIndex].getValue()))
					continue;

				/*
				 * If the first valueDateTupel contains Double.NaN, its value will be set to
				 * match the previously first one. If the following values are also Double.NaN,
				 * iterate through the array until a value != Double.NaN is found.
				 */
				if (fieldIndex == 0) {
					int localFieldIndex = 1;
					/* Iterate through the array until a value != Double.NaN is found */
					while (Double.isNaN(valueDateTupels[rowIndex][localFieldIndex].getValue())) {
						localFieldIndex++;
						/*
						 * If localFieldIndex reaches valueDateTupel[rowIndex].length there are no
						 * non-NaN values in the array. Thus no values can be correctly set. Also an
						 * ArrayOutOfBounds-Exception would be thrown on the next while-iteration.
						 */
						if (localFieldIndex == valueDateTupels[rowIndex].length)
							throw new IllegalArgumentException("Row at position " + rowIndex
									+ " contains only Double.NaN. Rows must contain at least one value != Double.NaN");
					}

					/*
					 * Update fieldIndex to save some iterations of the for loop, as all values up
					 * to localFieldIndex will already be valid.
					 */
					fieldIndex = localFieldIndex;

					/*
					 * If only one value has to be set execution can continue with the next loop
					 * iteration
					 */
					if (localFieldIndex == 1) {
						valueDateTupels[rowIndex][localFieldIndex - 1]
								.setValue(valueDateTupels[rowIndex][localFieldIndex].getValue());
						continue;
					}

					/*
					 * If multiple values have to be set iterate over them an fill them
					 * subsequently, starting from the last NaN before the first valid value.
					 */
					while (localFieldIndex >= 1) {
						valueDateTupels[rowIndex][localFieldIndex - 1]
								.setValue(valueDateTupels[rowIndex][localFieldIndex].getValue());
						localFieldIndex--;
					}
					continue;
				}

				/*
				 * The missing value will be set to average the values of its direct predecessor
				 * and successor.
				 * 
				 * If there are multiple values missing in a row, all of those will get the
				 * average value of the last position before and the first position after all
				 * missing values.
				 * 
				 * If all values until the last one are missing, all consecutive NaN values will
				 * be set to the last position before all NaNs.
				 */
				int localFieldIndexNext = fieldIndex + 1;

				/*
				 * If only the last value is NaN set it to be the previous value and break from
				 * loop.
				 */
				if (valueDateTupels[rowIndex].length == localFieldIndexNext) {
					valueDateTupels[rowIndex][fieldIndex]
							.setValue(valueDateTupels[rowIndex][fieldIndex - 1].getValue());
					break;
				}

				double valueToBeSet = Double.NaN;

				while (Double.isNaN(valueDateTupels[rowIndex][localFieldIndexNext].getValue())) {
					localFieldIndexNext++;
					/*
					 * If there are no values in the remaining array set all values to be the last
					 * non-NaN, which is at fieldIndex-1.
					 */
					if (localFieldIndexNext == valueDateTupels[rowIndex].length) {
						localFieldIndexNext--;
						valueToBeSet = valueDateTupels[rowIndex][fieldIndex - 1].getValue();
						break;
					}

				}
				/*
				 * If the value to be set has already been calculated then there is NaNs left in
				 * the array, no more real values, until the very last position. All remaining
				 * values can be set to this value then. After that the loop can be left.
				 */
				if (!Double.isNaN(valueToBeSet)) {
					while (fieldIndex <= localFieldIndexNext) {
						valueDateTupels[rowIndex][fieldIndex].setValue(valueToBeSet);
						fieldIndex++;
					}
					break;
				}

				/*
				 * The value to be set to all missing values is the average of the last non-NaN
				 * before the NaNs and the first non-NaN after the NaNs.
				 */
				valueToBeSet = (valueDateTupels[rowIndex][fieldIndex - 1].getValue()
						+ valueDateTupels[rowIndex][localFieldIndexNext].getValue()) / 2;

				/* Fill all values up to the next NaN with the calculated value. */
				while (fieldIndex < localFieldIndexNext) {
					valueDateTupels[rowIndex][fieldIndex].setValue(valueToBeSet);
					fieldIndex++;
				}

			}
		}

		return valueDateTupels;

	}

	/**
	 * Check if the given {@link ValueDateTupel} can be found in the given array of
	 * {@link ValueDateTupel}. Will only find exact matches.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} Array to be searched in.
	 * @param valueDateTupel  {@link ValueDateTupel} Value to be searched for.
	 * @return {@code boolean} True, if the given value can be found in the given
	 *         array, false otherwise.
	 */
	public static boolean contains(ValueDateTupel[] valueDateTupels, ValueDateTupel valueDateTupel) {
		List<ValueDateTupel> list = new ArrayList<>(Arrays.asList(valueDateTupels));
		return list.contains(valueDateTupel);
	}

	/**
	 * Check if the given array of {@link ValueDateTupel} contains the given
	 * {@link LocalDateTime}.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} Array to be searched in.
	 * @param dateTime        {@link LocalDateTime} Value to be searched for.
	 * @return {@code boolean} True, if the given value can be found inside the
	 *         given array, false otherwise.
	 * @throws IllegalArgumentException If the given array of {@link ValueDateTupel}
	 *                                  is null.
	 * @throws IllegalArgumentException If the given {@link LocalDateTime} is null.
	 * 
	 */
	public static boolean containsDate(ValueDateTupel[] valueDateTupels, LocalDateTime dateTime)
			throws IllegalArgumentException {
		if (valueDateTupels == null)
			throw new IllegalArgumentException("Given array cannot be null");
		if (dateTime == null)
			throw new IllegalArgumentException("Given LocalDateTime cannot be null");
		/* Load all values from the given array into an ArrayList. */
		List<LocalDateTime> list = new ArrayList<>(Arrays.asList(ValueDateTupel.getDates(valueDateTupels)));
		/* Utilize the generic contains method on ArrayList. */
		return list.contains(dateTime);
	}

	/**
	 * Adds a given {@link ValueDateTupel} to the given array of
	 * {@link ValueDateTupel} at the given position. Returns the given array
	 * extended with the given single value.
	 * 
	 * @param valueDateTupels         {@code ValueDateTupel[]} Array to be extended.
	 * @param valueDateTupelToBeAdded {@link ValueDateTupel} Value to be added.
	 * @param position                {@code int} Position the given value shall be
	 *                                put into.
	 * @return {@code ValueDateTupel[]} The extended array.
	 * @throws IllegalArgumentException If the given array is null.
	 * @throws IllegalArgumentException If the given value to be added is null.
	 * @throws IllegalArgumentException If the position is negative.
	 * @throws IllegalArgumentException If the given position is > the length of the
	 *                                  given array.
	 */
	public static ValueDateTupel[] addOneAt(ValueDateTupel[] valueDateTupels, ValueDateTupel valueDateTupelToBeAdded,
			int position) throws IllegalArgumentException {
		if (valueDateTupels == null)
			throw new IllegalArgumentException("Given array of ValueDateTupel must not be null");
		if (valueDateTupelToBeAdded == null)
			throw new IllegalArgumentException("Given valueDateTupelToBeAdded must not be null");
		if (position < 0)
			throw new IllegalArgumentException("Cannot not add a value at position < 0. Given position is " + position);
		if (position > valueDateTupels.length)
			throw new IllegalArgumentException("Cannot add a value at position > " + valueDateTupels.length
					+ ". Given position is " + position + ".");

		ValueDateTupel[] extendedArray = ValueDateTupel.createEmptyArray(valueDateTupels.length + 1);

		/* Add new ValueDateTupel at the beginning of the given array. */
		if (position == 0) {
			extendedArray[position] = valueDateTupelToBeAdded;
			System.arraycopy(valueDateTupels, 0, extendedArray, 1, valueDateTupels.length);
			return extendedArray;
		}
		/* Add new ValueDateTupel at the end of the given array. */
		if (position == valueDateTupels.length) {
			System.arraycopy(valueDateTupels, 0, extendedArray, 0, valueDateTupels.length);
			extendedArray[position] = valueDateTupelToBeAdded;
			return extendedArray;
		}
		/*
		 * This code is only reached, when the new ValueDateTupel shall not be added at
		 * end or at beginning.
		 */
		/* Add all values prior to the new ValueDateTupel */
		System.arraycopy(valueDateTupels, 0, extendedArray, 0, position);
		/* Add new ValueDateTupel at the given position. */
		extendedArray[position] = valueDateTupelToBeAdded;
		/* Add all values subsequent to the new ValueDateTupel */
		System.arraycopy(valueDateTupels, position, extendedArray, position + 1, valueDateTupels.length - position);
		return extendedArray;
	}

	/**
	 * Get all values from an array of {@link ValueDateTupel[]}.
	 * 
	 * @param tupels {@link ValueDateTupel[]} An array of {@link ValueDateTupel}}.
	 * @return {@code double[]} An array of values of the given
	 *         {@link ValueDateTupel[]}. Returns an empty array if the given array
	 *         is empty.
	 * @throws IllegalArgumentException if the given array is null.
	 */
	public static double[] getValues(ValueDateTupel[] tupels) throws IllegalArgumentException {
		if (tupels == null)
			throw new IllegalArgumentException("Given array must not be null");
		double[] values = {};
		for (ValueDateTupel tupel : tupels)
			values = ArrayUtils.add(values, tupel.getValue());
		return values;
	}

	/**
	 * Get all {@link LocalDateTime} from an array of {@link ValueDateTupel[]}.
	 * 
	 * @param tupels {@link ValueDateTupel[]} An array of {@link ValueDateTupel}}.
	 * @return {@code LocalDateTime[]} An array of {@link LocalDateTime} of the
	 *         given {@link ValueDateTupel[]}. Returns an empty array if the given
	 *         array is empty.
	 * @throws IllegalArgumentException if the given array is null.
	 */
	public static LocalDateTime[] getDates(ValueDateTupel[] tupels) throws IllegalArgumentException {
		if (tupels == null)
			throw new IllegalArgumentException("Given array must not be null");
		LocalDateTime[] values = {};
		for (ValueDateTupel tupel : tupels)
			values = ArrayUtils.add(values, tupel.getDate());
		return values;
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
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		ValueDateTupel other = (ValueDateTupel) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ValueDateTupel [value=" + value + ", date=" + date + "]";
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
