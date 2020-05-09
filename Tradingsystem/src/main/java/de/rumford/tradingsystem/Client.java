package de.rumford.tradingsystem;

import java.io.IOException;

import de.rumford.tradingsystem.helper.CsvFormat;
import de.rumford.tradingsystem.helper.DataSource;
import de.rumford.tradingsystem.helper.ValueDateTupel;

public class Client {

	private Client() {
	}

	public static void main(String[] args) throws IOException {
		ValueDateTupel[] values = readValuesFromCsv("resources/DAX.csv", CsvFormat.EU);
		ValueDateTupel[] shortIndexValues = readValuesFromCsv("resources/DAX_short.csv", CsvFormat.EU);
		BaseValue baseValue = new BaseValue("DAX", values, shortIndexValues);

		ValueDateTupel[][] notAligned = { values, shortIndexValues };
		ValueDateTupel[][] aligned = ValueDateTupel.alignDates(notAligned);
	}

	private static ValueDateTupel[] readValuesFromCsv(String path, CsvFormat csvFormat) throws IOException {
		return DataSource.getDataFromCsv(path, csvFormat);
	}
}
