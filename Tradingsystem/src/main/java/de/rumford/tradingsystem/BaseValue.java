/**
 * 
 */
package de.rumford.tradingsystem;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * de.rumford.tradingsystem
 * 
 * @author Max Rumford
 *
 */
public class BaseValue {

	private static final double SHORT_INDEX_INITIAL_VALUE = 1000d;

	private String name;
	private ValueDateTupel[] values;
	private ValueDateTupel[] shortIndexValues;
	private ValueDateTupel[] standardDeviationValues;

	/**
	 * Creates a new {@link BaseValue} instance using the passed {@code String} for
	 * identification and stores the passed {@link ValueDateTupel[]} as values.
	 * Short index values are calculated based on the given values as specified in
	 * {@link BaseValue#calculateShortIndexValues(double[])}.
	 * 
	 * @param name   {@code String} Name used to identify the represented base
	 *               value. Is not used for calculation of any kind. Must be of
	 *               length > {@code 0}.
	 * @param values {@link ValueDateTupel[]} Values of the represented base value.
	 *               Must be of length > {@code 0}.
	 * @throws IllegalArgumentException if the input values are not within
	 *                                  specification
	 */
	public BaseValue(String name, ValueDateTupel[] values) {
		this.validateInput(name, values);

		this.setName(name);
		this.setValues(values);

		this.setShortIndexValues(this.calculateShortIndexValues(values));
		this.setStandardDeviationValues(this.calculateStandardDeviationValues(values));
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
	public BaseValue(String name, ValueDateTupel[] values, ValueDateTupel[] shortIndexValues) {
		this(name, values);

		try {
			validateValues(shortIndexValues);
		} catch (Exception e) {
			throw new IllegalArgumentException("Given short index values do not meet the specifications.", e);
		}
		this.setShortIndexValues(shortIndexValues);
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
	private void validateInput(String name, ValueDateTupel[] values) {
		/* Check if name is null */
		if (name == null)
			throw new IllegalArgumentException("The given name must not be null");

		/* Check if values is null */
		if (values == null)
			throw new IllegalArgumentException("The given values must not be null");

		/* Check if name is not empty */
		if (name.length() == 0)
			throw new IllegalArgumentException("Name must not be an empty String");

		/* Check if passed values array contains elements */
		if (values.length == 0)
			throw new IllegalArgumentException("Values must not be an empty array");

		validateValues(values);

		validateDates(values);
	}

	/**
	 * Calculate the standard deviation values for the given base values. The first
	 * value is always Double.NaN.
	 * <p>
	 * {@code sd = baseValue * sqrt[ EWMA( return² ) ]}
	 * 
	 * @param baseValues {@code ValueDateTupel[]} the given base values.
	 * @return {@code ValueDateTupel[]} the calculated standard deviation values.
	 */
	private ValueDateTupel[] calculateStandardDeviationValues(ValueDateTupel[] baseValues) {

		/* Initiate the squared returns. The first value is always Double.NaN */
		ValueDateTupel[] squaredReturns = {};
		squaredReturns = ArrayUtils.add(squaredReturns, new ValueDateTupel(baseValues[0].getDate(), Double.NaN));
		/* Calculate the squared returns */
		for (int i = 1; i < baseValues.length; i++) {
			double returns;
			returns = Util.calculateReturn(baseValues[i - 1].getValue(), baseValues[i].getValue());
			squaredReturns = ArrayUtils.add(squaredReturns,
					new ValueDateTupel(baseValues[i].getDate(), Math.pow(returns, 2)));
		}

		/* Instantiate the EWMA used for the standard deviation. */
		EWMA ewmaOfStandardDeviation = new EWMA(squaredReturns, 25);

		/*
		 * The first value is always Double.NaN, as the first value cannot have standard
		 * deviation from itself.
		 */
		ValueDateTupel[] standardDeviationValues = {};
		standardDeviationValues = ArrayUtils.add(standardDeviationValues,
				new ValueDateTupel(baseValues[0].getDate(), Double.NaN));

		/* Fill in the calculated values. */
		for (int i = 1; i < squaredReturns.length; i++) {
			double squaredEwmaOfVolatility = ewmaOfStandardDeviation.getEwmaValues()[i].getValue();
			double ewmaOfVolatility = Math.sqrt(squaredEwmaOfVolatility);
			double standardDeviation = ewmaOfVolatility * baseValues[i].getValue();
			standardDeviationValues = ArrayUtils.add(standardDeviationValues,
					new ValueDateTupel(baseValues[i].getDate(), standardDeviation));
		}

		/* Return the standard deviations. */
		return standardDeviationValues;
	}

	/**
	 * Validates if the given array of ValueDateTupel is sorted in an ascending
	 * order.
	 * 
	 * @param values {@code ValueDateTupel[]} The given array of values.
	 * @throws IllegalArgumentException if the specifications above are not met.
	 */
	static void validateDates(ValueDateTupel[] values) throws IllegalArgumentException {
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
	 * <li>Be of length > 0</li>
	 * <li>Pass {@link #validateDates(ValueDateTupel[])}</li>
	 * <li>Not contain null</li>
	 * <li>Not contain NaNs as values</li>
	 * </ul>
	 * 
	 * @param values {@code ValueDateTupel[]} The given values.
	 * @throws IllegalArgumentException if the given array does not meet the above
	 *                                  specifications.
	 */
	static void validateValues(ValueDateTupel[] values) {
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

		validateDates(values);
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
	private ValueDateTupel[] calculateShortIndexValues(ValueDateTupel[] values) {
		/**
		 * Declare the return value. There are always as many short index values as
		 * there are base values.
		 */
		ValueDateTupel[] calculatedShortIndexValues = ValueDateTupel.createEmptyArray(values.length);
		calculatedShortIndexValues[0] = new ValueDateTupel(values[0].getDate(), SHORT_INDEX_INITIAL_VALUE);

		/**
		 * If the values array only contains one element no calculation has to take
		 * place and the array is preemptively returned.
		 */
		if (values.length == 1) {
			return calculatedShortIndexValues;
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

			/* Handle returnPercentagePoints == Double.NaN */
			double shortIndexValue;
			if (Double.isNaN(returnPercentagePoints)) {
				shortIndexValue = returnPercentagePoints;
			} else {
				shortIndexValue = calculatedShortIndexValues[i - 1].getValue()
						- calculatedShortIndexValues[i - 1].getValue() * returnPercentagePoints;
			}

			calculatedShortIndexValues[i] = new ValueDateTupel(latterValue.getDate(), shortIndexValue);
		}

		return calculatedShortIndexValues;
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
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(shortIndexValues);
		result = prime * result + Arrays.hashCode(values);
		return result;
	}

	@GeneratedCode
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

	@GeneratedCode
	@Override
	public String toString() {
		return "BaseValue [name=" + this.getName() + ", values=" + Arrays.toString(this.getValues())
				+ ", shortIndexValues=" + Arrays.toString(this.getShortIndexValues()) + "]";
	}

	/**
	 * ======================================================================
	 * FACTORIES
	 * ======================================================================
	 */

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

	/**
	 * @return standardDeviationValues BaseValue
	 */
	public ValueDateTupel[] getStandardDeviationValues() {
		return standardDeviationValues;
	}

	/**
	 * @param standardDeviationValues the standardDeviationValues to set
	 */
	private void setStandardDeviationValues(ValueDateTupel[] standardDeviationValues) {
		this.standardDeviationValues = standardDeviationValues;
	}
}
