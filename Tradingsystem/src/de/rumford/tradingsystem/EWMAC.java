package de.rumford.tradingsystem;

/**
 * 
 * @author Max Rumford
 *
 */
public class EWMAC extends Rule {

	private EWMA longHorizonEwma;
	private EWMA shortHorizonEwma;

	public EWMAC(int longHorizon, int shortHorizon) {
		EWMA longHorizonEwma = new EWMA(longHorizon);
		EWMA shortHorizonEwma = new EWMA(shortHorizon);
		this.setLongHorizonEwma(longHorizonEwma);
		this.setShortHorizonEwma(shortHorizonEwma);
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
	 * Get the long horizon {@code EWMA} for an {@code EWMAC}
	 * 
	 * @return long horizon {@code EWMA} for this {@code EWMAC}
	 */
	public EWMA getLongHorizonEwma() {
		return longHorizonEwma;
	}

	/**
	 * Set the long horizon {@code EWMA} for an {@code EWMAC}
	 * 
	 * @param longHorizonEwma {@link de.rumford.tradingsystem.EWMA} long horizon the
	 *                        be set for this {@code EWMAC}
	 */
	public void setLongHorizonEwma(EWMA longHorizonEwma) {
		this.longHorizonEwma = longHorizonEwma;
	}

	/**
	 * Get the short horizon {@code EWMA} for an {@code EWMAC}
	 * 
	 * @return short horizon {@link de.rumford.tradingsystem.EWMA} for this
	 *         {@code EWMAC}
	 */
	public EWMA getShortHorizonEwma() {
		return shortHorizonEwma;
	}

	/**
	 * Set the short horizon {@code EWMA} for an {@code EWMAC}
	 * 
	 * @param shortHorizonEwma short horizon {@link de.rumford.tradingsystem.EWMA}
	 *                         to be set for this {@code EWMAC}
	 */
	public void setShortHorizonEwma(EWMA shortHorizonEwma) {
		this.shortHorizonEwma = shortHorizonEwma;
	}

}
