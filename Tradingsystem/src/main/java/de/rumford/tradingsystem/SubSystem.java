/**
 * 
 */
package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * de.rumford.tradingsystem
 * 
 * @author Max Rumford
 *
 */
public class SubSystem {

	private static final double PRICE_FACTOR_BASE_SCALE = 1;

	private BaseValue baseValue;
	private RuleContainer ruleContainer;
	private DiversificationMultiplier diversificationMultiplier;
	private double capital;
	private double weight;
	private ValueDateTupel[] combinedForecasts;

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

		instanceRules = subdivideRules(tempRuleContainers);

		this.setRuleContainer(instanceRules);

		this.setBaseValue(baseValue);
		this.setCapital(capital);
		this.setDiversificationMultiplier(new DiversificationMultiplier(rules));
		this.setCombinedForecasts(calculateCombinedForecasts(this.getRuleContainer()));
	}

	private static ValueDateTupel[] calculateCombinedForecasts(RuleContainer ruleContainer) {
		/* Step through the tree until ruleContainers==null. */

		/*
		 * Take the forecasts of the rule inside the "current" ruleContainer multiply
		 * them by the weight of the "current" ruleContainer
		 */

		/* Add up the weighted forecasts for all rules -> combined forecasts */

		/* Apply Diversification Multiplier */

		return null;
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
	 * {@link Rule} from the array of rules. Keeps order. <br>
	 * All rules passed into this method will be assigned an even weight of
	 * 1/rules.length.
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
			RuleContainer rcToAdd = RuleContainer.fromRule(rule);
			/* All rules get the same weight */
			rcToAdd.setWeight(1d / rules.length);
			tempRuleContainers = ArrayUtils.add(tempRuleContainers, rcToAdd);
		}
		return tempRuleContainers;
	}

	/**
	 * Local class for wrapping of rules so they can be boxed into each other
	 * without altering the rules themselves. Keeps the Rules clean.
	 */
	static class RuleContainer {
		private Rule[] rules;
		private RuleContainer[] ruleContainers;
		private double weight = 1d;

		private RuleContainer(Rule[] rules, RuleContainer[] ruleContainers) {
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

		@GeneratedCode
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(ruleContainers);
			result = prime * result + Arrays.hashCode(rules);
			return result;
		}

		@GeneratedCode
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RuleContainer other = (RuleContainer) obj;
			if (!Arrays.equals(ruleContainers, other.ruleContainers))
				return false;
			if (!Arrays.equals(rules, other.rules))
				return false;
			return true;
		}

		@GeneratedCode
		@Override
		public String toString() {
			return "RuleContainer [weight=" + weight + ", rules=" + Arrays.toString(rules) + ", ruleContainers="
					+ Arrays.toString(ruleContainers) + "]";
		}

		private void setRules(Rule[] rules) {
			this.rules = rules;
		}

		public Rule[] getRules() {
			return this.rules;
		}

		private void setRuleContainers(RuleContainer[] ruleContainers) {
			this.ruleContainers = ruleContainers;
		}

		public RuleContainer[] getRuleContainers() {
			return this.ruleContainers;
		}

		/**
		 * @return weight RuleContainer
		 */
		public double getWeight() {
			return weight;
		}

		/**
		 * @param weight the weight to set
		 */
		public void setWeight(double weight) {
			this.weight = weight;
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
	 * Evaluate if the given rules can be used in this SubSystem. <br>
	 * Rules have to fulfill the following criteria:
	 * <ul>
	 * <li>Be unique by {@link SubSystem#areRulesUnique(Rule[])}</li>
	 * <li>Equal in startOfReferenceWindow and endOfReferenceWinow by
	 * {@link LocalDateTime#isEqual(ChronoLocalDateTime)}</li>
	 * </ul>
	 * 
	 * @param rules {@code Rule[]} Rules that are to be checked.
	 * @throws IllegalArgumentException if the given rules are not unique.
	 */
	private static void evaluateRules(Rule[] rules) {
		if (!areRulesUnique(rules))
			throw new IllegalArgumentException("The given rules are not unique. Only unique rules can be used.");

		/* All rules need to have the same reference window */
		for (int i = 1; i < rules.length; i++) {
			if (!rules[i].getStartOfReferenceWindow().isEqual(rules[i - 1].getStartOfReferenceWindow())
					|| !rules[i].getEndOfReferenceWindow().isEqual(rules[i - 1].getEndOfReferenceWindow())) {
				throw new IllegalArgumentException(
						"All rules need to have the same reference window but rules at position " + (i - 1) + " and "
								+ i + " differ.");
			}
		}

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

		/* If there are 3 rules or less no subdivision has to be done. */
		if (rules.length <= 3)
			return RuleContainer.fromRuleContainers(rules);

		/* Evaluate in multiples of three how many RuleContainer are passed */
		int loopsize = rules.length / 3;
		if (rules.length % 3 != 0)
			loopsize += 1;

		RuleContainer[] ruleContainers = {};
		for (int i = 1; i <= loopsize; i++) {
			RuleContainer[] relevantRules;
			/*
			 * If the number of remaining RuleContainer is > 3 there will be another loop,
			 * therefore the rules array must be cleaned of the RuleContainers taken out.
			 */
			if (rules.length > 3) {
				/* Extract the relevant rules */
				relevantRules = new RuleContainer[3];
				System.arraycopy(rules, 0, relevantRules, 0, 3);

				/*
				 * Make a temporary array to hold all values with the relevant rules taken out.
				 */
				RuleContainer[] newRules = new RuleContainer[rules.length - 3];
				System.arraycopy(rules, 3, newRules, 0, rules.length - 3);

				/* Keep those rules for the next loop iteration. */
				rules = newRules.clone();
			} else {
				relevantRules = new RuleContainer[rules.length];
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

	public double backtest(LocalDateTime startOfTestWindow, LocalDateTime endOfTestWindow) {
		if (startOfTestWindow == null)
			throw new IllegalArgumentException("The given start of test window must not be null");
		if (endOfTestWindow == null)
			throw new IllegalArgumentException("The given end of test window must not be null");
		if (!endOfTestWindow.isAfter(startOfTestWindow))
			throw new IllegalArgumentException("The end of test window must be after the start of the test window");

		ValueDateTupel[] baseValues = this.getBaseValue().getValues();

		if (!ValueDateTupel.containsDate(baseValues, startOfTestWindow))
			throw new IllegalArgumentException(
					"The given start of test window is not contained in the base value for this subsystem.");
		if (!ValueDateTupel.containsDate(baseValues, endOfTestWindow))
			throw new IllegalArgumentException(
					"The given end of test window is not contained in the base value for this subsystem.");

		double capitalAfterBacktest = this.getCapital();

		//
		//
		//
		//
		//
		/*
		 * Maybe bring all of this into a subfunction returning an Array of
		 * ValueDateTupel containing the performance values for each trading timespan
		 */
		//
		/* Fetch all base values inside the test window */
		ValueDateTupel[] relevantBaseValues = ValueDateTupel.getElements(baseValues, startOfTestWindow,
				endOfTestWindow);
		double productPriceFactor = calculateProductPriceFactor(ValueDateTupel.getValues(relevantBaseValues));

		/*
		 * Calculate the product prices based on the base value for each day and the
		 * calculated productPriceFactor
		 */
		ValueDateTupel[] productPrices = calculateProductPrices(relevantBaseValues, productPriceFactor);

		/*
		 * Calculate the short product prices based on the base value for each day and
		 * the calculated productPriceFactor
		 */
		ValueDateTupel[] relevantShortIndexValues = ValueDateTupel
				.getElements(this.getBaseValue().getShortIndexValues(), startOfTestWindow, endOfTestWindow);
		ValueDateTupel[] shortProductPrices = calculateProductPrices(relevantShortIndexValues, productPriceFactor);

		ValueDateTupel[] combinedForecasts = ValueDateTupel.getElements(
				// TODO
				this.getCombinedForecasts(), //
				startOfTestWindow, endOfTestWindow);

		int longProductsCount = 0;
		int shortProductsCount = 0;
		for (ValueDateTupel forecast : combinedForecasts) {
			// TODO Position sizing
			//// add value of last held position to capitalAfterBacktest
			//// subtract value of current position from capitalAfterBacktest
		}
		//
		//
		//
		//
		//

		return capitalAfterBacktest;
	}

	/**
	 * Calculate product prices based on the given array of values and a given
	 * product price factor.
	 * 
	 * @param baseValues         {@code ValueDateTupel[]} The values the prices are
	 *                           to be based on.
	 * @param productPriceFactor {@code double} The factor used to calculate the
	 *                           product prices.
	 * @return {@code ValueDateTupel[]} An array of prices using the dates of the
	 *         given baseValues.
	 */
	private static ValueDateTupel[] calculateProductPrices(ValueDateTupel[] baseValues, double productPriceFactor) {
		ValueDateTupel[] productPrices = {};
		for (ValueDateTupel baseValue : baseValues)
			ValueDateTupel.addOneAt(baseValues,
					new ValueDateTupel(baseValue.getDate(), baseValue.getValue() * productPriceFactor),
					productPrices.length);

		return productPrices;
	}

	/**
	 * Call {@link SubSystem#calculateProductPriceFactor(double[], double)} passing
	 * PRICE_FACTOR_BASE_SCALE as param.
	 * 
	 * @see SubSystem#calculateProductPriceFactor(double[], double)
	 * @param values {@code double[]} An Array of values the factor is to be
	 *               calculated for
	 * @return {@code double} The calculated factor.
	 */
	// TODO TEST ME
	private static double calculateProductPriceFactor(double[] values) {
		return SubSystem.calculateProductPriceFactor(values, PRICE_FACTOR_BASE_SCALE);
	}

	/**
	 * Calculate the factor by which all of the given values must be multiplied so
	 * their products have an average of priceFactorBaseScale. <br>
	 * The factor is calculated by inverting the average of the given values divided
	 * by the given priceFactorBaseScale.
	 * 
	 * @param values               {@code double[]} An Array of values the factor is
	 *                             to be calculated for
	 * @param priceFactorBaseScale {@code double} The base scale to use.
	 * @return {@code double} The calculated factor.
	 */
	// TODO TEST ME
	private static double calculateProductPriceFactor(double[] values, double priceFactorBaseScale) {
		double averageCourseValue = Util.calculateAverage(values);

		return 1 / (averageCourseValue / priceFactorBaseScale);
	}

	/**
	 * ======================================================================
	 * OVERRIDES
	 * ======================================================================
	 */
	@GeneratedCode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseValue == null) ? 0 : baseValue.hashCode());
		long temp;
		temp = Double.doubleToLongBits(capital);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((diversificationMultiplier == null) ? 0 : diversificationMultiplier.hashCode());
		result = prime * result + ((ruleContainer == null) ? 0 : ruleContainer.hashCode());
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@GeneratedCode
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubSystem other = (SubSystem) obj;
		if (baseValue == null) {
			if (other.baseValue != null)
				return false;
		} else if (!baseValue.equals(other.baseValue))
			return false;
		if (Double.doubleToLongBits(capital) != Double.doubleToLongBits(other.capital))
			return false;
		if (diversificationMultiplier == null) {
			if (other.diversificationMultiplier != null)
				return false;
		} else if (!diversificationMultiplier.equals(other.diversificationMultiplier))
			return false;
		if (ruleContainer == null) {
			if (other.ruleContainer != null)
				return false;
		} else if (!ruleContainer.equals(other.ruleContainer))
			return false;
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		return true;
	}

	@GeneratedCode
	@Override
	public String toString() {
		return "SubSystem [baseValue=" + baseValue + ", ruleContainers=" + ruleContainer
				+ ", diversificationMultiplier=" + diversificationMultiplier + ", capital=" + capital + ", weight="
				+ weight + "]";
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
	private void setRuleContainer(RuleContainer ruleContainer) {
		this.ruleContainer = ruleContainer;
	}

	/**
	 * @return
	 */
	public RuleContainer getRuleContainer() {
		return this.ruleContainer;
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

	/**
	 * @return combinedForecasts SubSystem
	 */
	public ValueDateTupel[] getCombinedForecasts() {
		return combinedForecasts;
	}

	/**
	 * @param combinedForecasts the combinedForecasts to set
	 */
	public void setCombinedForecasts(ValueDateTupel[] combinedForecasts) {
		this.combinedForecasts = combinedForecasts;
	}

}
