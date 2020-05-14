/**
 * 
 */
package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

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
	private DiversificationMultiplier diversificationMultiplier;
	private double capital;
	private double weight;
	private ValueDateTupel[] combinedForecasts;
	private double baseScale;
	private Rule[] rules;

	/**
	 * Constructor for the SubSystem class.
	 * 
	 * @param baseValue {@link BaseValue} The base value to be used for all the
	 *                  given rules' calculations.
	 * @param rules     {@code Rule[]} Array of {@link Rule} to be used for forecast
	 *                  calculations in this SubSystem.
	 * @param capital   {@code double} The capital to be managed by this SubSystem.
	 */
	public SubSystem(BaseValue baseValue, Rule[] rules, double capital, double baseScale) {

		validateInput(baseValue, rules, capital, baseScale);

		evaluateRules(rules);
		this.setRules(rules);

		this.setBaseValue(baseValue);
		this.setCapital(capital);
		this.setBaseScale(baseScale);
		this.setDiversificationMultiplier(new DiversificationMultiplier(rules));
		this.setCombinedForecasts(this.calculateCombinedForecasts());
	}

	/**
	 * Calculates the combined forecasts for all rules of this Sub System.
	 * 
	 * @return {@code ValueDateTupel[]} The combined forecasts for all rules,
	 *         multiplied by {@link DiversificationMultiplier#getValue()} of this
	 *         Sub System.
	 */
	// TODO TEST ME
	private ValueDateTupel[] calculateCombinedForecasts() {
		Rule[] instanceRules = this.getRules();
		/* Calculate the weight by which all rules' forecasts shall be multiplied by */
		double rulesWeight = instanceRules.length / 4d;

		ValueDateTupel[] calculatedCombinedForecasts = {};

		/* Step through the given rules */
		for (int rulesIndex = 0; rulesIndex < instanceRules.length; rulesIndex++) {

			/* For each rule: Step through the forecasts */
			ValueDateTupel[] forecasts = instanceRules[rulesIndex].getForecasts();
			for (int fcIndex = 0; fcIndex < forecasts.length; fcIndex++) {

				if (rulesIndex == 0) {
					/* Combined forecasts must be filled with values on first go-through */
					ValueDateTupel vdtToAdd = new ValueDateTupel(forecasts[fcIndex].getDate(),
							forecasts[fcIndex].getValue() * rulesWeight);
					calculatedCombinedForecasts = ArrayUtils.add(calculatedCombinedForecasts, vdtToAdd);
				} else {
					/*
					 * If this is not the first go-through add the weighted forecasts of the current
					 * rule
					 */
					ValueDateTupel vdtToAdd = new ValueDateTupel(calculatedCombinedForecasts[fcIndex].getDate(),
							calculatedCombinedForecasts[fcIndex].getValue()
									+ forecasts[fcIndex].getValue() * rulesWeight);
					calculatedCombinedForecasts[fcIndex] = vdtToAdd;
				}
			}
		}

		/*
		 * Apply Diversification Multiplier to all forecast values. Cut off Forecast
		 * values at 2 x base scale or -2 x base scale respectively
		 */
		final double instanceBaseScale = this.getBaseScale();
		final double diversificationMultiplierValue = this.getDiversificationMultiplier().getValue();
		final double MAX_VALUE = instanceBaseScale * 2;
		final double MIN_VALUE = 0 - MAX_VALUE;

		for (int fcIndex = 0; fcIndex < calculatedCombinedForecasts.length; fcIndex++) {
			double fcWithDM = calculatedCombinedForecasts[fcIndex].getValue() * diversificationMultiplierValue;
			if (fcWithDM > MAX_VALUE)
				fcWithDM = MAX_VALUE;
			if (fcWithDM < MIN_VALUE)
				fcWithDM = MIN_VALUE;

			calculatedCombinedForecasts[fcIndex].setValue(fcWithDM);
		}

		return calculatedCombinedForecasts;
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
	private static void validateInput(BaseValue baseValue, Rule[] rules, double capital, double baseScale) {
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

		if (Double.isNaN(baseScale))
			throw new IllegalArgumentException("Base scale must not be NaN");

		if (baseScale <= 0)
			throw new IllegalArgumentException("Base scale must be a positive decimal");
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

		ValueDateTupel[] combinedForecasts = ValueDateTupel.getElements(this.getCombinedForecasts(), startOfTestWindow,
				endOfTestWindow);

		int longProductsCount = 0;
		int shortProductsCount = 0;
		for (int i = 0; i < combinedForecasts.length; i++) {
			// TODO Position sizing

			/* Calculate the capital available for this time interval */
			capitalAfterBacktest += longProductsCount * productPrices[i].getValue();
			capitalAfterBacktest += shortProductsCount * shortProductPrices[i].getValue();

			//// Calculate current position

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
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		return true;
	}

	@GeneratedCode
	@Override
	public String toString() {
		return "SubSystem [baseValue=" + baseValue + ", diversificationMultiplier=" + diversificationMultiplier
				+ ", capital=" + capital + ", weight=" + weight + "]";
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
	public void setWeight(double weight) {
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

	/**
	 * @return baseScale SubSystem
	 */
	public double getBaseScale() {
		return baseScale;
	}

	/**
	 * @param baseScale the baseScale to set
	 */
	public void setBaseScale(double baseScale) {
		this.baseScale = baseScale;
	}

	/**
	 * @return rules SubSystem
	 */
	public Rule[] getRules() {
		return rules;
	}

	/**
	 * @param rules the rules to set
	 */
	public void setRules(Rule[] rules) {
		this.rules = rules;
	}

	/**
	 * @return priceFactorBaseScale SubSystem
	 */
	public static double getPriceFactorBaseScale() {
		return PRICE_FACTOR_BASE_SCALE;
	}
}
