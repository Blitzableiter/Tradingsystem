package de.rumford.tradingsystem;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Validator;

/**
 * The DiversificationMultiplier negates the fact that using multiple rules upon a single base value flattens the
 * forecast curve.
 * <p>
 * "The only free real estate in capital investment is diversification". By diversifying between assets the risk of an
 * investment shall be reduced.
 * <p>
 * By diversifying forecast calculation (e.g. taking more rules into account when predicting a base value's future
 * development) a similar effect takes place: Their respective predictions tend to reduce the risk taken when combined,
 * so their combined forecasts are less volatile than each rule's forecasts is (Robert Carver, Systematic Trading
 * (2015), pp. 129 f.).
 * <p>
 * Therefore a factor is needed to ensure the desired volatility target is met and to also ensure that the combined
 * forecasts move around the desired base scale (as defined in the {@link SubSystem} owning this
 * DiversificationMultiplier).
 * 
 * @author Max Rumford
 */
public class DiversificationMultiplier {

	/* The value of this diversification multiplier. */
	private BigDecimal value = BigDecimal.valueOf(0d);
	/* The weights of the given rules. */
	private BigDecimal[] weights;
	/* The relevant forecasts of the given rules. */
	private BigDecimal[][] relevantForecasts;
	/* The correaltions of the given rules. */
	private BigDecimal[][] correlations;

	/**
	 * Constructor for the class DiversificationMultiplier (DM). A DM is always only valid for a given set of rules
	 * (which should be, but don't have to be, unique). There rules must be known upon instantiation of a new DM. Based
	 * on the given rules the DM value is calculated, using the relative weights of all given rules (relative based on
	 * their placing inside the rules tree).
	 * <p>
	 * There can be as many rules in the given array as desired, though the desired ratio between rules (and their
	 * respective weights, which shrink with each new layer) and their significance to the whole system should be
	 * accounted for and is at the user's discretion.
	 * 
	 * @param rules {@code Rule[]} An array of {@link Rule}s to be accounted for in this DM. Must pass
	 *              {@link Validator#validateRules(Rule[])}.
	 */
	public DiversificationMultiplier(Rule[] rules) {
		validateInput(rules);

		WeightsAndForecasts weightsAndForecasts = getWeightsAndForecastsFromRules(rules);
		this.setWeights(weightsAndForecasts.weights);
		this.setRelevantForecasts(weightsAndForecasts.forecasts);

		this.setCorrelations(getCorrelationsFromForecasts(this.getRelevantForecasts()));

		this.setValue(this.calculateDiversificiationMultiplierValue());
	}

	/**
	 * Private class for extraction of weights and forecasts from the given rules.
	 */
	private class WeightsAndForecasts {
		public BigDecimal[] weights;
		public BigDecimal[][] forecasts;

		public WeightsAndForecasts(BigDecimal[] weights, BigDecimal[][] relevantForecasts) {
			this.weights = weights;
			this.forecasts = relevantForecasts;
		}
	}

	/**
	 * Calculate the diversification multiplier for the weights and correlations set with this class. Represents this
	 * formula with c = matrix of correlations, w = list of weights, i,j = indices: 1 / sqrt[ SUM( c_i,j * w_i * w_j ) ]
	 * 
	 * @return                          {@code BigDecimal} diversification multiplier for set weights and correlations
	 * @throws IllegalArgumentException if correlations or weights do not meet criteria: non-empty, same number of
	 *                                  values, correlations: same amount of rows and columns
	 */
	private BigDecimal calculateDiversificiationMultiplierValue() {
		BigDecimal[][] instanceCorrelations = this.getCorrelations();
		BigDecimal[] instanceWeights = this.getWeights();

		/* local field to hold sum of correlations multiplied with weights */
		BigDecimal sumOfCorrelationsWeights = BigDecimal.valueOf(0d);

		/*
		 * Get the sum of all correlations multiplier with both corresponding weights...
		 */
		for (int row = 0; row < instanceCorrelations.length; row++) {
			for (int col = 0; col < instanceCorrelations.length; col++) {
				/*
				 * ... by multiplying the correlation with both corresponding weights
				 */
				sumOfCorrelationsWeights = sumOfCorrelationsWeights.add( //
				        instanceCorrelations[row][col] //
				                .multiply(instanceWeights[row]) //
				                .multiply(instanceWeights[col]));
			}
		}

		/*
		 * sumOfCorrelationsWeights is always > 0 as there is always at least one weight > 0 and at least on correlation
		 * > 0 (self correlation)
		 */
		return BigDecimal.valueOf(1d).divide(sumOfCorrelationsWeights.sqrt(new MathContext(0)));
	}

	/**
	 * Calculate the correlations from the given array of forecast arrays.
	 * 
	 * @param  forecasts {@code BigDecimal[][]} The forecasts to be used for calculation.
	 * @return           {@code BigDecimal[][]} A matrix of correlations, as by
	 *                   {@link PearsonsCorrelation#getCorrelationMatrix()}.
	 */
	private static BigDecimal[][] getCorrelationsFromForecasts(BigDecimal[][] forecasts) {
		/*
		 * If there is only one row of data return a 1x1 self correlation matrix
		 */
		if (forecasts.length == 1) {
			return new BigDecimal[][] { { BigDecimal.valueOf(1d) } };
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
	 * Recursively get the weights and forecasts from the given array of Rules.
	 * 
	 * @param  rules {@code Rule[]} The array of rules to be searched.
	 * @return       {@link WeightsAndForecasts} The extracted weights and forecasts from the given array of Rules.
	 */
	private WeightsAndForecasts getWeightsAndForecastsFromRules(Rule[] rules) {
		BigDecimal[] weightsFromRules = {};
		BigDecimal[][] relevantForecastsFromRules = {};

		/* Iterate over the given rules */
		for (Rule rule : rules) {

			/* If a rule has variations get their weights and forecasts */
			if (rule.hasVariations()) {
				WeightsAndForecasts wafToAdd = getWeightsAndForecastsFromRules(rule.getVariations());
				for (BigDecimal weight : wafToAdd.weights)
					weightsFromRules = ArrayUtils.add(weightsFromRules, weight.multiply(rule.getWeight()));

				for (BigDecimal[] forecasts : wafToAdd.forecasts)
					relevantForecastsFromRules = ArrayUtils.add(relevantForecastsFromRules, forecasts);

			} else {
				BigDecimal weight = rule.getWeight();
				/*
				 * If a top level rule has no variations its weight has not been set. Manually set its weight to be
				 * 1/numberOfTopeLevelRules
				 */
				if (weight.compareTo(BigDecimal.valueOf(0d)) == 0)
					weight = BigDecimal.valueOf(1d).divide(BigDecimal.valueOf(rules.length));
				weightsFromRules = ArrayUtils.add(weightsFromRules, weight);

				relevantForecastsFromRules = ArrayUtils.add(relevantForecastsFromRules,
				        rule.extractRelevantForecastValues());
			}
		}
		return new WeightsAndForecasts(weightsFromRules, relevantForecastsFromRules);
	}

	/**
	 * Validates the given input values.
	 * 
	 * @param rules {@code Rule[]} Must pass {@link Validator#validateRules(Rule[])}.
	 */
	private static void validateInput(Rule[] rules) {
		Validator.validateRules(rules);
	}

	/**
	 * ====================================================================== OVERRIDES
	 * ======================================================================
	 */

	/**
	 * A hash code for this diversification multiplier.
	 */
	@GeneratedCode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(correlations);
		result = prime * result + Arrays.deepHashCode(relevantForecasts);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + Arrays.hashCode(weights);
		return result;
	}

	/**
	 * Checks if this diversification multiplier is equal to another diversification multiplier.
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
		DiversificationMultiplier other = (DiversificationMultiplier) obj;
		if (!Arrays.deepEquals(correlations, other.correlations))
			return false;
		if (!Arrays.deepEquals(relevantForecasts, other.relevantForecasts))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (!Arrays.equals(weights, other.weights))
			return false;
		return true;
	}

	/**
	 * Outputs the fields of this diversification multiplier as a {@code String}.
	 */
	@GeneratedCode
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DiversificationMultiplier [value=");
		builder.append(value);
		builder.append(", weights=");
		builder.append(Arrays.toString(weights));
		builder.append(", relevantForecasts=");
		builder.append(Arrays.toString(relevantForecasts));
		builder.append(", correlations=");
		builder.append(Arrays.toString(correlations));
		builder.append("]");
		return builder.toString();
	}

	/**
	 * ====================================================================== GETTERS AND SETTERS
	 * ======================================================================
	 */

	/**
	 * Get value of this {@link DiversificationMultiplier}
	 * 
	 * @return {@code BigDecimal} value of this {@link DiversificationMultiplier}
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * Set the value in this {@link DiversificationMultiplier}
	 * 
	 * @param value the value to set
	 */
	private void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * Get the weights considered in this {@link DiversificationMultiplier}
	 * 
	 * @return {@code BigDecimal[]} Weights in this {@link DiversificationMultiplier}
	 */
	public BigDecimal[] getWeights() {
		return weights;
	}

	/**
	 * Set the Weights in this {@link DiversificationMultiplier}
	 * 
	 * @param {@code BigDecimal[]} the weights to set
	 */
	private void setWeights(BigDecimal[] weights) {
		this.weights = weights;
	}

	/**
	 * Get the relevant forecasts in this {@link DiversificationMultiplier}
	 * 
	 * @return relevantForecasts DiversificationMultiplier
	 */
	public BigDecimal[][] getRelevantForecasts() {
		return relevantForecasts;
	}

	/**
	 * Set the relevant forecasts in this {@link DiversificationMultiplier}
	 * 
	 * @param relevantForecasts the relevantForecasts to set
	 */
	private void setRelevantForecasts(BigDecimal[][] relevantForecasts) {
		this.relevantForecasts = relevantForecasts;
	}

	/**
	 * Get the correlations in this {@link DiversificationMultiplier}
	 * 
	 * @return {@code BigDecimal[][]} The correlations in this {@link DiversificationMultiplier}
	 */
	public BigDecimal[][] getCorrelations() {
		return correlations;
	}

	/**
	 * Set the correlations to be used in this {@link DiversificationMultiplier}
	 * 
	 * @param {@code BigDecimal[][]} the correlations to be used in this {@link DiversificationMultiplier}
	 */
	private void setCorrelations(BigDecimal[][] correlations) {
		this.correlations = correlations;
	}
}