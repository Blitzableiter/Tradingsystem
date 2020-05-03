package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.DoubleStream;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * 
 * Abstract class to be extend on developing new rules for the trading system.
 * 
 * @author Max Rumford
 * @apiNote {@link #calculateAndSetDerivedValues()} is called on first
 *          invocation of {@link #getSdAdjustedForecasts()}.
 *
 */
public abstract class Rule {

	private double forecastScalar;
	private double weight;
	private Rule[] variations;
	private LocalDateTime startOfReferenceWindow;
	private LocalDateTime endOfReferenceWindow;
	private BaseValue baseValue;
	private double baseScale;
	private ValueDateTupel[] forecasts;
	private ValueDateTupel[] sdAdjustedForecasts = null;

	/**
	 * Public constructor for class Rule. Rule is an abstract class and depends on
	 * the way of working of the extending class.
	 * 
	 * @param baseValue              {@link BaseValue} The base value to be used in
	 *                               this rule's calculations.
	 * @param variations             {@code Rule[]} An array of up to 3 rules (or
	 *                               null).
	 * @param startOfReferenceWindow {@link LocalDateTime} The first LocalDateTime
	 *                               to be considered in calculations such as
	 *                               forecast scalar.
	 * @param endOfReferenceWindow   {@link LocalDateTime} The last LocalDateTime to
	 *                               be considered in calculations such as forecast
	 *                               scalar.
	 * @param baseScale              {@code double} How the forecasts shall be
	 *                               scaled.
	 */
	public Rule(BaseValue baseValue, Rule[] variations, LocalDateTime startOfReferenceWindow,
			LocalDateTime endOfReferenceWindow, double baseScale) {

		this.validateInputs(baseValue, startOfReferenceWindow, endOfReferenceWindow, baseScale);

		this.setBaseValue(baseValue);
		this.setStartOfReferenceWindow(startOfReferenceWindow);
		this.setEndOfReferenceWindow(endOfReferenceWindow);
		this.validateSetAndWeighVariations(variations);
		this.setBaseScale(baseScale);
	}

	/**
	 * Method to be called by extending classes to calculate derived values. This
	 * takes into consideration that not all relevant values might be known upon
	 * call of Rule constructor.
	 */
	private void calculateAndSetDerivedValues() {
		this.setSdAdjustedForecasts(this.calculateSdAdjustedForecasts());
		this.setForecastScalar(this.calculateForecastScalar());
		this.setForecasts(this.calculateScaledForecasts());
	}

	/**
	 * Validates if the given instance variables meet specifications.
	 * 
	 * @param baseValue              {@link BaseValue} The base value to be used in
	 *                               this rule's calculations.
	 * @param startOfReferenceWindow {@link LocalDateTime} The first LocalDateTime
	 *                               to be considered in calculations such as
	 *                               forecast scalar.
	 * @param endOfReferenceWindow   {@link LocalDateTime} The last LocalDateTime to
	 *                               be considered in calculations such as forecast
	 *                               scalar.
	 * @param baseScale              {@code double} How the forecasts shall be
	 *                               scaled.
	 */
	private void validateInputs(BaseValue baseValue, LocalDateTime startOfReferenceWindow,
			LocalDateTime endOfReferenceWindow, double baseScale) {
		/* Check if base value fulfills requirements. */
		if (baseValue == null)
			throw new IllegalArgumentException("Base value must not be null");
		/* Check if LocalDates are null */
		if (startOfReferenceWindow == null)
			throw new IllegalArgumentException("Start of reference window value must not be null");
		if (endOfReferenceWindow == null)
			throw new IllegalArgumentException("End of reference window value must not be null");
		/* Check if reference window is properly defined: end must be after start */
		if (!endOfReferenceWindow.isAfter(startOfReferenceWindow))
			throw new IllegalArgumentException(
					"End of reference window value must be after start of reference window value");

		/*
		 * The given startOfReferenceWindow must be included in the given base values.
		 */
		if (!ValueDateTupel.containsDate(baseValue.getValues(), startOfReferenceWindow))
			throw new IllegalArgumentException("Base values do not include given start value for reference window");
		/*
		 * The given startOfReferenceWindow must be included in the given base values.
		 */
		if (!ValueDateTupel.containsDate(baseValue.getValues(), endOfReferenceWindow))
			throw new IllegalArgumentException("Base values do not include given end value for reference window");

		/* Check for a meaningful scale. */
		if (baseScale <= 0)
			throw new IllegalArgumentException("The given baseScale must a positiv non-zero decimal.");
	}

	/**
	 * Calculates the raw forecast of this rule for a given LocalDateTime. The
	 * calculation of this value will heavily depend on the type of rule extending
	 * this abstract class.
	 * 
	 * @param forecastDateTime {@link LocalDateTime} The dateTime the raw forecast
	 *                         shall be calculated for.
	 * @return {@code double} The raw forecast value for the given LocalDateTime.
	 */
	abstract double calculateRawForecast(LocalDateTime forecastDateTime);

	/**
	 * Calculates the standard deviation adjusted forecasts for this rule, beginning
	 * from the start of the instance's reference window.
	 * 
	 * @return {@code ValueDateTupel[]} An array of standard deviation adjusted
	 *         forecasts.
	 */
	private ValueDateTupel[] calculateSdAdjustedForecasts() {
		/*
		 * All dates from startOfReferenceWindow are relevant for the calculation
		 */
		LocalDateTime[] relevantDates = ValueDateTupel.getDates(
				ValueDateTupel.getElements(this.getBaseValue().getValues(), this.getStartOfReferenceWindow(), null));

		ValueDateTupel[] calculatedSdAdjustedForecasts = {};

		/* For all relevant dates: Calculate the sd adjusted forecast */
		for (LocalDateTime dt : relevantDates) {
			calculatedSdAdjustedForecasts = ArrayUtils.add(calculatedSdAdjustedForecasts,
					new ValueDateTupel(dt, this.calculateSdAdjustedForecast(dt)));
		}
		return calculatedSdAdjustedForecasts;
	}

	/**
	 * Calculates the scaled forecasts for this rule, starting from this rule's
	 * start of reference window.
	 * 
	 * @return {@code ValueDateTupel[]} This rules scaled forecasts.
	 */
	private ValueDateTupel[] calculateScaledForecasts() {
		return this.calculateScaledForecasts(this.getStartOfReferenceWindow(), null);
	}

	/**
	 * Calculates the scaled forecasts for a given window of time. If this rule has
	 * variations, their forecasts are used being weighted and scaled. If this rule
	 * has no variations, this rule's standard deviation adjusted forecasts are
	 * used.
	 * 
	 * @param calculateFrom {@link LocalDateTime} The starting dateTime.
	 * @param calculateTo   {@link LocalDateTime} The ending dateTime.
	 * @return {@code ValueDateTupel[]} An array of scaled forecasts.
	 */
	private ValueDateTupel[] calculateScaledForecasts(LocalDateTime calculateFrom, LocalDateTime calculateTo) {
		ValueDateTupel[] calculatedScaledForecasts = null;

		Rule[] instanceVariations = this.getVariations();
		/*
		 * If a rule has variations only their forecasts matter. This rule's forecasts
		 * are the set to equal the forecasts of its variations.
		 */
		if (instanceVariations != null) {
			ValueDateTupel[][] variationsForecasts = {};
			double[] variationsWeights = {};

			/* Extract forecasts and weights of all variations. */
			for (Rule variation : instanceVariations) {
				variationsForecasts = ArrayUtils.add(variationsForecasts, variation.getForecasts());
				variationsWeights = ArrayUtils.add(variationsWeights, variation.getWeight());
			}

			calculatedScaledForecasts = ValueDateTupel.createEmptyArray(variationsForecasts[0].length);

			/* Loop over all variations */
			for (int variationsIndex = 0; variationsIndex < variationsForecasts.length; variationsIndex++) {

				/* Loop over all forecasts for each variation. */
				for (int i = 0; i < variationsForecasts[variationsIndex].length; i++) {

					/* Add the weighted and scaled variation's forecast */
					double valueToBeAdded = variationsForecasts[variationsIndex][i].getValue()
							* variationsWeights[variationsIndex];

					/*
					 * If the scaled and weighted forecast value at this position is null (i.e. when
					 * we're in the first variation's loop) create a new ValueDateTupel
					 */
					if (calculatedScaledForecasts[i] == null) {
						calculatedScaledForecasts[i] = new ValueDateTupel(
								variationsForecasts[variationsIndex][i].getDate(), valueToBeAdded);
					} else {
						/*
						 * If there already is a value at position i add the weighted and scaled
						 * forecast.
						 */
						calculatedScaledForecasts[i].setValue(calculatedScaledForecasts[i].getValue() + valueToBeAdded);
					}
				}
			}
			return calculatedScaledForecasts;
		}
		/*
		 * If the Rule does not have variations, use the sd adjusted forecasts of this
		 * Rule alone.
		 */
		LocalDateTime[] relevantDates = ValueDateTupel
				.getDates(ValueDateTupel.getElements(this.getBaseValue().getValues(), calculateFrom, calculateTo));

		ValueDateTupel[] instanceSdAdjustedForecasts = this.getSdAdjustedForecasts();

		for (int i = 0; i < relevantDates.length; i++) {
			LocalDateTime dt = relevantDates[i];
			calculatedScaledForecasts = ArrayUtils.add(calculatedScaledForecasts,
					new ValueDateTupel(dt, this.calculateScaledForecast(instanceSdAdjustedForecasts[i].getValue())));
		}
		return calculatedScaledForecasts;
	}

	/**
	 * Calculates the standard deviation adjusted Forecast for a given
	 * LocalDateTime.
	 * 
	 * @param forecastDateTime {@link LocalDateTime} The LocalDateTime the forecast
	 *                         is to be calculated of.
	 * @return {@code double} The standard deviation adjusted value. Double.NaN if
	 *         the standard deviation at the given LocalDateTime is zero.
	 */
	private double calculateSdAdjustedForecast(LocalDateTime forecastDateTime) {
		if (this.getVariations() != null) {
			return Double.NaN;
		}

		double rawForecast = this.calculateRawForecast(forecastDateTime);

		double sdValue = ValueDateTupel.getElement(this.getBaseValue().getStandardDeviationValues(), forecastDateTime)
				.getValue();

		return Util.adjustForStandardDeviation(rawForecast, sdValue);
	}

	/**
	 * Calculate the scaled forecast for the given LocalDateTime. Cut off forecast
	 * values if they exceed 2 * base scale positively or -2 * base scale
	 * negatively.
	 * 
	 * @param sdAdjustedForecast {@link double} The standard deviation adjusted
	 *                           value to be scaled.
	 * @return {@code double} the scaled forecast value.
	 */
	private double calculateScaledForecast(double sdAdjustedForecast) {
		double instanceBaseScale = this.getBaseScale();
		double instanceForecastScalar = this.getForecastScalar();

		final double MAX_FORECAST = instanceBaseScale * 2;
		final double MIN_FORECAST = 0 - MAX_FORECAST;

		double scaledForecast = sdAdjustedForecast * instanceForecastScalar;

		if (scaledForecast > MAX_FORECAST)
			return MAX_FORECAST;

		if (scaledForecast < MIN_FORECAST)
			return MIN_FORECAST;

		return scaledForecast;
	}

	/**
	 * Calculates the forecast scalar. If this rule has variations the variations'
	 * forecasts and respective weights are used to calculate the forecast scalar.
	 * Else this rule's standard deviation adjusted values are used.
	 * 
	 * @param baseScale {@code double} The base scale to which the forecast scalar
	 *                  should scale the forecasts.
	 * @return {@code double} The calculated forecast scalar.
	 */
	private double calculateForecastScalar() {

		double instanceBaseScale = this.getBaseScale();
		Rule[] instanceVariations = this.getVariations();
		ValueDateTupel[] relevantForecastValues;

		/*
		 * If the rule has variations, use the variations' forecasts multiplied with
		 * their respective weights method to get the base for the forecast scalar.
		 */
		if (instanceVariations != null) {
			/* local array of weighted and combined variations' forecasts. */
			relevantForecastValues = ValueDateTupel.createEmptyArray(instanceVariations[0].getForecasts().length);

			/* Loop over each variation */
			for (Rule variation : instanceVariations) {
				/* Loop over each forecast value inside each variation. */
				for (int i = 0; i < variation.getForecasts().length; i++) {

					/*
					 * Calculate the value to be added to the current weighted forecast value for
					 * this rule
					 */
					double valueToBeAdded = variation.getForecasts()[i].getValue() * variation.getWeight();

					/*
					 * If the variations forecast value at this position is null (i.e. when we're in
					 * the first variation's loop) create a new ValueDateTupel
					 */
					if (relevantForecastValues[i] == null) {
						relevantForecastValues[i] = new ValueDateTupel(variation.getForecasts()[i].getDate(),
								valueToBeAdded);
					} else {
						/*
						 * If there already is a value at position i add the value to the existing value
						 */
						relevantForecastValues[i].setValue(relevantForecastValues[i].getValue() + valueToBeAdded);
					}
				}
			}

		} else {
			/*
			 * If the rule doesn't have variations use this rules sd adjusted forecast
			 * values
			 */
			relevantForecastValues = this.getSdAdjustedForecasts();
		}

		relevantForecastValues = ValueDateTupel.getElements(relevantForecastValues, this.getStartOfReferenceWindow(),
				this.getEndOfReferenceWindow());

		double calculatedForecastScalar = Util.calculateForecastScalar(ValueDateTupel.getValues(relevantForecastValues),
				instanceBaseScale);
		if (Double.isNaN(calculatedForecastScalar))
			calculatedForecastScalar = 0;

		return calculatedForecastScalar;
	}

	/**
	 * Checks the given variations. If the given variations is null, null will be
	 * set. If there are more than 3 variations given an Exception will be thrown.
	 * 
	 * If there are null values in the given variations array an Exception will be
	 * thrown.
	 * 
	 * If there are 3 or less variations, they will be weighed utilizing
	 * {@link #weighVariations()}.
	 * 
	 * @param variations {@code Rule[]} The Variations to be checked, set and
	 *                   weighed.
	 * @throws IllegalArgumentException if the given array is not null and contains
	 *                                  more than 3 elements.
	 * @throws IllegalArgumentException if the given array contains null.
	 */
	private void validateSetAndWeighVariations(Rule[] variations) throws IllegalArgumentException {
		if (variations != null && variations.length > 3)
			throw new IllegalArgumentException("Each layer must not contain more than 3 rules/variations");
		this.setVariations(variations);
		if (variations != null) {
			for (int i = 0; i < variations.length; i++) {
				if (variations[i] == null)
					throw new IllegalArgumentException(
							"The variation at position " + i + " in the given variations array is null.");
			}

			/* Only if there are no null variations they can be weighed. */
			this.weighVariations();
		}
	}

	/**
	 * Calculates and sets the weights for this rule's variations based on their
	 * correlations. This calculation is an approximation of (Robert Carver,
	 * Systematic Trading (2015), p. 79, Table 8). Using the actual table would
	 * muddy the weights and render them inaccurate.
	 */
	private void weighVariations() {
		Rule[] instanceVariations = this.getVariations();

		/* If there is only 1 variation then its weight is 100% */
		switch (instanceVariations.length) {
		case 1:
			instanceVariations[0].setWeight(1d);
			break;
		/* If there are 2 variations their weights are 50% each */
		case 2:
			instanceVariations[0].setWeight(0.5d);
			instanceVariations[1].setWeight(0.5d);
			break;
		case 3:
			ValueDateTupel[] forecasts0 = ValueDateTupel.getElements(instanceVariations[0].getForecasts(),
					this.getStartOfReferenceWindow(), this.getEndOfReferenceWindow());
			ValueDateTupel[] forecasts1 = ValueDateTupel.getElements(instanceVariations[1].getForecasts(),
					this.getStartOfReferenceWindow(), this.getEndOfReferenceWindow());
			ValueDateTupel[] forecasts2 = ValueDateTupel.getElements(instanceVariations[2].getForecasts(),
					this.getStartOfReferenceWindow(), this.getEndOfReferenceWindow());

			ValueDateTupel[][] variationsForecasts = {};
			variationsForecasts = ArrayUtils.add(variationsForecasts, forecasts0);
			variationsForecasts = ArrayUtils.add(variationsForecasts, forecasts1);
			variationsForecasts = ArrayUtils.add(variationsForecasts, forecasts2);

			/*
			 * Extract the values from the forecasts array, as the Dates are not needed for
			 * correlation calculation.
			 */
			double[][] variationsForecastValues = {};
			variationsForecastValues = ArrayUtils.add(variationsForecastValues,
					ValueDateTupel.getValues(variationsForecasts[0]));
			variationsForecastValues = ArrayUtils.add(variationsForecastValues,
					ValueDateTupel.getValues(variationsForecasts[1]));
			variationsForecastValues = ArrayUtils.add(variationsForecastValues,
					ValueDateTupel.getValues(variationsForecasts[2]));

			/* Find the correlations for the given variations. */
			double[] correlations;
			try {
				correlations = Util.calculateCorrelationsOfThreeRows(variationsForecastValues);
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"Given reference window cannot be used as it contains all identical forecast values for at least one variation.",
						e);
			}

			double[] weights = {};
			/*
			 * Catch three correlations of 1 as they will break calculateWeights(double[])
			 */
			if (correlations[0] == 1 && correlations[0] == correlations[1] && correlations[0] == correlations[2]) {
				double correlationOfOneThird = 1d / 3d;
				weights = ArrayUtils.add(weights, correlationOfOneThird);
				weights = ArrayUtils.add(weights, correlationOfOneThird);
				weights = ArrayUtils.add(weights, correlationOfOneThird);
			} else {
				/* Find the weights corresponding to the calculated correlations. */
				weights = Rule.calculateWeights(correlations);
			}

			/* Set the weights of the underlying variations */
			for (int i = 0; i < weights.length; i++) {
				instanceVariations[i].setWeight(weights[i]);
			}
			break;
		}
	}

	/**
	 * Calculate the weights that should be given to the rows of values making up
	 * the given correlations. Expects an array of length 3, where position 0 holds
	 * the correlation of rows A and B, position 1 holds the correlation for rows B
	 * and C, and position 2 holds the correlation for rows C and A.
	 * 
	 * @param correlations {@code double[]} Three values representing the
	 *                     correlations between the rows A, B and C. The expected
	 *                     array is constructed as follows: { corr_AB, corr_BC,
	 *                     corr_CA }.
	 * @return {@code double[]} The calculated weights { w_A, w_B, w_C }.
	 * @throws IllegalArgumentException if a correlation value is < -1 or > 1.
	 */
	private static double[] calculateWeights(double[] correlations) throws IllegalArgumentException {
		/*
		 * Correlation values are within the bounds of -1 and +1. Other values cannot be
		 * real correlation values.
		 */
		for (int i = 0; i < correlations.length; i++) {
			/* Floor negative correlations at 0 (See Carver: "Systematic Trading", p. 79) */
			if (correlations[i] < 0)
				correlations[i] = 0;
		}

		/* Get the average correlation each row of values has */
		double averageCorrelationA = (correlations[0] + correlations[2]) / 2;
		double averageCorrelationB = (correlations[1] + correlations[0]) / 2;
		double averageCorrelationC = (correlations[2] + correlations[1]) / 2;

		double[] averageCorrelations = {};
		averageCorrelations = ArrayUtils.add(averageCorrelations, averageCorrelationA);
		averageCorrelations = ArrayUtils.add(averageCorrelations, averageCorrelationB);
		averageCorrelations = ArrayUtils.add(averageCorrelations, averageCorrelationC);

		/* Subtract each average correlation from 1 to get an inverse-ish value */
		for (int i = 0; i < averageCorrelations.length; i++)
			averageCorrelations[i] = 1 - averageCorrelations[i];

		/* Calculate the sum of average calculations. */
		double sumOfAverageCorrelations = DoubleStream.of(averageCorrelations).sum();
		/*
		 * Normalize the average correlations so they sum up to 1. These normalized
		 * values are the weights.
		 */
		for (int i = 0; i < averageCorrelations.length; i++)
			averageCorrelations[i] = averageCorrelations[i] / sumOfAverageCorrelations;

		return averageCorrelations;
	}

	@GeneratedCode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(baseScale);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((baseValue == null) ? 0 : baseValue.hashCode());
		result = prime * result + ((endOfReferenceWindow == null) ? 0 : endOfReferenceWindow.hashCode());
		temp = Double.doubleToLongBits(forecastScalar);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(forecasts);
		result = prime * result + Arrays.hashCode(sdAdjustedForecasts);
		result = prime * result + ((startOfReferenceWindow == null) ? 0 : startOfReferenceWindow.hashCode());
		result = prime * result + Arrays.hashCode(variations);
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
		Rule other = (Rule) obj;
		if (Double.doubleToLongBits(baseScale) != Double.doubleToLongBits(other.baseScale))
			return false;
		if (baseValue == null) {
			if (other.baseValue != null)
				return false;
		} else if (!baseValue.equals(other.baseValue))
			return false;
		if (endOfReferenceWindow == null) {
			if (other.endOfReferenceWindow != null)
				return false;
		} else if (!endOfReferenceWindow.equals(other.endOfReferenceWindow))
			return false;
		if (Double.doubleToLongBits(forecastScalar) != Double.doubleToLongBits(other.forecastScalar))
			return false;
		if (!Arrays.equals(forecasts, other.forecasts))
			return false;
		if (!Arrays.equals(sdAdjustedForecasts, other.sdAdjustedForecasts))
			return false;
		if (startOfReferenceWindow == null) {
			if (other.startOfReferenceWindow != null)
				return false;
		} else if (!startOfReferenceWindow.equals(other.startOfReferenceWindow))
			return false;
		if (!Arrays.equals(variations, other.variations))
			return false;
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		return true;
	}

	@GeneratedCode
	@Override
	public String toString() {
		return "Rule [forecastScalar=" + forecastScalar + ", weight=" + weight + ", variations="
				+ Arrays.toString(variations) + ", startOfReferenceWindow=" + startOfReferenceWindow
				+ ", endOfReferenceWindow=" + endOfReferenceWindow + ", baseValue=" + baseValue + ", baseScale="
				+ baseScale + ", forecasts=" + Arrays.toString(forecasts) + ", sdAdjustedForecasts="
				+ Arrays.toString(sdAdjustedForecasts) + "]";
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */

	/**
	 * Get the base value of this rule.
	 * 
	 * @return baseValue {@link BaseValue} The base value of this instance of rule.
	 */
	public final BaseValue getBaseValue() {
		return baseValue;
	}

	/**
	 * Set the base value of this rule.
	 * 
	 * @param baseValue {@link BaseValue} the baseValue to bet set for this instance
	 *                  of rule.
	 */
	private void setBaseValue(BaseValue baseValue) {
		this.baseValue = baseValue;
	}

	/**
	 * Get the forecast scalar of this rule
	 * 
	 * @return {@code double} forecast scalar of this rule
	 */
	public final double getForecastScalar() {
		if (sdAdjustedForecasts == null)
			this.calculateAndSetDerivedValues();
		return forecastScalar;
	}

	/**
	 * Set the forecast scalar of this rule
	 * 
	 * @param forecastScalar {@code double} forecast scalar to be set for this rule
	 */
	private void setForecastScalar(double forecastScalar) {
		this.forecastScalar = forecastScalar;
	}

	/**
	 * Get the weight of this rule
	 * 
	 * @return {@code double} the weight of this rule
	 */
	public final double getWeight() {
		return weight;
	}

	/**
	 * Set the weight of this rule
	 * 
	 * @param weight {@code double} the weight to be set for this rule
	 */
	private void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @return variations Rule
	 */
	public final Rule[] getVariations() {
		return variations;
	}

	/**
	 * @param variations the variations to set
	 */
	private void setVariations(Rule[] variations) {
		this.variations = variations;
	}

	/**
	 * @return startOfReferenceWindow Rule
	 */
	public final LocalDateTime getStartOfReferenceWindow() {
		return startOfReferenceWindow;
	}

	/**
	 * @param startOfReferenceWindow the startOfReferenceWindow to set
	 */
	private void setStartOfReferenceWindow(LocalDateTime startOfReferenceWindow) {
		this.startOfReferenceWindow = startOfReferenceWindow;
	}

	/**
	 * @return endOfReferenceWindow Rule
	 */
	public final LocalDateTime getEndOfReferenceWindow() {
		return endOfReferenceWindow;
	}

	/**
	 * @param endOfReferenceWindow the endOfReferenceWindow to set
	 */
	private void setEndOfReferenceWindow(LocalDateTime endOfReferenceWindow) {
		this.endOfReferenceWindow = endOfReferenceWindow;
	}

	/**
	 * @return baseScale Rule
	 */
	public final double getBaseScale() {
		return baseScale;
	}

	/**
	 * @param baseScale the baseScale to set
	 */
	private void setBaseScale(double baseScale) {
		this.baseScale = baseScale;
	}

	/**
	 * Get the adjusted and scaled forecasts of this Rule. Invokes
	 * {@link #calculateAndSetDerivedValues()} if
	 * {@code (this.sdAdjustedForecasts == null)} evaluates to {@code true}.
	 * 
	 * @return forecasts {@code ValueDateuTupel[]} The adjusted and scaled forecasts
	 *         of this Rule.
	 */
	public final ValueDateTupel[] getForecasts() {
		if (sdAdjustedForecasts == null)
			this.calculateAndSetDerivedValues();
		return forecasts;
	}

	/**
	 * @param forecasts the forecasts to set
	 */
	private void setForecasts(ValueDateTupel[] forecasts) {
		this.forecasts = forecasts;
	}

	/**
	 * Get the standard deviation adjusted forecasts for this rule.
	 * <p>
	 * Is set to private as the forecast values have no meaning unscaled.
	 * 
	 * @return sdAdjustedForecasts Rule
	 */
	private ValueDateTupel[] getSdAdjustedForecasts() {
		return sdAdjustedForecasts;
	}

	/**
	 * Set the standard deviation adjusted forecasts of this Rule.
	 * 
	 * @param sdAdjustedForecasts the sdAdjustedForecasts to set
	 */
	private void setSdAdjustedForecasts(ValueDateTupel[] sdAdjustedForecasts) {
		this.sdAdjustedForecasts = sdAdjustedForecasts;
	}

}
