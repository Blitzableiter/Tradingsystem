/**
 * 
 */
package de.rumford.tradingsystem;

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
	 * 
	 */
	public SubSystem(BaseValue baseValue, Rule[] rules, double capital) {
		this.evaluateRules(rules);

		this.setBaseValue(baseValue);
		this.setCapital(capital);
	}

	private class RuleContainer {
		public RuleContainer(Rule[] rules, RuleContainer[] ruleContainers) {
			this.setRules(rules);
			this.setRuleContainers(ruleContainers);
		}

		private Rule[] rules;
		private RuleContainer[] ruleContainers;

		public boolean hasRules() {
			return this.getRules() != null;
		}

		public boolean hasSubRules() {
			return this.getRulesContainers() != null;
		}

		public Rule[] getRules() {
			return rules;
		}

		private void setRules(Rule[] rules) {
			this.rules = rules;
		}

		public RuleContainer[] getRulesContainers() {
			return ruleContainers;
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
	public boolean areRulesUnique(Rule[] rules) {
		for (int i = 0; i < rules.length - 1; i++) {
			if (rules[i].equals(rules[i + 1])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Evaluate if the rules are unique.
	 * 
	 * @param rules
	 * @return
	 */
	private boolean evaluateRules(Rule[] rules) {
		if (rules == null)
			throw new IllegalArgumentException("Given rules must not be null");
		if (!areRulesUnique(rules))
			throw new IllegalArgumentException("The given rules are not unique. Only unique rules can be used.");

		// TODO Verpacken jeder einzelnen Rule in einen RuleContainer
		// TODO Übergeben des Arrays von RuleContainer nach subdivideRules
		RuleContainer instanceRules = subdivideRules(rules);

		/* If rules are unique set them. */
		this.setRules(instanceRules);

		return false;
	}

	private RuleContainer subdivideRules(Rule[] rules) {
		// TODO Baum von den Blättern her zu Containern à 3 zusammenfassen, dann Array
		// der Container rekursiv eingeben.
		RuleContainer returnRules = null;
		if (rules.length <= 3)
			return new RuleContainer(rules, null);

		/* number of layers is at this point 2 or higher */
		int numberOfLayersToGo = rules.length / 3 + 1;

		return returnRules;
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
	 * @return rules SubSystem
	 */
	private RuleContainer getRules() {
		return ruleContainers;
	}

	/**
	 * @param rules the rules to set
	 */
	private void setRules(RuleContainer ruleContainers) {
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
