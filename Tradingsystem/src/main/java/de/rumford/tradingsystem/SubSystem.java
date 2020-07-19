package de.rumford.tradingsystem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.Validator;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * The SubSystem is the most parental Structure in this library and contains (directly or indirectly) all other
 * (non-static) classes. It combines rules and base values and is thus the only point to calculate positions and perform
 * backtesting of performance. By containing a {@link DiversificationMultiplier} it copes with the diversity between its
 * rules and thus meets volatility target and scale.
 * 
 * @author Max Rumford
 */
public class SubSystem {

	/*
	 * The value to which the product prices shall be scaled to. Effect rises if higher and capital goes lower.
	 */
	private static final BigDecimal PRICE_FACTOR_BASE_SCALE = BigDecimal.valueOf(1d);

	/* An Exception message. */
	private static final String MESSAGE_ILLEGAL_TEST_WINDOW = "The given test window does not meet specifications.";

	/* The base value to use for performance calculation. */
	private BaseValue baseValue;
	/* The rules to be governed in this subsystem. */
	private Rule[] rules;
	/* The diversification multiplier for this subsystem. */
	private DiversificationMultiplier diversificationMultiplier;
	/* The starting capital used for performance calculation. */
	private BigDecimal capital;
	/* The combined forecasts of all rules. */
	private ValueDateTupel[] combinedForecasts;
	/* The value all forecasts shall be scaled to. */
	private BigDecimal baseScale;

	/**
	 * Constructor for the SubSystem class.
	 * 
	 * @param baseValue {@link BaseValue} The base value to be used for all the given rules' calculations. See
	 *                  {@link #validateInput( BaseValue, Rule[], BigDecimal, BigDecimal)} for limitations.
	 * @param rules     {@code Rule[]} Array of {@link Rule} to be used for forecast calculations in this SubSystem. See
	 *                  {@link #validateInput( BaseValue, Rule[], BigDecimal, BigDecimal)} for limitations.
	 * @param capital   {@code BigDecimal} The capital to be managed by this SubSystem. See
	 *                  {@link #validateInput( BaseValue, Rule[], BigDecimal, BigDecimal)} for limitations.
	 * @param baseScale {@code BigDecimal} The base scale for this SubSystem's forecasts. See
	 *                  {@link #validateInput( BaseValue, Rule[], BigDecimal, BigDecimal)} for limitations.
	 */
	public SubSystem(BaseValue baseValue, Rule[] rules, BigDecimal capital, BigDecimal baseScale) {

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
	 * Performs a backtest for the given parameters. Utilizes
	 * {@link #calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, ValueDateTupel[], BigDecimal, BigDecimal)}
	 * for actual performance calculation and returns performance value for the last day.
	 * 
	 * @see                      SubSystem#calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime,
	 *                           ValueDateTupel[], BigDecimal, BigDecimal)
	 * @param  baseValue         {@link BaseValue} The base value to be tested against.
	 * @param  startOfTestWindow {@link LocalDateTime} First time interval of test window.
	 * @param  endOfTestWindow   {@link LocalDateTime} Last time interval of test window.
	 * @param  combinedForecasts {@code ValueDateTupel[]} The forecasts to be used for performance calculation.
	 * @param  baseScale         {@code BigDecimal} The value to which to scale the forecasts to.
	 * @param  capital           {@code BigDecimal} The starting capital.
	 * @return                   {@code BigDecimal} The performance value on the last day of the given test window.
	 */
	public static BigDecimal backtest(BaseValue baseValue, LocalDateTime startOfTestWindow,
	        LocalDateTime endOfTestWindow, ValueDateTupel[] combinedForecasts, BigDecimal baseScale,
	        BigDecimal capital) {

		ValueDateTupel[] performanceValues = calculatePerformanceValues(baseValue, startOfTestWindow, endOfTestWindow,
		        combinedForecasts, baseScale, capital);

		return performanceValues[performanceValues.length - 1].getValue();
	}

	/**
	 * Calls
	 * {@link #calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, ValueDateTupel[], BigDecimal, BigDecimal)}
	 * with instance properties.
	 * 
	 * @param  startOfTestWindow {@link LocalDateTime} First time interval of test window.
	 * @param  endOfTestWindow   {@link LocalDateTime} Last time interval of test window.
	 * @return                   {@code BigDecimal} by way of
	 *                           {@link #backtest(BaseValue, LocalDateTime, LocalDateTime, ValueDateTupel[], BigDecimal, BigDecimal)}.
	 */
	public BigDecimal backtest(LocalDateTime startOfTestWindow, LocalDateTime endOfTestWindow) {
		return SubSystem.backtest(this.getBaseValue(), startOfTestWindow, endOfTestWindow, this.getCombinedForecasts(),
		        this.getBaseScale(), this.getCapital());
	}

	/**
	 * Calculates the performance values for the given time frame, based on the given baseValue, forecasts, baseScale
	 * and capital.
	 * 
	 * @param  baseValue         {@link BaseValue} The base value upon which the products' prices are to be based.
	 * @param  startOfTestWindow {@link LocalDateTime} First time interval for testing.
	 * @param  endOfTestWindow   {@link LocalDateTime} Last time interval for testing.
	 * @param  combinedForecasts {@code ValueDateTupel[]} Array of {@link ValueDateTupel} containing the forecasts for
	 *                           this performance calculation.
	 * @param  baseScale         {@code BigDecimal} The scale the given forecasts are based upon.
	 * @param  capital           {@code BigDecimal} The starting capital.
	 * @return                   {@code ValueDateTupel[]} An array of {@link ValueDateTupel} containing the value of all
	 *                           held assets + cash for each time interval between the given startOfTestWindow and
	 *                           endOfTestWindow.
	 */
	public static ValueDateTupel[] calculatePerformanceValues(BaseValue baseValue, LocalDateTime startOfTestWindow,
	        LocalDateTime endOfTestWindow, ValueDateTupel[] combinedForecasts, BigDecimal baseScale,
	        BigDecimal capital) {

		try {
			Validator.validateTimeWindow(startOfTestWindow, endOfTestWindow, baseValue.getValues());
		} catch (IllegalArgumentException e) {
			/*
			 * If the message contains "values" the message references an error in the given base values in combination
			 * with the given test window.
			 */
			if (e.getMessage().contains("values"))
				throw new IllegalArgumentException("Given base value and test window do not fit.", e);

			throw new IllegalArgumentException(MESSAGE_ILLEGAL_TEST_WINDOW, e);
		}

		try {
			Validator.validateTimeWindow(startOfTestWindow, endOfTestWindow, combinedForecasts);
		} catch (IllegalArgumentException e) {
			/*
			 * The general checks of the test window would have thrown Exceptions in the previous try-catch, so here we
			 * only have to deal with combinedForecasts specific Exceptions.
			 */
			throw new IllegalArgumentException("Given forecasts and test window do not fit.", e);
		}

		/* Fetch all base values inside the test window */
		ValueDateTupel[] relevantBaseValues = ValueDateTupel.getElements(baseValue.getValues(), startOfTestWindow,
		        endOfTestWindow);

		/*
		 * Get the product price factor to calculate long and short product prices
		 */
		BigDecimal productPriceFactor = calculateProductPriceFactor(ValueDateTupel.getValues(relevantBaseValues));

		/*
		 * Calculate the product prices based on the base value for each interval inside the testing timespan and the
		 * calculated productPriceFactor
		 */
		ValueDateTupel[] productPrices = calculateProductPrices(relevantBaseValues, productPriceFactor);

		/*
		 * Calculate the short product prices based on the base value for each interval inside the testing timespan and
		 * the calculated productPriceFactor
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
			 * Calculate the capital available for this time interval by "selling" off all currently held positions at
			 * the this time interval's prices.
			 */
			capital = capital.add(productPrices[i].getValue().multiply(BigDecimal.valueOf(longProductsCount)));
			capital = capital.add(shortProductPrices[i].getValue().multiply(BigDecimal.valueOf(shortProductsCount)));

			/* Reset the products count as they were sold off */
			shortProductsCount = 0;
			longProductsCount = 0;

			/*
			 * Add this capital as performance value, as the overall value of cash + assets held will not change during
			 * buying.
			 */
			ValueDateTupel performanceValue = new ValueDateTupel(relevantCombinedForecasts[i].getDate(), capital);
			performanceValues = ArrayUtils.add(performanceValues, performanceValue);

			if (relevantCombinedForecasts[i].getValue().compareTo(BigDecimal.valueOf(0d)) > 0) {
				/* Long position */
				longProductsCount = calculateProductsCount(capital, productPrices[i].getValue(),
				        relevantCombinedForecasts[i].getValue(), baseScale);

				/*
				 * "Buy" the calculated count of products and thus reduce the cash capital
				 */
				capital = capital.subtract(productPrices[i].getValue().multiply(BigDecimal.valueOf(longProductsCount)));

			} else if (relevantCombinedForecasts[i].getValue().compareTo(BigDecimal.valueOf(0d)) < 0) {
				/* short position */
				shortProductsCount = calculateProductsCount(capital, shortProductPrices[i].getValue(),
				        relevantCombinedForecasts[i].getValue(), baseScale);

				/*
				 * "Buy" the calculated count of products and thus reduce the cash capital
				 */
				capital = capital
				        .subtract(shortProductPrices[i].getValue().multiply(BigDecimal.valueOf(shortProductsCount)));
			} else {
				/*
				 * If forecast was 0 nothing would be bought so no default-else branch is needed.
				 */
			}

		}
		return performanceValues;
	}

	/**
	 * Calls
	 * {@link #calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, ValueDateTupel[], BigDecimal, BigDecimal)}
	 * with instance properties.
	 * 
	 * @param  startOfTestWindow {@link LocalDateTime} First time interval for testing.
	 * @param  endOfTestWindow   {@link LocalDateTime} Last time interval for testing.
	 * @return                   {@code ValueDateTupel[]} by way of
	 *                           {@link #calculatePerformanceValues(BaseValue, LocalDateTime, LocalDateTime, ValueDateTupel[], BigDecimal, BigDecimal)}.
	 */
	public ValueDateTupel[] calculatePerformanceValues(LocalDateTime startOfTestWindow, LocalDateTime endOfTestWindow) {
		return SubSystem.calculatePerformanceValues(this.getBaseValue(), startOfTestWindow, endOfTestWindow,
		        this.getCombinedForecasts(), this.getBaseScale(), this.getCapital());
	}

	/**
	 * Calculates the combined forecasts for all rules of this Sub System.
	 * 
	 * @return {@code ValueDateTupel[]} The combined forecasts for all rules, multiplied by
	 *         {@link DiversificationMultiplier#getValue()} of this Sub System.
	 */
	private ValueDateTupel[] calculateCombinedForecasts() {
		Rule[] instanceRules = this.getRules();
		/*
		 * Calculate the weight by which all rules' forecasts shall be multiplied by
		 */
		BigDecimal rulesWeight = BigDecimal.valueOf(1d).divide(BigDecimal.valueOf(instanceRules.length));

		ValueDateTupel[] calculatedCombinedForecasts = {};

		/* Step through the given rules */
		for (int rulesIndex = 0; rulesIndex < instanceRules.length; rulesIndex++) {

			/* For each rule: Step through the forecasts */
			ValueDateTupel[] forecasts = instanceRules[rulesIndex].getForecasts();
			for (int fcIndex = 0; fcIndex < forecasts.length; fcIndex++) {

				if (rulesIndex == 0) {
					/*
					 * Combined forecasts must be filled with values on first go-through
					 */
					ValueDateTupel vdtToAdd = new ValueDateTupel(forecasts[fcIndex].getDate(),
					        forecasts[fcIndex].getValue().multiply(rulesWeight));
					calculatedCombinedForecasts = ArrayUtils.add(calculatedCombinedForecasts, vdtToAdd);
				} else {
					/*
					 * If this is not the first go-through add the weighted forecasts of the current rule
					 */
					ValueDateTupel vdtToAdd = new ValueDateTupel(calculatedCombinedForecasts[fcIndex].getDate(),
					        calculatedCombinedForecasts[fcIndex].getValue() //
					                .add( //
					                        forecasts[fcIndex].getValue().multiply(rulesWeight) //
									));
					calculatedCombinedForecasts[fcIndex] = vdtToAdd;
				}
			}
		}

		/*
		 * Apply Diversification Multiplier to all forecast values. Cut off Forecast values at 2 x base scale or -2 x
		 * base scale respectively
		 */
		final BigDecimal instanceBaseScale = this.getBaseScale();
		final BigDecimal diversificationMultiplierValue = this.getDiversificationMultiplier().getValue();
		final BigDecimal MAX_VALUE = instanceBaseScale.multiply(BigDecimal.valueOf(2d));
		final BigDecimal MIN_VALUE = BigDecimal.valueOf(0d).subtract(MAX_VALUE);

		for (int fcIndex = 0; fcIndex < calculatedCombinedForecasts.length; fcIndex++) {
			BigDecimal fcWithDM = calculatedCombinedForecasts[fcIndex].getValue()
			        .multiply(diversificationMultiplierValue);
			if (fcWithDM.compareTo(MAX_VALUE) > 0)
				fcWithDM = MAX_VALUE;
			if (fcWithDM.compareTo(MIN_VALUE) < 0)
				fcWithDM = MIN_VALUE;

			calculatedCombinedForecasts[fcIndex].setValue(fcWithDM);
		}

		return calculatedCombinedForecasts;
	}

	/**
	 * Call {@link SubSystem#calculateProductPriceFactor(BigDecimal[], BigDecimal)} passing PRICE_FACTOR_BASE_SCALE as
	 * param.
	 * 
	 * @see           SubSystem#calculateProductPriceFactor(BigDecimal[], BigDecimal)
	 * @param  values {@code BigDecimal[]} An Array of values the factor is to be calculated for
	 * @return        {@code BigDecimal} The calculated factor.
	 */
	private static BigDecimal calculateProductPriceFactor(BigDecimal[] values) {
		return SubSystem.calculateProductPriceFactor(values, PRICE_FACTOR_BASE_SCALE);
	}

	/**
	 * Calculate the factor by which all of the given values must be multiplied so their products have an average of
	 * priceFactorBaseScale. <br>
	 * The factor is calculated by inverting the average of the given values divided by the given priceFactorBaseScale.
	 * 
	 * @param  values               {@code BigDecimal[]} An Array of values the factor is to be calculated for
	 * @param  priceFactorBaseScale {@code BigDecimal} The base scale to use.
	 * @return                      {@code BigDecimal} The calculated factor.
	 */
	private static BigDecimal calculateProductPriceFactor(BigDecimal[] values, BigDecimal priceFactorBaseScale) {
		BigDecimal averageCourseValue = Util.calculateAverage(values);

		return BigDecimal.valueOf(1d).divide( //
		        averageCourseValue.divide(priceFactorBaseScale) //
		);
	}

	/**
	 * Calculate product prices based on the given array of values and a given product price factor.
	 * 
	 * @param  baseValues         {@code ValueDateTupel[]} The values the prices are to be based on.
	 * @param  productPriceFactor {@code BigDecimal} The factor used to calculate the product prices.
	 * @return                    {@code ValueDateTupel[]} An array of prices using the dates of the given baseValues.
	 */
	private static ValueDateTupel[] calculateProductPrices(ValueDateTupel[] baseValues, BigDecimal productPriceFactor) {
		ValueDateTupel[] productPrices = {};
		for (ValueDateTupel baseValue : baseValues)
			productPrices = ValueDateTupel.addOneAt(productPrices,
			        new ValueDateTupel(baseValue.getDate(), baseValue.getValue().multiply(productPriceFactor)),
			        productPrices.length);

		return productPrices;
	}

	/**
	 * Calculates the products to buy during a trading period according to the given price and given forecast.
	 * 
	 * @param  capital   {@code BigDecimal} The capital available for trading.
	 * @param  price     {@code BigDecimal} The price at which a product can be bought.
	 * @param  forecast  {@code BigDecimal} The forecast for the current trading period.
	 * @param  baseScale {@code BigDecimal} The base scale by which the given forecast is scaled.
	 * @return           {@code int} The number of products to buy.
	 */
	private static long calculateProductsCount(BigDecimal capital, BigDecimal price, BigDecimal forecast,
	        BigDecimal baseScale) {
		/* Number of products if forecast had MAX_VALUE */
		BigDecimal maxProductsCount = capital.divide(price);

		/* Number of products if forecast was 1 */
		BigDecimal fcOneProductsCounts = maxProductsCount.divide( //
		        baseScale.multiply(BigDecimal.valueOf(2d)) //
		);

		/*
		 * Invert current forecast if it's negative to always generate a positive number of products
		 */
		if (forecast.compareTo(BigDecimal.valueOf(0d)) < 0)
			forecast = forecast.multiply(BigDecimal.valueOf(-1d));

		/*
		 * Number of products for actual forecast. Accept rounding inaccuracies.
		 */
		return fcOneProductsCounts.multiply(forecast).longValue();
	}

	/**
	 * Validate the given input parameters.
	 * 
	 * @param  baseValue                {@link BaseValue} The baseValue to validate. Must pass
	 *                                  {@link Validator#validateBaseValue(BaseValue)}.
	 * @param  rules                    {@code Rule[]} The rules to validate.
	 *                                  <ul>
	 *                                  <li>Must pass {@link Validator#validateRules(Rule[])}</li>
	 *                                  <li>Each rule's base value must be same as the given base value.</li>
	 *                                  </ul>
	 * @param  capital                  {@code BigDecimal} The capital to validate. Must pass
	 *                                  {@link Validator#validatePositiveDouble(BigDecimal)}.
	 * @param  baseScale                {@code BigDecimal} The base scale to validate. Must pass
	 *                                  {@link Validator#validatePositiveDouble(BigDecimal)}.
	 * @throws IllegalArgumentException if any of the above criteria is not met.
	 */
	private static void validateInput(BaseValue baseValue, Rule[] rules, BigDecimal capital, BigDecimal baseScale) {
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

		Validator.validateRulesVsBaseScale(rules, baseScale);
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
	 * @param  rules                    {@code Rule[]} Rules that are to be checked.
	 * @throws IllegalArgumentException if the given rules are not unique.
	 */
	private static void validateRules(Rule[] rules) {
		if (!Util.areRulesUnique(rules))
			throw new IllegalArgumentException("The given rules are not unique." + " Only unique rules can be used.");

		/* All rules need to have the same reference window */
		for (int i = 1; i < rules.length; i++) {
			if (!rules[i].getStartOfReferenceWindow().isEqual(rules[i - 1].getStartOfReferenceWindow())
			        || !rules[i].getEndOfReferenceWindow().isEqual(rules[i - 1].getEndOfReferenceWindow())) {
				throw new IllegalArgumentException("All rules need to have the same reference window but rules at"
				        + " position " + (i - 1) + " and " + i + " differ.");
			}
		}

	}

	/**
	 * ====================================================================== OVERRIDES
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
		result = prime * result + ((baseScale == null) ? 0 : baseScale.hashCode());
		result = prime * result + ((baseValue == null) ? 0 : baseValue.hashCode());
		result = prime * result + ((capital == null) ? 0 : capital.hashCode());
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
		if (baseScale == null) {
			if (other.baseScale != null)
				return false;
		} else if (!baseScale.equals(other.baseScale))
			return false;
		if (baseValue == null) {
			if (other.baseValue != null)
				return false;
		} else if (!baseValue.equals(other.baseValue))
			return false;
		if (capital == null) {
			if (other.capital != null)
				return false;
		} else if (!capital.equals(other.capital))
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
	 * ====================================================================== GETTERS AND SETTERS
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
	public BigDecimal getCapital() {
		return capital;
	}

	/**
	 * Set the capital for this subsystem.
	 * 
	 * @param capital the capital to set
	 */
	private void setCapital(BigDecimal capital) {
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
	public BigDecimal getBaseScale() {
		return baseScale;
	}

	/**
	 * Set the base scale for this subsystem.
	 * 
	 * @param baseScale the baseScale to set
	 */
	public void setBaseScale(BigDecimal baseScale) {
		this.baseScale = baseScale;
	}
}