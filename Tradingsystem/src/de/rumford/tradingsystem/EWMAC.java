package de.rumford.tradingsystem;

import java.time.LocalDateTime;

import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * 
 * @author Max Rumford
 *
 */
public class EWMAC extends Rule {

	private EWMA longHorizonEwma;
	private EWMA shortHorizonEwma;

	public EWMAC(BaseValue baseValue, Rule[] variations, LocalDateTime startOfReferenceWindow,
			LocalDateTime endOfReferenceWindow, int longHorizon, int shortHorizon) {

		super(baseValue, variations, startOfReferenceWindow, endOfReferenceWindow);

		// TODO Validate longHorizon and shortHorizon
		EWMA longHorizonEwma = new EWMA(longHorizon);
		EWMA shortHorizonEwma = new EWMA(shortHorizon);
		this.setLongHorizonEwma(longHorizonEwma);
		this.setShortHorizonEwma(shortHorizonEwma);
	}

	@Override
	double calculateRawForecast() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	ValueDateTupel[] calculateForecasts(LocalDateTime calculateFrom, LocalDateTime calculateTo) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Calculate the raw forecast value
	 * 
	 * @param shortHorizonForecast {@code double} short horizon EWMA forecast value
	 * @param longHorizonForecast  {@code double} long horizon EWMA forecast value
	 * @return {@code double} raw forecast value
	 */
	public double calculateRawForecast(double shortHorizonForecast, double longHorizonForecast) {
		return shortHorizonForecast - longHorizonForecast;
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */

	/**
	 * Get the long horizon {@link EWMA} for an {@link EWMAC}
	 * 
	 * @return long horizon {@link EWMA} for this {@link EWMAC}
	 */
	public EWMA getLongHorizonEwma() {
		return longHorizonEwma;
	}

	/**
	 * Set the long horizon {@link EWMA} for an {@link EWMAC}
	 * 
	 * @param longHorizonEwma {@link EWMA} long horizon the be set for this
	 *                        {@link EWMAC}
	 */
	public void setLongHorizonEwma(EWMA longHorizonEwma) {
		this.longHorizonEwma = longHorizonEwma;
	}

	/**
	 * Get the short horizon {@link EWMA} for an {@link EWMAC}
	 * 
	 * @return short horizon {@link EWMA} for this {@link EWMAC}
	 */
	public EWMA getShortHorizonEwma() {
		return shortHorizonEwma;
	}

	/**
	 * Set the short horizon {@link EWMA} for an {@link EWMAC}
	 * 
	 * @param shortHorizonEwma short horizon {@link EWMA} to be set for this
	 *                         {@link EWMAC}
	 */
	public void setShortHorizonEwma(EWMA shortHorizonEwma) {
		this.shortHorizonEwma = shortHorizonEwma;
	}

}
