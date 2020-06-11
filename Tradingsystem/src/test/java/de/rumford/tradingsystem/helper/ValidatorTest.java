package de.rumford.tradingsystem.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.BaseValue;
import de.rumford.tradingsystem.RuleTest.RealRule;

/**
 * Test class for {@link Validator}.
 * 
 * @author Max Rumford
 *
 */
class ValidatorTest {

  static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

  /**
   * Test method for {@link Validator#validateCorrelations(double[])}.
   */
  @Test
  void testValidateCorrelations_arrayNull() {
    String expectedMessage = "Correlations array must not be null";
    double[] correlations = null;

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> Validator.validateCorrelations(correlations),
        "Array of null is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for {@link Validator#validateCorrelations(double[])}.
   */
  @Test
  void testValidateCorrelations_arrayOfLengthNotThree() {
    String expectedMessage = "There must be exactly three correlation values in the given array";
    double[] correlations = { 0, 0 };

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> Validator.validateCorrelations(correlations),
        "Array of length != 3 is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for {@link Validator#validateCorrelations(double[])}.
   */
  @Test
  void testValidateCorrelations_arrayContainsNan() {
    String expectedMessage = "NaN-values are not allowed. Correlation at position 1 is NaN.";
    double[] correlations = { 0, Double.NaN, 0 };

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> Validator.validateCorrelations(correlations),
        "Array containing Double.NaN is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for {@link Validator#validateCorrelations(double[])}.
   */
  @Test
  void testValidateCorrelations_arrayContainsValueGreaterThan1() {
    String expectedMessage = "Correlation at position 1 is greater than 1";
    double[] correlations = { 0, 2, 0 };

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> Validator.validateCorrelations(correlations),
        "Array containing values greater than 1 is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for {@link Validator#validateCorrelations(double[])}.
   */
  @Test
  void testValidateCorrelations_arrayContainsValueLessThanNegative1() {
    String expectedMessage = "Correlation at position 1 is less than -1";
    double[] correlations = { 0, -2, 0 };

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> Validator.validateCorrelations(correlations),
        "Array containing values less than -1 is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for {@link Validator#validateRow(ValueDateTupel[])}.
   */
  @Test
  void testValidateRow_onlyNan() {
    ValueDateTupel v1 = new ValueDateTupel(
        LocalDateTime.of(2019, 1, 1, 22, 0), Double.NaN);
    ValueDateTupel v2 = new ValueDateTupel(
        LocalDateTime.of(2019, 1, 1, 22, 0), Double.NaN);
    ValueDateTupel[] values = { v1, v2 };

    String expectedMessage = "Row contains only Double.NaN. Rows must "
        + "contain at least one value != Double.NaN";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> Validator.validateRow(values),
        "Row only conataning NaNs is not properly handled");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for {@link Validator#validateRow(ValueDateTupel[])}.
   */
  @Test
  void testValidateRow_oneNonNan() {
    ValueDateTupel v1 = new ValueDateTupel(
        LocalDateTime.of(2019, 1, 1, 22, 0), Double.NaN);
    ValueDateTupel v2 = new ValueDateTupel(
        LocalDateTime.of(2019, 1, 1, 22, 0), 1d);
    ValueDateTupel[] values = { v1, v2 };

    Validator.validateRow(values);
  }

  /**
   * Test method for {@link Validator#validateRow(ValueDateTupel[])}.
   */
  @Test
  void testValidateRow_firstNonNan() {
    ValueDateTupel v1 = new ValueDateTupel(
        LocalDateTime.of(2019, 1, 1, 22, 0), 1d);
    ValueDateTupel v2 = new ValueDateTupel(
        LocalDateTime.of(2019, 1, 1, 22, 0), Double.NaN);
    ValueDateTupel[] values = { v1, v2 };

    Validator.validateRow(values);
  }

  /**
   * Test method for
   * {@link Validator#validateRulesVsBaseScale(de.rumford.tradingsystem.Rule[], double)}.
   */
  @Test
  void testValidateRulesVsBaseScale() {
    final double baseScale = 10;
    BaseValue baseValue = BaseValueFactory.jan1Jan31calcShort("TEST");
    LocalDateTime startOfRefWindow = LocalDateTime.of(2020, 1, 10, 22, 0);
    LocalDateTime endOfRefWindow = LocalDateTime.of(2020, 1, 12, 22, 0);

    RealRule rr1 = new RealRule(baseValue, null, startOfRefWindow,
        endOfRefWindow, baseScale, 1);
    RealRule rr2 = new RealRule(baseValue, null, startOfRefWindow,
        endOfRefWindow, baseScale, 2);

    RealRule[] rules = { rr1, rr2 };

    Validator.validateRulesVsBaseScale(rules, baseScale);
  }

  /**
   * Test method for
   * {@link Validator#validateRulesVsBaseScale(de.rumford.tradingsystem.Rule[], double)}.
   */
  @Test
  void testValidateRulesVsBaseScale_rulesNull() {
    final double baseScale = 10;
    RealRule[] rules = null;

    Validator.validateRulesVsBaseScale(rules, baseScale);
  }

  /**
   * Test method for
   * {@link Validator#validateRulesVsBaseScale(de.rumford.tradingsystem.Rule[], double)}.
   */
  @Test
  void testValidateRulesVsBaseScale_individualRuleNull() {
    final double baseScale = 10;
    BaseValue baseValue = BaseValueFactory.jan1Jan31calcShort("TEST");
    LocalDateTime startOfRefWindow = LocalDateTime.of(2020, 1, 10, 22, 0);
    LocalDateTime endOfRefWindow = LocalDateTime.of(2020, 1, 12, 22, 0);

    RealRule rr1 = new RealRule(baseValue, null, startOfRefWindow,
        endOfRefWindow, baseScale, 1);
    RealRule rr2 = null;

    RealRule[] rules = { rr1, rr2 };

    Validator.validateRulesVsBaseScale(rules, baseScale);
  }

  /**
   * Test method for
   * {@link Validator#validateRulesVsBaseScale(de.rumford.tradingsystem.Rule[], double)}.
   */
  @Test
  void testValidateRulesVsBaseScale_wrongBaseScaleInRule() {
    final double baseScale = 10;
    String expectedMessage = "The rule at index 0 does not share the given"
        + " base scale of " + baseScale + ".";
    final double wrongBaseScale = 20;
    BaseValue baseValue = BaseValueFactory.jan1Jan31calcShort("TEST");
    LocalDateTime startOfRefWindow = LocalDateTime.of(2020, 1, 10, 22, 0);
    LocalDateTime endOfRefWindow = LocalDateTime.of(2020, 1, 12, 22, 0);

    RealRule rr1 = new RealRule(baseValue, null, startOfRefWindow,
        endOfRefWindow, wrongBaseScale, 1);
    RealRule rr2 = new RealRule(baseValue, null, startOfRefWindow,
        endOfRefWindow, baseScale, 2);

    RealRule[] rules = { rr1, rr2 };

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> Validator.validateRulesVsBaseScale(rules, baseScale),
        "Incorrect base scale is not properly handled.");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }
}
