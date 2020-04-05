/**
 * 
 */
package de.rumford.tradingsystem;

/**
 * @author Max Rumford
 *
 */
public abstract class Rule {

	private double forecastScalar;
	private double standardDeviationAdjustedValue;
	private double weight;

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */

//	
//	TODO
//	ABSOLUTE VALUES FOR CALCULATION OF FORECAST SCALAR
//	

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
	public void setForecastScalar(double forecastScalar) {
		this.forecastScalar = forecastScalar;
	}

	/**
	 * Get the standard deviation adjusted value for this rule
	 * 
	 * @return {@code double} the standard deviation adjusted value for this rule
	 */
	public double getStandardDeviationAdjustedValue() {
		return standardDeviationAdjustedValue;
	}

	/**
	 * Set the standard deviation adjusted value for this rule
	 * 
	 * @param standardDeviationAdjustedValue {@code double} the standard deviation
	 *                                       adjusted value to be set for this rule
	 */
	public void setStandardDeviationAdjustedValue(double standardDeviationAdjustedValue) {
		this.standardDeviationAdjustedValue = standardDeviationAdjustedValue;
	}

	/**
	 * Get the weight of this rule
	 * 
	 * @return {@code double{@code  the weight of this rule
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Set the weight of this rule
	 * 
	 * @param weight {@code double} the weight to be set for this rule
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

}
