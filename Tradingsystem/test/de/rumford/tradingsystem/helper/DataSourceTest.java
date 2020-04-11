/**
 * 
 */
package de.rumford.tradingsystem.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * de.rumford.tradingsystem.helper
 * 
 * @author Max Rumford
 *
 */
class DataSourceTest {

	final static String FILE_NAME_CORRECT_FILE_EUR = "TEST_FILE_CORRECT_EUR.CSV";
	final static String FILE_NAME_CORRECT_FILE_US = "TEST_FILE_CORRECT_US.CSV";
	final static String FILE_NAME_NULL = null;
	final static String FILE_NAME_UNKOWN = "TEST_FILE_UNKNOWN.CSV";
	final static String FILE_NAME_DIRECTORY = "TEST_FILE_IS_DIRECTORY";
	final static String FILE_NAME_FILE_HAS_HEADINGS = "TEST_FILE_HAS_HEADINGS.CSV";
	final static String FILE_NAME_FOUR_COLUMNS = "TEST_FILE_FOUR_COLUMNS.CSV";
	final static String FILE_NAME_DAY_NON_INTEGER = "TEST_FILE_DAY_NON_INTEGER.CSV";
	final static String FILE_NAME_MONTH_NON_INTEGER = "TEST_FILE_MONTH_NON_INTEGER.CSV";
	final static String FILE_NAME_YEAR_NON_INTEGER = "TEST_FILE_YEAR_NON_INTEGER.CSV";
	final static String FILE_NAME_HOUR_NON_INTEGER = "TEST_FILE_HOUR_NON_INTEGER.CSV";
	final static String FILE_NAME_MINUTE_NON_INTEGER = "TEST_FILE_MINUTE_NON_INTEGER.CSV";
	final static String FILE_NAME_SECOND_NON_INTEGER = "TEST_FILE_SECOND_NON_INTEGER.CSV";
	final static String FILE_NAME_DATE_VALUE_OUT_OF_RANGE = "TEST_FILE_DATE_VALUE_OUT_OF_RANGE.CSV";
	final static String FILE_NAME_COURSE_VALUE_INVALID = "TEST_FILE_COURSE_VALUE_INVALID.CSV";
	static String[] FILE_NAMES = { //
			FILE_NAME_CORRECT_FILE_EUR, //
			FILE_NAME_CORRECT_FILE_US, //
			FILE_NAME_DIRECTORY, //
			FILE_NAME_FILE_HAS_HEADINGS, //
			FILE_NAME_FOUR_COLUMNS, //
			FILE_NAME_DAY_NON_INTEGER, //
			FILE_NAME_MONTH_NON_INTEGER, //
			FILE_NAME_YEAR_NON_INTEGER, //
			FILE_NAME_HOUR_NON_INTEGER, //
			FILE_NAME_MINUTE_NON_INTEGER, //
			FILE_NAME_SECOND_NON_INTEGER, //
			FILE_NAME_DATE_VALUE_OUT_OF_RANGE, //
			FILE_NAME_COURSE_VALUE_INVALID,//
	};

	static BufferedWriter bw_eu_ok;
	static BufferedWriter bw_us_ok;
	static BufferedWriter bw_headings;
	static BufferedWriter bw_four_columns;
	static BufferedWriter bw_day_non_integer;
	static BufferedWriter bw_month_non_integer;
	static BufferedWriter bw_year_non_integer;
	static BufferedWriter bw_hour_non_integer;
	static BufferedWriter bw_minute_non_integer;
	static BufferedWriter bw_second_non_integer;
	static BufferedWriter bw_date_value_out_of_range;
	static BufferedWriter bw_course_value_invalid;

	static BufferedWriter[] bufferedWriters = {};

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		bw_eu_ok = new BufferedWriter(new FileWriter(new File(FILE_NAME_CORRECT_FILE_EUR)));
		bw_us_ok = new BufferedWriter(new FileWriter(new File(FILE_NAME_CORRECT_FILE_US)));
		/*
		 * No Writer for FILE_NAME_NULL and FILE_NAME_UNKOWN
		 */
		new File(FILE_NAME_DIRECTORY).mkdirs();
		bw_headings = new BufferedWriter(new FileWriter(new File(FILE_NAME_FILE_HAS_HEADINGS)));
		bw_four_columns = new BufferedWriter(new FileWriter(new File(FILE_NAME_FOUR_COLUMNS)));
		bw_day_non_integer = new BufferedWriter(new FileWriter(new File(FILE_NAME_DAY_NON_INTEGER)));
		bw_month_non_integer = new BufferedWriter(new FileWriter(new File(FILE_NAME_MONTH_NON_INTEGER)));
		bw_year_non_integer = new BufferedWriter(new FileWriter(new File(FILE_NAME_YEAR_NON_INTEGER)));
		bw_hour_non_integer = new BufferedWriter(new FileWriter(new File(FILE_NAME_HOUR_NON_INTEGER)));
		bw_minute_non_integer = new BufferedWriter(new FileWriter(new File(FILE_NAME_MINUTE_NON_INTEGER)));
		bw_second_non_integer = new BufferedWriter(new FileWriter(new File(FILE_NAME_SECOND_NON_INTEGER)));
		bw_date_value_out_of_range = new BufferedWriter(new FileWriter(new File(FILE_NAME_DATE_VALUE_OUT_OF_RANGE)));
		bw_course_value_invalid = new BufferedWriter(new FileWriter(new File(FILE_NAME_COURSE_VALUE_INVALID)));

		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_eu_ok);
		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_us_ok);
		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_headings);
		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_four_columns);
		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_day_non_integer);
		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_month_non_integer);
		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_year_non_integer);
		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_hour_non_integer);
		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_minute_non_integer);
		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_second_non_integer);
		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_date_value_out_of_range);
		bufferedWriters = ArrayUtils.add(bufferedWriters, bw_course_value_invalid);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		for (String fileName : FILE_NAMES) {
			File file = new File(fileName);
			// TODO FIX ME
			System.out.println(file.delete());
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_EU_ok() {
		String line1 = "01.01.1981;22:00:00;480,92";
		String line2 = "02.01.1981;22:00:00;490,04";
		String line3 = "05.01.1981;22:00:00;493,05";
		String line4 = "06.01.1981;22:00:00;494,97";
		String line5 = "07.01.1981;22:00:00;489,89";
		String line6 = "08.01.1981;22:00:00;489,32";
		final int expectedValue = 6;
		try {
			bw_eu_ok.write(line1);
			bw_eu_ok.newLine();
			bw_eu_ok.write(line2);
			bw_eu_ok.newLine();
			bw_eu_ok.write(line3);
			bw_eu_ok.newLine();
			bw_eu_ok.write(line4);
			bw_eu_ok.newLine();
			bw_eu_ok.write(line5);
			bw_eu_ok.newLine();
			bw_eu_ok.write(line6);
			bw_eu_ok.newLine();
			bw_eu_ok.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		ValueDateTupel[] values = {};
		try {
			values = DataSource.getDataFromCsv(FILE_NAME_CORRECT_FILE_EUR, CsvFormat.EU);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("IllegalArgumentException getting Data");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException getting Data");
		}

		final int actualValue = values.length;

		assertEquals(expectedValue, actualValue, "Number of read files are not correct");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_US_ok() {
		String line1 = "01/01/1981,22:00:00,480.92";
		String line2 = "02/01/1981,22:00:00,490.04";
		String line3 = "05/01/1981,22:00:00,493.05";
		String line4 = "06/01/1981,22:00:00,494.97";
		String line5 = "07/01/1981,22:00:00,489.89";
		String line6 = "08/01/1981,22:00:00,489.32";
		final int expectedValue = 6;
		try {
			bw_us_ok.write(line1);
			bw_us_ok.newLine();
			bw_us_ok.write(line2);
			bw_us_ok.newLine();
			bw_us_ok.write(line3);
			bw_us_ok.newLine();
			bw_us_ok.write(line4);
			bw_us_ok.newLine();
			bw_us_ok.write(line5);
			bw_us_ok.newLine();
			bw_us_ok.write(line6);
			bw_us_ok.newLine();
			bw_us_ok.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		ValueDateTupel[] values = {};
		try {
			values = DataSource.getDataFromCsv(FILE_NAME_CORRECT_FILE_US, CsvFormat.US);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("IllegalArgumentException getting Data");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException getting Data");
		}

		final int actualValue = values.length;

		assertEquals(expectedValue, actualValue, "Number of read files are not correct");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_fileName_null() {
		String expectedValue = "The given path cannot be processed";
		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_NULL, CsvFormat.US),
				"null file name is not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_nonExistant_file() {
		String expectedValue = "Given source path does not point to an existing destination";
		Exception thrown = assertThrows(IOException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_UNKOWN, CsvFormat.US),
				"Unknown file name is not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_directory() {
		String expectedValue = "Given source path does not point to a file";
		Exception thrown = assertThrows(IOException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_DIRECTORY, CsvFormat.US),
				"Directory as file name is not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_headings() {
		String line0 = "DATE,TIME,VALUE";
		String line1 = "01/01/1981,22:00:00,480.92";
		try {
			bw_headings.write(line0);
			bw_headings.newLine();
			bw_headings.write(line1);
			bw_headings.newLine();
			bw_headings.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		assertThrows(Exception.class, () -> DataSource.getDataFromCsv(FILE_NAME_FILE_HAS_HEADINGS, CsvFormat.US),
				"Headings in CSV are not properly handled");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_four_columns() {
		String line1 = "01/01/1981,22:00:00,480.92,x";
		String expectedValue = "The passed CSV does not have an appropriate number of columns";
		try {
			bw_four_columns.write(line1);
			bw_four_columns.newLine();
			bw_four_columns.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_FOUR_COLUMNS, CsvFormat.US),
				"Headings in CSV are not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_day_non_integer() {
		String line1 = "01/0z/1981,22:00:00,480.92";
		String expectedValue = "The date values of the read CSV file cannot be parsed into numbers. Failing value >01/0z/1981<";
		try {
			bw_day_non_integer.write(line1);
			bw_day_non_integer.newLine();
			bw_day_non_integer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_DAY_NON_INTEGER, CsvFormat.US),
				"Unparsable day in date field is not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_month_non_integer() {
		String line1 = "0z/01/1981,22:00:00,480.92";
		String expectedValue = "The date values of the read CSV file cannot be parsed into numbers. Failing value >0z/01/1981<";
		try {
			bw_month_non_integer.write(line1);
			bw_month_non_integer.newLine();
			bw_month_non_integer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_MONTH_NON_INTEGER, CsvFormat.US),
				"Unparsable month in date field is not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_year_non_integer() {
		String line1 = "01/01/19q1,22:00:00,480.92";
		String expectedValue = "The date values of the read CSV file cannot be parsed into numbers. Failing value >01/01/19q1<";
		try {
			bw_year_non_integer.write(line1);
			bw_year_non_integer.newLine();
			bw_year_non_integer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_YEAR_NON_INTEGER, CsvFormat.US),
				"Unparsable year in date field is not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_hour_non_integer() {
		String line1 = "01/01/1981,2x:00:00,480.92";
		String expectedValue = "The time values of the read CSV file cannot be parsed into numbers. Failing value >2x:00:00<";
		try {
			bw_hour_non_integer.write(line1);
			bw_hour_non_integer.newLine();
			bw_hour_non_integer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_HOUR_NON_INTEGER, CsvFormat.US),
				"Unparsable hour in time field is not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_minute_non_integer() {
		String line1 = "01/01/1981,22:x0:00,480.92";
		String expectedValue = "The time values of the read CSV file cannot be parsed into numbers. Failing value >22:x0:00<";
		try {
			bw_minute_non_integer.write(line1);
			bw_minute_non_integer.newLine();
			bw_minute_non_integer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_MINUTE_NON_INTEGER, CsvFormat.US),
				"Unparsable minute in time field is not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_second_non_integer() {
		String line1 = "01/01/1981,22:00:0x,480.92";
		String expectedValue = "The time values of the read CSV file cannot be parsed into numbers. Failing value >22:00:0x<";
		try {
			bw_second_non_integer.write(line1);
			bw_second_non_integer.newLine();
			bw_second_non_integer.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_SECOND_NON_INTEGER, CsvFormat.US),
				"Unparsable second in time field is not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_date_out_of_range() {
		String line1 = "15/32/1981,22:00:00,480.92";
		String expectedValue = "The date or time values of the read CSV file cannot be parsed into a LocalDateTime instance. Failing values >15/32/1981< and >22:00:00<.";
		try {
			bw_date_value_out_of_range.write(line1);
			bw_date_value_out_of_range.newLine();
			bw_date_value_out_of_range.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_DATE_VALUE_OUT_OF_RANGE, CsvFormat.US),
				"Out of range date is not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

	/**
	 * Test method for
	 * {@link de.rumford.tradingsystem.helper.DataSource#getDataFromCsv(java.lang.String, de.rumford.tradingsystem.helper.CsvFormat)}.
	 */
	@Test
	void testGetDataFromCsv_course_value_invalid() {
		String line1 = "01/01/1981,22:00:00,14x0.92";
		String expectedValue = "The course value >14x0.92< cannot be parsed";
		try {
			bw_course_value_invalid.write(line1);
			bw_course_value_invalid.newLine();
			bw_course_value_invalid.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("File could not be written");
		}

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> DataSource.getDataFromCsv(FILE_NAME_COURSE_VALUE_INVALID, CsvFormat.US),
				"Out of range date is not properly handled");
		assertEquals(expectedValue, thrown.getMessage(), "Incorrect Exception message");
	}

}