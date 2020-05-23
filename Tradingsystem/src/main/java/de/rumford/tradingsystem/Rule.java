package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.DoubleStream;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.Validator;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * Rules are the centerpiece of a trading system. Based on these rules a system
 * tries to forecast future developments of a given asset and thus advises its
 * user.
 * <p>
 * Although every investor should develop their own, these rules need to share
 * some functionality so they can actually be used in this trading system. As
 * soon as the forecast determining calculation is done (done inside the
 * implementation of {@link #calculateRawForecast(LocalDateTime)}) all rules are
 * treated equally. This ensures compatibility and comparability between rules
 * an between trading systems.
 * 
 * Abstract class to be extend on developing new rules for the trading system.
 * 
 * {@link #calculateAndSetDerivedValues()} is called on first invocation of
 * {@link #getSdAdjustedForecasts()} and {@link #getForecastScalar()}
 * respectively.
 * 
 * @author Max Rumford
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
	 *                               this rule's calculations. See
	 *                               {@link #validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}
	 *                               for limitations.
	 * @param variations             {@code Rule[]} An array of up to 3 rules (or
	 *                               null). See
	 *                               {@link #validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}
	 *                               for limitations.
	 * @param startOfReferenceWindow {@link LocalDateTime} The first LocalDateTime
	 *                               to be considered in calculations such as
	 *                               forecast scalar. See
	 *                               {@link #validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}
	 *                               for limitations.
	 * @param endOfReferenceWindow   {@link LocalDateTime} The last LocalDateTime to
	 *                               be considered in calculations such as forecast
	 *                               scalar. See
	 *                               {@link #validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}
	 *                               for limitations.
	 * @param baseScale              {@code double} How the forecasts shall be
	 *                               scaled. See
	 *                               {@link #validateInputs(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}
	 *                               for limitations.
	 */
	public Rule(BaseValue baseValue, Rule[] variations, LocalDateTime startOfReferenceWindow,
			LocalDateTime endOfReferenceWindow, double baseScale) {

		validateInputs(baseValue, variations, startOfReferenceWindow, endOfReferenceWindow, baseScale);

		this.setBaseValue(baseValue);
		this.setStartOfReferenceWindow(startOfReferenceWindow);
		this.setEndOfReferenceWindow(endOfReferenceWindow);
		this.setVariations(variations);
		this.weighVariations();
		this.setBaseScale(baseScale);
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
	 * Calculates all values derived from raw forecasts. This takes into
	 * consideration that not all relevant values might be known upon call of Rule
	 * constructor.
	 */
	private void calculateAndSetDerivedValues() {
		this.setSdAdjustedForecasts(this.calculateSdAdjustedForecasts());
		this.setForecastScalar(this.calculateForecastScalar());
		this.setForecasts(this.calculateScaledForecasts());
	}

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
	 * Calculates the standard deviation adjusted Forecast for a given
	 * LocalDateTime.
	 * 
	 * @param forecastDateTime {@link LocalDateTime} The LocalDateTime the forecast
	 *                         is to be calculated of.
	 * @return {@code double} The standard deviation adjusted value. Double.NaN if
	 *         the standard deviation at the given LocalDateTime is zero.
	 */
	private double calculateSdAdjustedForecast(LocalDateTime forecastDateTime) {
		if (this.hasVariations()) {
			return Double.NaN;
		}

		double rawForecast = this.calculateRawForecast(forecastDateTime);

		double sdValue = ValueDateTupel.getElement(this.getBaseValue().getStandardDeviationValues(), forecastDateTime)
				.getValue();

		return Util.adjustForStandardDeviation(rawForecast, sdValue);
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
			throw new IllegalArgumentException("Illegal values in calulated forecast values. Adjust reference window.");

		return calculatedForecastScalar;
	}

	/**
	 * Calculates and sets the weights for this rule's variations based on their
	 * correlations. This calculation is an approximation of (Robert Carver,
	 * Systematic Trading (2015), p. 79, Table 8). Using the actual table would
	 * muddy the weights and render them inaccurate.
	 */
	private void weighVariations() {
		Rule[] instanceVariations = this.getVariations();
		if (instanceVariations == null)
			return;

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
			ValueDateTupel[] forecastsA = ValueDateTupel.getElements(instanceVariations[0].getForecasts(),
					this.getStartOfReferenceWindow(), this.getEndOfReferenceWindow());
			ValueDateTupel[] forecastsB = ValueDateTupel.getElements(instanceVariations[1].getForecasts(),
					this.getStartOfReferenceWindow(), this.getEndOfReferenceWindow());
			ValueDateTupel[] forecastsC = ValueDateTupel.getElements(instanceVariations[2].getForecasts(),
					this.getStartOfReferenceWindow(), this.getEndOfReferenceWindow());

			/*
			 * Extract the values from the forecasts array, as the Dates are not needed for
			 * correlation calculation.
			 */
			double[][] variationsForecasts = {};
			variationsForecasts = ArrayUtils.add(variationsForecasts, ValueDateTupel.getValues(forecastsA));
			variationsForecasts = ArrayUtils.add(variationsForecasts, ValueDateTupel.getValues(forecastsB));
			variationsForecasts = ArrayUtils.add(variationsForecasts, ValueDateTupel.getValues(forecastsC));

			/* Find the correlations for the given variations. */
			double[] correlations;
			correlations = Util.calculateCorrelationOfRows(variationsForecasts);

			if (ArrayUtils.contains(correlations, Double.NaN))
				throw new IllegalArgumentException(
						"Correlations cannot be calculated due to illegal values in given variations.");

			/* Find the weights corresponding to the calculated correlations. */
			double[] weights = Rule.calculateWeightsForThreeCorrelations(correlations);

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
	 * the correlation of rows A and B, position 1 holds the correlation for rows A
	 * and C, and position 2 holds the correlation for rows B and C.
	 * 
	 * @param correlations {@code double[]} Three values representing the
	 *                     correlations between the rows A, B and C. The expected
	 *                     array is constructed as follows: { corr_AB, corr_AC,
	 *                     corr_BC }. See {@link #validateCorrelations(double[])}
	 *                     for limitations.
	 * @return {@code double[]} The calculated weights { w_A, w_B, w_C }.
	 */
	public static double[] calculateWeightsForThreeCorrelations(double[] correlations) {

		validateCorrelations(correlations);

		for (int i = 0; i < correlations.length; i++) {
			/* Floor negative correlations at 0 (See Carver: "Systematic Trading", p. 79) */
			if (correlations[i] < 0)
				correlations[i] = 0;
		}

		double[] weights = {};
		/*
		 * Catch three equal correlations. Three correlations of 1 each would break
		 * further calculation.
		 */
		if (correlations[0] == correlations[1] && correlations[0] == correlations[2]) {
			double correlationOfOneThird = 1d / 3d;
			weights = ArrayUtils.add(weights, correlationOfOneThird);
			weights = ArrayUtils.add(weights, correlationOfOneThird);
			weights = ArrayUtils.add(weights, correlationOfOneThird);
			return weights;
		}

		/* Get the average correlation each row of values has */
		double averageCorrelationA = (correlations[0] + correlations[1]) / 2;
		double averageCorrelationB = (correlations[0] + correlations[2]) / 2;
		double averageCorrelationC = (correlations[1] + correlations[2]) / 2;

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
			weights = ArrayUtils.add(weights, averageCorrelations[i] / sumOfAverageCorrelations);

		return weights;
	}

	/**
	 * Validates the given correlations.
	 * 
	 * @param correlations {@code double[]} Correlations to be validated. Must not
	 *                     be null. Must have a length of 3. Must only contain
	 *                     values {@code !Double.NaN} and {@code -1 <= value <= 1}.
	 * @throws IllegalArgumentException if the above specifications are not met.
	 */
	private static void validateCorrelations(double[] correlations) {
		/* Check if the given array is null */
		if (correlations == null)
			throw new IllegalArgumentException("Correlations array must not be null");
		/* Check if the given array contains exactly three elements. */
		if (correlations.length != 3)
			throw new IllegalArgumentException("There must be exactly three correlation values in the given array");

		/* Check all given values inside the array */
		for (int i = 0; i < correlations.length; i++) {
			if (Double.isNaN(correlations[i]))
				throw new IllegalArgumentException(
						"NaN-values are not allowed. Correlation at position " + i + " is NaN.");
			if (correlations[i] > 1)
				throw new IllegalArgumentException("Correlation at position " + i + " is greater than 1");
			if (correlations[i] < -1)
				throw new IllegalArgumentException("Correlation at position " + i + " is less than -1");
		}
	}

	/**
	 * Extract the relevant forecast values for this rule.
	 * 
	 * @return {@code double[]} An array of the relevant forecast values for this
	 *         rule.
	 */
	public double[] getRelevantForecastValues() {
		ValueDateTupel[] relevantForecasts = ValueDateTupel.getElements(this.getForecasts(),
				this.getStartOfReferenceWindow(), this.getEndOfReferenceWindow());

		return ValueDateTupel.getValues(relevantForecasts);
	}

	/**
	 * Evaluates if the current rule has variations.
	 * 
	 * @return {@code boolean} True, if the rule has variations. False otherwise.
	 */
	public boolean hasVariations() {
		return this.getVariations() != null;
	}

	/**
	 * Validates if the given instance variables meet specifications.
	 * 
	 * @param baseValue              {@link BaseValue} The base value to be used in
	 *                               this rule's calculations. Must pass
	 *                               {@link Validator#validateBaseValue(BaseValue)}.
	 *                               Its values must pass
	 *                               {@link Validator#validateTimeWindow(LocalDateTime, LocalDateTime, ValueDateTupel[])}
	 * @param variations             {@code Rule[]} Can be null. If not
	 *                               <ul>
	 *                               <li>Must not contain more than 3 elements.</li>
	 *                               <li>Must not contain 0 elements.</li>
	 *                               <li>Must not contain null.</li>
	 *                               <li>All elements must have matching
	 *                               startOfReferenceWindow and endOfReferenceWindow
	 *                               and must be the same as given
	 *                               startOfReferenceWindow and
	 *                               endOfReferenceWindow.</li>
	 *                               </ul>
	 * @param startOfReferenceWindow {@link LocalDateTime} The first LocalDateTime
	 *                               to be considered in calculations such as
	 *                               forecast scalar. Must pass
	 *                               {@link Validator#validateTimeWindow(LocalDateTime, LocalDateTime, ValueDateTupel[])}
	 * @param endOfReferenceWindow   {@link LocalDateTime} The last LocalDateTime to
	 *                               be considered in calculations such as forecast
	 *                               scalar. Must pass
	 *                               {@link Validator#validateTimeWindow(LocalDateTime, LocalDateTime, ValueDateTupel[])}
	 * @param baseScale              {@code double} How the forecasts shall be
	 *                               scaled. Must pass
	 *                               {@link Validator#validatePositiveDouble(double)}.
	 * @throws IllegalArgumentException if the above specifications are not met.
	 */
	private static void validateInputs(BaseValue baseValue, Rule[] variations, LocalDateTime startOfReferenceWindow,
			LocalDateTime endOfReferenceWindow, double baseScale) {

		Validator.validateBaseValue(baseValue);

		try {
			Validator.validateTimeWindow(startOfReferenceWindow, endOfReferenceWindow, baseValue.getValues());
		} catch (IllegalArgumentException e) {
			/*
			 * If the message contains "values" the message references an error in the given
			 * base values in combination with the given reference window.
			 */
			if (e.getMessage().contains("values"))
				throw new IllegalArgumentException("Given base value and reference window do not fit.", e);

			throw new IllegalArgumentException("The given reference window does not meet specifications.", e);
		}

		/*
		 * The first time interval of the base values cannot have all derived values
		 * correctly calculated, as there will be no returns (due to lacking former
		 * value).
		 */
		if (baseValue.getValues()[0].getDate().equals(startOfReferenceWindow))
			throw new IllegalArgumentException(
					"Reference window must not start on first time interval of base value data.");

		/* A rule can have no variations, so variations == null is acceptable. */
		if (variations != null) {
			/* Check if there are too many variations for this rule */
			if (variations.length > 3)
				throw new IllegalArgumentException("A rule must not contain more than 3 variations.");

			/* Check if the given variations array is empty. */
			if (variations.length == 0)
				throw new IllegalArgumentException("The given variations array must not be empty.");

			for (int i = 0; i < variations.length; i++) {
				/* Check if the given variations array contains nulls. */
				if (variations[i] == null)
					throw new IllegalArgumentException(
							"The variation at position " + i + " in the given variations array is null.");

				/* Check if main rule and variations share reference window. */
				if (!variations[i].getStartOfReferenceWindow().equals(startOfReferenceWindow)) {
					throw new IllegalArgumentException(
							"The given reference window does not match the variation's at position " + i
									+ ". The given start of reference window is different.");
				}
				if (!variations[i].getEndOfReferenceWindow().equals(endOfReferenceWindow)) {
					throw new IllegalArgumentException(
							"The given reference window does not match the variation's at position " + i
									+ ". The given end of reference window is different.");
				}
			}

			try {
				Validator.validateRulesVsBaseValue(variations, baseValue);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("The given variations do not meet specifications.", e);
			}
		}

		try {
			Validator.validatePositiveDouble(baseScale);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("The given base scale does not meet specifications.", e);
		}
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
	 * Get the forecast scalar of this rule. Invokes
	 * {@link #calculateAndSetDerivedValues()} if
	 * {@code (this.sdAdjustedForecasts == null)} evaluates to {@code true}.
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
