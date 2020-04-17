/**
 * 
 */
package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * @author Max Rumford
 *
 */
public abstract class Rule {

	// TODO
	// GETTER & SETTER: PROTECTED VS. PACKAGE PROTECTED

	private double forecastScalar;
	private double standardDeviationAdjustedValue;
	private double weight;
	private Rule[] variations;
	private LocalDateTime startOfReferenceWindow;
	private LocalDateTime endOfReferenceWindow;
	private BaseValue baseValue;

	/**
	 * 
	 * @param variations {@link Rule[]} or {@code null}
	 */
	public Rule(BaseValue baseValue, Rule[] variations, LocalDateTime startOfReferenceWindow,
			LocalDateTime endOfReferenceWindow) {
		this.setBaseValue(baseValue);
		this.setVariations(variations);
		this.setStartOfReferenceWindow(startOfReferenceWindow);
		this.setEndOfReferenceWindow(endOfReferenceWindow);
		if (variations != null)
			this.weighVariations();
	}

	protected abstract double calculateRawForecast();

	protected abstract ValueDateTupel[] calculateForecasts(LocalDateTime startOfReferenceWindow,
			LocalDateTime endOfReferenceWindow);

	final private void weighVariations() {
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
			ValueDateTupel[] forecasts1 = variations[0].calculateForecasts(startOfReferenceWindow,
					endOfReferenceWindow);
			ValueDateTupel[] forecasts2 = variations[1].calculateForecasts(startOfReferenceWindow,
					endOfReferenceWindow);
			ValueDateTupel[] forecasts3 = variations[2].calculateForecasts(startOfReferenceWindow,
					endOfReferenceWindow);

			/*
			 * FIXME Insert averaging values when missing OR
			 * 
			 * delete values when another row doesn't have the current LocalDateTime.
			 * 
			 * EXTRACTING OF MINIMUM LENGTH CAN BE OMITTED THEN
			 */

			/*
			 * Extract the values from the forecasts array, as the Dates are not needed for
			 * correlation calculation.
			 */
			double[][] values = {};
			values = ArrayUtils.add(values, ValueDateTupel.getValues(forecasts1));
			values = ArrayUtils.add(values, ValueDateTupel.getValues(forecasts2));
			values = ArrayUtils.add(values, ValueDateTupel.getValues(forecasts3));

			/*
			 * Extract the minimum length of all arrays so the longer ones can be cut.
			 * Correlations can only be done when the underlying arrays have the same value
			 */
			/* FIXME */
			int minLength = Integer.MAX_VALUE;
			for (double[] value : values)
				if (value.length < minLength)
					minLength = value.length;

			for (int i = 0; i < values.length; i++) {
				if (values[i].length != minLength) {
					double[] localValues = new double[minLength];
					System.arraycopy(values[i], 0, localValues, 0, minLength);
					values[i] = localValues.clone();
				}
			}

			/* Load the extracted values into rows */
			BlockRealMatrix matrix = new BlockRealMatrix(values);
			/* Transpose the values into columns to get the correct correlations */
			matrix = matrix.transpose();

			/* Needs double[][] */
			PearsonsCorrelation correlations = new PearsonsCorrelation(values);
			correlations.getCorrelationMatrix();

		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseValue == null) ? 0 : baseValue.hashCode());
		result = prime * result + ((endOfReferenceWindow == null) ? 0 : endOfReferenceWindow.hashCode());
		long temp;
		temp = Double.doubleToLongBits(forecastScalar);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(standardDeviationAdjustedValue);
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
		if (Double.doubleToLongBits(standardDeviationAdjustedValue) != Double
				.doubleToLongBits(other.standardDeviationAdjustedValue))
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
		return "Rule [forecastScalar=" + forecastScalar + ", standardDeviationAdjustedValue="
				+ standardDeviationAdjustedValue + ", weight=" + weight + ", variations=" + Arrays.toString(variations)
				+ ", startOfReferenceWindow=" + startOfReferenceWindow + ", endOfReferenceWindow="
				+ endOfReferenceWindow + ", baseValue=" + baseValue + "]";
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
	void setStandardDeviationAdjustedValue(double standardDeviationAdjustedValue) {
		this.standardDeviationAdjustedValue = standardDeviationAdjustedValue;
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
		/* TODO Prüfung, ob die Regel Variationen hat, wenn ja, dann ? */
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
	void setVariations(Rule[] variations) throws IllegalArgumentException {
		if (variations == null)
			return;
		if (variations.length > 3)
			throw new IllegalArgumentException("Each layer must not contain more than 3 rules/variations");
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

}
