package de.rumford.tradingsystem;

import java.io.IOException;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class Treiber {

	public static void main(String[] args) throws IOException {

		double[][] data = { //
				{ 1d, 2d, 3d, 9d, 9d, 8d, 2.5d, 17d, 83d, 1d, -1d }, //
				{ 8d, 3d, 2d, 7.4d, 1d, 9d, 2d, 93d, -3d, 0d, 2d } //
		};

		double[][] moreData = { //
				{ 1d, 8d }, //
				{ 2d, 3d }, //
				{ 3d, 2d }, //
				{ 9d, 7.4d }//
		};
		BlockRealMatrix rm = new BlockRealMatrix(data);
		rm = rm.transpose();

		PearsonsCorrelation corr = new PearsonsCorrelation(rm);
		System.out.println(corr.getCorrelationMatrix());

	}

}
