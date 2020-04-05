package de.rumford.tradingsystem;

/**
 * 
 * @author Max Rumford
 *
 */
public class EWMA {

	int horizon;
	private double decay;

	/**
	 * Constructor for the {@code EWMA} class
	 * 
	 * @param horizon {@code int} horizon this EWMA is to be over
	 */
	public EWMA(int horizon) {
		super();
		this.setHorizon(horizon);
		this.setDecay(this.getHorizon());
	}

	/**
	 * Calculate the EWMA-value for given previous value and base value
	 * 
	 * @param previousEWMA {@code double} EWMA of the previous time period
	 * @param baseValue    {@code double} base value of the current time period
	 * @return {@code double} EWMA for the current time period
	 */
	public double calculateEWMA(double previousEWMA, double baseValue) {
		// E_t = A * P_t + [E_t-1 * ( 1 - A ) ]
		double _ewma = this.getDecay() * baseValue + (previousEWMA * (1d - this.getDecay()));
		return _ewma;
	}

	//
	//
	//
	// OVERRIDES
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + horizon;
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
		EWMA other = (EWMA) obj;
		if (horizon != other.horizon)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EWMA [horizon=" + horizon + "]";
	}

	/**
	 * ======================================================================
	 * GETTERS AND SETTERS
	 * ======================================================================
	 */
	/**
	 * Get the decay of an EWMA
	 * 
	 * @return {@code double} decay of the EWMA
	 */
	public double getDecay() {
		return this.decay;
	}

	/**
	 * Set the decay of an EWMA Is only called upon creation of a new instance,
	 * hence private
	 * 
	 * @param horizon {@code int} horizon on which the decay is derived from
	 */
	private void setDecay(int horizon) {
		double _horizon = (double) horizon;
		this.decay = 2d / (_horizon + 1d);
	}

	/**
	 * Get the horizon of an EWMA
	 * 
	 * @return {@code int} horizon of the EWMA
	 */
	public int getHorizon() {
		return horizon;
	}

	/**
	 * Set the horizon of an EWMA
	 * 
	 * @param horizon {@code int} horizon to be set
	 */
	public void setHorizon(int horizon) {
		this.horizon = horizon;
	}

}
