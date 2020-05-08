/**
 * 
 */
package de.rumford.tradingsystem;

import org.apache.commons.lang3.ArrayUtils;

/**
 * de.rumford.tradingsystem
 * 
 * @author Max Rumford
 *
 */
public class SubSystem {

	private BaseValue baseValue;
	private RuleContainer ruleContainers;
	private DiversificationMultiplier diversificationMultiplier;
	private double capital;
	private double weight;

	/**
	 * Constructor for the SubSystem class.
	 * 
	 * @param baseValue {@link BaseValue} The base value to be used for all the
	 *                  given rules' calculations.
	 * @param rules     {@code Rule[]} Array of {@link Rule} to be used for forecast
	 *                  calculations in this SubSystem.
	 * @param capital   {@code double} The capital to be managed by this SubSystem.
	 */
	public SubSystem(BaseValue baseValue, Rule[] rules, double capital) {

		validateInput(baseValue, rules, capital);

		evaluateRules(rules);

		RuleContainer[] tempRuleContainers = putRulesIntoRuleContainers(rules);

		RuleContainer instanceRules;
		/*
		 * If there are more than 3 rules subdivide them to build up a tree structure.
		 */
		if (tempRuleContainers.length > 3) {
			instanceRules = subdivideRules(tempRuleContainers);
		} else {
			instanceRules = RuleContainer.fromRuleContainers(tempRuleContainers);
		}

		this.setRuleContainers(instanceRules);

		this.setBaseValue(baseValue);
		this.setCapital(capital);
		this.setDiversificationMultiplier(new DiversificationMultiplier(rules));
	}

	/**
	 * Validate the given input parameters.
	 * 
	 * @param baseValue {@link BaseValue} Must not be null.
	 * @param rules     {@code Rule[]} Must not be null. Must not be an empty array.
	 * @param capital   {@code double} Must not be Double.NaN. Must not be 0. Must
	 *                  not be negative.
	 * @throws IllegalArgumentException if any of the above criteria is not met.
	 */
	private static void validateInput(BaseValue baseValue, Rule[] rules, double capital) {
		if (baseValue == null)
			throw new IllegalArgumentException("Base value must not be null");

		if (rules == null)
			throw new IllegalArgumentException("Rules must not be null");

		if (rules.length == 0)
			throw new IllegalArgumentException("Rules must not be an empty array");

		if (Double.isNaN(capital))
			throw new IllegalArgumentException("Capital must not be Double.NaN");

		if (capital == 0)
			throw new IllegalArgumentException("Capital must not be zero");

		if (capital < 0)
			throw new IllegalArgumentException("Capital must be a positive value.");
	}

	/**
	 * Return an array of {@link RuleContainer}, each containing one of the given
	 * {@link Rule} from the array of rules. Keeps order.
	 * 
	 * @param rules {@code Rule[]} An array of {@link Rule}.
	 * @return {@code RuleContainer[]} An array of {@link RuleContainer}, each
	 *         wrapping one of the given {@link Rule} from the passed array of
	 *         rules.
	 */
	private RuleContainer[] putRulesIntoRuleContainers(Rule[] rules) {
		RuleContainer[] tempRuleContainers = {};
		/* Wrap each rule into a RuleContainer. */
		for (Rule rule : rules) {
			tempRuleContainers = ArrayUtils.add(tempRuleContainers, RuleContainer.fromRule(rule));
		}
		return tempRuleContainers;
	}

	/**
	 * Local class for wrapping of rules so they can be boxed into each other
	 * without altering the rules themselves. Keeps the Rules clean.
	 */
	private static class RuleContainer {
		private Rule[] rules;
		private RuleContainer[] ruleContainers;

		RuleContainer(Rule[] rules, RuleContainer[] ruleContainers) {
			this.setRules(rules);
			this.setRuleContainers(ruleContainers);
		}

		public static RuleContainer fromRule(Rule rule) {
			Rule[] ruleAsArray = { rule };
			return new RuleContainer(ruleAsArray, null);
		}

		public static RuleContainer fromRuleContainers(RuleContainer[] ruleContainers) {
			return new RuleContainer(null, ruleContainers);
		}

		private void setRules(Rule[] rules) {
			this.rules = rules;
		}

		private void setRuleContainers(RuleContainer[] ruleContainers) {
			this.ruleContainers = ruleContainers;
		}
	}

	/**
	 * Check if the given rules are unique by utilizing {@link Rule#equals(Object)}
	 * 
	 * @param rules {@code Rule} An array of rules to be check for uniqueness.
	 * @return {@code boolean} True, if the rules are unique. False otherwise.
	 */
	public static boolean areRulesUnique(Rule[] rules) {
		for (int i = 0; i < rules.length - 1; i++) {
			if (rules[i].equals(rules[i + 1])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Evaluate if the rules are unique by {@link SubSystem#areRulesUnique(Rule[])}.
	 * 
	 * @param rules {@code Rule[]} Rules that are to be checked.
	 * @throws IllegalArgumentException if the given rules are not unique.
	 */
	private static void evaluateRules(Rule[] rules) {
		if (!areRulesUnique(rules))
			throw new IllegalArgumentException("The given rules are not unique. Only unique rules can be used.");
	}

	/**
	 * Subdivides the given array of given RuleContainer by 3 into RuleContainers.
	 * If the resulting array of RuleContainers is longer than 3
	 * {@link #subdivideRules(RuleContainer[])} will be called recursively. If not,
	 * these 3 or less RuleContainers are put into another RuleContainer which is
	 * then returned to the caller.
	 * 
	 * @param rules {@code RuleContainer[]} The array of {@link RuleContainer} to be
	 *              subdivided.
	 * @return {@link RuleContainer} A RuleContainer containing the an array of
	 *         RuleContainers, each containing three RuleContainer out of the given
	 *         array of RuleContainers.
	 */
	private static RuleContainer subdivideRules(RuleContainer[] rules) {
		/* Evaluate in multiples of three how many RuleContainer are passed */
		int loopsize = rules.length / 3;
		if (rules.length % 3 != 0)
			loopsize += 1;

		RuleContainer[] ruleContainers = {};
		for (int i = 1; i <= loopsize; i++) {
			RuleContainer[] relevantRules = {};
			/*
			 * If the number of remaining RuleContainer is > 3 there will be another loop,
			 * therefore the rules array must be cleaned of the RuleContainers taken out.
			 */
			if (rules.length > 3) {
				System.arraycopy(rules, 0, relevantRules, 0, 3);
				System.arraycopy(rules, 3, rules, 0, rules.length - 3);
			} else {
				System.arraycopy(rules, 0, relevantRules, 0, rules.length);
			}
			/*
			 * Create a new RuleContainer to store the newly retrieved rules and add it to
			 * the array of RuleContainer.
			 */
			RuleContainer tempRuleContainer = RuleContainer.fromRuleContainers(relevantRules);
			ruleContainers = ArrayUtils.add(ruleContainers, tempRuleContainer);
		}

		/*
		 * If the array still consists of more than 3 elements another layer must be
		 * added. Therefore subdivideRules() is called recursively.
		 */
		if (ruleContainers.length > 3)
			return subdivideRules(ruleContainers);

		return RuleContainer.fromRuleContainers(ruleContainers);
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */

	/**
	 * @return baseValue SubSystem
	 */
	public BaseValue getBaseValue() {
		return baseValue;
	}

	/**
	 * @param baseValue the baseValue to set
	 */
	private void setBaseValue(BaseValue baseValue) {
		this.baseValue = baseValue;
	}

	/**
	 * @param rules the rules to set
	 */
	private void setRuleContainers(RuleContainer ruleContainers) {
		this.ruleContainers = ruleContainers;
	}

	/**
	 * @return diversificationMultiplier SubSystem
	 */
	public DiversificationMultiplier getDiversificationMultiplier() {
		return diversificationMultiplier;
	}

	/**
	 * @param diversificationMultiplier the diversificationMultiplier to set
	 */
	private void setDiversificationMultiplier(DiversificationMultiplier diversificationMultiplier) {
		this.diversificationMultiplier = diversificationMultiplier;
	}

	/**
	 * @return capital SubSystem
	 */
	public double getCapital() {
		return capital;
	}

	/**
	 * @param capital the capital to set
	 */
	private void setCapital(double capital) {
		this.capital = capital;
	}

	/**
	 * @return weight SubSystem
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	private void setWeight(double weight) {
		this.weight = weight;
	}

}
