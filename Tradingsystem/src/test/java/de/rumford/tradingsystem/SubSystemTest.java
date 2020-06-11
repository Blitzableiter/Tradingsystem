package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.RuleTest.RealRule;
import de.rumford.tradingsystem.helper.BaseValueFactory;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * Test class for {@link SubSystem}.
 * 
 * @author Max Rumford
 *
 */
class SubSystemTest {
  static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

  static final String BASE_VALUE_NAME = "Base value name";
  static BaseValue baseValue;
  static final double VARIATOR = 1;
  static final double BASE_SCALE = 10;

  static final double CAPITAL = 10000000;

  static LocalDateTime localDateTime2019Dec31220000;
  static LocalDateTime localDateTimeJan02220000;
  static LocalDateTime localDateTimeJan09220000;
  static LocalDateTime localDateTimeJan10220000;
  static LocalDateTime localDateTimeJan11220000;
  static LocalDateTime localDateTimeJan12220000;
  static LocalDateTime localDateTimeFeb04220000;
  static LocalDateTime localDateTimeFeb05220000;
  static LocalDateTime localDateTimeDec31220000;

  static Rule r1;
  static Rule r2;
  static Rule r3;
  static Rule r4;
  static Rule[] rules;

  static SubSystem subSystem;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    baseValue = BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME);
    localDateTime2019Dec31220000 = LocalDateTime.of(2019, 12, 31, 22, 0);
    localDateTimeJan02220000 = LocalDateTime.of(2020, 01, 2, 22, 0);
    localDateTimeJan09220000 = LocalDateTime.of(2020, 01, 9, 22, 0);
    localDateTimeJan10220000 = LocalDateTime.of(2020, 01, 10, 22, 0);
    localDateTimeJan11220000 = LocalDateTime.of(2020, 01, 11, 22, 0);
    localDateTimeJan12220000 = LocalDateTime.of(2020, 01, 12, 22, 0);
    localDateTimeFeb04220000 = LocalDateTime.of(2020, 02, 4, 22, 0);
    localDateTimeFeb05220000 = LocalDateTime.of(2020, 02, 5, 22, 0);
    localDateTimeDec31220000 = LocalDateTime.of(2020, 12, 31, 22, 0);
  }

  @BeforeEach
  void setUp() throws Exception {
    r1 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, VARIATOR);
    r2 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, 2);
    r3 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, 3);
    r4 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, 4);

    rules = null;
    rules = ArrayUtils.add(rules, r1);
    rules = ArrayUtils.add(rules, r2);
    rules = ArrayUtils.add(rules, r3);
    rules = ArrayUtils.add(rules, r4);

    subSystem = new SubSystem(baseValue, rules, CAPITAL, BASE_SCALE);
  }

  /**
   * Test method for {@link SubSystem#SubSystem(BaseValue, Rule[], double)}
   */
  @Test
  void testSubSystem() {
    SubSystem subsys = new SubSystem(baseValue, rules, CAPITAL,
        BASE_SCALE);
    SubSystem subsys2 = new SubSystem(baseValue, rules, CAPITAL,
        BASE_SCALE);
    assertEquals(subsys, subsys2,
        "Equal Objects are not considered equal");
  }

  /**
   * Test method for {@link SubSystem#evaluateRules(Rule[])}.
   */
  @Test
  void testEvaluateRules_identicalRules() {
    r1 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, VARIATOR);
    r2 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, VARIATOR);
    Rule[] rules = { r1, r2 };

    String expectedMessage = "The given rules are not unique. Only unique rules can be used.";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(baseValue, rules, CAPITAL, BASE_SCALE),
        "Non-unique rules are not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for {@link SubSystem#evaluateRules(Rule[])}.
   */
  @Test
  void testEvaluateRules_differentStartOfReferenceWindow() {
    r2 = RealRule.from(baseValue, null, localDateTimeJan09220000,
        localDateTimeJan12220000, BASE_SCALE, 2);
    Rule[] rules = { r1, r2 };
    String expectedMessage = "All rules need to have the same reference window but rules at position 0 and 1 differ.";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(baseValue, rules, CAPITAL, BASE_SCALE),
        "Differing start of reference windows are not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for {@link SubSystem#evaluateRules(Rule[])}.
   */
  @Test
  void testEvaluateRules_differentEndOfReferenceWindow() {
    r2 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan11220000, BASE_SCALE, 2);
    Rule[] rules = { r1, r2 };
    String expectedMessage = "All rules need to have the same reference window but rules at position 0 and 1 differ.";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(baseValue, rules, CAPITAL, BASE_SCALE),
        "Differing end of reference windows are not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
   */
  @Test
  void testValidateInput_baseValueNull() {
    BaseValue nullBaseValue = null;
    String expectedMessage = "Base value must not be null";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(nullBaseValue, rules, CAPITAL, BASE_SCALE),
        "Null base value is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
   */
  @Test
  void testValidateInput_rulesNull() {
    Rule[] nullRules = null;
    String expectedMessage = "Rules must not be null";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(baseValue, nullRules, CAPITAL, BASE_SCALE),
        "Null rules are not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
   */
  @Test
  void testValidateInput_rulesEmptyArray() {
    Rule[] emptyRulesArray = {};
    String expectedMessage = "Rules must not be an empty array";

    Exception thrown = assertThrows(
        IllegalArgumentException.class, () -> new SubSystem(baseValue,
            emptyRulesArray, CAPITAL, BASE_SCALE),
        "Empty rules array is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
   */
  @Test
  void testValidateInput_rules_baseValueDoesntMatch_givenBaseValue() {
    BaseValue newBaseValue = BaseValueFactory
        .jan1Jan31calcShort(BASE_VALUE_NAME);
    Rule[] rules = {
        new RealRule(newBaseValue, null, localDateTimeJan10220000,
            localDateTimeJan12220000, BASE_SCALE, VARIATOR) };
    String expectedMessage = "The base value of all rules must be equal to given base value but the rule at position 0 does not comply.";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(baseValue, rules, CAPITAL, BASE_SCALE),
        "Empty rules array is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
   */
  @Test
  void testValidateInput_capitalNaN() {
    double nanCapital = Double.NaN;
    String expectedMessage = "Given capital does not meet specifications.";
    String expectedCause = "Value must not be Double.NaN";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(baseValue, rules, nanCapital, BASE_SCALE),
        "Capital of Double.NaN is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
   */
  @Test
  void testValidateInput_capitalZeroOrLess() {
    double zeroCapital = 0;
    double negativeCapital = -1;
    String expectedMessage = "Given capital does not meet specifications.";
    String expectedCause = "Value must be a positive decimal";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(baseValue, rules, negativeCapital, BASE_SCALE),
        "Negative capital value is not properly handled");

    Exception thrown2 = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(baseValue, rules, zeroCapital, BASE_SCALE),
        "Capital of 0 is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedMessage, thrown2.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown2.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
   */
  @Test
  void testValidateInput_baseScaleNaN() {
    double baseScaleNaN = Double.NaN;
    String expectedMessage = "Given base scale does not meet specifications.";
    String expectedCause = "Value must not be Double.NaN";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(baseValue, rules, CAPITAL, baseScaleNaN),
        "Base scale of NaN is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
   */
  @Test
  void testValidateInput_baseScaleZeroOrLess() {
    double baseScaleZero = 0;
    double baseScaleSubZero = -1;
    String expectedMessage = "Given base scale does not meet specifications.";
    String expectedCause = "Value must be a positive decimal";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(baseValue, rules, CAPITAL, baseScaleZero),
        "Base scale of zero is not properly handled");
    Exception thrown2 = assertThrows(IllegalArgumentException.class,
        () -> new SubSystem(baseValue, rules, CAPITAL, baseScaleSubZero),
        "Base scale sub zero is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);

    assertEquals(expectedMessage, thrown2.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown2.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for {@link SubSystem#calculateCombinedForecasts()}.
   */
  @Test
  void testCalculateCombinedForecasts() {
    double expectedValue1 = 13.398963140010043; // Excel: 13.3988598882083,
    // diff. approx
    // 0.0007706%
    double expectedValue2 = 20; // Excel: 20

    assertEquals(expectedValue1,
        subSystem.getCombinedForecasts()[0].getValue(),
        "Combined forecasts are not correctly calculated");
    assertEquals(expectedValue2,
        subSystem
            .getCombinedForecasts()[subSystem.getCombinedForecasts().length
                - 1].getValue(),
        "Combined forecasts are not correctly calculated");
  }

  /**
   * Test method for
   * {@link SubSystem#backtest(LocalDateTime startOfTestWindow, LocalDateTime endOfTestWindow)}.
   */
  @Test
  void testBacktest() {
    double expectedValue = 1831472.7037588374; // Excel: 1,831,582.23,
                                               // diff.
    // approx 0.00598%

    assertEquals(expectedValue,
        subSystem.backtest(localDateTimeJan10220000,
            localDateTimeFeb05220000),
        "Backtest performance is not correctly calculated");
  }

  /**
   * Test method for
   * {@link SubSystem#backtest(LocalDateTime startOfTestWindow, LocalDateTime endOfTestWindow)}.
   */
  @Test
  void testBacktest_positiveAndNegativeForecasts() {
    VolatilityDifference volDif = new VolatilityDifference(baseValue, null,
        localDateTimeJan10220000, localDateTimeJan12220000, 4, BASE_SCALE);
    Rule[] rules = { volDif };
    subSystem = new SubSystem(baseValue, rules, CAPITAL, BASE_SCALE);

    double expectedValue = 16815027.90331543; // Excel: 16815027.1988897

    assertEquals(expectedValue,
        subSystem.backtest(localDateTimeJan10220000,
            localDateTimeFeb04220000),
        "Backtest performance is not correctly calculated");
  }

  /**
   * Test method for
   * {@link SubSystem#backtest(LocalDateTime startOfTestWindow, LocalDateTime endOfTestWindow)}.
   */
  @Test
  void testBacktest_startOfTestWindow_null() {
    String expectedMessage = "The given test window does not meet specifications.";
    String expectedCause = "Start of time window value must not be null";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> subSystem.backtest(null, localDateTimeFeb05220000),
        "Start of test window of null is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#backtest(LocalDateTime startOfTestWindow, LocalDateTime endOfTestWindow)}.
   */
  @Test
  void testBacktest_endOfTestWindow_null() {
    String expectedMessage = "The given test window does not meet specifications.";
    String expectedCause = "End of time window value must not be null";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> subSystem.backtest(localDateTimeJan10220000, null),
        "End of test window of null is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#backtest(LocalDateTime startOfTestWindow, LocalDateTime endOfTestWindow)}.
   */
  @Test
  void testBacktest_endOfTestWindow_not_after_startOfTestWindow() {
    String expectedMessage = "The given test window does not meet specifications.";
    String expectedCause = "End of time window value must be after start of time window value";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> subSystem.backtest(localDateTimeJan10220000,
            localDateTimeJan10220000),
        "End of test window not after start of test window is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#calculatePerformanceValues(LocalDateTime, LocalDateTime)}.
   */
  @Test
  void testCalculatePerformanceValues_static() {
    double expectedValue = 16454612.6646818; // Excel: 16454586.0867138,
    // diff. approx 0.000162%

    ValueDateTupel[] performanceValues = subSystem
        .calculatePerformanceValues(localDateTimeJan10220000,
            localDateTimeFeb05220000);
    assertEquals(expectedValue,
        performanceValues[performanceValues.length - 2].getValue(),
        "Performance values are not properly calculated");
  }

  /**
   * Test method for
   * {@link SubSystem#calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, de.rumford.tradingsystem.helper.ValueDateTupel[], double, double)}.
   */
  @Test
  void testCalculatePerformanceValues_instance() {
    double expectedValue = 16454612.6646818; // Excel: 16454586.0867138,
    // diff. approx 0.000162%

    ValueDateTupel[] performanceValues = SubSystem
        .calculatePerformanceValues(subSystem.getBaseValue(),
            localDateTimeJan10220000, localDateTimeFeb05220000,
            subSystem.getCombinedForecasts(), subSystem.getBaseScale(),
            subSystem.getCapital());
    assertEquals(expectedValue,
        performanceValues[performanceValues.length - 2].getValue(),
        "Performance values are not properly calculated");
  }

  /**
   * Test method for
   * {@link SubSystem#calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, de.rumford.tradingsystem.helper.ValueDateTupel[], double, double)}.
   */
  @Test
  void testCalculatePerformanceValues_positiveAndNegativeForecasts() {
    VolatilityDifference volDif4 = new VolatilityDifference(baseValue,
        null, localDateTimeJan10220000, localDateTimeJan12220000, 4,
        BASE_SCALE);
    VolatilityDifference volDif8 = new VolatilityDifference(baseValue,
        null, localDateTimeJan10220000, localDateTimeJan12220000, 8,
        BASE_SCALE);

    Rule[] rules = { volDif4, volDif8 };
    subSystem = new SubSystem(baseValue, rules, CAPITAL, BASE_SCALE);

    double expectedValue = 96201.5377744669; // Excel: 96201.5377744669

    ValueDateTupel[] performanceValues = SubSystem
        .calculatePerformanceValues(subSystem.getBaseValue(),
            localDateTimeJan10220000, localDateTimeFeb05220000,
            subSystem.getCombinedForecasts(), subSystem.getBaseScale(),
            subSystem.getCapital());
    assertEquals(expectedValue,
        performanceValues[performanceValues.length - 1].getValue(),
        "Performance values are not properly calculated");
  }

  /**
   * Test method for
   * {@link SubSystem#calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, de.rumford.tradingsystem.helper.ValueDateTupel[], double, double)}.
   */
  @Test
  void testCalculatePerformanceValues_positiveNegativeAndZeroForecasts() {
    VolatilityDifference volDif4 = new VolatilityDifference(baseValue,
        null, localDateTimeJan10220000, localDateTimeJan12220000, 4,
        BASE_SCALE);
    RealRule rr = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, VARIATOR);

    Rule[] rules = { volDif4, rr };
    subSystem = new SubSystem(baseValue, rules, CAPITAL, BASE_SCALE);

    double expectedValue = 1271620.1875697833; // Excel: 1271620.18756978

    ValueDateTupel[] performanceValues = SubSystem
        .calculatePerformanceValues(subSystem.getBaseValue(),
            localDateTimeJan10220000, localDateTimeFeb05220000,
            subSystem.getCombinedForecasts(), subSystem.getBaseScale(),
            subSystem.getCapital());
    assertEquals(expectedValue,
        performanceValues[performanceValues.length - 1].getValue(),
        "Performance values are not properly calculated");
  }

  /**
   * Test method for
   * {@link SubSystem#calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, de.rumford.tradingsystem.helper.ValueDateTupel[], double, double)}.
   */
  @Test
  void testCalculatePerformanceValues_startOfTestWindowNull() {
    String expectedMessage = "The given test window does not meet specifications.";
    String expectedCause = "Start of time window value must not be null";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> SubSystem.calculatePerformanceValues(
            subSystem.getBaseValue(), null, localDateTimeFeb05220000,
            subSystem.getCombinedForecasts(), subSystem.getBaseScale(),
            subSystem.getCapital()),
        "Invalid start of test window is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, de.rumford.tradingsystem.helper.ValueDateTupel[], double, double)}.
   */
  @Test
  void testCalculatePerformanceValues_endOfTestWindowNull() {
    String expectedMessage = "The given test window does not meet specifications.";
    String expectedCause = "End of time window value must not be null";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> SubSystem.calculatePerformanceValues(
            subSystem.getBaseValue(), localDateTimeJan10220000, null,
            subSystem.getCombinedForecasts(), subSystem.getBaseScale(),
            subSystem.getCapital()),
        "Invalid end of test window is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, de.rumford.tradingsystem.helper.ValueDateTupel[], double, double)}.
   */
  @Test
  void testCalculatePerformanceValues_endOfTestWindow_not_after_startOfTestWindow() {
    String expectedMessage = "The given test window does not meet specifications.";
    String expectedCause = "End of time window value must be after start of time window value";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> SubSystem.calculatePerformanceValues(
            subSystem.getBaseValue(), localDateTimeJan10220000,
            localDateTimeJan10220000, subSystem.getCombinedForecasts(),
            subSystem.getBaseScale(), subSystem.getCapital()),
        "End of test window not after start of test window is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, de.rumford.tradingsystem.helper.ValueDateTupel[], double, double)}.
   */
  @Test
  void testCalculatePerformanceValues_startOfTestWindow_not_in_baseValue() {
    String expectedMessage = "Given base value and test window do not fit.";
    String expectedCause = "Given values do not include given start value for time window";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> SubSystem.calculatePerformanceValues(
            subSystem.getBaseValue(), localDateTime2019Dec31220000,
            localDateTimeFeb05220000, subSystem.getCombinedForecasts(),
            subSystem.getBaseScale(), subSystem.getCapital()),
        "Start of test window not in base values is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, de.rumford.tradingsystem.helper.ValueDateTupel[], double, double)}.
   */
  @Test
  void testCalculatePerformanceValues_endOfTestWindow_not_in_baseValue() {
    String expectedMessage = "Given base value and test window do not fit.";
    String expectedCause = "Given values do not include given end value for time window";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> SubSystem.calculatePerformanceValues(
            subSystem.getBaseValue(), localDateTimeJan10220000,
            localDateTimeDec31220000, subSystem.getCombinedForecasts(),
            subSystem.getBaseScale(), subSystem.getCapital()),
        "End of test window not in base values is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for
   * {@link SubSystem#calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, de.rumford.tradingsystem.helper.ValueDateTupel[], double, double)}.
   */
  @Test
  void testCalculatePerformanceValues_startOfTestWindow_not_in_forecasts() {
    String expectedMessage = "Given forecasts and test window do not fit.";
    String expectedCause = "Given values do not include given start value for time window";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> SubSystem.calculatePerformanceValues(
            subSystem.getBaseValue(), localDateTimeJan09220000,
            localDateTimeFeb05220000, subSystem.getCombinedForecasts(),
            subSystem.getBaseScale(), subSystem.getCapital()),
        "Start of test window not in forecasts is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

}
