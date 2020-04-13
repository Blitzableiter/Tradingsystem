/**
 * 
 */
package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * de.rumford.tradingsystem
 * 
 * @author Max Rumford
 *
 */
public class BaseValue {

	private final static double SHORT_INDEX_INITIAL_VALUE = 1000d;

	private String name;
	private ValueDateTupel[] values;
	private ValueDateTupel[] shortIndexValues;

	/**
	 * Creates a new {@link BaseValue} instance using the passed {@code String} for
	 * identification and stores the passed {@link ValueDateTupel[]} as values.
	 * Short index values are calculated based on the given values as specified in
	 * {@link de.rumford.tradingsystem.BaseValue#calculateShortIndexValues(double[])}.
	 * 
	 * @param name   {@code String} Name used to identify the represented base
	 *               value. Is not used for calculation of any kind. Must be of
	 *               length > {@code 0}.
	 * @param values {@link ValueDateTupel[]} Values of the represented base value.
	 *               Must be of length > {@code 0}.
	 * @throws IllegalArgumentException if the input values are not within
	 *                                  specification
	 */
	public BaseValue(String name, ValueDateTupel[] values) throws IllegalArgumentException {

		try {
			this.validateAndSetInput(name, values);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		ValueDateTupel[] shortIndexValues = this.calculateShortIndexValues(values);
		this.setShortIndexValues(shortIndexValues);
	}

	/**
	 * Creates a new {@link BaseValue} instance using the passed {@code String} for
	 * identification and stores the passed {@link ValueDateTupel[]} as values and
	 * the second passed {@link ValueDateTupel[]} as shortIndexValues.
	 * 
	 * @param name             {@code String} Name used to identify the represented
	 *                         base value. Fulfills no purpose and is not used for
	 *                         calculation of any kind. Must be of length >
	 *                         {@code 0}.
	 * @param values           {@link ValueDateTupel[]} Values of the represented
	 *                         base value
	 * @param shortIndexValues {@link ValueDateTupel[]} Short index values of the
	 *                         represented base value
	 * @throws IllegalArgumentException if the input values are not within
	 *                                  specification
	 */
	public BaseValue(String name, ValueDateTupel[] values, ValueDateTupel[] shortIndexValues)
			throws IllegalArgumentException {
		try {
			this.validateAndSetInput(name, values, shortIndexValues);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Validates the given parameters. Used by the Constructors to validate the
	 * constructor parameters.
	 * 
	 * @param name   {@code String} Name to be set for a {@link BaseValue}. Must not
	 *               have a length of {@code 0}.
	 * @param values {@link ValueDateTupel[]} Values to be set for a
	 *               {@link BaseValue}. Must contain at least {@code 1} element.
	 * @throws IllegalArgumentException if one of the above specifications is not
	 *                                  met.
	 */
	private void validateAndSetInput(String name, ValueDateTupel[] values) throws IllegalArgumentException {
		/* Check if name is not empty */
		if (name.length() == 0)
			throw new IllegalArgumentException("Name must not be an empty String");

		/* Check if passed values array contains elements */
		if (values.length == 0)
			throw new IllegalArgumentException("Values must not be an empty array");

		try {
			validateDates(values);
		} catch (IllegalArgumentException e) {
			throw e;
		}

		this.setName(name);
		this.setValues(values);
	}

	/**
	 * Validates the given parameters. Used by the Constructors to validate the
	 * constructor parameters. Utilizes
	 * {@link de.rumford.tradingsystem.BaseValue#validateAndSetInput(String, double[])}.
	 * 
	 * 
	 * @param name             {@code String} Name to be set for a
	 *                         {@link BaseValue}. Specifications see
	 *                         {@link de.rumford.tradingsystem.BaseValue#validateAndSetInput(String, double[])}.
	 * @param values           {@link ValueDateTupel[]} Values to be set for a
	 *                         {@link BaseValue}. Specifications see
	 *                         {@link de.rumford.tradingsystem.BaseValue#validateAndSetInput(String, double[])}.
	 * @param shortIndexValues {@link ValueDateTupel[]} Short index values to be set
	 *                         for a {@link BaseValue}. Must contain at least
	 *                         {@code 1} element.
	 * @throws IllegalArgumentException if one of the above specifications is not
	 *                                  met.
	 */
	private void validateAndSetInput(String name, ValueDateTupel[] values, ValueDateTupel[] shortIndexValues)
			throws IllegalArgumentException {
		/* Check if passed values array contains elements */
		if (shortIndexValues.length == 0)
			throw new IllegalArgumentException("Short index values must not be an empty array");

		try {
			validateAndSetInput(name, values);
		} catch (IllegalArgumentException e) {
			throw e;
		}

		try {
			validateDates(shortIndexValues);
		} catch (IllegalArgumentException e) {
			throw e;
		}

		this.setShortIndexValues(shortIndexValues);
	}

	private static void validateDates(ValueDateTupel[] values) throws IllegalArgumentException {
		/* Extract dates out of the values array and add it to a HashSet */
		Set<LocalDateTime> set = new HashSet<>();
		for (ValueDateTupel value : values)
			set.add(value.getDate());
		/*
		 * HashSets only store unique values. If the set has a smaller size than the
		 * original values array there must have been duplicate dates in the values
		 * array
		 */
		if (values.length != set.size())
			throw new IllegalArgumentException("Date/time values in given values array are not unique");

		/* Check if values are in correct order */
		for (int i = 1; i < values.length; i++) {

			/*
			 * The values cannot be used if they are not in ascending order.
			 */
			if (!ValueDateTupel.isSortedAscending(values))
				throw new IllegalArgumentException("Given values are not properly sorted");
		}
	}

	/**
	 * Calculates the short index values corresponding with a list of given values.
	 * The initial value is set to be {@code 1000}. The short index decreases by the
	 * same percentage the base value increases. If the base value increases by
	 * {@code 10%}, the short index decreases by {@code 10%} and vice versa.
	 * 
	 * <p>
	 * The short index value is calculated as follows:
	 * {@code v_s,t = v_s,t-1 - v_s,t-1 *
	 * return_t,t-1}. Variables:
	 * </p>
	 * 
	 * <ul>
	 * <li>{@code v_s,t} = short value on time interval {@code t}</li>
	 * <li>{@code return_t,t-1} = returns in the base value between two consecutive
	 * time intervals</li>
	 * </ul>
	 * 
	 * <p>
	 * If return of the base value exceeds 50% the return used to calculate the
	 * short index value is floored to 50%.
	 * 
	 * @param values {@link ValueDateTupel[]} values to base the short index values
	 *               on
	 * @return {@link ValueDateTupel[]} array of short index values
	 * @throws IllegalArgumentException if the passed values array contains no
	 *                                  elements
	 */
	private ValueDateTupel[] calculateShortIndexValues(ValueDateTupel[] values) throws IllegalArgumentException {
		/* Empty values array is not allowed */
		if (values.length == 0)
			throw new IllegalArgumentException("Empty values array not allowed");

		/**
		 * Declare the return value. There are always as many short index values as
		 * there are base values.
		 */
		ValueDateTupel[] shortIndexValues = new ValueDateTupel[values.length];
		shortIndexValues[0] = new ValueDateTupel(values[0].getDate(), SHORT_INDEX_INITIAL_VALUE);

		/**
		 * If the values array only contains one element no calculation has to take
		 * place and the array is preemptively returned.
		 */
		if (values.length == 1) {
			return shortIndexValues;
		}

		ValueDateTupel formerValue;
		ValueDateTupel latterValue;

		/**
		 * Loop over the provided values array and calculate the corresponding short
		 * index value for every time interval t > 0.
		 */
		for (int i = 1; i < values.length; i++) {
			formerValue = values[i - 1];
			latterValue = values[i];

			double returnPercentagePoints = Util.calculateReturn(formerValue.getValue(), latterValue.getValue());

			/**
			 * If the base value generates more than 50% in returns (and thus decreasing the
			 * short index value by more than 50%) the return percentage is set to 50%.
			 */
			if (returnPercentagePoints > 0.5)
				returnPercentagePoints = 0.5;

			double shortIndexValue = shortIndexValues[i - 1].getValue()
					- shortIndexValues[i - 1].getValue() * returnPercentagePoints;

			shortIndexValues[i] = new ValueDateTupel(latterValue.getDate(), shortIndexValue);
		}

		return shortIndexValues;
	}

	/**
	 * Check if the {@link BaseValue} instance contains the given
	 * {@link LocalDateTime} in its values. Returns the containing
	 * {@link ValueDateTupel} if so, returns {@code null} otherwise.
	 * 
	 * @param valueToCheck {@link LocalDateTime} Value to be found inside the
	 *                     {@link BaseValue} values.
	 * @return {@link ValueDateTupel} containing the given {@link LocalDateTime}.
	 *         {@code null} if the given {@link LocalDateTime} cannot be found.
	 */
	public ValueDateTupel getValue(LocalDateTime valueToCheck) {
		for (ValueDateTupel baseValue : this.getValues()) {
			if (baseValue.getDate().equals(valueToCheck))
				return baseValue;
		}
		return null;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(shortIndexValues);
		result = prime * result + Arrays.hashCode(values);
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
		BaseValue other = (BaseValue) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(shortIndexValues, other.shortIndexValues))
			return false;
		if (!Arrays.equals(values, other.values))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BaseValue [name=" + name + ", values=" + Arrays.toString(values) + ", shortIndexValues="
				+ Arrays.toString(shortIndexValues) + "]";
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */
	/**
	 * Get the name of this {@link BaseValue}
	 * 
	 * @return name {@code String} of this {@link BaseValue}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this {@link BaseValue}
	 * 
	 * @param name {@code String} the name to be set in this {@link BaseValue}
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the values of this {@link BaseValue}
	 * 
	 * @return values {@link ValueDateTupel[]} BaseValue
	 */
	public ValueDateTupel[] getValues() {
		return values;
	}

	/**
	 * Set the values of this {@link BaseValue}
	 * 
	 * @param values {@link ValueDateTupel[]} the values to be set
	 */
	private void setValues(ValueDateTupel[] values) {
		this.values = values;
	}

	/**
	 * Get the shortIndexValues of this {@link BaseValue}
	 * 
	 * @return shortIndexValues {@link ValueDateTupel[]} shortIndexValues of this
	 *         {@link BaseValue}
	 */
	public ValueDateTupel[] getShortIndexValues() {
		return shortIndexValues;
	}

	/**
	 * Set the shortIndexValues of this {@link BaseValue}
	 * 
	 * @param shortIndexValues {@link ValueDateTupel[]} the shortIndexValues to be
	 *                         set
	 */
	private void setShortIndexValues(ValueDateTupel[] shortIndexValues) {
		this.shortIndexValues = shortIndexValues;
	}

}
