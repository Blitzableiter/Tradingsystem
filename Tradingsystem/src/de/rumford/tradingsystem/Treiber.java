package de.rumford.tradingsystem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import de.rumford.tradingsystem.helper.ValueDateTupel;

public class Treiber {

	public static void main(String[] args) {
		ValueDateTupel valuedatetupel1 = new ValueDateTupel(
				LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0)), 200d);
		ValueDateTupel valuedatetupel2 = new ValueDateTupel(
				LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0)), 400d);
		ValueDateTupel valuedatetupel3 = new ValueDateTupel(
				LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0)), 500d);
		ValueDateTupel[] values = { valuedatetupel1, valuedatetupel2, valuedatetupel3 };
		BaseValue bv = new BaseValue("value", values);

		ValueDateTupel[] vs = bv.getValues();
		ValueDateTupel[] sv = bv.getShortIndexValues();

		for (ValueDateTupel tup : vs)
			System.out.println(tup);
		for (ValueDateTupel tup : sv)
			System.out.println(tup);

	}

}
