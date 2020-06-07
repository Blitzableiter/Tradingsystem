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
  static final double CAPITAL = 100000;

  static final String WORKING_DIR = Path.of("src", "test", "resources")
      .toString();

  static final String DAX = "DAX";
  static final String DAX_FILE_NAME = Path.of(WORKING_DIR, "DAX.csv")
      .toString();
  static final String DAX_SHORT_FILE_NAME = Path
      .of(WORKING_DIR, "DAX_short.csv").toString();
  static final String DAX_VOLATILITY_FILE_NAME = Path
      .of(WORKING_DIR, "DAX_VDAX.csv").toString();

  static final String STOXX = "EURO STOXX 50";
  static final String STOXX_FILE_NAME = Path.of(WORKING_DIR, "STOXX.csv")
      .toString();
  static final String STOXX_SHORT_FILE_NAME = Path
      .of(WORKING_DIR, "STOXX-Short.csv").toString();
  static final String STOXX_VOLATILITY_FILE_NAME = Path
      .of(WORKING_DIR, "STOXX-VSTOXX.csv").toString();

  static final String SP500 = "S&P 500";
  static final String SP500_FILE_NAME = Path.of(WORKING_DIR, "S&P.csv")
      .toString();
  static final String SP500_VOLATILITY_FILE_NAME = Path
      .of(WORKING_DIR, "S&P_VIX.csv").toString();

  static final LocalDateTime START_OF_REFERENCE_WINDOW = LocalDateTime
      .of(2014, 1, 2, 22, 0);
  static final LocalDateTime END_OF_REFERENCE_WINDOW = LocalDateTime
      .of(2018, 12, 28, 22, 0);
  static final double BASE_SCALE = 10;

  static final int LOOKBACK_WINDOW_8 = 8;

  static final LocalDateTime START_OF_TEST_WINDOW = LocalDateTime.of(2019,
      1, 2, 22, 0);
  static final LocalDateTime END_OF_TEST_WINDOW = LocalDateTime.of(2019,
      12, 30, 22, 0);

  private static final Logger logger = Logger
      .getLogger(ExampleClient.class);

  private ExampleClient() {
  }

  /**
   * Main method depicting how to all components of this system.
   * 
   * @param args Arguments. Empty.
   * @throws IOException if the given filenames cannot be found.
   */
  public static void main(String[] args) throws IOException {
    double daxPerf = forOneBaseValueWithShort(DAX, DAX_FILE_NAME,
        DAX_SHORT_FILE_NAME, DAX_VOLATILITY_FILE_NAME, CAPITAL * 0.2002);

    double stoxxPerf = forOneBaseValueWithShort(STOXX, STOXX_FILE_NAME,
        STOXX_SHORT_FILE_NAME, STOXX_VOLATILITY_FILE_NAME,
        CAPITAL * 0.407);

    double spPerf = forOneBaseValue(SP500, SP500_FILE_NAME,
        SP500_VOLATILITY_FILE_NAME, CAPITAL * 0.3928);

    formatPerformance(CAPITAL, daxPerf + stoxxPerf + spPerf);
  }

  /**
   * Perform all calculations for one base value. No Short index values
   * given.
   * 
   * @param baseValueName      Name of the base value.
   * @param longFileName       File name with long index values.
   * @param volatilityFileName File name with volatility index values.
   * @param capital            Capital to be spent on this base value.
   * @return The backtested performance value
   * @throws IOException if the filenames cause issues in file handling.
   */
  private static double forOneBaseValue(String baseValueName,
      String longFileName, String volatilityFileName, double capital)
      throws IOException {

    ValueDateTupel[] baseValues = DataSource.getDataFromCsv(longFileName,
        CsvFormat.EU);
    ValueDateTupel[] volatilityIndexValues = DataSource
        .getDataFromCsv(volatilityFileName, CsvFormat.EU);

    ValueDateTupel[][] aligned = ValueDateTupel.alignDates(
        new ValueDateTupel[][] { baseValues, volatilityIndexValues });

    baseValues = aligned[0];
    volatilityIndexValues = aligned[1];

    BaseValue baseValue = new BaseValue(baseValueName, baseValues);
    logger.info("BaseValue " + baseValue.getName() + " created.");

    return instantiateAndBacktest(capital, volatilityIndexValues,
        baseValue);
  }

  /**
   * Perform all calculations for one base value. Short index values given.
   * 
   * @param baseValueName      Name of the base value.
   * @param longFileName       File name with long index values.
   * @param shortFileName      File name with short index values.
   * @param volatilityFileName File name with volatility index values.
   * @param capital            Capital to be spent on this base value.
   * @return The backtested performance value
   * @throws IOException if the filenames cause issues in file handling.
   */
  private static double forOneBaseValueWithShort(String baseValueName,
      String longFileName, String shortFileName, String volatilityFileName,
      double capital) throws IOException {

    ValueDateTupel[] baseValues = DataSource.getDataFromCsv(longFileName,
        CsvFormat.EU);
    ValueDateTupel[] shortIndexValues = DataSource
        .getDataFromCsv(shortFileName, CsvFormat.EU);
    ValueDateTupel[] volatilityIndexValues = DataSource
        .getDataFromCsv(volatilityFileName, CsvFormat.EU);

    ValueDateTupel[][] aligned = ValueDateTupel
        .alignDates(new ValueDateTupel[][] { baseValues, shortIndexValues,
            volatilityIndexValues });

    baseValues = aligned[0];
    shortIndexValues = aligned[1];
    volatilityIndexValues = aligned[2];

    BaseValue baseValue = new BaseValue(baseValueName, baseValues,
        shortIndexValues);
    logger.info("BaseValue " + baseValue.getName() + " created.");

    return instantiateAndBacktest(capital, volatilityIndexValues,
        baseValue);
  }

  private static double instantiateAndBacktest(double capital,
      ValueDateTupel[] volatilityIndexValues, BaseValue baseValue) {
    Rule[] rules = createRules(volatilityIndexValues, baseValue);

    SubSystem subSystem = new SubSystem(baseValue, rules, capital,
        BASE_SCALE);
    logger.info("SubSystem created.");

    double performanceValue = performBacktest(subSystem);

    formatPerformance(capital, performanceValue);

    return performanceValue;
  }

  private static Rule[] createRules(ValueDateTupel[] volatilityIndexValues,
      BaseValue baseValue) {
    VolatilityDifference volDif = volDif(volatilityIndexValues, baseValue);

    EWMAC ewmacTop = ewmacs(baseValue);

    return new Rule[] { volDif, ewmacTop };
  }

  private static double performBacktest(SubSystem subSystem) {
    logger.info("Starting backtest for testing window "
        + START_OF_TEST_WINDOW + " - " + END_OF_TEST_WINDOW);
    logger.info("Testing...");
    return subSystem.backtest(START_OF_TEST_WINDOW, END_OF_TEST_WINDOW);
  }

  private static void formatPerformance(double capital,
      double performanceValue) {
    double performancePercentage = Util.calculateReturn(capital,
        performanceValue) * 100;
    NumberFormat moneyFormatter = NumberFormat
        .getCurrencyInstance(Locale.GERMANY);
    String capitalString = moneyFormatter.format(capital);
    String performanceValueString = moneyFormatter
        .format(performanceValue);

    NumberFormat decimalFormatter = NumberFormat
        .getNumberInstance(Locale.GERMANY);
    String performancePercentageString = decimalFormatter
        .format(performancePercentage);

    logger.info(
        "Done Testing. Value after backtest: " + performanceValueString);
    logger.info("With your starting capital of " + capitalString
        + " that's a net return of " + performancePercentageString + "%.");
  }

  private static EWMAC ewmacs(BaseValue baseValue) {
    EWMAC ewmacShort = new EWMAC(baseValue, null,
        START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW, 8, 2,
        BASE_SCALE);
    logger.info("EWMAC with EWMAs of 2 and 8 for BaseValue "
        + baseValue.getName() + " created.");

    EWMAC ewmacMiddle = new EWMAC(baseValue, null,
        START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW, 16, 4,
        BASE_SCALE);
    logger.info("EWMAC with EWMAs of 4 and 16 for BaseValue "
        + baseValue.getName() + " created.");

    EWMAC ewmacLong = new EWMAC(baseValue, null, START_OF_REFERENCE_WINDOW,
        END_OF_REFERENCE_WINDOW, 32, 8, BASE_SCALE);
    logger.info("EWMAC with EWMAs of 8 and 32 for BaseValue "
        + baseValue.getName() + " created.");

    EWMAC[] ewmacVariations = { //
        ewmacShort, //
        ewmacMiddle, //
        ewmacLong };
    EWMAC ewmacTop = new EWMAC(baseValue, ewmacVariations,
        START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW, 0, 0,
        BASE_SCALE);

    logger.info("Top level EWMAC created.");

    return ewmacTop;
  }

  private static VolatilityDifference volDif(
      ValueDateTupel[] volatilityIndexValues, BaseValue baseValue) {

    VolatilityDifference volDif = new VolatilityDifference(baseValue, null,
        START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW,
        LOOKBACK_WINDOW_8, BASE_SCALE, volatilityIndexValues);
    logger.info("VolatilityDifference with lookback window "
        + volDif.getLookbackWindow() + " for BaseValue "
        + baseValue.getName() + " created.");
    return volDif;
  }
}
