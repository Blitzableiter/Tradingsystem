/**
 * 
 */
package de.rumford.tradingsystem.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

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
	 * {@link CsvFormat} has to be passed. The underlying enum is not final and can
	 * be altered to suit the user's needs.
	 * <p>
	 * The CSV file to be parsed is expected not to have column headings. If so, the
	 * values of the first row might not be parsed and an IllegalArgumentException
	 * as explained below might be thrown. If the row can be parsed it will most
	 * likely not contain any useful information and might result in incorrect
	 * calculation results. The CSV file should always be cleared of headings.
	 * 
	 * @param sourcePath {@code String} The path to the CSV file to be read.
	 * @param format     {@link CsvFormat} The format of the CSV file.
	 * @return {@link ValueDateTupel[]} An array of {@link ValueDateTupel}
	 *         representing the read data.
	 * @throws FileNotFoundException    if the FileReader can not find a file for
	 *                                  the given {@code sourcePath}.
	 * @throws IOException              if the given {@code sourcePath} cannot be
	 *                                  properly resolved to an actual file.
	 * @throws IllegalArgumentException if the given path is invalid.
	 * @throws IllegalArgumentException if any of the rows in the read CSV file does
	 *                                  not contain exactly 3 columns.
	 */
	public static ValueDateTupel[] getDataFromCsv(String sourcePath, CsvFormat format) throws IOException {
		File file;
		try {
			file = new File(sourcePath);
		} catch (Exception e) {
			throw new IllegalArgumentException("The given path cannot be processed");
		}

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
			String[] columns = line.split(Pattern.quote(format.getFieldSeparator()));

			if (columns.length != 3) {
				br.close();
				throw new IllegalArgumentException("The passed CSV does not have an appropriate number of columns");
			}

			/*
			 * Parse the first and second field (date, time) into a LocalDateTime instance
			 */
			String[] dateAndTimeStrings = new String[2];
			System.arraycopy(columns, 0, dateAndTimeStrings, 0, 2);
			LocalDateTime localDateTime;
			try {
				localDateTime = parseLocalDateTime(dateAndTimeStrings, format);
			} catch (IllegalArgumentException e) {
				br.close();
				throw e;
			}
			/*
			 * Catch Exception so BufferedReader can be closed on unknown Exceptions to
			 * memory avoid leakage.
			 */
			catch (Exception e) {
				br.close();
				throw e;
			}

			/* Pass the third field (course value) into a double */
			String[] valueStrings = new String[1];
			System.arraycopy(columns, 2, valueStrings, 0, 1);
			double value;
			try {
				value = parseCourseValue(valueStrings, format);
			} catch (IllegalArgumentException e) {
				br.close();
				throw e;
			}
			/*
			 * Catch Exception so BufferedReader can be closed on unknown Exceptions to
			 * memory avoid leakage.
			 */
			catch (Exception e) {
				br.close();
				throw e;
			}

			ValueDateTupel newElement = new ValueDateTupel(localDateTime, value);

			returnValues = ArrayUtils.add(returnValues, newElement);
		}

		br.close();
		return returnValues;
	}

	/**
	 * Parse the given columns {date, time} into a {@link LocalDateTime} instance.
	 * Expects an array of Strings of length 2.
	 * 
	 * @param columns {@code String[]} The columns containing the String
	 *                representing date and time.
	 * @param format  {@link CsvFormat} The {@link CsvFormat} representing the given
	 *                CSV file.
	 * @return {@link LocalDateTime} representation of the passed {date, time}
	 *         columns.
	 * @throws IllegalArgumentException If there are not exactly two columns in the
	 *                                  passed {@code String[]}.
	 * @throws IllegalArgumentException If the date pattern could not be recognized
	 *                                  in subroutine
	 *                                  {@link de.rumford.tradingsystem.helper.DataSource#DataSource.evaluateDatePattern(CsvFormat)}.
	 * @throws IllegalArgumentException If the given date values cannot be parsed to
	 *                                  Integers.
	 * @throws IllegalArgumentException If the given time values cannot be parsed to
	 *                                  Integers.
	 * @throws IllegalArgumentException If the given date and time values cannot be
	 *                                  parsed to {@link LocalDateTime}}.
	 */
	private static LocalDateTime parseLocalDateTime(String[] columns, CsvFormat format)
			throws IllegalArgumentException {
		if (columns.length != 2)
			throw new IllegalArgumentException("Given date and time cannot be parsed into LocalDateTime");

		/* Extract the relevant date values */
		String[] date = columns[0].split(Pattern.quote(format.getDateSeparator()));

		/* Evaluate the date pattern */
		int[] datePositions = new int[3];
		try {
			datePositions = evaluateDatePattern(format);
		} catch (IllegalArgumentException e) {
			throw e;
		}

		int dayOfMonth;
		int month;
		int year;
		try {
			dayOfMonth = Integer.parseInt(date[datePositions[0]]);
			month = Integer.parseInt(date[datePositions[1]]);
			year = Integer.parseInt(date[datePositions[2]]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"The date values of the read CSV file cannot be parsed into numbers. Failing value >" + columns[0]
							+ "<");
		}
		/*
		 * Catch Exception so BufferedReader can be closed (in calling method) on
		 * unknown Exceptions to memory avoid leakage.
		 */
		catch (Exception e) {
			throw e;
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
			throw new IllegalArgumentException(
					"The time values of the read CSV file cannot be parsed into numbers. Failing value >" + columns[1]
							+ "<");
		}

		LocalDateTime localDateTime;
		try {
			localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"The date or time values of the read CSV file cannot be parsed into a LocalDateTime instance. Failing values >"
							+ columns[0] + "< and >" + columns[1] + "<.");
		}
		return localDateTime;
	}

	/**
	 * Evaluate the date pattern from the given {@link CsvFormat}. If
	 * {@link CsvFormat} is extended this method has to be overridden, else an
	 * {@link IllegalArgumentException} will be thrown due to an unknown format.
	 * 
	 * @param format {@link CsvFormat} Format of the CSV file to be parsed.
	 * @return {@code int[]} containing the position of {day, month, year} values
	 *         inside the date field of the given CSV.
	 * @throws IllegalArgumentException if the date given date pattern is not
	 *                                  recognized.
	 */
	public static int[] evaluateDatePattern(CsvFormat format) throws IllegalArgumentException {
		int monthPosition, dayPosition, yearPosition;
		if (format.getMonthDayOrder() == MonthDayOrder.DAY_MONTH_YEAR) {
			dayPosition = 0;
			monthPosition = 1;
			yearPosition = 2;
		} else if (format.getMonthDayOrder() == MonthDayOrder.MONTH_DAY_YEAR) {
			dayPosition = 1;
			monthPosition = 0;
			yearPosition = 2;
		} else if (format.getMonthDayOrder() == MonthDayOrder.YEAR_MONTH_DAY) {
			dayPosition = 2;
			monthPosition = 1;
			yearPosition = 0;
		} else {
			throw new IllegalArgumentException("Date pattern not recognzied");
		}

		int[] returnArray = { dayPosition, monthPosition, yearPosition };

		return returnArray;
	}

	/**
	 * Parse the course value String into a {@code double} representing its values.
	 * 
	 * @param columns {@code String[]} The String representation of the CSV column
	 *                containing the value.
	 * @param format  {@link CsvFormat} Format the CSV file is in.
	 * @return {@code double} The parsed value.
	 * @throws IllegalArgumentException if the passed String cannot be properly
	 *                                  parsed
	 */
	private static double parseCourseValue(String[] columns, CsvFormat format) throws IllegalArgumentException {
		String valueString = columns[0];
		/* Eliminate thousands separator from String */
		if (valueString.contains(format.getThousandsSeparator())) {
			valueString = valueString.replaceAll(Pattern.quote(format.getThousandsSeparator()), "");
		}
		/* Replace non-US decimal points with US decimal points */
		if (format.getDecimalPoint() != CsvFormat.US.getDecimalPoint()) {
			valueString = valueString.replace(format.getDecimalPoint(), CsvFormat.US.getDecimalPoint());
		}

		double value;
		/* At this point all hindering sings have been eradicated from the String */
		try {
			value = Double.parseDouble(valueString);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("The course value >" + columns[0] + "< cannot be parsed");
		} catch (Exception e) {
			throw e;
		}

		return value;
	}

}