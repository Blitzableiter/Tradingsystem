package de.rumford.tradingsystem.helper;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import de.rumford.tradingsystem.BaseValue;
import de.rumford.tradingsystem.Rule;

/**
 * Validator class as used throughout the library containing solely of
 * static methods representing validation of input values.
 * 
 * @author Max Rumford
 *
 */
public class Validator {

  /**
   * Constructor for the {@link Validator} class<br/>
   * Only supports static methods, hence no instance shall be created,
   * hence a private constructor
   */
  private Validator() {
  }

  /**
   * Validates the given array of arrays.
   * <ul>
   * <li>Must not be null.</li>
   * </ul>
   * 
   * @param values {@code double[]} The array to be validated.
   * @throws IllegalArgumentException if any of the above specifications
   *                                  are not met.
   */
  public static void validateArrayOfDoubles(double[] values) {
    if (values == null)
      throw new IllegalArgumentException("Given array must not be null");
  }

  /**
   * Validates the given {@link BaseValue}.
   * <ul>
   * <li>Must not be null.</li>
   * </ul>
   * 
   * @param baseValue {@link BaseValue} The base value to be validated.
   */
  public static void validateBaseValue(BaseValue baseValue) {
    if (baseValue == null)
      throw new IllegalArgumentException("Base value must not be null");
  }

  /**
   * Validates the given correlations.
   * 
   * @param correlations {@code double[]} Correlations to be validated.
   *                     Must not be null. Must have a length of 3. Must
   *                     only contain values {@code !Double.NaN} and
   *                     {@code -1 <= value <= 1}.
   * @throws IllegalArgumentException if the above specifications are not
   *                                  met.
   */
  public static void validateCorrelations(double[] correlations) {
    /* Check if the given array is null */
    if (correlations == null)
      throw new IllegalArgumentException(
          "Correlations array must not be null");
    /* Check if the given array contains exactly three elements. */
    if (correlations.length != 3)
      throw new IllegalArgumentException(
          "There must be exactly three correlation values in the given"
              + " array");

    /* Check all given values inside the array */
    for (int i = 0; i < correlations.length; i++) {
      if (Double.isNaN(correlations[i]))
        throw new IllegalArgumentException(
            "NaN-values are not allowed. Correlation at position " + i
                + " is NaN.");
      if (correlations[i] > 1)
        throw new IllegalArgumentException(
            "Correlation at position " + i + " is greater than 1");
      if (correlations[i] < -1)
        throw new IllegalArgumentException(
            "Correlation at position " + i + " is less than -1");
    }
  }

  /**
   * Validates if the given array of ValueDateTupel is sorted in an
   * ascending order.
   * 
   * @param values {@code ValueDateTupel[]} The given array of values.
   * @throws IllegalArgumentException if the specifications above are not
   *                                  met.
   */
  public static void validateDates(ValueDateTupel[] values) {
    /*
     * The values cannot be used if they are not in ascending order.
     */
    if (!ValueDateTupel.isSortedAscending(values))
      throw new IllegalArgumentException(
          "Given values are not properly sorted or there are non-unique"
              + " values.");
  }

  /**
   * Validates the given double value. The value must meet the following
   * specifications:
   * <ul>
   * <li>Must not be Double.NaN.</li>
   * <li>Must be a positive decimal.</li>
   * </ul>
   * 
   * @param value {@code double} The value to validate.
   * @throws IllegalArgumentException if the above specifications are not
   *                                  met.
   */
  public static void validatePositiveDouble(double value) {
    if (Double.isNaN(value))
      throw new IllegalArgumentException("Value must not be Double.NaN");

    if (value <= 0)
      throw new IllegalArgumentException(
          "Value must be a positive decimal");

  }

  /**
   * Validates the given Array of {@link Rule}. Must meet the following
   * specifications:
   * <ul>
   * <li>Must not be null.</li>
   * <li>Must not be an empty array.</li>
   * </ul>
   * 
   * @param rules {@code Rule[]} The Array of {@link Rule} to be validated.
   * @throws IllegalArgumentException if the above specifications are not
   *                                  met.
   */
  public static void validateRules(Rule[] rules) {
    if (rules == null)
      throw new IllegalArgumentException("Rules must not be null");

    if (rules.length == 0)
      throw new IllegalArgumentException(
          "Rules must not be an empty array");
  }

  /**
   * Validates the given array of {@link Rule} against the given
   * {@link BaseValue}.
   * <ul>
   * <li>All rules must have the given {@link BaseValue} as their own
   * {@link BaseValue}.</li>
   * </ul>
   * 
   * @param rules     {@code Rule[]} The array of {@link Rule} to be
   *                  validated.
   * @param baseValue {@link BaseValue} The {@link BaseValue} to be
   *                  validated.
   * @throws IllegalArgumentException if any of the above specifications
   *                                  are not met.
   */
  public static void validateRulesVsBaseValue(Rule[] rules,
      BaseValue baseValue) {
    for (int i = 0; i < rules.length; i++)
      if (!rules[i].getBaseValue().equals(baseValue))
        throw new IllegalArgumentException(
            "The base value of all rules must be equal to given base value"
                + " but the rule at position " + i + " does not comply.");
  }

  /**
   * Validates the given time window values. Values are needed for checking
   * of containment. Must meet the following specifications:
   * <ul>
   * <li>startOfTimeWindow must not be null.</li>
   * <li>endOfTimeWindow must not be null.</li>
   * <li>endOfTimeWindow must be after startOfTimeWindow. See
   * {@link LocalDateTime#isAfter(java.time.chrono.ChronoLocalDateTime)}.
   * </li>
   * <li>values must contain startOfTimeWindow.</li>
   * <li>values must contain endOfTimeWindow.</li>
   * </ul>
   * 
   * @param startOfTimeWindow {@link LocalDateTime} The start of Time
   *                          window to be checked.
   * @param endOfTimeWindow   {@link LocalDateTime} The end of Time window
   *                          to be checked.
   * @param values            {@code ValueDateTupel[]} The array of
   *                          {@link ValueDateTupel} to be checked.
   * @throws IllegalArgumentException if any of the above specifications
   *                                  are not met.
   */
  public static void validateTimeWindow(LocalDateTime startOfTimeWindow,
      LocalDateTime endOfTimeWindow, ValueDateTupel[] values) {
    /* Check if LocalDateTimes are null */
    if (startOfTimeWindow == null)
      throw new IllegalArgumentException(
          "Start of time window value must not be null");
    if (endOfTimeWindow == null)
      throw new IllegalArgumentException(
          "End of time window value must not be null");
    /* Check if time window is properly defined: end must be after start */
    if (!endOfTimeWindow.isAfter(startOfTimeWindow))
      throw new IllegalArgumentException(
          "End of time window value must be after start of time window"
              + " value");

    /*
     * The given startOfTimeWindow must be included in the given base
     * values.
     */
    if (!ValueDateTupel.containsDate(values, startOfTimeWindow))
      throw new IllegalArgumentException(
          "Given values do not include given start value for time window");
    /*
     * The given endOfTimeWindow must be included in the given base values.
     */
    if (!ValueDateTupel.containsDate(values, endOfTimeWindow))
      throw new IllegalArgumentException(
          "Given values do not include given end value for time window");
  }

  /**
   * Validates the given array of ValueDateTupel. The given array must
   * fulfill the following specifications:
   * <ul>
   * <li>Must not be null</li>
   * <li>Must be of length greater than 0</li>
   * <li>Must not contain null</li>
   * <li>Must not contain NaNs as values</li>
   * </ul>
   * 
   * @param values {@code ValueDateTupel[]} The values to be validated.
   * @throws IllegalArgumentException if the given array does not meet the
   *                                  above specifications.
   */
  public static void validateValues(ValueDateTupel[] values) {
    /* Check if passed values array is null */
    if (values == null)
      throw new IllegalArgumentException(
          "The given values array must not be null");
    /* Check if passed values array contains elements */
    if (values.length == 0)
      throw new IllegalArgumentException(
          "Values must not be an empty array");

    for (ValueDateTupel value : values) {
      /* Validate if there are null values in the given values array. */
      if (value == null)
        throw new IllegalArgumentException(
            "Given values must not contain null.");

      /* Validate if there are NaN values in the given values array. */
      if (Double.isNaN(value.getValue()))
        throw new IllegalArgumentException(
            "Given values must not contain NaN.");
    }
  }

  /**
   * Validates the given variations array of {@link Rule} against the given
   * {@link LocalDateTime} values for startOfReferenceWindow and
   * endOfReferenceWindow and the given {@link BaseValue}. Must fulfill the
   * following specifications:
   * <ul>
   * <li>Must not be of length greater than 3.</li>
   * <li>Must not be of length 0.</li>
   * <li>Elements must not be null.</li>
   * <li>Elements' {@link Rule#getStartOfReferenceWindow()} must equal the
   * given startOfReferenceWindow.</li>
   * <li>Elements' {@link Rule#getEndOfReferenceWindow()} must equal the
   * given endOfReferenceWindow.</li>
   * <li>Must pass
   * {@link #validateRulesVsBaseValue(Rule[], BaseValue)}.</li>
   * </ul>
   * 
   * @param variations             {@code Rule[]} An array of {@link Rule}
   *                               representing a rules' variations.
   * @param startOfReferenceWindow {@link LocalDateTime} The start of
   *                               reference window of the {@link Rule}
   *                               containing the given variations.
   * @param endOfReferenceWindow   {@link LocalDateTime} The end of
   *                               reference window of the {@link Rule}
   *                               containing the given variations.
   * @param baseValue              {@link BaseValue} The base value of the
   *                               {@link Rule} containing the given
   *                               variations.
   */
  public static void validateVariations(Rule[] variations,
      LocalDateTime startOfReferenceWindow,
      LocalDateTime endOfReferenceWindow, BaseValue baseValue) {
    /* Check if there are too many variations for this rule */
    if (variations.length > 3)
      throw new IllegalArgumentException(
          "A rule must not contain more than 3 variations.");

    /* Check if the given variations array is empty. */
    if (variations.length == 0)
      throw new IllegalArgumentException(
          "The given variations array must not be empty.");

    for (int i = 0; i < variations.length; i++) {
      /* Check if the given variations array contains nulls. */
      if (variations[i] == null)
        throw new IllegalArgumentException("The variation at position " + i
            + " in the given variations array is null.");

      /* Check if main rule and variations share reference window. */
      if (!variations[i].getStartOfReferenceWindow()
          .equals(startOfReferenceWindow)) {
        throw new IllegalArgumentException(
            "The given reference window does not match the variation's at"
                + " position " + i
                + ". The given start of reference window is different.");
      }
      if (!variations[i].getEndOfReferenceWindow()
          .equals(endOfReferenceWindow)) {
        throw new IllegalArgumentException(
            "The given reference window does not match the variation's at"
                + " position " + i
                + ". The given end of reference window is different.");
      }
    }

    try {
      Validator.validateRulesVsBaseValue(variations, baseValue);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "The given variations do not meet specifications.", e);
    }
  }

  /**
   * Validates the given row of values. The row has to have at least 1
   * value not Double.NaN.
   * 
   * @param valueDateTupels {@code ValueDateTupel[]} The array of
   *                        {@link ValueDateTupel} to be validated.
   * @throws IllegalArgumentException if the given row contains only
   *                                  Double.NaN.
   */
  public static void validateRow(ValueDateTupel[] valueDateTupels) {
    double[] values = ValueDateTupel.getValues(valueDateTupels);
    /* If the first value is NaN, check if the array only contains NaN. */
    if (Double.isNaN(values[0])) {
      Set<Double> uniqueValues = new HashSet<>();
      for (double value : values)
        uniqueValues.add(value);

      /*
       * If the size of a set of all values is 1 then it contains only this
       * one value in all elements. If this value is Double.NaN, no values
       * were set but Double.NaN.
       */
      if (uniqueValues.size() == 1)
        throw new IllegalArgumentException(
            "Row contains only Double.NaN. Rows must contain at least one"
                + " value != Double.NaN");
    }
  }

  /**
   * Validates if the rules in the given array of {@link Rule} all share
   * the given base scale.
   * 
   * @param rules     The array of {@link Rule} to be examined.
   * @param baseScale The comparing base scale.
   * @throws IllegalArgumentException if the given rules do not share the
   *                                  given base scale.
   */
  public static void validateRulesVsBaseScale(Rule[] rules,
      double baseScale) {
    if (rules != null) {
      for (int i = 0; i < rules.length; i++)
        if (rules[i] != null && rules[i].getBaseScale() != baseScale)
          throw new IllegalArgumentException("The rule at index " + i
              + " does not share the given base scale of " + baseScale
              + ".");
    }
  }
}