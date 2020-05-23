package de.rumford.tradingsystem.helper;

/**
 * Comma separated files can have different formats. This enumeration represents
 * four combinations of EU and US CSVs, each combined with a different order in
 * date values.
 * 
 * @author Max Rumford
 *
 */
public enum CsvFormat {
	EU(";", ".", ":", ",", ".", DateOrder.DAY_MONTH_YEAR),
	EU_YEAR_MONTH_DAY(";", ".", ":", ",", ".", DateOrder.YEAR_MONTH_DAY),
	US(",", "/", ":", ".", ",", DateOrder.MONTH_DAY_YEAR),
	US_YEAR_MONTH_DAY(",", "/", ":", ".", ",", DateOrder.YEAR_MONTH_DAY);

	private final String fieldSeparator;
	private final String dateSeparator;
	private final String timeSeparator;
	private final String decimalPoint;
	private final String thousandsSeparator;
	private final DateOrder dateOrder;

	CsvFormat(String fieldSeparator, String dateSeparator, String timeSeparator, String decimalPoint,
			String thousandSeparator, DateOrder dateOrder) {
		this.fieldSeparator = fieldSeparator;
		this.dateSeparator = dateSeparator;
		this.timeSeparator = timeSeparator;
		this.decimalPoint = decimalPoint;
		this.thousandsSeparator = thousandSeparator;
		this.dateOrder = dateOrder;
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

	/**
	 * @return dateOrder CsvFormat
	 */
	public DateOrder getMonthDayOrder() {
		return dateOrder;
	}
}
