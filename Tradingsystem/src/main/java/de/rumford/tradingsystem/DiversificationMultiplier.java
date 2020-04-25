package de.rumford.tradingsystem;

import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 * @author Max Rumford
 *
 */
public class DiversificationMultiplier {

	private double value = 0d;
	private double[] weights;
	private double[][] correlations;

	/**
	 * 
	 */
	/*
	 * TODO siehe setCorrelations -> bekommt nicht double[][] correlations, sondern
	 * double[][] values und nutzt dann set.Correlations
	 */
	public DiversificationMultiplier(double[] weights, double[][] correlations) throws IllegalArgumentException {
		this.setWeights(weights);
		this.setCorrelations(correlations);

		try {
			this.validateConstructorArguments();
		} catch (IllegalArgumentException e) {
			throw e;
		}

		try {
			this.value = this.calculateDiversificiationMultiplierValue();
		} catch (IllegalArgumentException e) {
			throw e;
		}
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
	private double calculateDiversificiationMultiplierValue() throws IllegalArgumentException {
		double[][] correlations = this.getCorrelations();
		double[] weights = this.getWeights();

		/* local field to hold sum of correlations multiplied with weights */
		double sumOfCorrelationsWeights = 0f;

		/* Get the sum of all correlations multiplier with both corresponding weights */
		for (int row = 0; row < correlations.length; row++) {
			// Check if self correlations are correctly filled (diagonal line) from "top
			// left" to "bottom right" (these values have to be 1), e.g.
			// { {1d, 0.5d, 0.75d},
			// {0.5d, 1d, 0.6d},
			// {0.75d, 0.6d, 1d}
			// }
			if (correlations[row][row] != 1d)
				throw new IllegalArgumentException("Self correlations are not properly populated");
			for (int col = 0; col < correlations.length; col++) {
				// Check if correlations matrix is symmetrical, e.g.
				// { {1d, 0.5d, 0.75d},
				// {0.5d, 1d, 0.6d},
				// {0.75d, 0.6d, 1d}
				// }
				if (correlations[row][col] != correlations[col][row])
					throw new IllegalArgumentException("Correlations matrix is not properly populated");

				/* multiply the correlation with both corresponding weights */
				sumOfCorrelationsWeights += correlations[row][col] * weights[row] * weights[col];
			}
		}

		/*
		 * sumOfCorrelationsWeights is always > 0 as there is always at least one weight
		 * > 0 and at least on correlation > 0 (self correlation)
		 */
		return 1 / Math.sqrt(sumOfCorrelationsWeights);
	}

	/**
	 * Check if the arguments passed to the constructor meet the constraints for
	 * calculating a diversification multiplier
	 * 
	 * @throws IllegalArgumentException if any constraint is not fulfilled
	 */
	private void validateConstructorArguments() throws IllegalArgumentException {
		/* Check if correlations has values in it */
		if (correlations.length == 0)
			throw new IllegalArgumentException("Correlations must not have zero values");

		/* Check if weights has values in it */
		if (weights.length == 0)
			throw new IllegalArgumentException("Weights must not have zero values");

		/*
		 * Check if correlations is a square two dimensional array (number of rows and
		 * columns are the same on every row)
		 */
		for (int i = 0; i < correlations.length; i++) {
			if (correlations.length != correlations[i].length)
				throw new IllegalArgumentException("Correlations must have as many rows as columns"
						+ "and all columns and rows must have the same length.");
		}

		/* Check if weights and correlations arrays have the same length */
		if (weights.length != correlations.length)
			throw new IllegalArgumentException("There must be as many weights as correlations columns/rows");

		/* Check for negative weights */
		Double[] list = ArrayUtils.toObject(weights);
		double min = Collections.min(Arrays.asList(list));
		if (min < 0)
			throw new IllegalArgumentException("Negative weights are not allowed");

		/* Check if weights add up to 1 */
		double sum = Arrays.stream(weights).sum();
		if (sum != 1)
			throw new IllegalArgumentException("Weights don't sum up to 1");
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
		result = prime * result + Arrays.deepHashCode(correlations);
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(weights);
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
		DiversificationMultiplier other = (DiversificationMultiplier) obj;
		if (!Arrays.deepEquals(correlations, other.correlations))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		if (!Arrays.equals(weights, other.weights))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DiversificationMultiplier [value=" + value + ", weights=" + Arrays.toString(weights) + ", correlations="
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
		/* TODO */
		/* TODO */
		/* TODO */
		/* TODO */
		double[][] data = {};
		PearsonsCorrelation corr = new PearsonsCorrelation(data);
		/* TODO */
		/* TODO */
		/* TODO */
		/* TODO */
		this.correlations = correlations;
	}

}
