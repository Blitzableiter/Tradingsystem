/**
 * 
 */
package de.rumford.tradingsystem;

import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * de.rumford.tradingsystem
 * 
 * @author Max Rumford
 *
 */
public class BaseValue {

	private String name;
	private ValueDateTupel[] values;
	private ValueDateTupel[] shortIndexValues;

	public BaseValue() {
//		TODO
//		this.validateInput();
	}

	public BaseValue(String name, ValueDateTupel[] values) {
		this();
		this.setName(name);
		this.setValues(values);
	}

	/**
	 * 
	 */
	public BaseValue(String name, ValueDateTupel[] values, ValueDateTupel[] shortIndexValues) {
		this(name, values);
		this.setShortIndexValues(shortIndexValues);
	}

	private ValueDateTupel[] calculateShortIndexValues(ValueDateTupel[] values) {
		ValueDateTupel[] shortIndexValues = new ValueDateTupel[values.length];
		shortIndexValues[0].setValue(1000d);

		ValueDateTupel formerValue = values[0];
		ValueDateTupel latterValue;

		for (int i = 0; i < values.length; i++) {
			latterValue = values[i];

			double returnValue = Util.calculateReturn(formerValue.getValue(), latterValue.getValue());

			/**
			 * The short index decreases by the same percentage the base value increases. If
			 * the base value increases by 10%, the short index decreases by 10% and vice
			 * versa.
			 * 
			 * Formula: v_s,t = v_s,t-1 + v_s,t-1 * return_t,t-1
			 */
			shortIndexValues[i]
					.setValue(shortIndexValues[i - 1].getValue() + shortIndexValues[i - 1].getValue() * returnValue);
		}

		return shortIndexValues;
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

}
