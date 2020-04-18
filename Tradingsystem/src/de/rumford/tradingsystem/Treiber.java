package de.rumford.tradingsystem;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class Treiber {

	public static void main(String[] args) throws IOException {

		double[] values1 = { 1, 2, 3, 4 };
		double[] values2 = { 1, 4, 5, 5 };
		double[] values3 = { 2, 3, 2, 4 };

		double[][] values = {};
		values = ArrayUtils.add(values, values1);
		values = ArrayUtils.add(values, values2);
		values = ArrayUtils.add(values, values3);

		/* Load the extracted values into rows */
		RealMatrix matrix = new BlockRealMatrix(values);
		/* Transpose the values into columns to get the correct correlations */
		matrix = matrix.transpose();

		/* Get the correlations of the passed value arrays */
		PearsonsCorrelation correlations = new PearsonsCorrelation(matrix);
		RealMatrix correlationMatrix = correlations.getCorrelationMatrix();

		System.out.println(correlationMatrix);
		System.out.println("corr 0/1: " + correlationMatrix.getEntry(0, 1));
		System.out.println("corr 1/2: " + correlationMatrix.getEntry(1, 2));
		System.out.println("corr 0/2: " + correlationMatrix.getEntry(0, 2));

		// corr 0/1 = BlockRealMatrix[0][1]
		// corr 1/2 = BlockRealMatrix[1][2]
		// corr 0/2 = BlockRealMatrix[2][0]

	}

}
