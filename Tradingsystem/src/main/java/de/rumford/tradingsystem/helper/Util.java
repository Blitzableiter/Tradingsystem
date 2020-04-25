package de.rumford.tradingsystem.helper;

import java.util.DoubleSummaryStatistics;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
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
	};

	/**
	 * Adjusts a given value for the given standard deviation
	 * 
	 * @param value             {@code double} value to be adjusted
	 * @param standardDeviation {@code double} standard deviation to be adjusted for
	 * @return {@code double} standard deviation adjusted value
	 * @throws IllegalArgumentException if the given standard deviation is zero
	 */
	public static double adjustForStandardDeviation(double value, double standardDeviation) {
		if (standardDeviation == 0)
			throw new IllegalArgumentException("Standard deviation must not be zero");
		return value / standardDeviation;
	}

	/**
	 * Scales the forecast based on the given scalar
	 * 
	 * @param unscaledForecast {@code double} unscaled forecast to be scaled
	 * @param scalar           {@code double} scalar to scale the unscaled forecast
	 * @return {@code double} the scaled forecast
	 */
	public static double calculateForecast(double unscaledForecast, double scalar) throws IllegalArgumentException {
		if (scalar == 0)
			throw new IllegalArgumentException("Forecast scalar must not be zero");
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
	public static double calculateForecastScalar(double[] values, double baseScale) throws IllegalArgumentException {
		if (values.length == 0)
			throw new IllegalArgumentException("Given array of values must not be empty");

		if (baseScale == 0)
			throw new IllegalArgumentException("Base scale must not be 0.");

		/* helper array */
		double[] absoluteValues = new double[values.length];

		/* Calculate the absolute values for all values in the given array */
		for (int i = 0; i < values.length; i++)
			absoluteValues[i] = Math.abs(values[i]);

		/* Calculate the average of absolute values */
		DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
		/* Load absolute values into stats */
		for (double value : absoluteValues)
			stats.accept(value);
		/* Get average of all values */
		double averageOfAbsolutes = stats.getAverage();

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
	 *         represented in percentage points
	 * @throws IllegalArgumentException if the given former value is zero
	 */
	public static double calculateReturn(double formerValue, double latterValue) {
		if (formerValue == 0)
			throw new IllegalArgumentException("Former value cannot be zero");
		return latterValue / formerValue - 1d;
	}

	/**
	 * Calculate the correlations between the given array of value arrays.
	 * 
	 * @param values {@code double[][]} An array of 3 arrays, A, B, and C, of
	 *               values.
	 * @return {@code double[]} The correlations between the given rows A, B, and C
	 *         as { corr_AB, corr_BC, corr_CA }.
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
	public static double[] calculateCorrelationsOfThreeRows(double[][] values) {
		/* Check if the given array is null */
		if (values == null)
			throw new IllegalArgumentException("The given array of arrays is null.");
		/* Check if the given array contains exactly three values. */
		if (values.length != 3)
			throw new IllegalArgumentException("The given array of arrays contains " + values.length
					+ " elements altough only 3 values were expected.");

		/* Check if the array contains null. */
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null)
				throw new IllegalArgumentException(
						"The given array contains null at position " + i + " although null is not permitted.");
			for (int j = 0; j < values[i].length; j++)
				if (Double.isNaN(values[i][j]))
					throw new IllegalArgumentException(
							"Array at position " + i + " contains Double.NaN at position " + j);
		}
		/* Check if the given arrays all have the same length. */
		if (values[0].length != values[1].length || values[0].length != values[2].length)
			throw new IllegalArgumentException("The given arrays are not of the same length.");

		/* Load the given values into rows of a matrix */
		RealMatrix matrix = new BlockRealMatrix(values);
		/* Transpose the values into columns to get the correct correlations */
		matrix = matrix.transpose();

		/* Get the correlations of the passed value arrays */
		PearsonsCorrelation pearsonsCorrelations = new PearsonsCorrelation(matrix);
		RealMatrix correlationMatrix = pearsonsCorrelations.getCorrelationMatrix();

		/*
		 * E.g. the correlation between the columns 0 and 1 is at matrix position 0/1.
		 */
		double correlation01 = correlationMatrix.getEntry(0, 1);
		double correlation12 = correlationMatrix.getEntry(1, 2);
		double correlation02 = correlationMatrix.getEntry(0, 2);

		double[] correlations = {};
		correlations = ArrayUtils.add(correlations, correlation01);
		correlations = ArrayUtils.add(correlations, correlation12);
		correlations = ArrayUtils.add(correlations, correlation02);

		return correlations;
	}

}