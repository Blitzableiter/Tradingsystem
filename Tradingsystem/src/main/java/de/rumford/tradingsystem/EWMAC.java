package de.rumford.tradingsystem;

import java.time.LocalDateTime;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.ValueDateTupel;

/**
 * The EWMAC is a non-binary {@link Rule} utilizing the different horizons
 * of its 2 underlying {@link EWMA}. When the shorter horizon EWMA raises
 * above the longer horizon EWMA in value the underlying asset has been
 * rising in the not-so-distant past and is thus expected to rise further,
 * and vice-versa.
 * 
 * @author Max Rumford
 *
 */
public class EWMAC extends Rule {

	/* The longer horizon EWMA. */
	private EWMA longHorizonEwma;
	/* The shorter horizon EWMA. */
	private EWMA shortHorizonEwma;

	/**
	 * A rule based on the relation between two exponentially weighted moving
	 * averages - EWMAs. The EWMAC assumes that an asset is rising in value,
	 * if its short term values are greater than its long term values. If the
	 * short horizon EWMA is greater than the long horizon one, a positive
	 * forecast is given. Else, a negative forecast is produced.
	 * <p>
	 * This non-binary rule allows for a base value scale independent
	 * detailed forecast calculation. The greater the difference between the
	 * two EWMAs the greater in extent the forecast will be.
	 * 
	 * @param baseValue              Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param variations             {@code EWMAC[]} An array of three or
	 *                               less rules. Represents the variations of
	 *                               this rule. Same limitations as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param startOfReferenceWindow Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param endOfReferenceWindow   Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 * @param longHorizon            {@code int} The horizon of the long
	 *                               horizon EWMA. Should be 4* shortHorizon,
	 *                               but can be anything greater than
	 *                               shortHorizon.
	 * @param shortHorizon           {@code int} The horizon of the short
	 *                               horizon EWMA. Must be greater or equal
	 *                               to 2;
	 * @param baseScale              Same as in
	 *                               {@link Rule#Rule(BaseValue, Rule[], LocalDateTime, LocalDateTime, double)}.
	 */
	public EWMAC(BaseValue baseValue, EWMAC[] variations,
			LocalDateTime startOfReferenceWindow,
			LocalDateTime endOfReferenceWindow, int longHorizon,
			int shortHorizon, double baseScale) {
		super(baseValue, variations, startOfReferenceWindow,
				endOfReferenceWindow, baseScale);

		this.validateHorizonValues(longHorizon, shortHorizon);

		if (variations == null) {
			EWMA localLongHorizonEwma = new EWMA(this.getBaseValue().getValues(),
					longHorizon);
			EWMA localShortHorizonEwma = new EWMA(
					this.getBaseValue().getValues(), shortHorizon);
			this.setLongHorizonEwma(localLongHorizonEwma);
			this.setShortHorizonEwma(localShortHorizonEwma);
		}
	}

	/**
	 * Calculates the raw forecast for a given LocalDateTime by subtracting
	 * the long horizon EWMA value from the short horizon EWMA value for this
	 * LocalDateTime.
	 */
	@Override
	double calculateRawForecast(LocalDateTime forecastDateTime) {
		double longHorizonEwmaValue = ValueDateTupel
				.getElement(this.getLongHorizonEwma().getEwmaValues(),
						forecastDateTime)
				.getValue();
		double shortHorizonEwmaValue = ValueDateTupel
				.getElement(this.getShortHorizonEwma().getEwmaValues(),
						forecastDateTime)
				.getValue();

		return shortHorizonEwmaValue - longHorizonEwmaValue;
	}

	/**
	 * Validate the given longHorizon and shortHorizon values.
	 * 
	 * @param longHorizon  {@code int} The given long horizon. Must be >
	 *                     shortHorizon.
	 * @param shortHorizon {@code int} The given short horizon. Must be >= 2.
	 * @throws IllegalArgumentException if the above specifications are not
	 *                                  met.
	 */
	private void validateHorizonValues(int longHorizon, int shortHorizon) {

		/* The horizons are not used when this rule has variations. */
		if (this.getVariations() == null) {
			if (longHorizon <= shortHorizon)
				throw new IllegalArgumentException(
						"The long horizon must be greater than the short horizon");

			if (shortHorizon < 2)
				throw new IllegalArgumentException(
						"The short horizon must not be < 2");
		}
	}

	/**
	 * ======================================================================
	 * OVERRIDES
	 * ======================================================================
	 */
	/**
	 * A hash code for this EWMAC.
	 */
	@GeneratedCode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((longHorizonEwma == null) ? 0 : longHorizonEwma.hashCode());
		result = prime * result
				+ ((shortHorizonEwma == null) ? 0 : shortHorizonEwma.hashCode());
		return result;
	}

	/**
	 * Checks if this EWMAC is equal to another EWMAC.
	 */
	@GeneratedCode
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

	/**
	 * Outputs the fields of this EWMAC as a {@code String}.
	 */
	@GeneratedCode
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EWMAC [longHorizonEwma=");
		builder.append(longHorizonEwma);
		builder.append(", shortHorizonEwma=");
		builder.append(shortHorizonEwma);
		builder.append("]");
		return builder.toString();
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
