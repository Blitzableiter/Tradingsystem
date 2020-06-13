package de.rumford.tradingsystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

import org.apache.log4j.Logger;

import de.rumford.tradingsystem.helper.*;

/**
 * The ExampleClient is an example of how to use this library.
 * 
 * @author Max Rumford
 *
 */
@JacocoIgnoreGenerated
public class ExampleClient {
  static final double CAPITAL = 100000;

  static String workingDir = Path.of("src", "test", "resources")
      .toString();

  static final String DAX = "DAX";
  static String daxFileName = "DAX.csv";
  static String daxShortFileName = "DAX_short.csv";
  static String daxVolatilityFileName = "DAX_VDAX.csv";

  static final String STOXX = "EURO STOXX 50";
  static String stoxxFileName = "STOXX.csv";
  static String stoxxShortFileName = "STOXX-Short.csv";
  static String stoxxVolatilityFileName = "STOXX-VSTOXX.csv";

  static final String SP500 = "S&P 500";
  static String sp500FileName = "S&P.csv";
  static String sp500VolatilityFileName = "S&P_VIX.csv";

  static final LocalDateTime START_OF_REFERENCE_WINDOW = LocalDateTime
      .of(2015, 1, 2, 22, 0);
  static final LocalDateTime END_OF_REFERENCE_WINDOW = LocalDateTime
      .of(2019, 12, 30, 22, 0);
  static final double BASE_SCALE = 10;

  static final int LOOKBACK_WINDOW_8 = 8;

  static final LocalDateTime START_OF_TEST_WINDOW = LocalDateTime.of(2020,
      1, 2, 22, 0);
  static final LocalDateTime END_OF_TEST_WINDOW = LocalDateTime.of(2020, 3,
      31, 22, 0);

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
    String basePath = null;
    if (args.length != 0) {
      basePath = args[0];
    }
    setPathNames(basePath);

    LocalDateTime startingTime = LocalDateTime.now();

    if (args.length > 1) {
      switch (args[1]) {
      case "1":
        exampleForOneBaseValue();
        break;
      case "3":
        exampleForThreeBaseValues();
        break;
      default:
        logger.info(String.format(
            "No fitting routing found for argument %s. Running default.",
            args[1]));
        exampleForThreeBaseValues();
        break;
      }
    } else {
      logger.info("Running default.");
      exampleForThreeBaseValues();
    }

    logDuration(startingTime);
  }

  private static void setPathNames(String basePath) throws IOException {
    if (basePath != null) {
      File f = Path.of(basePath).toFile();
      if (!f.exists()) {
        logger.warn(String.format(
            "Given path %s does not exist. Using default path %s.",
            f.getCanonicalPath(),
            Path.of(workingDir).toFile().getCanonicalPath()));
      } else {
        workingDir = basePath;
      }
    }
    daxFileName = getPathAsStringByFileName(daxFileName);
    daxShortFileName = getPathAsStringByFileName(daxShortFileName);
    daxVolatilityFileName = getPathAsStringByFileName(
        daxVolatilityFileName);

    stoxxFileName = getPathAsStringByFileName(stoxxFileName);
    stoxxShortFileName = getPathAsStringByFileName(stoxxShortFileName);
    stoxxVolatilityFileName = getPathAsStringByFileName(
        stoxxVolatilityFileName);

    sp500FileName = getPathAsStringByFileName(sp500FileName);
    sp500VolatilityFileName = getPathAsStringByFileName(
        sp500VolatilityFileName);
  }

  private static String getPathAsStringByFileName(String fileName) {
    return Path.of(workingDir, fileName).toString();
  }

  /**
   * Performs all necessary constructions for one base values.
   * 
   * @throws IOException if any file handling goes wrong.
   */
  private static void exampleForOneBaseValue() throws IOException {

    forOneBaseValueWithShort(STOXX, stoxxFileName, stoxxShortFileName,
        stoxxVolatilityFileName, CAPITAL);

  }

  /**
   * Performs all necessary constructions for three base values.
   * 
   * @throws IOException if any file handling goes wrong.
   */
  private static void exampleForThreeBaseValues() throws IOException {
    double daxPerf = forOneBaseValueWithShort(DAX, daxFileName,
        // daxShortFileName, daxVolatilityFileName, CAPITAL *
        // 0.2002); // 2014-2018, 2019
        daxShortFileName, daxVolatilityFileName, CAPITAL * 0.2121); // 2015-2019,
                                                                    // 202001-202003

    double stoxxPerf = forOneBaseValueWithShort(STOXX, stoxxFileName,
        stoxxShortFileName, stoxxVolatilityFileName,
        // CAPITAL * 0.407); // 2014-2018, 2019
        CAPITAL * 0.3674); // 2015-2019, 202001-202003

    double spPerf = forOneBaseValue(SP500, sp500FileName,
        // sp500VolatilityFileName, CAPITAL * 0.3928); // 2014-2018,
        // 2019
        sp500VolatilityFileName, CAPITAL * 0.4205); // 2015-2019,
                                                    // 202001-202003

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
    // logger.info("BaseValue " + baseValue.getName() + " created.");

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
    // logger.info("BaseValue " + baseValue.getName() + " created.");

    return instantiateAndBacktest(capital, volatilityIndexValues,
        baseValue);
  }

  /**
   * Sets up the rules and runs a backtest for the set testing window.
   * 
   * @param capital               The capital to be invested upon the given
   *                              base value.
   * @param volatilityIndexValues The volatility index values to be used in
   *                              the volatility difference rule.
   * @param baseValue             The base value to be evaluated.
   * @return The result as of
   *         {@link SubSystem#backtest(LocalDateTime, LocalDateTime)}
   */
  private static double instantiateAndBacktest(double capital,
      ValueDateTupel[] volatilityIndexValues, BaseValue baseValue) {

    /* Create the rules for the given base value. */
    Rule[] rules = createRules(volatilityIndexValues, baseValue);

    /* Create the subsystem. */
    SubSystem subSystem = new SubSystem(baseValue, rules, capital,
        BASE_SCALE);

    /*
     * Perform the backtest. Gets the available capital after the last
     * trading period.
     */
    double performanceValue = performBacktest(subSystem);

    /* Formats and logs the realized returns. */
    formatPerformance(capital, performanceValue);

    /*
     * Extracts the last forecast given in the subsystem. Also depicts the
     * position held after end of test window.
     */
    double lastForecast = subSystem
        .getCombinedForecasts()[subSystem.getCombinedForecasts().length
            - 1].getValue();
    logger.info(
        "Current position: " + Util.getPositionFromForecast(lastForecast)
            + ", " + lastForecast);

    return performanceValue;
  }

  /**
   * Performs the backtest for for the given subsystem.
   * 
   * @param subSystem The subsystem to be tested.
   * @return The result as of
   *         {@link SubSystem#backtest(LocalDateTime, LocalDateTime)}
   */
  private static double performBacktest(SubSystem subSystem) {
    logger.info("Starting backtest for BaseValue "
        + subSystem.getBaseValue().getName() + " with testing window "
        + START_OF_TEST_WINDOW + " - " + END_OF_TEST_WINDOW);
    return subSystem.backtest(START_OF_TEST_WINDOW, END_OF_TEST_WINDOW);
  }

  /**
   * Pretty print the given performance value in realtion to the given
   * starting capital.
   * 
   * @param capital          The starting capital
   * @param performanceValue The performance achieved.
   */
  private static void formatPerformance(double capital,
      double performanceValue) {
    /* Calculate the returns percentage achieved. */
    double performancePercentage = Util.calculateReturn(capital,
        performanceValue) * 100;

    /* Pretty print given capital and performance value. */
    NumberFormat moneyFormatter = NumberFormat
        .getCurrencyInstance(Locale.GERMANY);
    String capitalString = moneyFormatter.format(capital);
    String performanceValueString = moneyFormatter
        .format(performanceValue);

    /* Pretty print the returns percentage */
    NumberFormat decimalFormatter = NumberFormat
        .getNumberInstance(Locale.GERMANY);
    String performancePercentageString = decimalFormatter
        .format(performancePercentage);

    logger.info(
        "Done Testing. Value after backtest: " + performanceValueString);
    logger.info("With your starting capital of " + capitalString
        + " that's a net return of " + performancePercentageString + "%.");
  }

  /**
   * Creates the rules for the given base value.
   * 
   * @param volatilityIndexValues volatility index values to be used in the
   *                              {@link VolatilityDifference}.
   * @param baseValue             the base value to be used in the rules.
   * @return An array of top level rules.
   */
  private static Rule[] createRules(ValueDateTupel[] volatilityIndexValues,
      BaseValue baseValue) {
    VolatilityDifference volDif = createOneVolatilityDifference(
        volatilityIndexValues, baseValue);

    EWMAC ewmacTop = createEwmacs(baseValue);

    return new Rule[] { volDif, ewmacTop };
  }

  /**
   * Creates all {@link EWMAC}s for this example.
   * 
   * @param baseValue the {@link BaseValue} to be used in the
   *                  {@link EWMAC}s.
   * @return The top level {@link EWMAC}.
   */
  private static EWMAC createEwmacs(BaseValue baseValue) {
    EWMAC ewmacShort = createOneEwmac(baseValue, null, 8, 2);

    EWMAC ewmacMiddle = createOneEwmac(baseValue, null, 16, 4);

    EWMAC ewmacLong = createOneEwmac(baseValue, null, 32, 8);

    EWMAC[] ewmacVariations = { //
        ewmacShort, //
        ewmacMiddle, //
        ewmacLong };

    return createOneEwmac(baseValue, ewmacVariations, 0, 0);
  }

  /**
   * Creates one {@link EWMAC} using the given parameters.
   * 
   * @param baseValue    the {@link BaseValue} to be used in the
   *                     {@link EWMAC}.
   * @param variations   The array of {@link EWMAC} to be given to the
   *                     {@link EWMAC} as variations.
   * @param longHorizon  The value for instantiation of the long horizon
   *                     {@link EWMA}.
   * @param shortHorizon The value for instantiation of the short horizon
   *                     {@link EWMA}.
   * @return The created {@link EWMAC}.
   */
  private static EWMAC createOneEwmac(BaseValue baseValue,
      EWMAC[] variations, int longHorizon, int shortHorizon) {
    return new EWMAC(baseValue, variations, START_OF_REFERENCE_WINDOW,
        END_OF_REFERENCE_WINDOW, longHorizon, shortHorizon, BASE_SCALE);
  }

  /**
   * Creates one {@link VolatilityDifference} for the given parameters.
   * 
   * @param volatilityIndexValues The volatility index values to be used in
   *                              the new {@link VolatilityDifference}
   * @param baseValue             The {@link BaseValue} ot be used for the
   *                              new {@link VolatilityDifference}.
   * @return The new {@link VolatilityDifference}.
   */
  private static VolatilityDifference createOneVolatilityDifference(
      ValueDateTupel[] volatilityIndexValues, BaseValue baseValue) {

    return new VolatilityDifference(baseValue, null,
        START_OF_REFERENCE_WINDOW, END_OF_REFERENCE_WINDOW,
        LOOKBACK_WINDOW_8, BASE_SCALE, volatilityIndexValues);
  }

  /**
   * Log the time elapsed between the given starting time and
   * {@link LocalDateTime#now()}
   * 
   * @param startingTime The {@link LocalDateTime} to be used for
   *                     comparison.
   */
  private static void logDuration(LocalDateTime startingTime) {
    Duration duration = Duration.between(startingTime,
        LocalDateTime.now());
    logger.info("Runtime: " + duration.toMillis() / 1000d + " seconds.");
  }
}
