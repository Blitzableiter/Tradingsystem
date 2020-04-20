/**
 * 
 */
package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.DoubleStream;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.helper.Util;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * @author Max Rumford
 *
 */
public abstract class Rule {

	// TODO
	// GETTER & SETTER: PROTECTED VS. PACKAGE PROTECTED
	// https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html

	private double forecastScalar;
	private double weight;
	private Rule[] variations;
	private LocalDateTime startOfReferenceWindow;
	private LocalDateTime endOfReferenceWindow;
	private BaseValue baseValue;
	private double baseScale;
	private ValueDateTupel[] forecasts;

	/**
	 * 
	 * @param variations {@link Rule[]} or {@code null}
	 */
	public Rule(BaseValue baseValue, Rule[] variations, LocalDateTime startOfReferenceWindow,
			LocalDateTime endOfReferenceWindow, double baseScale) {
		this.setBaseValue(baseValue);
		this.validateSetAndWeighVariations(variations);
		this.setStartOfReferenceWindow(startOfReferenceWindow);
		this.setEndOfReferenceWindow(endOfReferenceWindow);
		this.setBaseScale(baseScale);

		this.calculateAndSetForecastScalar(this.getBaseScale());
	}

	abstract double calculateRawForecast(LocalDateTime forecastDateTime);

	/**
	 * Calculate the scaled forecasts
	 * 
	 * @param calculateFrom
	 * @param calculateTo
	 * @return
	 */
	final ValueDateTupel[] calculateForecasts(LocalDateTime calculateFrom, LocalDateTime calculateTo) {
		ValueDateTupel[] forecasts = {};

		Rule[] variations = this.getVariations();
		/*
		 * If a rule has variations only their forecasts matter. This rules forecasts
		 * are the set to equal the forecasts of its varations.
		 */
		if (variations != null) {
			ValueDateTupel[][] variationsForecasts = {};
			double[] weights = {};

			/* Extract forecasts and weights of all variations. */
			for (Rule variation : variations) {
				variationsForecasts = ArrayUtils.add(variationsForecasts,
						variation.calculateForecasts(calculateFrom, calculateTo));
				weights = ArrayUtils.add(weights, variation.getWeight());
			}

			/* Loop over all variations */
			for (int varIndex = 0; varIndex < variationsForecasts.length; varIndex++) {

				/* Loop over all forecasts for each variation. */
				for (int i = 0; i < variationsForecasts[varIndex].length; i++) {
					/*
					 * Add the weighted forecast of each variation for each LocalDateTime to the
					 * forecast for each LocalDateTime for this rule.
					 */
					forecasts[i].setValue(
							forecasts[i].getValue() + variationsForecasts[varIndex][i].getValue() * weights[varIndex]);
				}
			}

			return forecasts;

		}
		LocalDateTime[] relevantDates = ValueDateTupel
				.getDates(ValueDateTupel.getElements(this.getBaseValue().getValues(), calculateFrom, calculateTo));
		for (LocalDateTime dt : relevantDates)
			forecasts = ArrayUtils.add(forecasts, new ValueDateTupel(dt, this.calculateScaledForecast(dt)));

		return forecasts;
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
	// TODO SANITIZE INPUTS
	private double calculateSdAdjustedForecast(LocalDateTime forecastDateTime) {
		if (this.getVariations() != null) {
			return Double.NaN;
		}

		double rawForecast = this.calculateRawForecast(forecastDateTime);
		double sdValue = ValueDateTupel.getElement(this.getBaseValue().getStandardDeviationValues(), forecastDateTime)
				.getValue();
		try {
			return Util.adjustForStandardDeviation(rawForecast, sdValue);
		} catch (IllegalArgumentException e) {
			return Double.NaN;
		}
	}

	/**
	 * Calculate the scaled forecast for the given LocalDateTime. Cut off forecast
	 * values if they exceed 2 * base scale positively or -2 * base scale
	 * negatively.
	 * 
	 * @param forecastDateTime {@link LocalDateTime} The LocalDateTime the scaled
	 *                         forecast is to be calculated for.
	 * @return {@code double} he scaled forecast value.
	 */
	// TODO SANITIZE INPUTS
	double calculateScaledForecast(LocalDateTime forecastDateTime) {
		double baseScale = this.getBaseScale();
		double forecastScalar = this.getForecastScalar();

		final double MAX_FORECAST = baseScale * 2;
		final double MIN_FORECAST = 0 - MAX_FORECAST;

		double scaledForecast = this.calculateSdAdjustedForecast(forecastDateTime) * forecastScalar;

		if (scaledForecast > MAX_FORECAST)
			return MAX_FORECAST;

		if (scaledForecast < MIN_FORECAST)
			return MIN_FORECAST;

		return scaledForecast;

	}

	// TODO COMMENT ME
	private void calculateAndSetForecastScalar(double baseScale) {
		// TODO
		// TODO
		/*
		 * If the rule has variations, use the variations
		 * calculateForecasts(LocalDateTime, LocalDateTime) method to get the base for
		 * the forecast scalar.
		 * 
		 * If not, use this rules sd adjusted forecast values.
		 */
		// TODO
		// TODO
		Rule[] variations = this.getVariations();
		if (variations != null) {

		}

		LocalDateTime[] relevantDates = ValueDateTupel.getDates(this.getBaseValue().getValues());

		ValueDateTupel[] sdAdjustedForecasts = {};
		for (LocalDateTime dt : relevantDates) {
			sdAdjustedForecasts = ArrayUtils.add(sdAdjustedForecasts,
					new ValueDateTupel(dt, this.calculateSdAdjustedForecast(dt)));
		}

		double forecastScalar = Util.calculateForecastScalar(ValueDateTupel.getValues(sdAdjustedForecasts), baseScale);
		if (Double.isNaN(forecastScalar))
			forecastScalar = 0;

		this.setForecastScalar(forecastScalar);
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
	final private void validateSetAndWeighVariations(Rule[] variations) throws IllegalArgumentException {
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

	final private void weighVariations() throws IllegalArgumentException {
		Rule[] variations = this.getVariations();

		/* If there is only 1 variation then its weight is 100% */
		switch (variations.length) {
		case 1:
			variations[0].setWeight(1d);
			break;
		/* If there are 2 variations their weights are 50% each */
		case 2:
			variations[0].setWeight(0.5d);
			variations[1].setWeight(0.5d);
			break;
		case 3:
			ValueDateTupel[] forecasts0 = variations[0].calculateForecasts(startOfReferenceWindow,
					endOfReferenceWindow);
			ValueDateTupel[] forecasts1 = variations[1].calculateForecasts(startOfReferenceWindow,
					endOfReferenceWindow);
			ValueDateTupel[] forecasts2 = variations[2].calculateForecasts(startOfReferenceWindow,
					endOfReferenceWindow);

			ValueDateTupel[][] forecasts = {};
			forecasts = ArrayUtils.add(forecasts, forecasts0);
			forecasts = ArrayUtils.add(forecasts, forecasts1);
			forecasts = ArrayUtils.add(forecasts, forecasts2);

			/* Amend missing values so correlations can be calculated. */
			ValueDateTupel[][] forecastsWithAlignedDates;
			try {
				forecastsWithAlignedDates = ValueDateTupel.alignDates(forecasts);
			} catch (IllegalArgumentException e) {
				throw e;
			}

			/*
			 * Extract the values from the forecasts array, as the Dates are not needed for
			 * correlation calculation.
			 */
			double[][] values = {};
			values = ArrayUtils.add(values, ValueDateTupel.getValues(forecastsWithAlignedDates[0]));
			values = ArrayUtils.add(values, ValueDateTupel.getValues(forecastsWithAlignedDates[1]));
			values = ArrayUtils.add(values, ValueDateTupel.getValues(forecastsWithAlignedDates[2]));

			/* Find the correlations for the given variations. */
			double[] correlations;
			try {
				correlations = Util.calculateCorrelationsOfThreeRows(values);
			} catch (IllegalArgumentException e) {
				throw e;
			}

			/* Find the weights corresponding to those correlations. */
			double[] weights;
			try {
				weights = Rule.calculateWeights(correlations);
			} catch (IllegalArgumentException e) {
				throw e;
			}

			/* Set the weights of the underlying variations */
			for (int i = 0; i < weights.length; i++) {
				variations[i].setWeight(weights[i]);
			}
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
	 * @throws IllegalArgumentException if the given array is null.
	 * @throws IllegalArgumentException if the given array is not of length 3.
	 * @throws IllegalArgumentException if a correlation value is < -1 or > 1.
	 */
	final private static double[] calculateWeights(double[] correlations) throws IllegalArgumentException {
		/* The given array must no be null */
		if (correlations == null)
			throw new IllegalArgumentException("Array of correlations must not be null");

		/*
		 * There must be exactly three rows of values for this method to work properly.
		 */
		if (correlations.length != 3)
			throw new IllegalArgumentException("Given array of correlations contains " + correlations.length
					+ " items although an array of length 3 was expected.");

		/*
		 * Correlation values are within the bounds of -1 and +1. Other values cannot be
		 * real correlation values.
		 */
		for (int i = 0; i < correlations.length; i++) {
			if (correlations[i] < -1 || correlations[i] > 1)
				throw new IllegalArgumentException("Correlation value " + correlations[i] + " at position " + i
						+ " is out of bounds. Correlation values must be between -1 and +1 (including).");

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

	/**
	 * ======================================================================
	 * OVERRIDES
	 * ======================================================================
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseValue == null) ? 0 : baseValue.hashCode());
		result = prime * result + ((endOfReferenceWindow == null) ? 0 : endOfReferenceWindow.hashCode());
		long temp;
		temp = Double.doubleToLongBits(forecastScalar);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((startOfReferenceWindow == null) ? 0 : startOfReferenceWindow.hashCode());
		result = prime * result + Arrays.hashCode(variations);
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
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

	@Override
	public String toString() {
		return "Rule [forecastScalar=" + forecastScalar + ", weight=" + weight + ", variations="
				+ Arrays.toString(variations) + ", startOfReferenceWindow=" + startOfReferenceWindow
				+ ", endOfReferenceWindow=" + endOfReferenceWindow + ", baseValue=" + baseValue + "]";
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */

	/*
	 * TODO ABSOLUTE VALUES FOR CALCULATION OF FORECAST SCALAR
	 */

	/**
	 * @return baseValue Rule
	 */
	public BaseValue getBaseValue() {
		return baseValue;
	}

	/**
	 * @param baseValue the baseValue to set
	 */
	void setBaseValue(BaseValue baseValue) {
		this.baseValue = baseValue;
	}

	/**
	 * Get the forecast scalar of this rule
	 * 
	 * @return {@code double} forecast scalar of this rule
	 */
	public double getForecastScalar() {
		return forecastScalar;
	}

	/**
	 * Set the forecast scalar of this rule
	 * 
	 * @param forecastScalar {@code double} forecast scalar to be set for this rule
	 */
	void setForecastScalar(double forecastScalar) {
		this.forecastScalar = forecastScalar;
	}

	/**
	 * Get the weight of this rule
	 * 
	 * @return {@code double} the weight of this rule
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Set the weight of this rule
	 * 
	 * @param weight {@code double} the weight to be set for this rule
	 */
	void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @return variations Rule
	 */
	private Rule[] getVariations() {
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
	public LocalDateTime getStartOfReferenceWindow() {
		return startOfReferenceWindow;
	}

	/**
	 * @param startOfReferenceWindow the startOfReferenceWindow to set
	 */
	void setStartOfReferenceWindow(LocalDateTime startOfReferenceWindow) {
		this.startOfReferenceWindow = startOfReferenceWindow;
	}

	/**
	 * @return endOfReferenceWindow Rule
	 */
	public LocalDateTime getEndOfReferenceWindow() {
		return endOfReferenceWindow;
	}

	/**
	 * @param endOfReferenceWindow the endOfReferenceWindow to set
	 */
	void setEndOfReferenceWindow(LocalDateTime endOfReferenceWindow) {
		this.endOfReferenceWindow = endOfReferenceWindow;
	}

	/**
	 * @return baseScale Rule
	 */
	public double getBaseScale() {
		return baseScale;
	}

	/**
	 * @param baseScale the baseScale to set
	 */
	private void setBaseScale(double baseScale) {
		this.baseScale = baseScale;
	}

	/**
	 * @return forecasts Rule
	 */
	public ValueDateTupel[] getForecasts() {
		return forecasts;
	}

	/**
	 * @param forecasts the forecasts to set
	 */
	private void setForecasts(ValueDateTupel[] forecasts) {
		this.forecasts = forecasts;
	}

}
