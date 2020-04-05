package de.rumford.tradingsystem;

public class Treiber {

	public static void main(String[] args) {
		double[] weights = { 0.5d, 0.6d };
		final double[][] correlations = { { 1d, 0.75d }, { 0.75d, 1d } };

		try {
			DiversificationMultiplier dm = new DiversificationMultiplier(weights, correlations);
			dm.getValue();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
