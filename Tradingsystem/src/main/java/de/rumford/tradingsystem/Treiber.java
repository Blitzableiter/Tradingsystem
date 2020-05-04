package de.rumford.tradingsystem;

import java.util.Arrays;

import de.rumford.tradingsystem.helper.GeneratedCode;
import de.rumford.tradingsystem.helper.Util;

public class Treiber {

	@GeneratedCode
	public static void main(String[] args) {

		double[] row1 = { -20, -20, -12.31, -5.34 };
		double[] row2 = { -20, -20, -20, -17.93 };
		double[] row3 = { -9.59, -10.62, -9.8, -9.23 };
		double[] row4 = { 1, 2, 3, 5 };
		double[][] values = { row1, row2, row3, row4 };
		// Excel: 0.857736784518697, 0.688500766307298, 0.656405176216209
		double[] expectedValue = { 0.8577367845186973, 0.6885007663072988, 0.6564051762162094, 0.96621196324353,
				0.87831006565368, 0.506946395591579 };

		System.out.println(Arrays.toString(Util.calculateCorrelationOfRows(values)));

	}

}
