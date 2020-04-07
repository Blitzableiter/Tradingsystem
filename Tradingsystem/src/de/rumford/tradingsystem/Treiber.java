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
		ValueDateTupel valuedatetupel4 = new ValueDateTupel(
				LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0)), 400d);
		ValueDateTupel[] values = new ValueDateTupel[0];
		values = ValueDateTupel.appendElement(values, valuedatetupel1);
		values = ValueDateTupel.appendElement(values, valuedatetupel2);
		values = ValueDateTupel.appendElement(values, valuedatetupel3);
		values = ValueDateTupel.appendElement(values, valuedatetupel4);

		for (ValueDateTupel tup : values)
			System.out.println(tup);

		BaseValue bv = new BaseValue("value", values);

		ValueDateTupel[] vs = bv.getValues();
		ValueDateTupel[] sv = bv.getShortIndexValues();

		for (ValueDateTupel tup : vs)
			System.out.println("long: " + tup);
		for (ValueDateTupel tup : sv)
			System.out.println("short: " + tup);

	}

}
