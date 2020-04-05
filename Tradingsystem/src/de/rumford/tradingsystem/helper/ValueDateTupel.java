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
	 * Constructor the the {@code ValueDateTupel} class
	 */
	public ValueDateTupel(LocalDateTime date, double value) {
		this.setDate(date);
		this.setValue(value);
	}
	
	/**
	 * ==================
	 * OVERRIDDEN METHODS
	 * ==================
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
	 * ===================
	 * GETTERS AND SETTERS
	 * ===================
	 */
	/**
	 * Get the value of a {@code ValueDateTupel}
	 * @return {@code double} value of the {@code ValueDateTupel}
	 */
	public double getValue() {
		return value;
	}
	/**
	 * Set the value of a {@code ValueDateTupel}
	 * @param value {@code double} value to be set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Get the date of a {@code ValueDateTupel}
	 * @return {@code LocalDateTime} date for the {@code ValueDateTupel}
	 */
	public LocalDateTime getDate() {
		return date;
	}
	/**
	 * Set the date for a {@code ValueDateTupel}
	 * @param date {@code LocalDateTime} date to be set for the {@code ValueDateTupel}
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

}
