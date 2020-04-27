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

	/**
	 * A rule based on the relation between two exponentially weighted moving
	 * averages - EWMAs. The EWMAC assumes that an asset is rising in value, if its
	 * short term values are greater than its long term values. If the short horizon
	 * EWMA is greater than the long horizon one, a positive forecast is given.
	 * Else, a negative forecast is produced.
	 * <p>
	 * This non-binary rule allows for a base value scale independent detailed
	 * forecast calculation. The greater the difference between the two EWMAs the
	 * greater in extent the forecast will be.
	 * 
	 * @param baseValue              {@link BaseValue} The base value used for the
	 *                               calculation.
	 * @param variations             {@code EWMAC[]} An array of three or less
	 *                               rules. Represents the variations of this rule.
	 * @param startOfReferenceWindow {@code LocalDateTime} The first LocalDateTime
	 *                               to be used in the forecast scalar and forecast
	 *                               values calculations.
	 * @param endOfReferenceWindow   {@code LocalDateTime} The last LocalDateTime to
	 *                               be used in the forecast scalar.
	 * @param longHorizon            {@code int} The horizon of the long horizon
	 *                               EWMA. Should be 4* shortHorizon, but can be
	 *                               anything > shortHorizon.
	 * @param shortHorizon           {@code int} The horizon of the short horizon
	 *                               EWMA. Must be >= 2;
	 * @param baseScale              {@code double} The base scale used for the
	 *                               calculation of the forecast scalar and thus in
	 *                               scaled forecast calculations as well.
	 */
	public EWMAC(BaseValue baseValue, EWMAC[] variations, LocalDateTime startOfReferenceWindow,
			LocalDateTime endOfReferenceWindow, int longHorizon, int shortHorizon, double baseScale) {
		super(baseValue, variations, startOfReferenceWindow, endOfReferenceWindow, baseScale);

		this.validateHorizonValues(longHorizon, shortHorizon);

		EWMA localLongHorizonEwma = new EWMA(this.getBaseValue().getValues(), longHorizon);
		EWMA localShortHorizonEwma = new EWMA(this.getBaseValue().getValues(), shortHorizon);
		this.setLongHorizonEwma(localLongHorizonEwma);
		this.setShortHorizonEwma(localShortHorizonEwma);
	}

	/**
	 * Validate the given longHorizon and shortHorizon values.
	 * 
	 * @param longHorizon  {@code int} The given long horizon.
	 * @param shortHorizon {@code int} The given short horizon.
	 * @throws IllegalArgumentException if the long horizon is smaller than the
	 *                                  short horizon.
	 * @throws IllegalArgumentException if the short horizon is smaller than 0.
	 */
	private void validateHorizonValues(int longHorizon, int shortHorizon) {
		if (longHorizon <= shortHorizon)
			throw new IllegalArgumentException("The long horizon must be greater than the short horizon");

		if (shortHorizon < 2)
			throw new IllegalArgumentException("The short horizon must not be < 2");
	}

	/**
	 * Calculates the raw forecast for a given LocalDateTime by subtracting the long
	 * horizon EWMA value from the short horizon EWMA value for this LocalDateTime.
	 */
	@Override
	double calculateRawForecast(LocalDateTime forecastDateTime) {
		double longHorizonEwmaValue = ValueDateTupel
				.getElement(this.getLongHorizonEwma().getEwmaValues(), forecastDateTime).getValue();
		double shortHorizonEwmaValue = ValueDateTupel
				.getElement(this.getShortHorizonEwma().getEwmaValues(), forecastDateTime).getValue();

		return shortHorizonEwmaValue - longHorizonEwmaValue;
	}

	@JaCoCoIgnore
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((longHorizonEwma == null) ? 0 : longHorizonEwma.hashCode());
		result = prime * result + ((shortHorizonEwma == null) ? 0 : shortHorizonEwma.hashCode());
		return result;
	}

	@JaCoCoIgnore
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EWMAC other = (EWMAC) obj;
		if (longHorizonEwma == null) {
			if (other.longHorizonEwma != null)
				return false;
		} else if (!longHorizonEwma.equals(other.longHorizonEwma))
			return false;
		if (shortHorizonEwma == null) {
			if (other.shortHorizonEwma != null)
				return false;
		} else if (!shortHorizonEwma.equals(other.shortHorizonEwma))
			return false;
		return true;
	}

	@JaCoCoIgnore
	@Override
	public String toString() {
		return "EWMAC [longHorizonEwma=" + longHorizonEwma + ", shortHorizonEwma=" + shortHorizonEwma + ", toString()="
				+ super.toString() + "]";
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
	private void setLongHorizonEwma(EWMA longHorizonEwma) {
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
	private void setShortHorizonEwma(EWMA shortHorizonEwma) {
		this.shortHorizonEwma = shortHorizonEwma;
	}

}
