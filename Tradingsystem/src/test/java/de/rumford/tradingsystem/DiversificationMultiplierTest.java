package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.rumford.tradingsystem.RuleTest.RealRule;
import de.rumford.tradingsystem.helper.BaseValueFactory;

/**
 * Test class for {@link DiversificationMultiplier}.
 * 
 * @author Max Rumford
 *
 */
class DiversificationMultiplierTest {

  static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

  static DiversificationMultiplier divMulti;

  static RealRule ss1;
  static RealRule ss2;
  static RealRule ss3;
  static RealRule s1;
  static RealRule s2;
  static RealRule s3;
  static RealRule s4;
  static RealRule s5;
  static RealRule t1;
  static RealRule t2;

  static RealRule realRule;
  static Rule[] variations;
  static BaseValue baseValue;
  static double variator;

  static final String BASE_VALUE_NAME = "Base value name";
  static final int BASE_SCALE = 10;

  static LocalDateTime localDateTimeJan02220000;
  static LocalDateTime localDateTimeJan04220000;
  static LocalDateTime localDateTimeJan05220000;
  static LocalDateTime localDateTimeJan07220000;
  static LocalDateTime localDateTimeJan10220000;
  static LocalDateTime localDateTimeJan12220000;
  static LocalDateTime localDateTimeJan13220000;
  static LocalDateTime localDateTimeJan15220000;
  static LocalDateTime localDateTimeJan16220000;
  static LocalDateTime localDateTimeJan18220000;

  @BeforeAll
  static void setUpBeforeClass() {
    baseValue = BaseValueFactory.jan1Jan31calcShort(BASE_VALUE_NAME);

    localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2),
        LocalTime.of(22, 0));
    localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4),
        LocalTime.of(22, 0));
    localDateTimeJan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5),
        LocalTime.of(22, 0));
    localDateTimeJan07220000 = LocalDateTime.of(LocalDate.of(2020, 1, 7),
        LocalTime.of(22, 0));
    localDateTimeJan10220000 = LocalDateTime.of(LocalDate.of(2020, 1, 10),
        LocalTime.of(22, 0));
    localDateTimeJan12220000 = LocalDateTime.of(LocalDate.of(2020, 1, 12),
        LocalTime.of(22, 0));
    localDateTimeJan13220000 = LocalDateTime.of(LocalDate.of(2020, 1, 13),
        LocalTime.of(22, 0));
    localDateTimeJan15220000 = LocalDateTime.of(LocalDate.of(2020, 1, 15),
        LocalTime.of(22, 0));
    localDateTimeJan16220000 = LocalDateTime.of(LocalDate.of(2020, 1, 16),
        LocalTime.of(22, 0));
    localDateTimeJan18220000 = LocalDateTime.of(LocalDate.of(2020, 1, 18),
        LocalTime.of(22, 0));
  }

  @BeforeEach
  void setUp() {
    variator = 1;
    ss1 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, 1);
    ss2 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, .5);
    ss3 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, -1.07);
    Rule[] s1variations = { ss1, ss2, ss3 };
    s1 = RealRule.from(baseValue, s1variations, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, -1);
    s2 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, -2.32);
    s3 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, -14);
    Rule[] t1variations = { s1, s2, s3 };
    t1 = RealRule.from(baseValue, t1variations, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, .5);

    s4 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, 0.8);
    s5 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, -4.67);
    Rule[] t2variations = { s4, s5 };
    t2 = RealRule.from(baseValue, t2variations, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, -10);
    Rule[] realRuleVariations = { t1, t2 };

    realRule = RealRule.from(baseValue, realRuleVariations,
        localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE,
        variator);
    variations = realRule.getVariations();

    divMulti = new DiversificationMultiplier(variations);
  }

  /**
   * Test method for
   * {@link DiversificationMultiplier#DiversificationMultiplier(double[], double[][])}.
   */
  @Test
  void testDiversificationMultiplier() {
    assertTrue(divMulti instanceof DiversificationMultiplier,
        "Instance of DiversificationMultiplier not recognized");
  }

  /**
   * Test method for
   * {@link DiversificationMultiplier #calculateDiversificiationMultiplierValue()}.
   */
  @Test
  void testCalculateDiversificiationMultiplierValue() {
    double expectedDiversificationMultiplier = 3.862140866820605; // Excel:
    // 3.8621408668206

    double actualDiversificationMultiplier = divMulti.getValue();

    assertEquals(expectedDiversificationMultiplier,
        actualDiversificationMultiplier,
        "Diversification multiplier value is not correctly calculated");
  }

  /**
   * Test method for
   * {@link DiversificationMultiplier #calculateDiversificiationMultiplierValue()}.
   */
  @Test
  void testCalculateDiversificiationMultiplierValue_rulesHaveNoVars() {
    double expectedValue = 1.000109838860305; // Excel: 1.00010213205928
    ss1 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, 1);
    ss2 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, 2);
    ss3 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, 3);
    RealRule ss4 = RealRule.from(baseValue, null, localDateTimeJan10220000,
        localDateTimeJan12220000, BASE_SCALE, 4);
    Rule[] rules = { ss1, ss2, ss3, ss4 };

    divMulti = new DiversificationMultiplier(rules);

    assertEquals(expectedValue, divMulti.getValue(),
        "Diversification Multiplier is not correctly calculated when rules"
            + " have no variations");
  }
}