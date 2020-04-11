/**
 * 
 */
package de.rumford.tradingsystem.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.ArrayUtils;

/**
 * de.rumford.tradingsystem.helper
 * 
 * @author Max Rumford
 *
 */
public class DataSource {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private DataSource() {
	}

	/**
	 * Reads the data from a given CSV path. Assumes the following "columns": Date,
	 * Time, value. Depending on the formatting of the CSV file the corresponding
	 * {@link CsvFormat} has to be passed. The underlying Enum is not final and can
	 * be altered to suit the user's needs.
	 * 
	 * @param sourcePath {@code String} The path to the CSV file to be read.
	 * @param format     {@link CsvFormat} The format of the CSV file.
	 * @return {@link ValueDateTupel[]} An array of {@link ValueDateTupel}
	 *         representing the read data.
	 * @throws IOException If the given {@code sourcePath} cannot be properly
	 *                     resolved to an actual file.
	 */
	public static ValueDateTupel[] getDataFromCsv(String sourcePath, CsvFormat format)
			throws IOException, IllegalArgumentException {
		File file = new File(sourcePath);
		if (!file.exists())
			throw new IOException("Given source path does not point to an existing destination");
		if (!file.isFile())
			throw new IOException("Given source path does not point to a file");
		if (!file.canRead())
			throw new IOException("Given file path cannot be read");

		BufferedReader br = new BufferedReader(new FileReader(file));

		String line;
		ValueDateTupel[] returnValues = ValueDateTupel.createEmptyArray();
		while ((line = br.readLine()) != null) {
			/* Extract the fields into separate Strings */
			String[] columns = line.split(format.getFieldSeparator());

			/*
			 * Parse the first and second field (date, time) into a LocalDateTime instance
			 */
			String[] dateAndTimeStrings = {};
			System.arraycopy(columns, 0, dateAndTimeStrings, 0, 2);
			LocalDateTime localDateTime;
			try {
				localDateTime = parseLocalDateTime(dateAndTimeStrings, format);
			} catch (IllegalArgumentException e) {
				throw e;
			}

			/* Pass the third field (course value) into a double */
			String[] valueStrings = {};
			System.arraycopy(columns, 2, valueStrings, 0, 1);
			double value;
			try {
				value = parseCourseValue(valueStrings, format);
			} catch (IllegalArgumentException e) {
				throw e;
			}

			ValueDateTupel newElement = new ValueDateTupel(localDateTime, value);

			ArrayUtils.add(returnValues, newElement);
		}

		br.close();
		return returnValues;
	}

	private static LocalDateTime parseLocalDateTime(String[] columns, CsvFormat format)
			throws IllegalArgumentException {
		/* Extract the relevant date values */
		String[] date = columns[0].split(format.getDateSeparator());
		int year;
		int month;
		int dayOfMonth;
		try {
			year = Integer.parseInt(date[0]);
			month = Integer.parseInt(date[1]);
			dayOfMonth = Integer.parseInt(date[2]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("The date values of the read CSV file cannot be parsed into numbers");
		}
		
		/* Extract the relevant time values */
		String[] time = columns[1].split(format.getTimeSeparator());
		int hour;
		int minute;
		int second;
		try {
			hour = Integer.parseInt(time[0]);
			minute = Integer.parseInt(time[1]);
			second = Integer.parseInt(time[2]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("The time values of the read CSV file cannot be parsed into numbers");
		}

		LocalDateTime localDateTime;
		try {
			localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"The date or time values of the read CSV file cannot be parsed into a LocalDateTime instance");
		}

		return localDateTime;
	}

	private static double parseCourseValue(String[] columns, CsvFormat format) throws IllegalArgumentException {
		String valueString = columns[0];
		/* Eliminate thousands separator from String */
		if (valueString.contains(format.getThousandsSeparator()))
			valueString.replace(format.getThousandsSeparator(), "");
		/* Replace non-US decimal points with US decimal points */
		if (format.getDecimalPoint() != CsvFormat.US.getDecimalPoint())
			valueString.replace(format.getDecimalPoint(), CsvFormat.US.getDecimalPoint());

		double value;
		/* At this point all hindering sings have been eradicated from the String */
		try {
			value = Double.parseDouble(valueString);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("The course value of the read CSV file cannot be parsed");
		}

		return value;
	}

}
