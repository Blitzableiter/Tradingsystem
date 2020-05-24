package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.Validator;
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

	/*
	 * The value to which the product prices shall be scaled to. Effect rises if
	 * higher and capital goes lower.
	 */
	private static final double PRICE_FACTOR_BASE_SCALE = 1;

	/* An Exception message. */
	private static final String MESSAGE_ILLEGAL_TEST_WINDOW = "The given test window does not meet specifications.";

	/* The base value to use for performance calculation. */
	private BaseValue baseValue;
	/* The rules to be governed in this subsystem. */
	private Rule[] rules;
	/* The diversification multiplier for this subsystem. */
	private DiversificationMultiplier diversificationMultiplier;
	/* The starting capital used for performance calculation. */
	private double capital;
	/* The combined forecasts of all rules. */
	private ValueDateTupel[] combinedForecasts;
	/* The value all forecasts shall be scaled to. */
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

		validateRules(rules);
		this.setRules(rules);

		this.setBaseValue(baseValue);
		this.setCapital(capital);
		this.setBaseScale(baseScale);
		this.setDiversificationMultiplier(new DiversificationMultiplier(rules));
		this.setCombinedForecasts(this.calculateCombinedForecasts());
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

		try {
			Validator.validateTimeWindow(startOfTestWindow, endOfTestWindow, baseValue.getValues());
		} catch (IllegalArgumentException e) {
			/*
			 * If the message contains "values" the message references an error in the given
			 * base values in combination with the given test window.
			 */
			if (e.getMessage().contains("values"))
				throw new IllegalArgumentException("Given base value and test window do not fit.", e);

			throw new IllegalArgumentException(MESSAGE_ILLEGAL_TEST_WINDOW, e);
		}

		try {
			Validator.validateTimeWindow(startOfTestWindow, endOfTestWindow, combinedForecasts);
		} catch (IllegalArgumentException e) {
			/*
			 * The general checks of the test window would have thrown Exceptions in the
			 * previous try-catch, so here we only have to deal with combinedForecasts
			 * specific Exceptions.
			 */
			throw new IllegalArgumentException("Given forecasts and test window do not fit.", e);
		}

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
	 * Validate the given input parameters.
	 * 
	 * @param baseValue {@link BaseValue} The baseValue to validate. Must pass
	 *                  {@link Validator#validateBaseValue(BaseValue)}.
	 * @param rules     {@code Rule[]} The rules to validate.
	 *                  <ul>
	 *                  <li>Must pass {@link Validator#validateRules(Rule[])}</li>
	 *                  <li>Each rule's base value must be same as the given base
	 *                  value.</li>
	 *                  </ul>
	 * @param capital   {@code double} The capital to validate. Must pass
	 *                  {@link Validator#validatePositiveDouble(double)}.
	 * @param baseScale {@code double} The base scale to validate. Must pass
	 *                  {@link Validator#validatePositiveDouble(double)}.
	 * @throws IllegalArgumentException if any of the above criteria is not met.
	 */
	private static void validateInput(BaseValue baseValue, Rule[] rules, double capital, double baseScale) {
		Validator.validateBaseValue(baseValue);

		Validator.validateRules(rules);

		Validator.validateRulesVsBaseValue(rules, baseValue);

		try {
			Validator.validatePositiveDouble(capital);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Given capital does not meet specifications.", e);
		}

		try {
			Validator.validatePositiveDouble(baseScale);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Given base scale does not meet specifications.", e);
		}
	}

	/**
	 * Validate the given rules. <br>
	 * Rules have to fulfill the following criteria:
	 * <ul>
	 * <li>Be unique by {@link Util#areRulesUnique(Rule[])}</li>
	 * <li>Equal in startOfReferenceWindow and endOfReferenceWinow by
	 * {@link LocalDateTime#isEqual(ChronoLocalDateTime)}</li>
	 * </ul>
	 * 
	 * @param rules {@code Rule[]} Rules that are to be checked.
	 * @throws IllegalArgumentException if the given rules are not unique.
	 */
	private static void validateRules(Rule[] rules) {
		if (!Util.areRulesUnique(rules))
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
	 * ======================================================================
	 * OVERRIDES
	 * ======================================================================
	 */

	/**
	 * A hash code for this SubSystem.
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

	/**
	 * Checks if this SubSystem is equal to another SubSystem.
	 */
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

	/**
	 * Outputs the fields of this SubSystem as a {@code String}.
	 */
	@GeneratedCode
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubSystem [baseValue=");
		builder.append(baseValue);
		builder.append(", rules=");
		builder.append(Arrays.toString(rules));
		builder.append(", diversificationMultiplier=");
		builder.append(diversificationMultiplier);
		builder.append(", capital=");
		builder.append(capital);
		builder.append(", combinedForecasts=");
		builder.append(Arrays.toString(combinedForecasts));
		builder.append(", baseScale=");
		builder.append(baseScale);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */

	/**
	 * Get the {@link BaseValue} for this subsystem.
	 * 
	 * @return baseValue SubSystem
	 */
	public BaseValue getBaseValue() {
		return baseValue;
	}

	/**
	 * Set the {@link BaseValue} for this subsystem.
	 * 
	 * @param baseValue the baseValue to set
	 */
	private void setBaseValue(BaseValue baseValue) {
		this.baseValue = baseValue;
	}

	/**
	 * Get the rules for this subsystem.
	 * 
	 * @return rules SubSystem
	 */
	public Rule[] getRules() {
		return rules;
	}

	/**
	 * Set the rules for this subsystem.
	 * 
	 * @param rules the rules to set
	 */
	public void setRules(Rule[] rules) {
		this.rules = rules;
	}

	/**
	 * Get the {@link DiversificationMultiplier} for this subsystem.
	 * 
	 * @return diversificationMultiplier SubSystem
	 */
	public DiversificationMultiplier getDiversificationMultiplier() {
		return diversificationMultiplier;
	}

	/**
	 * Set the {@link DiversificationMultiplier} for this subsystem.
	 * 
	 * @param diversificationMultiplier the diversificationMultiplier to set
	 */
	private void setDiversificationMultiplier(DiversificationMultiplier diversificationMultiplier) {
		this.diversificationMultiplier = diversificationMultiplier;
	}

	/**
	 * Get the capital for this subsystem.
	 * 
	 * @return capital SubSystem
	 */
	public double getCapital() {
		return capital;
	}

	/**
	 * Set the capital for this subsystem.
	 * 
	 * @param capital the capital to set
	 */
	private void setCapital(double capital) {
		this.capital = capital;
	}

	/**
	 * Get the combined forecasts for this subsystem.
	 * 
	 * @return combinedForecasts SubSystem
	 */
	public ValueDateTupel[] getCombinedForecasts() {
		return combinedForecasts;
	}

	/**
	 * Set the combined forecasts for this subsystem.
	 * 
	 * @param combinedForecasts the combinedForecasts to set
	 */
	public void setCombinedForecasts(ValueDateTupel[] combinedForecasts) {
		this.combinedForecasts = combinedForecasts;
	}

	/**
	 * Get the base scale for this subsystem.
	 * 
	 * @return baseScale SubSystem
	 */
	public double getBaseScale() {
		return baseScale;
	}

	/**
	 * Set the base scale for this subsystem.
	 * 
	 * @param baseScale the baseScale to set
	 */
	public void setBaseScale(double baseScale) {
		this.baseScale = baseScale;
	}

}
