/**
 * 
 */
package de.rumford.tradingsystem.helper;

/**
 * de.rumford.tradingsystem.helper
 * 
 * @author Max Rumford
 *
 */
public enum CsvFormat {
	EU(";", ".", ":", ",", "."), US(",", "/", ":", ".", ",");

	private final String fieldSeparator;
	private final String dateSeparator;
	private final String timeSeparator;
	private final String decimalPoint;
	private final String thousandsSeparator;

	CsvFormat(String fieldSeparator, String dateSeparator, String timeSeparator, String decimalPoint,
			String thousandSeparator) {
		this.fieldSeparator = fieldSeparator;
		this.dateSeparator = dateSeparator;
		this.timeSeparator = timeSeparator;
		this.decimalPoint = decimalPoint;
		this.thousandsSeparator = thousandSeparator;
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */
	/**
	 * @return fieldSeparator CsvFormat
	 */
	public String getFieldSeparator() {
		return fieldSeparator;
	}

	/**
	 * @return dateSeparator CsvFormat
	 */
	public String getDateSeparator() {
		return dateSeparator;
	}

	/**
	 * @return timeSeparator CsvFormat
	 */
	public String getTimeSeparator() {
		return timeSeparator;
	}

	/**
	 * @return decimalPoint CsvFormat
	 */
	public String getDecimalPoint() {
		return decimalPoint;
	}

	/**
	 * @return thousandSeparator CsvFormat
	 */
	public String getThousandsSeparator() {
		return thousandsSeparator;
	}

}
