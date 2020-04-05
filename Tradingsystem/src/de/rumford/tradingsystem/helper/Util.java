package de.rumford.tradingsystem.helper;

/**
 * 
 * @author Max Rumford
 *
 */
public final class Util {
	
	/**
	 * Constructor for the {@code Util} class<br/>
	 * Only supports static methods, hence no instance shall be created, hence a private constructor
	 */
	private Util() {};
	
	/**
	 * Adjusts a given value for the given standard deviation
	 * @param value {@code double} value to be adjusted
	 * @param standardDeviation {@code double} standard deviation to be adjusted for
	 * @return {@code double} standard deviation adjusted value
	 * @throws IllegalArgumentException if the given standard deviation is zero
	 */
	public static double adjustForStandardDeviation(double value, double standardDeviation) throws IllegalArgumentException {
		if (standardDeviation == 0) throw new IllegalArgumentException("Standard devation cannot be zero");
		return value / standardDeviation;
	}
	
	/**
	 * Calculates the average value of a given array of values
	 * @param values {@code double[]} array of values to be averaged
	 * @return 0 if there are no elements in {@code values}, average value of the given values if otherwise 
	 */
	public static double calculateAverage(double[] values) throws IllegalArgumentException {
		if (values.length == 0) throw new IllegalArgumentException("No values in given array");
		
		double sum = 0;
		for (int i = 0; i<values.length; i++) {
			sum += values[i];
		}
		
		return sum/values.length;
	}

	/**
	 * Scales the forecast based on the given scalar
	 * @param unscaledForecast {@code double} unscaled forecast to be scaled
	 * @param scalar {@code double} scalar to scale the unscaled forecast
	 * @return {@code double} the scaled forecast
	 */
	public static double calculateForecast(double unscaledForecast, double scalar) throws IllegalArgumentException {
		if (scalar == 0) throw new IllegalArgumentException("Forecast scalar must not be zero");
		return unscaledForecast * scalar;
	}
	
	/**
	 * Calculates the forecast scalar for the given array of values in the scale of the given base scale
	 * @param values {@code double[]} values to be scaled by the forecast scalar 
	 * @param baseScale {@code double} base scale for scaling of the forecast scalar
	 * @return {@code double} forecast scalar to scale the given values to fit the given scalar base 
 	 * @throws IllegalArgumentException if the average of the absolutes of the given values is zero
	 */
	public static double calculateForecastScalar(double[] values, double baseScale) throws IllegalArgumentException {
		if (values.length == 0) throw new IllegalArgumentException("Given array of values must not be empty");
		
		// helper array
		double[] absoluteValues = new double[values.length];
		
		// Calculate the absolute values for all values in the given array
		for (int i = 0; i<values.length; i++) absoluteValues[i] = Math.abs(values[i]);
		double averageOfAbsolutes = Util.calculateAverage(absoluteValues);
		
		if (averageOfAbsolutes == 0) throw new IllegalArgumentException("Average of absolutes must not be zero");
		
		return baseScale / averageOfAbsolutes;
	}
	
	/**
	 * Calculates the difference between two values in percentage points of change as seen from the former value
	 * @param formerValue {@code double} value the value of the difference is based on
	 * @param latterValue {@code double} "new" value which represents a changed value in comparison to formerValue
	 * @return {@code double} difference between formerValue and latterValue represented in percentage points
	 * @throws IllegalArgumentException if the given former value is zero
	 */
	public static double calculateReturn(double formerValue, double latterValue) throws IllegalArgumentException {
		if (formerValue == 0) throw new IllegalArgumentException("Former value cannot be zero");
		return latterValue / formerValue - 1d;
	}

}
