/**
 * 
 */
package de.rumford.tradingsystem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

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
	public BaseValue(String name, ValueDateTupel[] values) {
		this.validateAndSetInput(name, values);
		this.setShortIndexValues(this.calculateShortIndexValues(values));
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
		this.validateAndSetInput(name, values, shortIndexValues);
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

		validateDates(values);

		this.setName(name);
		this.setValues(values);
		this.setStandardDeviationValues(this.calculateStandardDeviationValues(values));
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

		/* Check if given array is null */
		if (baseValues == null)
			throw new IllegalArgumentException("Given base values must not be null");

		/* Initiate the squared returns. The first value is always Double.NaN */
		ValueDateTupel[] squaredReturns = {};
		squaredReturns = ArrayUtils.add(squaredReturns, new ValueDateTupel(baseValues[0].getDate(), Double.NaN));
		/* Calculate the squared returns */
		for (int i = 1; i < baseValues.length; i++) {
			double returns;
			try {
				returns = Util.calculateReturn(baseValues[i - 1].getValue(), baseValues[i].getValue());
			} catch (IllegalArgumentException e) {
				/*
				 * If the value cannot be calculated because the first value is zero set the
				 * returns to Double.Nan
				 */
				returns = Double.NaN;
			}
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
	private void validateAndSetInput(String name, ValueDateTupel[] values, ValueDateTupel[] shortIndexValues) {
		/* Check if passed values array contains elements */
		if (shortIndexValues.length == 0)
			throw new IllegalArgumentException("Short index values must not be an empty array");

		validateAndSetInput(name, values);
		validateDates(shortIndexValues);

		this.setShortIndexValues(shortIndexValues);
	}

	static void validateDates(ValueDateTupel[] values) {
		/*
		 * The values cannot be used if they are not in ascending order.
		 */
		if (!ValueDateTupel.isSortedAscending(values))
			throw new IllegalArgumentException("Given values are not properly sorted or there are non-unique values.");
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
	 * FACTORIES
	 * ======================================================================
	 */
	static final BaseValue jan1_jan5_22_00_00_val100_500_calc_short(String name) {
		LocalDateTime localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));

		ValueDateTupel valuedatetupel1 = new ValueDateTupel(localDateTimeJan01220000, 100d);
		ValueDateTupel valuedatetupel2 = new ValueDateTupel(localDateTimeJan02220000, 200d);
		ValueDateTupel valuedatetupel3 = new ValueDateTupel(localDateTimeJan03220000, 300d);
		ValueDateTupel valuedatetupel4 = new ValueDateTupel(localDateTimeJan04220000, 400d);
		ValueDateTupel valuedatetupel5 = new ValueDateTupel(localDateTimeJan05220000, 500d);

		ValueDateTupel[] values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);
		values = ArrayUtils.add(values, valuedatetupel5);

		return new BaseValue(name, values);
	}

	static final BaseValue jan1_jan4_22_00_00_val200_400_500_200_calc_short(String name) {
		LocalDateTime localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));

		ValueDateTupel valuedatetupel1 = new ValueDateTupel(localDateTimeJan01220000, 200d);
		ValueDateTupel valuedatetupel2 = new ValueDateTupel(localDateTimeJan02220000, 400d);
		ValueDateTupel valuedatetupel3 = new ValueDateTupel(localDateTimeJan03220000, 500d);
		ValueDateTupel valuedatetupel4 = new ValueDateTupel(localDateTimeJan04220000, 200d);

		ValueDateTupel[] values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);

		return new BaseValue(name, values);
	}

	static final BaseValue jan1_jan31_22_00_00_calc_short(String name) {
		LocalDateTime localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan06220000 = LocalDateTime.of(LocalDate.of(2020, 1, 6), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan07220000 = LocalDateTime.of(LocalDate.of(2020, 1, 7), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan08220000 = LocalDateTime.of(LocalDate.of(2020, 1, 8), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan09220000 = LocalDateTime.of(LocalDate.of(2020, 1, 9), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan10220000 = LocalDateTime.of(LocalDate.of(2020, 1, 10), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan11220000 = LocalDateTime.of(LocalDate.of(2020, 1, 11), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan12220000 = LocalDateTime.of(LocalDate.of(2020, 1, 12), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan13220000 = LocalDateTime.of(LocalDate.of(2020, 1, 13), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan14220000 = LocalDateTime.of(LocalDate.of(2020, 1, 14), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan15220000 = LocalDateTime.of(LocalDate.of(2020, 1, 15), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan16220000 = LocalDateTime.of(LocalDate.of(2020, 1, 16), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan17220000 = LocalDateTime.of(LocalDate.of(2020, 1, 17), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan18220000 = LocalDateTime.of(LocalDate.of(2020, 1, 18), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan19220000 = LocalDateTime.of(LocalDate.of(2020, 1, 19), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan20220000 = LocalDateTime.of(LocalDate.of(2020, 1, 20), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan21220000 = LocalDateTime.of(LocalDate.of(2020, 1, 21), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan22220000 = LocalDateTime.of(LocalDate.of(2020, 1, 22), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan23220000 = LocalDateTime.of(LocalDate.of(2020, 1, 23), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan24220000 = LocalDateTime.of(LocalDate.of(2020, 1, 24), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan25220000 = LocalDateTime.of(LocalDate.of(2020, 1, 25), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan26220000 = LocalDateTime.of(LocalDate.of(2020, 1, 26), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan27220000 = LocalDateTime.of(LocalDate.of(2020, 1, 27), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan28220000 = LocalDateTime.of(LocalDate.of(2020, 1, 28), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan29220000 = LocalDateTime.of(LocalDate.of(2020, 1, 29), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan30220000 = LocalDateTime.of(LocalDate.of(2020, 1, 30), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan31220000 = LocalDateTime.of(LocalDate.of(2020, 1, 31), LocalTime.of(22, 0));

		ValueDateTupel valuedatetupel1 = new ValueDateTupel(localDateTimeJan01220000, 200d);
		ValueDateTupel valuedatetupel2 = new ValueDateTupel(localDateTimeJan02220000, 400d);
		ValueDateTupel valuedatetupel3 = new ValueDateTupel(localDateTimeJan03220000, 500d);
		ValueDateTupel valuedatetupel4 = new ValueDateTupel(localDateTimeJan04220000, 200d);
		ValueDateTupel valuedatetupel5 = new ValueDateTupel(localDateTimeJan05220000, 100d);
		ValueDateTupel valuedatetupel6 = new ValueDateTupel(localDateTimeJan06220000, 150d);
		ValueDateTupel valuedatetupel7 = new ValueDateTupel(localDateTimeJan07220000, 200d);
		ValueDateTupel valuedatetupel8 = new ValueDateTupel(localDateTimeJan08220000, 250d);
		ValueDateTupel valuedatetupel9 = new ValueDateTupel(localDateTimeJan09220000, 300d);
		ValueDateTupel valuedatetupel10 = new ValueDateTupel(localDateTimeJan10220000, 200d);
		ValueDateTupel valuedatetupel11 = new ValueDateTupel(localDateTimeJan11220000, 400d);
		ValueDateTupel valuedatetupel12 = new ValueDateTupel(localDateTimeJan12220000, 300d);
		ValueDateTupel valuedatetupel13 = new ValueDateTupel(localDateTimeJan13220000, 200d);
		ValueDateTupel valuedatetupel14 = new ValueDateTupel(localDateTimeJan14220000, 250d);
		ValueDateTupel valuedatetupel15 = new ValueDateTupel(localDateTimeJan15220000, 300d);
		ValueDateTupel valuedatetupel16 = new ValueDateTupel(localDateTimeJan16220000, 400d);
		ValueDateTupel valuedatetupel17 = new ValueDateTupel(localDateTimeJan17220000, 500d);
		ValueDateTupel valuedatetupel18 = new ValueDateTupel(localDateTimeJan18220000, 900d);
		ValueDateTupel valuedatetupel19 = new ValueDateTupel(localDateTimeJan19220000, 500d);
		ValueDateTupel valuedatetupel20 = new ValueDateTupel(localDateTimeJan20220000, 450d);
		ValueDateTupel valuedatetupel21 = new ValueDateTupel(localDateTimeJan21220000, 400d);
		ValueDateTupel valuedatetupel22 = new ValueDateTupel(localDateTimeJan22220000, 450d);
		ValueDateTupel valuedatetupel23 = new ValueDateTupel(localDateTimeJan23220000, 400d);
		ValueDateTupel valuedatetupel24 = new ValueDateTupel(localDateTimeJan24220000, 350d);
		ValueDateTupel valuedatetupel25 = new ValueDateTupel(localDateTimeJan25220000, 300d);
		ValueDateTupel valuedatetupel26 = new ValueDateTupel(localDateTimeJan26220000, 200d);
		ValueDateTupel valuedatetupel27 = new ValueDateTupel(localDateTimeJan27220000, 150d);
		ValueDateTupel valuedatetupel28 = new ValueDateTupel(localDateTimeJan28220000, 175d);
		ValueDateTupel valuedatetupel29 = new ValueDateTupel(localDateTimeJan29220000, 175d);
		ValueDateTupel valuedatetupel30 = new ValueDateTupel(localDateTimeJan30220000, 200d);
		ValueDateTupel valuedatetupel31 = new ValueDateTupel(localDateTimeJan31220000, 180d);

		ValueDateTupel[] values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);
		values = ArrayUtils.add(values, valuedatetupel5);
		values = ArrayUtils.add(values, valuedatetupel6);
		values = ArrayUtils.add(values, valuedatetupel7);
		values = ArrayUtils.add(values, valuedatetupel8);
		values = ArrayUtils.add(values, valuedatetupel9);
		values = ArrayUtils.add(values, valuedatetupel10);
		values = ArrayUtils.add(values, valuedatetupel11);
		values = ArrayUtils.add(values, valuedatetupel12);
		values = ArrayUtils.add(values, valuedatetupel13);
		values = ArrayUtils.add(values, valuedatetupel14);
		values = ArrayUtils.add(values, valuedatetupel15);
		values = ArrayUtils.add(values, valuedatetupel16);
		values = ArrayUtils.add(values, valuedatetupel17);
		values = ArrayUtils.add(values, valuedatetupel18);
		values = ArrayUtils.add(values, valuedatetupel19);
		values = ArrayUtils.add(values, valuedatetupel20);
		values = ArrayUtils.add(values, valuedatetupel21);
		values = ArrayUtils.add(values, valuedatetupel22);
		values = ArrayUtils.add(values, valuedatetupel23);
		values = ArrayUtils.add(values, valuedatetupel24);
		values = ArrayUtils.add(values, valuedatetupel25);
		values = ArrayUtils.add(values, valuedatetupel26);
		values = ArrayUtils.add(values, valuedatetupel27);
		values = ArrayUtils.add(values, valuedatetupel28);
		values = ArrayUtils.add(values, valuedatetupel29);
		values = ArrayUtils.add(values, valuedatetupel30);
		values = ArrayUtils.add(values, valuedatetupel31);

		return new BaseValue(name, values);
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
