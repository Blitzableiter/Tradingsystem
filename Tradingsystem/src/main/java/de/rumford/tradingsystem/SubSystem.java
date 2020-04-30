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

	private Rule[] rules;
	private BaseValue baseValue;
	private double capital;
	private double weight;
	private DiversificationMultiplier diversificationMultiplier;

	/**
	 * 
	 */
	public SubSystem(BaseValue baseValue, Rule[] rules, double capital) throws IllegalArgumentException {
		this.evaluateAndSetRules(rules);

		this.setBaseValue(baseValue);
		this.setCapital(capital);

		/* Calculates and recursively sets weights for all rules and their variations */
		this.calculateRuleWeights();
	}

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
	private boolean evaluateAndSetRules(Rule[] rules) {
		if (!areRulesUnique(rules))
			throw new IllegalArgumentException("The given rules are not unique. Only unique rules can be used.");

		/* If rules are unique set them. */
		this.setRules(rules);

		return false;
	}

	private void calculateRuleWeights() {
		Rule[] rules = this.getRules();

	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */

	/**
	 * @return rules SubSystem
	 */
	private Rule[] getRules() {
		return rules;
	}

	/**
	 * @param rules the rules to set
	 */
	private void setRules(Rule[] rules) {
		this.rules = rules;
	}

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

}
