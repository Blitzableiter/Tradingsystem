package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * The SubSystem is the most parental Structure in this library and contains
 * (directly or indirectly) all other (non-static) classes. It combines rules
 * and base values and is thus the only point to calculate positions and perform
 * backtesting of performance. By containing a {@link DiversificationMultiplier}
 * it copes with the diversity between its rules and thus meets volatility
 * target and scale.
 * 
 * @author Max Rumford
 *
 */
public class SubSystem {

	private static final double PRICE_FACTOR_BASE_SCALE = 1;

	private BaseValue baseValue;
	private Rule[] rules;
	private DiversificationMultiplier diversificationMultiplier;
	private double capital;
	private ValueDateTupel[] combinedForecasts;
	private double baseScale;

	/**
	 * Constructor for the SubSystem class.
	 * 
	 * @param baseValue {@link BaseValue} The base value to be used for all the
	 *                  given rules' calculations. See
	 *                  {@link #validateInput(BaseValue, Rule[], double, double)}
	 *                  for limitations.
	 * @param rules     {@code Rule[]} Array of {@link Rule} to be used for forecast
	 *                  calculations in this SubSystem. See
	 *                  {@link #validateInput(BaseValue, Rule[], double, double)}
	 *                  for limitations.
	 * @param capital   {@code double} The capital to be managed by this SubSystem.
	 *                  See
	 *                  {@link #validateInput(BaseValue, Rule[], double, double)}
	 *                  for limitations.
	 * @param baseScale {@code double} The base scale for this SubSystem's
	 *                  forecasts. See
	 *                  {@link #validateInput(BaseValue, Rule[], double, double)}
	 *                  for limitations.
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
	private ValueDateTupel[] calculateCombinedForecasts() {
		Rule[] instanceRules = this.getRules();
		/* Calculate the weight by which all rules' forecasts shall be multiplied by */
		double rulesWeight = 1d / instanceRules.length;

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
	 * Performs a backtest for this SubSystem in the given time period. Utilizes
	 * {@link #calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, ValueDateTupel[], double, double)}
	 * for actual performance calculation and returns performance value for the last
	 * day.
	 * 
	 * @see SubSystem#calculatePerformanceValues(BaseValue, LocalDateTime,
	 *      LocalDateTime, ValueDateTupel[], double, double)
	 * 
	 * @param startOfTestWindow {@link LocalDateTime} First time interval of test
	 *                          window.
	 * @param endOfTestWindow   {@link LocalDateTime} Last time interval of test
	 *                          window.
	 * @return {@code double} The performance value on the last day of the given
	 *         test window.
	 */
	public double backtest(LocalDateTime startOfTestWindow, LocalDateTime endOfTestWindow) {
		if (startOfTestWindow == null)
			throw new IllegalArgumentException("Start of test window must not be null");
		if (endOfTestWindow == null)
			throw new IllegalArgumentException("End of test window must not be null");
		if (!endOfTestWindow.isAfter(startOfTestWindow))
			throw new IllegalArgumentException("End of test window must be after start of test window.");

		BaseValue instanceBaseValue = this.getBaseValue();
		double capitalAfterBacktest = this.getCapital();

		ValueDateTupel[] performanceValues = calculatePerformanceValues(instanceBaseValue, startOfTestWindow,
				endOfTestWindow, this.getCombinedForecasts(), this.getBaseScale(), capitalAfterBacktest);

		return performanceValues[performanceValues.length - 1].getValue();
	}

	/**
	 * Calculates the performance values for the given time frame, based on the
	 * given baseValue, forecasts, baseScale and capital.
	 * 
	 * @param baseValue         {@link BaseValue} The base value upon which the
	 *                          products' prices are to be based.
	 * @param startOfTestWindow {@link LocalDateTime} First time interval for
	 *                          testing.
	 * @param endOfTestWindow   {@link LocalDateTime} Last time interval for
	 *                          testing.
	 * @param combinedForecasts {@code ValueDateTupel[]} Array of
	 *                          {@link ValueDateTupel} containing the forecasts for
	 *                          this performance calculation.
	 * @param baseScale         {@code double} The scale the given forecasts are
	 *                          based upon.
	 * @param capital           {@code double} The starting capital.
	 * @return {@code ValueDateTupel} An array of {@link ValueDateTupel} containing
	 *         the value of all held assets + cash for each time interval between
	 *         the given startOfTestWindow and endOfTestWindow.
	 */
	public static ValueDateTupel[] calculatePerformanceValues(BaseValue baseValue, LocalDateTime startOfTestWindow,
			LocalDateTime endOfTestWindow, ValueDateTupel[] combinedForecasts, double baseScale, double capital) {

		if (startOfTestWindow == null)
			throw new IllegalArgumentException("The given start of test window must not be null");
		if (endOfTestWindow == null)
			throw new IllegalArgumentException("The given end of test window must not be null");
		if (!endOfTestWindow.isAfter(startOfTestWindow))
			throw new IllegalArgumentException("The end of test window must be after the start of the test window");

		if (!ValueDateTupel.containsDate(baseValue.getValues(), startOfTestWindow))
			throw new IllegalArgumentException(
					"The given start of test window is not contained in the base value for this subsystem.");
		if (!ValueDateTupel.containsDate(baseValue.getValues(), endOfTestWindow))
			throw new IllegalArgumentException(
					"The given end of test window is not contained in the base value for this subsystem.");

		if (!ValueDateTupel.containsDate(combinedForecasts, startOfTestWindow))
			throw new IllegalArgumentException(
					"No forecast for given start of test window. Start of test window is probably before start of reference window");

		/* Fetch all base values inside the test window */
		ValueDateTupel[] relevantBaseValues = ValueDateTupel.getElements(baseValue.getValues(), startOfTestWindow,
				endOfTestWindow);

		/* Get the product price factor to calculate long and short product prices */
		double productPriceFactor = calculateProductPriceFactor(ValueDateTupel.getValues(relevantBaseValues));

		/*
		 * Calculate the product prices based on the base value for each interval inside
		 * the testing timespan and the calculated productPriceFactor
		 */
		ValueDateTupel[] productPrices = calculateProductPrices(relevantBaseValues, productPriceFactor);

		/*
		 * Calculate the short product prices based on the base value for each interval
		 * inside the testing timespan and the calculated productPriceFactor
		 */
		ValueDateTupel[] relevantShortIndexValues = ValueDateTupel.getElements(baseValue.getShortIndexValues(),
				startOfTestWindow, endOfTestWindow);
		ValueDateTupel[] shortProductPrices = calculateProductPrices(relevantShortIndexValues, productPriceFactor);

		/* Fetch all forecasts relevant for this backtest. */
		ValueDateTupel[] relevantCombinedForecasts = ValueDateTupel.getElements(combinedForecasts, startOfTestWindow,
				endOfTestWindow);

		ValueDateTupel[] performanceValues = {};

		long longProductsCount = 0;
		long shortProductsCount = 0;
		for (int i = 0; i < relevantCombinedForecasts.length; i++) {

			/*
			 * Calculate the capital available for this time interval by "selling" off all
			 * currently held positions at the this time interval's prices.
			 */
			capital += longProductsCount * productPrices[i].getValue();
			capital += shortProductsCount * shortProductPrices[i].getValue();

			/* Reset the products count as they were sold off */
			shortProductsCount = 0;
			longProductsCount = 0;

			/*
			 * Add this capital as performance value, as the overall value of cash + assets
			 * held will not change during buying.
			 */
			ValueDateTupel performanceValue = new ValueDateTupel(relevantCombinedForecasts[i].getDate(), capital);
			performanceValues = ArrayUtils.add(performanceValues, performanceValue);

			if (relevantCombinedForecasts[i].getValue() > 0) {
				/* Long position */
				longProductsCount = calculateProductsCount(capital, productPrices[i].getValue(),
						relevantCombinedForecasts[i].getValue(), baseScale);

				/* "Buy" the calculated count of products and thus reduce the cash capital */
				capital -= longProductsCount * productPrices[i].getValue();

			} else if (relevantCombinedForecasts[i].getValue() < 0) {
				/* short position */
				shortProductsCount = calculateProductsCount(capital, shortProductPrices[i].getValue(),
						relevantCombinedForecasts[i].getValue(), baseScale);

				/* "Buy" the calculated count of products and thus reduce the cash capital */
				capital -= shortProductsCount * shortProductPrices[i].getValue();
			} else {
				/*
				 * If forecast was 0 nothing would be bought so no default-else branch is
				 * needed.
				 */
			}

		}
		return performanceValues;
	}

	/**
	 * Calculates the products to buy during a trading period according to the given
	 * price and given forecast.
	 * 
	 * @param capital   {@code double} The capital available for trading.
	 * @param price     {@code double} The price at which a product can be bought.
	 * @param forecast  {@code double} The forecast for the current trading period.
	 * @param baseScale {@code double} The base scale by which the given forecast is
	 *                  scaled.
	 * @return {@code int} The number of products to buy.
	 */
	private static long calculateProductsCount(double capital, double price, double forecast, double baseScale) {
		/* Number of products if forecast had MAX_VALUE */
		double maxProductsCount = capital / price;

		/* Number of products if forecast was 1 */
		double fcOneProductsCounts = maxProductsCount / (baseScale * 2);

		/*
		 * Invert current forecast if it's negative to always generate a positive number
		 * of products
		 */
		if (forecast < 0)
			forecast *= -1;

		/* Number of products for actual forecast. Accept rounding inaccuracies. */
		return (long) (fcOneProductsCounts * forecast);
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
			productPrices = ValueDateTupel.addOneAt(productPrices,
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
	 * Validate the given input parameters.
	 * 
	 * @param baseValue {@link BaseValue} The baseValue to validate. Must not be
	 *                  null.
	 * @param rules     {@code Rule[]} The rules to validate.
	 *                  <ul>
	 *                  <li>Must not be null.</li>
	 *                  <li>Must not be an empty array.</li>
	 *                  <li>Each rule's base value must be same as the given base
	 *                  value.</li>
	 *                  </ul>
	 * @param capital   {@code double} The capital to validate.
	 *                  <ul>
	 *                  <li>Must not be Double.NaN.</li>
	 *                  <li>Must be > 0.</li>
	 *                  </ul>
	 * @param baseScale {@code double} The base scale to validate.
	 *                  <ul>
	 *                  <li>Must not be Double.NaN.</li>
	 *                  <li>Must be > 0</li>
	 *                  </ul>
	 * @throws IllegalArgumentException if any of the above criteria is not met.
	 */
	private static void validateInput(BaseValue baseValue, Rule[] rules, double capital, double baseScale) {
		if (baseValue == null)
			throw new IllegalArgumentException("Base value must not be null");

		if (rules == null)
			throw new IllegalArgumentException("Rules must not be null");

		if (rules.length == 0)
			throw new IllegalArgumentException("Rules must not be an empty array");

		for (int i = 0; i < rules.length; i++)
			if (!rules[i].getBaseValue().equals(baseValue))
				throw new IllegalArgumentException(
						"The base value of all rules must be equal to given base value but the rule at position " + i
								+ " does not comply.");

		if (Double.isNaN(capital))
			throw new IllegalArgumentException("Capital must not be Double.NaN");

		if (capital <= 0)
			throw new IllegalArgumentException("Capital must be a positive decimal");

		if (Double.isNaN(baseScale))
			throw new IllegalArgumentException("Base scale must not be NaN");

		if (baseScale <= 0)
			throw new IllegalArgumentException("Base scale must be a positive decimal");
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
	 * Check if the given rules are unique by utilizing {@link Rule#equals(Object)}
	 * 
	 * @param rules {@code Rule} An array of rules to be check for uniqueness.
	 * @return {@code boolean} True, if the rules are unique. False otherwise.
	 */
	static boolean areRulesUnique(Rule[] rules) {
		if (rules == null)
			throw new IllegalArgumentException("The given rules must not be null");
		if (ArrayUtils.contains(rules, null))
			throw new IllegalArgumentException("The given array must not contain nulls");
		if (rules.length == 0)
			throw new IllegalArgumentException("The given array of rules must not be empty.");

		for (int i = 0; i < rules.length - 1; i++) {
			if (rules[i].equals(rules[i + 1])) {
				return false;
			}
		}
		return true;
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
		long temp;
		temp = Double.doubleToLongBits(baseScale);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((baseValue == null) ? 0 : baseValue.hashCode());
		temp = Double.doubleToLongBits(capital);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(combinedForecasts);
		result = prime * result + ((diversificationMultiplier == null) ? 0 : diversificationMultiplier.hashCode());
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
		SubSystem other = (SubSystem) obj;
		if (Double.doubleToLongBits(baseScale) != Double.doubleToLongBits(other.baseScale))
			return false;
		if (baseValue == null) {
			if (other.baseValue != null)
				return false;
		} else if (!baseValue.equals(other.baseValue))
			return false;
		if (Double.doubleToLongBits(capital) != Double.doubleToLongBits(other.capital))
			return false;
		if (!Arrays.equals(combinedForecasts, other.combinedForecasts))
			return false;
		if (diversificationMultiplier == null) {
			if (other.diversificationMultiplier != null)
				return false;
		} else if (!diversificationMultiplier.equals(other.diversificationMultiplier))
			return false;
		if (!Arrays.equals(rules, other.rules))
			return false;
		return true;
	}

	@GeneratedCode
	@Override
	public String toString() {
		return "SubSystem [baseValue=" + baseValue + ", diversificationMultiplier=" + diversificationMultiplier
				+ ", capital=" + capital + ", combinedForecasts=" + Arrays.toString(combinedForecasts) + ", baseScale="
				+ baseScale + ", rules=" + Arrays.toString(rules) + "]";
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

}
