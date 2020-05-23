package de.rumford.tradingsystem;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.Validator;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * The BaseValue is a substantial part for every trading system and provides the
 * values to be decided upon by the rules. It encapsulates the underlying value,
 * e.g. a stocks tracker, and represents its values as an array of
 * {@link ValueDateTupel}. This are guaranteed to be in ascending order and free
 * of duplicates.
 * <p>
 * Each BaseValue has two final static values that cannot be changed and are
 * deemed to preserve comparability of base values. The first such value is the
 * lookback window. It indicates the amount of values relevant (enough) for
 * standard deviation calculation. A lookback window of 25, as implemented,
 * deems all values "older" than 25 time intervals (as stated by the given
 * ValueDateTupels) irrelevant for standard deviation calculation. As a matter
 * of fact, these older values are still considered in standard deviation
 * calculation, but the factor they are being multiplied with (due to the
 * recursive calculation of the standard deviation value) is being considered as
 * too small to actually make a noticable difference.
 * <p>
 * The second predefined value is the short index initial value. If no array
 * representing an adequate short index is given into the constructor, an array
 * of such values is calculated by subtracting the returns (percentage wise)
 * between two time intervals from the previous short index value. The short
 * index initial value simply marks the value to be set for the first time
 * interval. Its value does not play any role in further calculations, as
 * proportions will remain unaltered, no matter the actual initial value.
 * 
 * @author Max Rumford
 *
 */
public class BaseValue {

	private static final int LOOKBACK_WINDOW = 25;
	private static final double SHORT_INDEX_INITIAL_VALUE = 1000d;

	private String name;
	private ValueDateTupel[] values;
	private ValueDateTupel[] shortIndexValues;
	private ValueDateTupel[] standardDeviationValues;

	/**
	 * Creates a new {@link BaseValue} instance using the passed {@code String} for
	 * identification and stores the passed array of {@link ValueDateTupel} as
	 * values. Short index values are calculated based on the given values as
	 * specified in {@link BaseValue#calculateShortIndexValues(ValueDateTupel[])}.
	 * 
	 * @param name   {@code String} Name used to identify the represented base
	 *               value. Is not used for calculation of any kind. Must be of
	 *               length greater than {@code 0}.
	 * @param values {@code ValueDateTupel[]} Values of the represented base value.
	 *               Must not be null. Must be of length greater than {@code 0}.
	 *               Must be in an ascending order. Must not contain nulls. Must not
	 *               contain values of Double.NaN.
	 * @throws IllegalArgumentException if the input values are not within
	 *                                  specification
	 */
	public BaseValue(String name, ValueDateTupel[] values) {
		validateInput(name, values);

		this.setName(name);
		this.setValues(values);

		this.setShortIndexValues(calculateShortIndexValues(values));
		this.setStandardDeviationValues(calculateStandardDeviationValues(values));
	}

	/**
	 * Creates a new {@link BaseValue} instance using the passed {@code String} for
	 * identification and stores the passed array of {@link ValueDateTupel} as
	 * values and the second passed array of {@link ValueDateTupel} as
	 * shortIndexValues.
	 * 
	 * @param name             {@code String} Name used to identify the represented
	 *                         base value. Fulfills no purpose and is not used for
	 *                         calculation of any kind. Must be of length greater
	 *                         than {@code 0}.
	 * @param values           {@code ValueDateTupel[]} Values of the represented
	 *                         base value. Must not be null. Must be of length
	 *                         greater than {@code 0}. Must be in an ascending
	 *                         order. Must not contain nulls. Must not contain
	 *                         values of Double.NaN.
	 * @param shortIndexValues {@code ValueDateTupel[]} Short index values of the
	 *                         represented base value. Must not be null. Must be of
	 *                         length greater than {@code 0}. Must be in an
	 *                         ascending order. Must not contain nulls. Must not
	 *                         contain values of Double.NaN.
	 * @throws IllegalArgumentException if the input values are not within
	 *                                  specification
	 */
	public BaseValue(String name, ValueDateTupel[] values, ValueDateTupel[] shortIndexValues) {
		this(name, values);

		try {
			Validator.validateValues(shortIndexValues);
			Validator.validateDates(shortIndexValues);
		} catch (Exception e) {
			throw new IllegalArgumentException("Given short index values do not meet the specifications.", e);
		}

		ValueDateTupel[][] valuesAndShortIndexValues = { values, shortIndexValues };

		ValueDateTupel[][] alignedValuesAndShortIndexValues = ValueDateTupel.alignDates(valuesAndShortIndexValues);
		this.setValues(alignedValuesAndShortIndexValues[0]);
		this.setShortIndexValues(alignedValuesAndShortIndexValues[1]);
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
	 * @param values {@codeValueDateTupel[]} values to base the short index values
	 *               on
	 * @return {@code ValueDateTupel[]} array of short index values
	 * @throws IllegalArgumentException if the passed values array contains no
	 *                                  elements
	 */
	private static ValueDateTupel[] calculateShortIndexValues(ValueDateTupel[] values) {
		/**
		 * Declare the return value. There are always as many short index values as
		 * there are base values.
		 */
		ValueDateTupel[] calculatedShortIndexValues = ValueDateTupel.createEmptyArray(values.length);
		calculatedShortIndexValues[0] = new ValueDateTupel(values[0].getDate(), SHORT_INDEX_INITIAL_VALUE);

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

			double shortIndexValue = calculatedShortIndexValues[i - 1].getValue()
					- calculatedShortIndexValues[i - 1].getValue() * returnPercentagePoints;

			calculatedShortIndexValues[i] = new ValueDateTupel(latterValue.getDate(), shortIndexValue);
		}

		return calculatedShortIndexValues;
	}

	/**
	 * Calculate the standard deviation values for the given base values. The first
	 * value is always Double.NaN.
	 * <p>
	 * {@code sd = baseValue * sqrt[ EWMA( return^2 ) ]}
	 * 
	 * @param baseValues {@code ValueDateTupel[]} the given base values.
	 * @return {@code ValueDateTupel[]} the calculated standard deviation values.
	 */
	private static ValueDateTupel[] calculateStandardDeviationValues(ValueDateTupel[] baseValues) {

		/* Initiate the squared returns. The first value is always Double.NaN */
		ValueDateTupel[] squaredReturns = {};
//		squaredReturns = ArrayUtils.add(squaredReturns, new ValueDateTupel(baseValues[0].getDate(), Double.NaN));
		/* Calculate the squared returns */
		for (int i = 0; i < baseValues.length - 1; i++) {
			double returns;
			returns = Util.calculateReturn(baseValues[i].getValue(), baseValues[i + 1].getValue());
			squaredReturns = ArrayUtils.add(squaredReturns,
					new ValueDateTupel(baseValues[i + 1].getDate(), Math.pow(returns, 2)));
		}

		/* Instantiate the EWMA used for the standard deviation. */
		EWMA ewmaOfStandardDeviation = new EWMA(squaredReturns, LOOKBACK_WINDOW);

		/*
		 * The first value is always Double.NaN, as the first value cannot have standard
		 * deviation from itself.
		 */
		ValueDateTupel[] standardDeviationValues = {};
//		standardDeviationValues = ArrayUtils.add(standardDeviationValues,
//				new ValueDateTupel(baseValues[0].getDate(), Double.NaN));

		/* Fill in the calculated values. */
		for (int i = 0; i < squaredReturns.length; i++) {
			double squaredEwmaOfVolatility = ewmaOfStandardDeviation.getEwmaValues()[i].getValue();
			double ewmaOfVolatility = Math.sqrt(squaredEwmaOfVolatility);
			/*
			 * The base values array has one more value than the standardDeviationValues
			 * will have, as there cannot be a standard deviation value for the first time
			 * interval. The first base value will not have a standard deviation value.
			 * Therefore to e.g. calculate the _first_ sd value, the _second_ base value has
			 * to be used.
			 */
			double standardDeviation = ewmaOfVolatility * baseValues[i + 1].getValue();
			standardDeviationValues = ArrayUtils.add(standardDeviationValues,
					new ValueDateTupel(baseValues[i + 1].getDate(), standardDeviation));
		}

		/* Return the standard deviations. */
		return standardDeviationValues;
	}

	/**
	 * Validates the given parameters. Used by the Constructors to validate the
	 * constructor parameters.
	 * 
	 * @param name   {@code String} Name to be set for a {@link BaseValue}. Must not
	 *               be null. Must not have a length of {@code 0}.
	 * @param values {@code ValueDateTupel[]} Values to be set for a
	 *               {@link BaseValue}. Must pass
	 *               {@link Validator#validateValues(ValueDateTupel[])} and
	 *               {@link Validator#validateDates(ValueDateTupel[])}.
	 * @throws IllegalArgumentException if one of the above specifications is not
	 *                                  met.
	 */
	private static void validateInput(String name, ValueDateTupel[] values) {
		/* Check if name is null */
		if (name == null)
			throw new IllegalArgumentException("The given name must not be null");

		/* Check if name is not empty */
		if (name.length() == 0)
			throw new IllegalArgumentException("Name must not be an empty String");

		Validator.validateValues(values);
		Validator.validateDates(values);
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
	 * @return values {@code ValueDateTupel[]} BaseValue
	 */
	public ValueDateTupel[] getValues() {
		return values;
	}

	/**
	 * Set the values of this {@link BaseValue}
	 * 
	 * @param values {@code ValueDateTupel[]} the values to be set
	 */
	private void setValues(ValueDateTupel[] values) {
		this.values = values;
	}

	/**
	 * Get the shortIndexValues of this {@link BaseValue}
	 * 
	 * @return shortIndexValues {@code ValueDateTupel[]} shortIndexValues of this
	 *         {@link BaseValue}
	 */
	public ValueDateTupel[] getShortIndexValues() {
		return shortIndexValues;
	}

	/**
	 * Set the shortIndexValues of this {@link BaseValue}
	 * 
	 * @param shortIndexValues {@code ValueDateTupel[]} the shortIndexValues to be
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
