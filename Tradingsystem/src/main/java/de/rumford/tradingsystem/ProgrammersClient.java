package de.rumford.tradingsystem;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

import de.rumford.tradingsystem.helper.CsvFormat;
import de.rumford.tradingsystem.helper.DataSource;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * The ProgrammersClient is an example of how to use this library.
 * 
 * @author Max Rumford
 *
 */
public class ProgrammersClient {
	static final String WORKING_DIR = Path.of("src", "test", "resources")
			.toString();

	static final double CAPITAL = 10000;
	static final String BASE_VALUE_NAME = "DAX";
	static final String BASE_VALUE_FILE_NAME = Path
			.of(WORKING_DIR, "DAX - mod.csv").toString();
	static final String SHORT_INDEX_VALUE_FILE_NAME = Path
			.of(WORKING_DIR, "DAX_short - mod.csv").toString();

	static final LocalDateTime START_OF_REFERENCE_WINDOW = LocalDateTime
			.of(2019, 1, 2, 22, 0);
	static final LocalDateTime END_OF_REFERENCE_WINDOW = LocalDateTime.of(2019,
			12, 30, 22, 0);
	static final double BASE_SCALE = 10;

	static final int LOOKBACK_WINDOW_2 = 2;
	static final int LOOKBACK_WINDOW_4 = 4;
	static final int LOOKBACK_WINDOW_8 = 8;

	static BaseValue baseValue;

	static VolatilityDifference volDif2;
	static VolatilityDifference volDif4;
	static VolatilityDifference volDif8;
	static VolatilityDifference volDifTop;

	static EWMAC ewmacShort;
	static EWMAC ewmacMiddle;
	static EWMAC ewmacLong;
	static EWMAC ewmacTop;

	static SubSystem subSystem;

	private ProgrammersClient() {
	}

	public static void main(String[] args) throws IOException {
		ValueDateTupel[] shortIndexValues = DataSource
				.getDataFromCsv(SHORT_INDEX_VALUE_FILE_NAME, CsvFormat.EU);
		baseValue = new BaseValue(BASE_VALUE_NAME,
				DataSource.getDataFromCsv(BASE_VALUE_FILE_NAME, CsvFormat.EU),
				shortIndexValues);
		System.out.println("after base value");

		volDif2 = new VolatilityDifference(baseValue, null,
				START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW,
				LOOKBACK_WINDOW_2, BASE_SCALE);
		System.out.println("after voldif2");
		volDif4 = new VolatilityDifference(baseValue, null,
				START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW,
				LOOKBACK_WINDOW_4, BASE_SCALE);
		System.out.println("after voldif4");
		volDif8 = new VolatilityDifference(baseValue, null,
				START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW,
				LOOKBACK_WINDOW_8, BASE_SCALE);
		System.out.println("after voldif8");
		VolatilityDifference[] volDifVariations = { //
				volDif2, //
				volDif4, //
				volDif8 };
		volDifTop = new VolatilityDifference(baseValue, volDifVariations,
				START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW, 8,
				BASE_SCALE);
		System.out.println("after voldiftop");

		ewmacShort = new EWMAC(baseValue, null, START_OF_REFERENCE_WINDOW,
				END_OF_REFERENCE_WINDOW, 8, 2, BASE_SCALE);
		System.out.println("after ewmac short");
		ewmacMiddle = new EWMAC(baseValue, null, START_OF_REFERENCE_WINDOW,
				END_OF_REFERENCE_WINDOW, 16, 4, BASE_SCALE);
		System.out.println("after ewmac middle");
		ewmacLong = new EWMAC(baseValue, null, START_OF_REFERENCE_WINDOW,
				END_OF_REFERENCE_WINDOW, 32, 8, BASE_SCALE);
		System.out.println("after ewmac long");
		EWMAC[] ewmacVariations = { //
				ewmacShort, //
				ewmacMiddle, //
				ewmacLong };
		ewmacTop = new EWMAC(baseValue, ewmacVariations,
				START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW, 0, 0,
				BASE_SCALE);

		System.out.println(ValueDateTupel.getElement(baseValue.getValues(),
				END_OF_REFERENCE_WINDOW));

		Rule[] rules = { volDifTop, ewmacTop };

		subSystem = new SubSystem(baseValue, rules, CAPITAL, BASE_SCALE);

	}

}
