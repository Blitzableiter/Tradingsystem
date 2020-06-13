package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.helper.BaseValueFactory;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * Test class for {@link EWMA}.
 * 
 * @author Max Rumford
 *
 */
class EWMATest {

  static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

  EWMA ewma2;
  EWMA ewma2_1;
  EWMA ewma4;
  EWMA ewma8;

  BaseValue baseValue = BaseValueFactory
      .jan1Jan5calcShort("My base value");

  @BeforeEach
  void setUp() throws Exception {
    ewma2 = new EWMA(baseValue.getValues(), 2);
    ewma2_1 = new EWMA(baseValue.getValues(), 2);
    ewma4 = new EWMA(baseValue.getValues(), 4);
    ewma8 = new EWMA(baseValue.getValues(), 8);
  }

  /**
   * Test method for {@link EWMA#EWMA(ValueDateTupel[], int)}.
   */
  @Test
  void testEWMA_ewma_instanceof_EWMA() {
    assertTrue(ewma2 instanceof EWMA, "ewma2 is instanceof EWMA");
    assertEquals(ewma2, ewma2_1,
        "Two EWMAs with the same horizon are equal");
  }

  /**
   * Test method for {@link EWMA#validateHorizon(int)}.
   */
  @Test
  void testValidateHorizon_Horizon1() {
    int horizonOf1 = 1;
    String expectedMessage = "The horizon must not be < 2";

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new EWMA(baseValue.getValues(), horizonOf1),
        "Horizon less than 2 is not properly handled.");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for {@link EWMA#validateBaseValues(ValueDateTupel[])}.
   */
  @Test
  void testValidateBaseValues_emptyBaseValuesArray() {
    String expectedMessage = "The given values do not meet the "
        + "specifications.";
    String expectedCause = "Values must not be an empty array";

    ValueDateTupel[] emptyValuesArray = ValueDateTupel.createEmptyArray();

    Exception thrown = assertThrows(IllegalArgumentException.class,
        () -> new EWMA(emptyValuesArray, 2),
        "Empty base values array is not properly handled.");

    assertEquals(expectedMessage, thrown.getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
    assertEquals(expectedCause, thrown.getCause().getMessage(),
        MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
  }

  /**
   * Test method for {@link EWMA#calculateEWMA(double, double)}.
   */
  @Test
  void testCalculateEWMA_Horizon2_baseValue786point75_Is_524point5() {
    double calculatedValue = ewma2.calculateEWMA(0d, 786.75d);
    double expectedValue = 524.5d;
    assertEquals(expectedValue, calculatedValue,
        "Calculated EWMA is expected EWMA");
  }

  /**
   * Test method for {@link EWMA#calculateEWMA(double, double)}.
   */
  @Test
  void testCalculateEWMA_Horizon8_baseValue786point75_Is_174point833() {
    double calculatedValue = ewma2.calculateEWMA(0d, 100d);
    double expectedValue = (0d + 2d / 3d) * 100d;
    assertEquals(expectedValue, calculatedValue,
        "Calculated EWMA is expected EWMA");
  }

  /**
   * Test method for {@link EWMA#calculateDecay(int)}.
   */
  @Test
  void testCalculateDecay_DecayForHorizon2_Is_point67() {
    double expectedValue = 2d / 3d;
    assertEquals(expectedValue, ewma2.getDecay(),
        "Calculated Decay is expected Decay");
  }
}