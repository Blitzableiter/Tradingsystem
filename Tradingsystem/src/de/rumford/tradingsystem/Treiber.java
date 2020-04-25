package de.rumford.tradingsystem;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Treiber {

	public static void main(String[] args) throws IOException {

		EWMAC e1 = new EWMAC(BaseValue.jan1_jan5_22_00_00_val100_500_calc_short("test"), null,
				LocalDateTime.of(2020, 1, 1, 22, 0), LocalDateTime.of(2020, 1, 5, 22, 0), 4, 2, 10);
		System.out.println(Arrays.toString(e1.getForecasts()));
		System.out.println(e1.toString());
	}

}
