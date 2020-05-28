package de.rumford.tradingsystem;

import java.io.IOException;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

import org.apache.log4j.Logger;

import de.rumford.tradingsystem.helper.CsvFormat;
import de.rumford.tradingsystem.helper.DataSource;
import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * The ExampleClient is an example of how to use this library.
 * 
 * @author Max Rumford
 *
 */
public class ExampleClient {
  static final String WORKING_DIR = Path.of("src", "test", "resources")
      .toString();

  static final double CAPITAL = 10000;
  static final String BASE_VALUE_NAME = "DAX";
  static final String BASE_VALUE_FILE_NAME = Path
      .of(WORKING_DIR, "DAX - mod.csv").toString();
  static final String SHORT_INDEX_VALUE_FILE_NAME = Path
      .of(WORKING_DIR, "DAX_short - mod.csv").toString();

  static final LocalDateTime START_OF_REFERENCE_WINDOW = LocalDateTime
      .of(2014, 1, 2, 22, 0);
  static final LocalDateTime END_OF_REFERENCE_WINDOW = LocalDateTime
      .of(2018, 12, 28, 22, 0);
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

  static final LocalDateTime START_OF_TEST_WINDOW = LocalDateTime.of(2019,
      1, 2, 22, 0);
  static final LocalDateTime END_OF_TEST_WINDOW = LocalDateTime.of(2019,
      12, 30, 22, 0);

  private static final Logger logger = Logger
      .getLogger(ExampleClient.class);

  private ExampleClient() {
  }

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    ValueDateTupel[] shortIndexValues = DataSource
        .getDataFromCsv(SHORT_INDEX_VALUE_FILE_NAME, CsvFormat.EU);
    baseValue = new BaseValue(BASE_VALUE_NAME,
        DataSource.getDataFromCsv(BASE_VALUE_FILE_NAME, CsvFormat.EU),
        shortIndexValues);
    logger.info("BaseValue " + baseValue.getName() + " created.");

    volDif2 = new VolatilityDifference(baseValue, null,
        START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW,
        LOOKBACK_WINDOW_2, BASE_SCALE);
    logger.info(
        "VolatilityDifference with lookback window " + LOOKBACK_WINDOW_2
            + " for BaseValue " + baseValue.getName() + " created.");

    volDif4 = new VolatilityDifference(baseValue, null,
        START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW,
        LOOKBACK_WINDOW_4, BASE_SCALE);
    logger.info(
        "VolatilityDifference with lookback window " + LOOKBACK_WINDOW_4
            + " for BaseValue " + baseValue.getName() + " created.");

    volDif8 = new VolatilityDifference(baseValue, null,
        START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW,
        LOOKBACK_WINDOW_8, BASE_SCALE);
    logger.info(
        "VolatilityDifference with lookback window " + LOOKBACK_WINDOW_8
            + " for BaseValue " + baseValue.getName() + " created.");

    VolatilityDifference[] volDifVariations = { //
        volDif2, //
        volDif4, //
        volDif8 };
    volDifTop = new VolatilityDifference(baseValue, volDifVariations,
        START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW, 8, BASE_SCALE);
    logger.info("Top level VolatilityDifference created.");

    ewmacShort = new EWMAC(baseValue, null, START_OF_REFERENCE_WINDOW,
        END_OF_REFERENCE_WINDOW, 8, 2, BASE_SCALE);
    logger.info("EWMAC with EWMAs of 2 and 8 for BaseValue "
        + baseValue.getName() + " created.");

    ewmacMiddle = new EWMAC(baseValue, null, START_OF_REFERENCE_WINDOW,
        END_OF_REFERENCE_WINDOW, 16, 4, BASE_SCALE);
    logger.info("EWMAC with EWMAs of 4 and 16 for BaseValue "
        + baseValue.getName() + " created.");

    ewmacLong = new EWMAC(baseValue, null, START_OF_REFERENCE_WINDOW,
        END_OF_REFERENCE_WINDOW, 32, 8, BASE_SCALE);
    logger.info("EWMAC with EWMAs of 8 and 32 for BaseValue "
        + baseValue.getName() + " created.");

    EWMAC[] ewmacVariations = { //
        ewmacShort, //
        ewmacMiddle, //
        ewmacLong };
    ewmacTop = new EWMAC(baseValue, ewmacVariations,
        START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW, 0, 0,
        BASE_SCALE);
    logger.info("Top level EWMAC created.");

    Rule[] rules = { volDifTop, ewmacTop };

    subSystem = new SubSystem(baseValue, rules, CAPITAL, BASE_SCALE);
    logger.info("SubSystem created.");

    logger.info("Starting backtest for testing window "
        + START_OF_TEST_WINDOW + " - " + END_OF_TEST_WINDOW);
    logger.info("Testing...");
    double performanceValue = subSystem.backtest(START_OF_TEST_WINDOW,
        END_OF_TEST_WINDOW);
    logger.info("Done Testing. Value after backtest: " + performanceValue);

    double performancePercentage = Util.calculateReturn(CAPITAL,
        performanceValue) * 100;

    NumberFormat moneyFormatter = NumberFormat
        .getCurrencyInstance(Locale.GERMANY);
    String capitalString = moneyFormatter.format(CAPITAL);

    NumberFormat decimalFormatter = NumberFormat
        .getNumberInstance(Locale.GERMANY);
    String performancePercentageString = decimalFormatter
        .format(performancePercentage);

    logger.info("With your starting capital of " + capitalString
        + " that's a net return of " + performancePercentageString + "%");
  }
}
