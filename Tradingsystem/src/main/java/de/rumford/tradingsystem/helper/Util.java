package de.rumford.tradingsystem.helper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import de.rumford.tradingsystem.Rule;

/**
 * Utility class as used throughout the library containing solely of static methods representing mainly mathematical
 * calculations.
 * 
 * @author Max Rumford
 */
public final class Util {

	/**
	 * Constructor for the {@link Util} class<br/>
	 * Only supports static methods, hence no instance shall be created, hence a private constructor
	 */
	private Util() {
	}

	/**
	 * Adjusts a given value for the given standard deviation
	 * 
	 * @param  value             {@code double} value to be adjusted
	 * @param  standardDeviation {@code double} standard deviation to be adjusted for
	 * @return                   {@code double} standard deviation adjusted value. Double.NaN, if the given standard
	 *                           deviation is zero.
	 */
	public static BigDecimal adjustForStandardDeviation(BigDecimal value, BigDecimal standardDeviation) {
		if (standardDeviation.compareTo(BigDecimal.valueOf(0)) == 0)
			return BigDecimal.valueOf(Double.NaN);
		return value.divide(standardDeviation);
	}

	/**
	 * Check if the given rules are unique by utilizing {@link Rule#equals(Object)}
	 * 
	 * @param  rules {@code Rule} An array of rules to be check for uniqueness.
	 * @return       {@code boolean} True, if the rules are unique. False otherwise.
	 */
	public static boolean areRulesUnique(Rule[] rules) {
		if (rules == null)
			throw new IllegalArgumentException("The given rules must not be null");
		if (ArrayUtils.contains(rules, null))
			throw new IllegalArgumentException("The given array must not contain nulls");
		if (rules.length == 0)
			throw new IllegalArgumentException("The given array of rules must not be empty.");

		for (int i = 0; i < rules.length - 1; i++) {
			if (rules[i].equals(rules[i + 1])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Calculates the average value of the given array of values.
	 * 
	 * @param  values                   {@code double[]} An array of values.
	 * @return                          {@code double} The average value of the given values.
	 * @throws IllegalArgumentException if the given array is null.
	 */
	public static BigDecimal calculateAverage(BigDecimal[] values) {
		Validator.validateArrayOfBigDecimals(values);

		BigDecimal sum = BigDecimal.valueOf(0);

		/* Add up all values */
		for (BigDecimal value : values)
			sum.add(value);
		/* Get average of all values */
		return sum.divide(BigDecimal.valueOf(values.length));
	}

	/**
	 * Calculate the correlations between the given array of value arrays.
	 * 
	 * @param  valuesMatrix             {@code double[][]} An array of n arrays of values.
	 * @return                          {@code double[]} The correlations between the given rows as by
	 *                                  {@link PearsonsCorrelation#getCorrelationMatrix()}.
	 * @throws IllegalArgumentException if the given array is null.
	 * @throws IllegalArgumentException if the given array does not contain exactly 3 values.
	 * @throws IllegalArgumentException if the given array contains null.
	 * @throws IllegalArgumentException if the given array contains arrays containing null.
	 * @throws IllegalArgumentException if the given array contains arrays containing Double.NaN.
	 * @throws IllegalArgumentException if the given array contains arrays not of the same length.
	 */
	public static BigDecimal[] calculateCorrelationOfRows(BigDecimal[][] valuesMatrix) {
		/*
		 * If one of the rows contains all identical values no correlation can be calculated, as a division by zero will
		 * occur in correlations calculation.
		 */
		for (int i = 0; i < valuesMatrix.length; i++) {
			HashSet<BigDecimal> hashSetOfValues = new HashSet<>(Arrays.asList(valuesMatrix[i]));
			if (hashSetOfValues.size() == 1) {
				throw new IllegalArgumentException("Correlations cannot be calculated caused by all identical "
				        + "values in row at position " + i + ".");
			}
		}

		/* Load the given values into rows of a matrix */
		RealMatrix matrix = new BlockRealMatrix(valuesMatrix);
		/* Transpose the values into columns to get the correct correlations */
		matrix = matrix.transpose();

		/* Get the correlations of the passed value arrays */
		PearsonsCorrelation pearsonsCorrelations = new PearsonsCorrelation(matrix);
		RealMatrix correlationMatrix = pearsonsCorrelations.getCorrelationMatrix();

		double[] correlations = {};
		for (int rowIndex = 0; rowIndex < correlationMatrix.getRowDimension(); rowIndex++) {
			for (int columnIndex = 0; columnIndex < correlationMatrix.getColumnDimension(); columnIndex++) {
				if (columnIndex < rowIndex)
					correlations = ArrayUtils.add(correlations, correlationMatrix.getEntry(rowIndex, columnIndex));
			}
		}
		return correlations;
	}

	/**
	 * Scales the forecast based on the given scalar
	 * 
	 * @param  unscaledForecast {@code double} unscaled forecast to be scaled
	 * @param  scalar           {@code double} scalar to scale the unscaled forecast
	 * @return                  {@code double} the scaled forecast
	 */
	public static BigDecimal calculateForecast(BigDecimal unscaledForecast, BigDecimal scalar) {
		return unscaledForecast.multiply(scalar);
	}

	/**
	 * Calculates the forecast scalar for the given array of values in the scale of the given base scale. Formula: F =
	 * baseScale / [ sum( |fc| ) / n ] Base scale divided by the average of absolutes.
	 * 
	 * @param  values                   {@code double[]} values to be scaled by the forecast scalar
	 * @param  baseScale                {@code double} base scale for scaling of the forecast scalar
	 * @return                          {@code double} forecast scalar to scale the given values to fit the given scalar
	 *                                  base. Returns Double.NaN if the average of absolute values is 0
	 * @throws IllegalArgumentException if the average of the absolutes of the given values is zero
	 * @throws IllegalArgumentException if the given baseScale is zero
	 */
	public static BigDecimal calculateForecastScalar(BigDecimal[] values, BigDecimal baseScale) {

		Validator.validateArrayOfBigDecimals(values);
		if (values.length == 0)
			throw new IllegalArgumentException("Given array of values must not be empty");

		try {
			Validator.validatePositiveDouble(baseScale);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Given base scale does not meet specifications.", e);
		}

		/* helper array */
		BigDecimal[] absoluteValues = new BigDecimal[values.length];

		/* Calculate the absolute values for all values in the given array */
		for (int i = 0; i < values.length; i++)
			absoluteValues[i] = values[i].abs();

		/* Get average of all values */
		BigDecimal averageOfAbsolutes = Util.calculateAverage(absoluteValues);

		if (averageOfAbsolutes.compareTo(BigDecimal.valueOf(0)) == 0)
			return BigDecimal.valueOf(Double.NaN);

		return baseScale.divide(averageOfAbsolutes);
	}

	/**
	 * Calculates the difference between two values in percentage points of change as seen from the former value
	 * 
	 * @param  formerValue {@code double} value the value of the difference is based on
	 * @param  latterValue {@code double} "new" value which represents a changed value in comparison to formerValue
	 * @return             {@code double} difference between formerValue and latterValue represented in percentage
	 *                     points. Double.NaN if the given formerValue is zero.
	 */
	public static BigDecimal calculateReturn(BigDecimal formerValue, BigDecimal latterValue) {
		if (formerValue.compareTo(BigDecimal.valueOf(0)) == 0)
			return BigDecimal.valueOf(Double.NaN);
		return latterValue //
		        .divide(formerValue) //
		        .subtract(BigDecimal.valueOf(1d));
	}

	/**
	 * Calculate the weights that should be given to the rows of values making up the given correlations. Expects an
	 * array of length 3, where position 0 holds the correlation of rows A and B, position 1 holds the correlation for
	 * rows A and C, and position 2 holds the correlation for rows B and C.
	 * 
	 * @param  correlations {@code double[]} Three values representing the correlations between the rows A, B and C. The
	 *                      expected array is constructed as follows: { corr_AB, corr_AC, corr_BC }. See
	 *                      {@link Validator#validateCorrelations(double[])} for limitations.
	 * @return              {@code double[]} The calculated weights { w_A, w_B, w_C }.
	 */
	public static BigDecimal[] calculateWeightsForThreeCorrelations(BigDecimal[] correlations) {
		Validator.validateCorrelations(correlations);

		for (int i = 0; i < correlations.length; i++) {
			/*
			 * Floor negative correlations at 0 (See Carver: "Systematic Trading", p. 79)
			 */
			if (correlations[i].compareTo(BigDecimal.valueOf(0d)) < 0)
				correlations[i] = BigDecimal.valueOf(0d);
		}

		BigDecimal[] weights = {};
		/*
		 * Catch three equal correlations. Three correlations of 1 each would break further calculation.
		 */
		if (correlations[0].compareTo(correlations[1]) == 0 && correlations[0].compareTo(correlations[2]) == 0) {
			BigDecimal correlationOfOneThird = BigDecimal.valueOf(1d / 3d);
			weights = ArrayUtils.add(weights, correlationOfOneThird);
			weights = ArrayUtils.add(weights, correlationOfOneThird);
			weights = ArrayUtils.add(weights, correlationOfOneThird);
			return weights;
		}

		/* Get the average correlation each row of values has */
		BigDecimal averageCorrRowA = correlations[0].add(correlations[1]).divide(BigDecimal.valueOf(2d));
		BigDecimal averageCorrRowB = correlations[0].add(correlations[2]).divide(BigDecimal.valueOf(2d));
		BigDecimal averageCorrRowC = correlations[1].add(correlations[2]).divide(BigDecimal.valueOf(2d));

		BigDecimal[] averageCorrelations = { averageCorrRowA, averageCorrRowB, averageCorrRowC };

		/*
		 * Subtract each average correlation from 1 to get an inverse-ish value
		 */
		for (int i = 0; i < averageCorrelations.length; i++)
			averageCorrelations[i] = BigDecimal.valueOf(1d).subtract(averageCorrelations[i]);

		/* Calculate the sum of average correlations. */
		BigDecimal sumOfAverageCorrelations = BigDecimal.valueOf(0d);
		for (BigDecimal avgCorrelation : averageCorrelations)
			sumOfAverageCorrelations = sumOfAverageCorrelations.add(avgCorrelation);

		/*
		 * Normalize the average correlations so they sum up to 1. These normalized values are the weights.
		 */
		for (int i = 0; i < averageCorrelations.length; i++)
			weights = ArrayUtils.add(weights, averageCorrelations[i].divide(sumOfAverageCorrelations));

		return weights;
	}

	/**
	 * Returns the position literal for a given forecast.
	 * <ul>
	 * <li>"Long" for forecasts greater 0.</li>
	 * <li>"Short" for forecasts less than 0.</li>
	 * <li>"Hold" for forecasts of 0.</li>
	 * </ul>
	 * 
	 * @param  forecast a forecast.
	 * @return          The String literal for the given forecast.
	 */
	public static String getPositionFromForecast(BigDecimal forecast) {
		if (forecast.compareTo(BigDecimal.valueOf(0d)) > 0)
			return "Long";
		if (forecast.compareTo(BigDecimal.valueOf(0d)) < 0)
			return "Short";
		return "Hold";
	}
}