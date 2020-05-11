package de.rumford.tradingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.*;

import de.rumford.tradingsystem.RuleTest.RealRule;
import de.rumford.tradingsystem.SubSystem.RuleContainer;
import de.rumford.tradingsystem.helper.BaseValueFactory;

class SubSystemTest {
	static final String MESSAGE_INCORRECT_EXCEPTION_MESSAGE = "Incorrect Exception message";

	static final String BASE_VALUE_NAME = "Base value name";
	static BaseValue baseValue;
	static final double VARIATOR = 1;
	static final double BASE_SCALE = 10;

	static final double CAPITAL = 10000000;

	static LocalDateTime localDateTimeJan09220000;
	static LocalDateTime localDateTimeJan10220000;
	static LocalDateTime localDateTimeJan11220000;
	static LocalDateTime localDateTimeJan12220000;

	static Rule r1;
	static Rule r2;
	static Rule r3;
	static Rule r4;
	static Rule[] rules;

	static SubSystem subsystem;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		baseValue = BaseValueFactory.jan1Feb05calcShort(BASE_VALUE_NAME);
		localDateTimeJan09220000 = LocalDateTime.of(2020, 01, 9, 22, 0);
		localDateTimeJan10220000 = LocalDateTime.of(2020, 01, 10, 22, 0);
		localDateTimeJan11220000 = LocalDateTime.of(2020, 01, 11, 22, 0);
		localDateTimeJan12220000 = LocalDateTime.of(2020, 01, 12, 22, 0);

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		r1 = RealRule.from(baseValue, null, localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, VARIATOR);
		r2 = RealRule.from(baseValue, null, localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, 2);
		r3 = RealRule.from(baseValue, null, localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, 3);
		r4 = RealRule.from(baseValue, null, localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, 4);

		rules = null;
		rules = ArrayUtils.add(rules, r1);
		rules = ArrayUtils.add(rules, r2);
		rules = ArrayUtils.add(rules, r3);
		rules = ArrayUtils.add(rules, r4);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link SubSystem#SubSystem(BaseValue, Rule[], double)}
	 */
	@Test
	void testSubSystem() {
		SubSystem subsys = new SubSystem(baseValue, rules, CAPITAL);
		SubSystem subsys2 = new SubSystem(baseValue, rules, CAPITAL);
		assertEquals(subsys, subsys2, "Equal Objects are not considered equal");
	}

	/**
	 * Test method for {@link SubSystem#subdivideRules(RuleContainer[])}
	 */
	@Test
	void testSubdivideRules() {
		int expectedLength = 2;
		int expectedSubLength1 = 3;
		int expectedSubLength2 = 1;
		SubSystem subsys = new SubSystem(baseValue, rules, CAPITAL);

		int actualLength = subsys.getRuleContainer().getRuleContainers().length;
		int actualSubLength1 = subsys.getRuleContainer().getRuleContainers()[0].getRuleContainers().length;
		int actualSubLength2 = subsys.getRuleContainer().getRuleContainers()[1].getRuleContainers().length;

		assertEquals(expectedLength, actualLength, "Top level structure not correct");
		assertEquals(expectedSubLength1, actualSubLength1, "Full second level structure not correct");
		assertEquals(expectedSubLength2, actualSubLength2, "Partial second level structure not correct");
	}

	/**
	 * Test method for {@link SubSystem#areRulesUnique(Rule[])}.
	 */
	@Test
	void testAreRulesUnique_identicalRules() {
		r2 = RealRule.from(baseValue, null, localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE, VARIATOR);
		Rule[] rules = { r1, r2 };

		assertFalse(SubSystem.areRulesUnique(rules), "Identical rules are not identified.");
	}

	/**
	 * Test method for {@link SubSystem#areRulesUnique(Rule[])}.
	 */
	@Test
	void testAreRulesUnique_uniqueRules() {
		Rule[] variations = { r3 };
		r2 = RealRule.from(baseValue, variations, localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE,
				VARIATOR);
		Rule[] rules = { r1, r2 };
		assertTrue(SubSystem.areRulesUnique(rules), "Unique rules are not identified.");

		r2 = RealRule.from(baseValue, null, localDateTimeJan09220000, localDateTimeJan12220000, BASE_SCALE, VARIATOR);
		Rule[] rules2 = { r1, r2 };
		assertTrue(SubSystem.areRulesUnique(rules2), "Unique rules are not identified.");

		r2 = RealRule.from(baseValue, null, localDateTimeJan10220000, localDateTimeJan11220000, BASE_SCALE, VARIATOR);
		Rule[] rules3 = { r1, r2 };
		assertTrue(SubSystem.areRulesUnique(rules3), "Unique rules are not identified.");

		@SuppressWarnings("unused")
		double diffBaseScale = (BASE_SCALE - 1 <= 0 ? BASE_SCALE + 1 : BASE_SCALE - 1);
		r2 = RealRule.from(baseValue, null, localDateTimeJan10220000, localDateTimeJan12220000, diffBaseScale,
				VARIATOR);
		Rule[] rules4 = { r1, r2 };
		assertTrue(SubSystem.areRulesUnique(rules4), "Unique rules are not identified.");

		double diffVariator = VARIATOR - 1;
		r2 = RealRule.from(baseValue, null, localDateTimeJan10220000, localDateTimeJan12220000, BASE_SCALE,
				diffVariator);
		Rule[] rules5 = { r1, r2 };
		assertTrue(SubSystem.areRulesUnique(rules5), "Unique rules are not identified.");
	}

	/**
	 * Test method for {@link SubSystem#evaluateRules(Rule[])}.
	 */
	@Test
	void testEvaluateRules_differentStartOfReferenceWindow() {
		r2 = RealRule.from(baseValue, null, localDateTimeJan09220000, localDateTimeJan12220000, BASE_SCALE, 2);
		Rule[] rules = { r1, r2 };
		String expectedMessage = "All rules need to have the same reference window but rules at position 0 and 1 differ.";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> subsystem = new SubSystem(baseValue, rules, CAPITAL),
				"Differing start of reference windows are not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link SubSystem#evaluateRules(Rule[])}.
	 */
	@Test
	void testEvaluateRules_differentEndOfReferenceWindow() {
		r2 = RealRule.from(baseValue, null, localDateTimeJan10220000, localDateTimeJan11220000, BASE_SCALE, 2);
		Rule[] rules = { r1, r2 };
		String expectedMessage = "All rules need to have the same reference window but rules at position 0 and 1 differ.";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> subsystem = new SubSystem(baseValue, rules, CAPITAL),
				"Differing end of reference windows are not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
	 */
	@Test
	void testValidateInput_baseValueNull() {
		BaseValue nullBaseValue = null;
		String expectedMessage = "Base value must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new SubSystem(nullBaseValue, rules, CAPITAL), "Null base value is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
	 */
	@Test
	void testValidateInput_rulesNull() {
		Rule[] nullRules = null;
		String expectedMessage = "Rules must not be null";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new SubSystem(baseValue, nullRules, CAPITAL), "Null rules are not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
	 */
	@Test
	void testValidateInput_rulesEmptyArray() {
		Rule[] emptyRulesArray = {};
		String expectedMessage = "Rules must not be an empty array";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new SubSystem(baseValue, emptyRulesArray, CAPITAL), "Empty rules array is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
	 */
	@Test
	void testValidateInput_capitalNaN() {
		double nanCapital = Double.NaN;
		String expectedMessage = "Capital must not be Double.NaN";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new SubSystem(baseValue, rules, nanCapital), "Capital of Double.NaN is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
	 */
	@Test
	void testValidateInput_capital0() {
		double zeroCapital = 0;
		String expectedMessage = "Capital must not be zero";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new SubSystem(baseValue, rules, zeroCapital), "Capital of 0 is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

	/**
	 * Test method for {@link SubSystem#validateInput(BaseValue, Rule[], double)}.
	 */
	@Test
	void testValidateInput_capitalSub0() {
		double negativeCapital = -1;
		String expectedMessage = "Capital must be a positive value.";

		Exception thrown = assertThrows(IllegalArgumentException.class,
				() -> new SubSystem(baseValue, rules, negativeCapital),
				"Negative capital value is not properly handled");

		assertEquals(expectedMessage, thrown.getMessage(), MESSAGE_INCORRECT_EXCEPTION_MESSAGE);
	}

}
