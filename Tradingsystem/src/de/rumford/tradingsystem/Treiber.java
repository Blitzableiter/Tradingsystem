package de.rumford.tradingsystem;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class Treiber {

	public static void main(String[] args) {

		double[] values = { 1d, 2d, 3d };
		StandardDeviation sd = new StandardDeviation();

		System.out.println(sd.evaluate(values));

	}

}
