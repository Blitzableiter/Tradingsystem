/**
 * 
 */
package de.rumford.tradingsystem;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * @author Max Rumford
 *
 */
public abstract class Rule {

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

	abstract double calculateRawForecast();

	abstract ValueDateTupel[] calculateForecasts(LocalDateTime startOfReferenceWindow,
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

			double[][] values = {};
			values = ArrayUtils.add(values, ValueDateTupel.getValues(forecasts1));
			values = ArrayUtils.add(values, ValueDateTupel.getValues(forecasts2));
			values = ArrayUtils.add(values, ValueDateTupel.getValues(forecasts3));

			/* Needs double[][] */
			PearsonsCorrelation correlations = new PearsonsCorrelation(values);
			correlations.getCorrelationMatrix();

		}
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
	private BaseValue getBaseValue() {
		return baseValue;
	}

	/**
	 * @param baseValue the baseValue to set
	 */
	private void setBaseValue(BaseValue baseValue) {
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
	public void setWeight(double weight) {
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
	private void setVariations(Rule[] variations) throws IllegalArgumentException {
		if (variations == null)
			return;
		if (variations.length > 3)
			throw new IllegalArgumentException("Each layer must not contain more than 3 rules/variations");
		this.variations = variations;
	}

	/**
	 * @return startOfReferenceWindow Rule
	 */
	private LocalDateTime getStartOfReferenceWindow() {
		return startOfReferenceWindow;
	}

	/**
	 * @param startOfReferenceWindow the startOfReferenceWindow to set
	 */
	private void setStartOfReferenceWindow(LocalDateTime startOfReferenceWindow) {
		this.startOfReferenceWindow = startOfReferenceWindow;
	}

	/**
	 * @return endOfReferenceWindow Rule
	 */
	private LocalDateTime getEndOfReferenceWindow() {
		return endOfReferenceWindow;
	}

	/**
	 * @param endOfReferenceWindow the endOfReferenceWindow to set
	 */
	private void setEndOfReferenceWindow(LocalDateTime endOfReferenceWindow) {
		this.endOfReferenceWindow = endOfReferenceWindow;
	}

}
