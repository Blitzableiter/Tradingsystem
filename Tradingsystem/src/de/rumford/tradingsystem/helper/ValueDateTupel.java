/**
 * 
 */
package de.rumford.tradingsystem.helper;

import java.time.LocalDateTime;

/**
 * @author Max Rumford
 *
 */
public class ValueDateTupel {

	private double value;
	private LocalDateTime date;

	/**
	 * Creates a new {@code ValueDateTupel} instance using... TODO
	 * 
	 * @param date  {@code LocalDateTime} The dateTime to be set for this
	 *              {@code ValueDateTupel}
	 * @param value {@code double} The value to be set for this
	 *              {@code ValueDateTupel}
	 */
	public ValueDateTupel(LocalDateTime date, double value) {
		this.setDate(date);
		this.setValue(value);
	}

	/**
	 * Creates an empty array of {@code ValueDateTupel}.
	 * 
	 * @return {@code ValueDateTupel[]} An Empty array of {@code ValueDateTupel}.
	 */
	public static ValueDateTupel[] createEmptyArray() {
		return new ValueDateTupel[0];
	}

	/**
	 * Append a {@code ValueDateTupel} to an array of {@code ValueDateTupel}.
	 * 
	 * @param valueDateTupels {@code ValueDateTupel[]} Array of
	 *                        {@code ValueDateTupel} an element should be appended
	 *                        to.
	 * @param newElement      {@code ValueDateTupel} to be appended to the given
	 *                        array.
	 * @return {@code ValueDateTupel[]) Passed array with the passed Element in the last index. @deprecated
	 *         Use {@link org.apache.commons.lang3.ArrayUtils#add(Object[], Object)}
	 *         instead
	 */
	public static ValueDateTupel[] appendElement(ValueDateTupel[] valueDateTupels, ValueDateTupel newElement) {
		ValueDateTupel[] returnValueDateTupels = new ValueDateTupel[valueDateTupels.length + 1];

		for (int i = 0; i < valueDateTupels.length; i++)
			returnValueDateTupels[i] = valueDateTupels[i];

		returnValueDateTupels[returnValueDateTupels.length - 1] = newElement;

		return returnValueDateTupels;
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
	 * Get the value of a {@code ValueDateTupel}
	 * 
	 * @return {@code double} value of the {@code ValueDateTupel}
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Set the value of a {@code ValueDateTupel}
	 * 
	 * @param value {@code double} value to be set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Get the date of a {@code ValueDateTupel}
	 * 
	 * @return {@code LocalDateTime} date for the {@code ValueDateTupel}
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * Set the date for a {@code ValueDateTupel}
	 * 
	 * @param date {@code LocalDateTime} date to be set for the
	 *             {@code ValueDateTupel}
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

}
