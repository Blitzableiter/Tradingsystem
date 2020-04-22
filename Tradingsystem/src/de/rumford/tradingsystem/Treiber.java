package de.rumford.tradingsystem;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import de.rumford.tradingsystem.helper.ValueDateTupel;

public class Treiber {

	public static void main(String[] args) throws IOException {


		ValueDateTupel[] test = ValueDateTupel.createEmptyArray(5);
		System.out.println(test[0]!=null);
		
	}

}
