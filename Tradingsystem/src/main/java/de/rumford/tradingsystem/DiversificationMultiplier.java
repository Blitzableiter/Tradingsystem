package de.rumford.tradingsystem;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import de.rumford.tradingsystem.helper.GeneratedCode;

/**
 * @author Max Rumford
 *
 */
public class DiversificationMultiplier {

	private double value = 0d;
	private double[] weights;
	private double[][] relevantForecasts;
	private double[][] correlations;

	/**
	 * Constructor for the class DiversificationMultiplier (DM). A DM is always only
	 * valid for a given set of rules (which should be, but don't have to be,
	 * unique). There rules must be known upon instantiation of a new DM. Based on
	 * the given rules the DM value is calculated, using the relative weights of all
	 * given rules (relative based on their placing inside the rules tree).
	 * <p>
	 * There can be as many rules in the given array as desired, though the desired
	 * ratio between rules (and their respective weights, which shrink with each new
	 * layer) and their significance to the whole system should be accounted for and
	 * is at the user's discretion.
	 * 
	 * @param rules {@code Rule[]} An array of {@link Rule}s to be accounted for in
	 *              this DM.
	 */
	public DiversificationMultiplier(Rule[] rules) {
		this.setWeights(getWeightsFromRules(rules));
		this.setRelevantForecasts(getRelevantForecastsFromRules(rules));

		this.setCorrelations(getCorrelationsFromForecasts(this.getRelevantForecasts()));

		this.setValue(this.calculateDiversificiationMultiplierValue());
	}

	/**
	 * Recursively get the weights from the given array of Rules. Uses the same
	 * algorithm for determination as
	 * {@link #getRelevantForecastsFromRules(Rule[])}.
	 * 
	 * @param rules {@code Rule[]} The array of rules to be searched.
	 * @return {@code double[]} The extracted from the given array of Rules.
	 */
	private static double[] getWeightsFromRules(Rule[] rules) {
		double[] weights = {};
		/* Iterate over the given rules */
		for (Rule rule : rules) {
			/* If a rule has variations get their weights */
			if (rule.hasVariations()) {
				double[] weightsToAdd = getWeightsFromRules(rule.getVariations());
				for (double weight : weightsToAdd)
					weights = ArrayUtils.add(weights, weight * rule.getWeight());
			} else {
				double weight = rule.getWeight();
				/*
				 * If a top level rule has no variations its weight has not been set. Manually
				 * set its weight to be 1/numberOfTopeLevelRules
				 */
				if (weight == 0)
					weight = 1d / rules.length;
				weights = ArrayUtils.add(weights, weight);
			}
		}
		return weights;
	}

	/**
	 * Recursively get the relevant forecasts from the given array of Rules. Uses
	 * the same algorithm for determination as {@link #getWeightsFromRules(Rule[])}
	 * 
	 * @param rules {@code Rule[]} The array of rules to be searched.
	 * @return {@code double[]} The extracted from the given array of Rules.
	 */
	private static double[][] getRelevantForecastsFromRules(Rule[] rules) {
		double[][] relevantForecasts = {};
		/* Iterate over the given rules */
		for (Rule rule : rules) {
			/* If a rule has variations get their forecasts */
			if (rule.hasVariations()) {
				double[][] forecastsToAdd = getRelevantForecastsFromRules(rule.getVariations());
				for (double[] forecasts : forecastsToAdd)
					relevantForecasts = ArrayUtils.add(relevantForecasts, forecasts);
			} else {
				relevantForecasts = ArrayUtils.add(relevantForecasts, rule.getRelevantForecastValues());
			}
		}
		return relevantForecasts;
	}

	/**
	 * Calculate the correlations from the given array of forecast arrays.
	 * 
	 * @param forecasts {@code double[][]} The forecasts to be used for calculation.
	 * @return {@code double[][]} A matrix of correlations, as by
	 *         {@link PearsonsCorrelation#getCorrelationMatrix()}.
	 */
	private static double[][] getCorrelationsFromForecasts(double[][] forecasts) {
		/* If there is only one row of data return a 1x1 self correlation matrix */
		if (forecasts.length == 1) {
			double[][] returnValue = { { 1 } };
			return returnValue;
		}

		/* Load the given values into rows of a matrix */
		BlockRealMatrix matrix = new BlockRealMatrix(forecasts);
		/* Transpose the values into columns to get the correct correlations */
		matrix = matrix.transpose();

		/* Get the correlations of the passed value arrays */
		PearsonsCorrelation pearsonsCorrelations = new PearsonsCorrelation(matrix);
		return pearsonsCorrelations.getCorrelationMatrix().getData();
	}

	/**
	 * Calculate the diversification multiplier for the weights and correlations set
	 * with this class. Represents this formula with c = matrix of correlations, w =
	 * list of weights, i,j = indices:
	 * 
	 * 1 / sqrt[ SUM( c_i,j * w_i * w_j ) ]
	 * 
	 * @return {@code double} diversification multiplier for set weights and
	 *         correlations
	 * @throws IllegalArgumentException if correlations or weights do not meet
	 *                                  criteria: non-empty, same number of values,
	 *                                  correlations: same amount of rows and
	 *                                  columns
	 */
	private double calculateDiversificiationMultiplierValue() {
		double[][] instanceCorrelations = this.getCorrelations();
		double[] instanceWeights = this.getWeights();

		/* local field to hold sum of correlations multiplied with weights */
		double sumOfCorrelationsWeights = 0f;

		/*
		 * Get the sum of all correlations multiplier with both corresponding weights...
		 */
		for (int row = 0; row < instanceCorrelations.length; row++) {
			for (int col = 0; col < instanceCorrelations.length; col++) {
				/* ... by multiplying the correlation with both corresponding weights */
				sumOfCorrelationsWeights += instanceCorrelations[row][col] * instanceWeights[row]
						* instanceWeights[col];
			}
		}

		/*
		 * sumOfCorrelationsWeights is always > 0 as there is always at least one weight
		 * > 0 and at least on correlation > 0 (self correlation)
		 */
		return 1 / Math.sqrt(sumOfCorrelationsWeights);
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
		result = prime * result + Arrays.deepHashCode(correlations);
		result = prime * result + Arrays.deepHashCode(relevantForecasts);
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(weights);
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
		DiversificationMultiplier other = (DiversificationMultiplier) obj;
		if (!Arrays.deepEquals(correlations, other.correlations))
			return false;
		if (!Arrays.deepEquals(relevantForecasts, other.relevantForecasts))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		if (!Arrays.equals(weights, other.weights))
			return false;
		return true;
	}

	@GeneratedCode
	@Override
	public String toString() {
		return "DiversificationMultiplier [value=" + value + ", weights=" + Arrays.toString(weights)
				+ ", relevantForecasts=" + Arrays.toString(relevantForecasts) + ", correlations="
				+ Arrays.toString(correlations) + "]";
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */
	/**
	 * Get value of this {@link DiversificationMultiplier}
	 * 
	 * @return {@code double} value of this {@link DiversificationMultiplier}
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	private void setValue(double value) {
		this.value = value;
	}

	/**
	 * Get the weights considered in this {@link DiversificationMultiplier}
	 * 
	 * @return {@code double[]} Weights in this {@link DiversificationMultiplier}
	 */
	public double[] getWeights() {
		return weights;
	}

	/**
	 * Set the Weights in this {@link DiversificationMultiplier}
	 * 
	 * @param {@code double[]} the weights to set
	 */
	private void setWeights(double[] weights) {
		this.weights = weights;
	}

	/**
	 * @return relevantForecasts DiversificationMultiplier
	 */
	public double[][] getRelevantForecasts() {
		return relevantForecasts;
	}

	/**
	 * @param relevantForecasts the relevantForecasts to set
	 */
	private void setRelevantForecasts(double[][] relevantForecasts) {
		this.relevantForecasts = relevantForecasts;
	}

	/**
	 * Get the correlations in this {@link DiversificationMultiplier}
	 * 
	 * @return {@code double[][]} The correlations in this
	 *         {@link DiversificationMultiplier}
	 */
	public double[][] getCorrelations() {
		return correlations;
	}

	/**
	 * Set the correlations to be used in this {@link DiversificationMultiplier}
	 * 
	 * @param {@code double[][]} the correlations to be used in this
	 * {@link DiversificationMultiplier}
	 */
	private void setCorrelations(double[][] correlations) {
		this.correlations = correlations;
	}

}
