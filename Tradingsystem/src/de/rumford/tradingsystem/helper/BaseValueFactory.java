/**
 * 
 */
package de.rumford.tradingsystem.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.commons.lang3.ArrayUtils;

import de.rumford.tradingsystem.BaseValue;

/**
 * de.rumford.tradingsystem.helper
 * 
 * @author Max Rumford
 *
 */
public class BaseValueFactory {

	/* Don't let anyone instantiate this class. */
	private BaseValueFactory() {
	}

	static BaseValue jan1_jan5_val100_500_calc_short(String name) {
		LocalDateTime localDateTimeJan01_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan02_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan03_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan04_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan05_22_00_00 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));

		ValueDateTupel valuedatetupel1 = new ValueDateTupel(localDateTimeJan01_22_00_00, 100d);
		ValueDateTupel valuedatetupel2 = new ValueDateTupel(localDateTimeJan02_22_00_00, 200d);
		ValueDateTupel valuedatetupel3 = new ValueDateTupel(localDateTimeJan03_22_00_00, 300d);
		ValueDateTupel valuedatetupel4 = new ValueDateTupel(localDateTimeJan04_22_00_00, 400d);
		ValueDateTupel valuedatetupel5 = new ValueDateTupel(localDateTimeJan05_22_00_00, 500d);

		ValueDateTupel[] values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);
		values = ArrayUtils.add(values, valuedatetupel5);

		return new BaseValue(name, values);
	}

}
