package de.rumford.tradingsystem.helper;

import java.util.DoubleSummaryStatistics;
import java.util.stream.DoubleStream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import de.rumford.tradingsystem.Rule;

/**
 * Utility class as used throughout the library containing solely of static
 * methods representing mainly mathematical calculations.
 * 
 * @author Max Rumford
 *
 */
public final class Util {

	/**
	 * Constructor for the {@link Util} class<br/>
	 * Only supports static methods, hence no instance shall be created, hence a
	 * private constructor
	 */
	private Util() {
	}

	/**
	 * Check if the given rules are unique by utilizing {@link Rule#equals(Object)}
	 * 
	 * @param rules {@code Rule} An array of rules to be check for uniqueness.
	 * @return {@code boolean} True, if the rules are unique. False otherwise.
	 */
	public static boolean areRulesUnique(Rule[] rules) {
		if (rules == null)
			throw new IllegalArgumentException(
					"The given rules must not be null");
		if (ArrayUtils.contains(rules, null))
			throw new IllegalArgumentException(
					"The given array must not contain nulls");
		if (rules.length == 0)
			throw new IllegalArgumentException(
					"The given array of rules must not be empty.");

		for (int i = 0; i < rules.length - 1; i++) {
			if (rules[i].equals(rules[i + 1])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Adjusts a given value for the given standard deviation
	 * 
	 * @param value             {@code double} value to be adjusted
	 * @param standardDeviation {@code double} standard deviation to be adjusted for
	 * @return {@code double} standard deviation adjusted value. Double.NaN, if the
	 *         given standard deviation is zero.
	 */
	public static double adjustForStandardDeviation(double value,
			double standardDeviation) {
		if (standardDeviation == 0)
			return Double.NaN;
		return value / standardDeviation;
	}

	/**
	 * Calculates the average value of the given array of values.
	 * 
	 * @param values {@code double[]} An array of values.
	 * @return {@code double} The average value of the given values.
	 * @throws IllegalArgumentException if the given array is null.
	 */
	public static double calculateAverage(double[] values) {
		Validator.validateArrayOfDoubles(values);

		/* Calculate the average of absolute values */
		DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
		/* Load absolute values into stats */
		for (double value : values)
			stats.accept(value);
		/* Get average of all values */
		return stats.getAverage();
	}

	/**
	 * Calculate the correlations between the given array of value arrays.
	 * 
	 * @param valuesMatrix {@code double[][]} An array of n arrays of values.
	 * @return {@code double[]} The correlations between the given rows as by
	 *         {@link PearsonsCorrelation#getCorrelationMatrix()}.
	 * @throws IllegalArgumentException if the given array is null.
	 * @throws IllegalArgumentException if the given array does not contain exactly
	 *                                  3 values.
	 * @throws IllegalArgumentException if the given array contains null.
	 * @throws IllegalArgumentException if the given array contains arrays
	 *                                  containing null.
	 * @throws IllegalArgumentException if the given array contains arrays
	 *                                  containing Double.NaN.
	 * @throws IllegalArgumentException if the given array contains arrays not of
	 *                                  the same length.
	 * 
	 */
	public static double[] calculateCorrelationOfRows(
			double[][] valuesMatrix) {
		/*
		 * If one of the rows contains all identical values no correlation can be
		 * calculated, as a division by zero will occur in correlations calculation.
		 */
		for (int i = 0; i < valuesMatrix.length; i++) {
			double[] noDuplicates = DoubleStream.of(valuesMatrix[i]).distinct()
					.toArray();
			if (noDuplicates.length == 1) {
				throw new IllegalArgumentException(
						"Correlations cannot be calculated caused by all identical values in row at position "
								+ i + ".");
			}
		}

		/* Load the given values into rows of a matrix */
		RealMatrix matrix = new BlockRealMatrix(valuesMatrix);
		/* Transpose the values into columns to get the correct correlations */
		matrix = matrix.transpose();

		/* Get the correlations of the passed value arrays */
		PearsonsCorrelation pearsonsCorrelations = new PearsonsCorrelation(
				matrix);
		RealMatrix correlationMatrix = pearsonsCorrelations
				.getCorrelationMatrix();

		double[] correlations = {};
		for (int rowIndex = 0; rowIndex < correlationMatrix
				.getRowDimension(); rowIndex++) {
			for (int columnIndex = 0; columnIndex < correlationMatrix
					.getColumnDimension(); columnIndex++) {
				if (columnIndex < rowIndex)
					correlations = ArrayUtils.add(correlations,
							correlationMatrix.getEntry(rowIndex, columnIndex));
			}
		}
		return correlations;
	}

	/**
	 * Scales the forecast based on the given scalar
	 * 
	 * @param unscaledForecast {@code double} unscaled forecast to be scaled
	 * @param scalar           {@code double} scalar to scale the unscaled forecast
	 * @return {@code double} the scaled forecast
	 */
	public static double calculateForecast(double unscaledForecast,
			double scalar) {
		return unscaledForecast * scalar;
	}

	/**
	 * Calculates the forecast scalar for the given array of values in the scale of
	 * the given base scale.
	 * 
	 * Formula: F = baseScale / [ sum( |fc| ) / n ]
	 * 
	 * Base scale divided by the average of absolutes.
	 * 
	 * @param values    {@code double[]} values to be scaled by the forecast scalar
	 * @param baseScale {@code double} base scale for scaling of the forecast scalar
	 * @return {@code double} forecast scalar to scale the given values to fit the
	 *         given scalar base. Returns Double.NaN if the average of absolute
	 *         values is 0
	 * @throws IllegalArgumentException if the average of the absolutes of the given
	 *                                  values is zero
	 * @throws IllegalArgumentException if the given baseScale is zero
	 */
	public static double calculateForecastScalar(double[] values,
			double baseScale) {

		Validator.validateArrayOfDoubles(values);
		if (values.length == 0)
			throw new IllegalArgumentException(
					"Given array of values must not be empty");

		try {
			Validator.validatePositiveDouble(baseScale);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"Given base scale does not meet specifications.", e);
		}

		/* helper array */
		double[] absoluteValues = new double[values.length];

		/* Calculate the absolute values for all values in the given array */
		for (int i = 0; i < values.length; i++)
			absoluteValues[i] = Math.abs(values[i]);

		/* Get average of all values */
		double averageOfAbsolutes = Util.calculateAverage(absoluteValues);

		if (averageOfAbsolutes == 0)
			return Double.NaN;

		return baseScale / averageOfAbsolutes;
	}

	/**
	 * Calculates the difference between two values in percentage points of change
	 * as seen from the former value
	 * 
	 * @param formerValue {@code double} value the value of the difference is based
	 *                    on
	 * @param latterValue {@code double} "new" value which represents a changed
	 *                    value in comparison to formerValue
	 * @return {@code double} difference between formerValue and latterValue
	 *         represented in percentage points. Double.NaN if the given formerValue
	 *         is zero.
	 */
	public static double calculateReturn(double formerValue,
			double latterValue) {
		if (formerValue == 0)
			return Double.NaN;
		return latterValue / formerValue - 1d;
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
	 *                     corr_BC }. See
	 *                     {@link Validator#validateCorrelations(double[])} for
	 *                     limitations.
	 * @return {@code double[]} The calculated weights { w_A, w_B, w_C }.
	 */
	public static double[] calculateWeightsForThreeCorrelations(
			double[] correlations) {
		Validator.validateCorrelations(correlations);

		for (int i = 0; i < correlations.length; i++) {
			/*
			 * Floor negative correlations at 0 (See Carver: "Systematic Trading", p. 79)
			 */
			if (correlations[i] < 0)
				correlations[i] = 0;
		}

		double[] weights = {};
		/*
		 * Catch three equal correlations. Three correlations of 1 each would break
		 * further calculation.
		 */
		if (correlations[0] == correlations[1]
				&& correlations[0] == correlations[2]) {
			double correlationOfOneThird = 1d / 3d;
			weights = ArrayUtils.add(weights, correlationOfOneThird);
			weights = ArrayUtils.add(weights, correlationOfOneThird);
			weights = ArrayUtils.add(weights, correlationOfOneThird);
			return weights;
		}

		/* Get the average correlation each row of values has */
		double averageCorrRowA = (correlations[0] + correlations[1]) / 2;
		double averageCorrRowB = (correlations[0] + correlations[2]) / 2;
		double averageCorrRowC = (correlations[1] + correlations[2]) / 2;

		double[] averageCorrelations = { averageCorrRowA, averageCorrRowB,
				averageCorrRowC };

		/*
		 * Subtract each average correlation from 1 to get an inverse-ish value
		 */
		for (int i = 0; i < averageCorrelations.length; i++)
			averageCorrelations[i] = 1 - averageCorrelations[i];

		/* Calculate the sum of average correlations. */
		double sumOfAverageCorrelations = DoubleStream.of(averageCorrelations)
				.sum();

		/*
		 * Normalize the average correlations so they sum up to 1. These normalized
		 * values are the weights.
		 */
		for (int i = 0; i < averageCorrelations.length; i++)
			weights = ArrayUtils.add(weights,
					averageCorrelations[i] / sumOfAverageCorrelations);

		return weights;
	}

}
