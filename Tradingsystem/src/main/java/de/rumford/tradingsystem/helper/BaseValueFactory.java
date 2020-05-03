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

	public static final BaseValue jan1Jan4calcShort(String name) {
		LocalDateTime localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));

		ValueDateTupel valuedatetupel1 = new ValueDateTupel(localDateTimeJan01220000, 200d);
		ValueDateTupel valuedatetupel2 = new ValueDateTupel(localDateTimeJan02220000, 400d);
		ValueDateTupel valuedatetupel3 = new ValueDateTupel(localDateTimeJan03220000, 500d);
		ValueDateTupel valuedatetupel4 = new ValueDateTupel(localDateTimeJan04220000, 200d);

		ValueDateTupel[] values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);

		return new BaseValue(name, values);
	}

	public static final BaseValue jan1Jan7lowValscalcShort(String name) {
		LocalDateTime localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan06220000 = LocalDateTime.of(LocalDate.of(2020, 1, 6), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan07220000 = LocalDateTime.of(LocalDate.of(2020, 1, 7), LocalTime.of(22, 0));

		ValueDateTupel valuedatetupel1 = new ValueDateTupel(localDateTimeJan01220000, 1d);
		ValueDateTupel valuedatetupel2 = new ValueDateTupel(localDateTimeJan02220000, 1.1d);
		ValueDateTupel valuedatetupel3 = new ValueDateTupel(localDateTimeJan03220000, 1.2d);
		ValueDateTupel valuedatetupel4 = new ValueDateTupel(localDateTimeJan04220000, 1.3d);
		ValueDateTupel valuedatetupel5 = new ValueDateTupel(localDateTimeJan05220000, 0.01d);
		ValueDateTupel valuedatetupel6 = new ValueDateTupel(localDateTimeJan06220000, 0.0001d);
		ValueDateTupel valuedatetupel7 = new ValueDateTupel(localDateTimeJan07220000, 0.000001d);

		ValueDateTupel[] values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);
		values = ArrayUtils.add(values, valuedatetupel5);
		values = ArrayUtils.add(values, valuedatetupel6);
		values = ArrayUtils.add(values, valuedatetupel7);

		return new BaseValue(name, values);
	}

	public static final BaseValue jan1Jan5calcShort(String name) {
		LocalDateTime localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));

		ValueDateTupel valuedatetupel1 = new ValueDateTupel(localDateTimeJan01220000, 100d);
		ValueDateTupel valuedatetupel2 = new ValueDateTupel(localDateTimeJan02220000, 200d);
		ValueDateTupel valuedatetupel3 = new ValueDateTupel(localDateTimeJan03220000, 300d);
		ValueDateTupel valuedatetupel4 = new ValueDateTupel(localDateTimeJan04220000, 400d);
		ValueDateTupel valuedatetupel5 = new ValueDateTupel(localDateTimeJan05220000, 500d);

		ValueDateTupel[] values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);
		values = ArrayUtils.add(values, valuedatetupel5);

		return new BaseValue(name, values);
	}

	public static final BaseValue jan1Jan31calcShort(String name) {
		LocalDateTime localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan06220000 = LocalDateTime.of(LocalDate.of(2020, 1, 6), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan07220000 = LocalDateTime.of(LocalDate.of(2020, 1, 7), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan08220000 = LocalDateTime.of(LocalDate.of(2020, 1, 8), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan09220000 = LocalDateTime.of(LocalDate.of(2020, 1, 9), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan10220000 = LocalDateTime.of(LocalDate.of(2020, 1, 10), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan11220000 = LocalDateTime.of(LocalDate.of(2020, 1, 11), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan12220000 = LocalDateTime.of(LocalDate.of(2020, 1, 12), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan13220000 = LocalDateTime.of(LocalDate.of(2020, 1, 13), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan14220000 = LocalDateTime.of(LocalDate.of(2020, 1, 14), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan15220000 = LocalDateTime.of(LocalDate.of(2020, 1, 15), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan16220000 = LocalDateTime.of(LocalDate.of(2020, 1, 16), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan17220000 = LocalDateTime.of(LocalDate.of(2020, 1, 17), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan18220000 = LocalDateTime.of(LocalDate.of(2020, 1, 18), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan19220000 = LocalDateTime.of(LocalDate.of(2020, 1, 19), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan20220000 = LocalDateTime.of(LocalDate.of(2020, 1, 20), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan21220000 = LocalDateTime.of(LocalDate.of(2020, 1, 21), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan22220000 = LocalDateTime.of(LocalDate.of(2020, 1, 22), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan23220000 = LocalDateTime.of(LocalDate.of(2020, 1, 23), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan24220000 = LocalDateTime.of(LocalDate.of(2020, 1, 24), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan25220000 = LocalDateTime.of(LocalDate.of(2020, 1, 25), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan26220000 = LocalDateTime.of(LocalDate.of(2020, 1, 26), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan27220000 = LocalDateTime.of(LocalDate.of(2020, 1, 27), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan28220000 = LocalDateTime.of(LocalDate.of(2020, 1, 28), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan29220000 = LocalDateTime.of(LocalDate.of(2020, 1, 29), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan30220000 = LocalDateTime.of(LocalDate.of(2020, 1, 30), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan31220000 = LocalDateTime.of(LocalDate.of(2020, 1, 31), LocalTime.of(22, 0));

		ValueDateTupel valuedatetupel1 = new ValueDateTupel(localDateTimeJan01220000, 200d);
		ValueDateTupel valuedatetupel2 = new ValueDateTupel(localDateTimeJan02220000, 400d);
		ValueDateTupel valuedatetupel3 = new ValueDateTupel(localDateTimeJan03220000, 500d);
		ValueDateTupel valuedatetupel4 = new ValueDateTupel(localDateTimeJan04220000, 200d);
		ValueDateTupel valuedatetupel5 = new ValueDateTupel(localDateTimeJan05220000, 100d);
		ValueDateTupel valuedatetupel6 = new ValueDateTupel(localDateTimeJan06220000, 150d);
		ValueDateTupel valuedatetupel7 = new ValueDateTupel(localDateTimeJan07220000, 200d);
		ValueDateTupel valuedatetupel8 = new ValueDateTupel(localDateTimeJan08220000, 250d);
		ValueDateTupel valuedatetupel9 = new ValueDateTupel(localDateTimeJan09220000, 300d);
		ValueDateTupel valuedatetupel10 = new ValueDateTupel(localDateTimeJan10220000, 200d);
		ValueDateTupel valuedatetupel11 = new ValueDateTupel(localDateTimeJan11220000, 400d);
		ValueDateTupel valuedatetupel12 = new ValueDateTupel(localDateTimeJan12220000, 300d);
		ValueDateTupel valuedatetupel13 = new ValueDateTupel(localDateTimeJan13220000, 200d);
		ValueDateTupel valuedatetupel14 = new ValueDateTupel(localDateTimeJan14220000, 250d);
		ValueDateTupel valuedatetupel15 = new ValueDateTupel(localDateTimeJan15220000, 300d);
		ValueDateTupel valuedatetupel16 = new ValueDateTupel(localDateTimeJan16220000, 400d);
		ValueDateTupel valuedatetupel17 = new ValueDateTupel(localDateTimeJan17220000, 500d);
		ValueDateTupel valuedatetupel18 = new ValueDateTupel(localDateTimeJan18220000, 900d);
		ValueDateTupel valuedatetupel19 = new ValueDateTupel(localDateTimeJan19220000, 500d);
		ValueDateTupel valuedatetupel20 = new ValueDateTupel(localDateTimeJan20220000, 450d);
		ValueDateTupel valuedatetupel21 = new ValueDateTupel(localDateTimeJan21220000, 400d);
		ValueDateTupel valuedatetupel22 = new ValueDateTupel(localDateTimeJan22220000, 450d);
		ValueDateTupel valuedatetupel23 = new ValueDateTupel(localDateTimeJan23220000, 400d);
		ValueDateTupel valuedatetupel24 = new ValueDateTupel(localDateTimeJan24220000, 350d);
		ValueDateTupel valuedatetupel25 = new ValueDateTupel(localDateTimeJan25220000, 300d);
		ValueDateTupel valuedatetupel26 = new ValueDateTupel(localDateTimeJan26220000, 200d);
		ValueDateTupel valuedatetupel27 = new ValueDateTupel(localDateTimeJan27220000, 150d);
		ValueDateTupel valuedatetupel28 = new ValueDateTupel(localDateTimeJan28220000, 175d);
		ValueDateTupel valuedatetupel29 = new ValueDateTupel(localDateTimeJan29220000, 175d);
		ValueDateTupel valuedatetupel30 = new ValueDateTupel(localDateTimeJan30220000, 200d);
		ValueDateTupel valuedatetupel31 = new ValueDateTupel(localDateTimeJan31220000, 180d);

		ValueDateTupel[] values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);
		values = ArrayUtils.add(values, valuedatetupel5);
		values = ArrayUtils.add(values, valuedatetupel6);
		values = ArrayUtils.add(values, valuedatetupel7);
		values = ArrayUtils.add(values, valuedatetupel8);
		values = ArrayUtils.add(values, valuedatetupel9);
		values = ArrayUtils.add(values, valuedatetupel10);
		values = ArrayUtils.add(values, valuedatetupel11);
		values = ArrayUtils.add(values, valuedatetupel12);
		values = ArrayUtils.add(values, valuedatetupel13);
		values = ArrayUtils.add(values, valuedatetupel14);
		values = ArrayUtils.add(values, valuedatetupel15);
		values = ArrayUtils.add(values, valuedatetupel16);
		values = ArrayUtils.add(values, valuedatetupel17);
		values = ArrayUtils.add(values, valuedatetupel18);
		values = ArrayUtils.add(values, valuedatetupel19);
		values = ArrayUtils.add(values, valuedatetupel20);
		values = ArrayUtils.add(values, valuedatetupel21);
		values = ArrayUtils.add(values, valuedatetupel22);
		values = ArrayUtils.add(values, valuedatetupel23);
		values = ArrayUtils.add(values, valuedatetupel24);
		values = ArrayUtils.add(values, valuedatetupel25);
		values = ArrayUtils.add(values, valuedatetupel26);
		values = ArrayUtils.add(values, valuedatetupel27);
		values = ArrayUtils.add(values, valuedatetupel28);
		values = ArrayUtils.add(values, valuedatetupel29);
		values = ArrayUtils.add(values, valuedatetupel30);
		values = ArrayUtils.add(values, valuedatetupel31);

		return new BaseValue(name, values);
	}

	public static final BaseValue jan1Jan31allVal0calcShort(String name) {
		LocalDateTime localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan06220000 = LocalDateTime.of(LocalDate.of(2020, 1, 6), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan07220000 = LocalDateTime.of(LocalDate.of(2020, 1, 7), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan08220000 = LocalDateTime.of(LocalDate.of(2020, 1, 8), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan09220000 = LocalDateTime.of(LocalDate.of(2020, 1, 9), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan10220000 = LocalDateTime.of(LocalDate.of(2020, 1, 10), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan11220000 = LocalDateTime.of(LocalDate.of(2020, 1, 11), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan12220000 = LocalDateTime.of(LocalDate.of(2020, 1, 12), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan13220000 = LocalDateTime.of(LocalDate.of(2020, 1, 13), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan14220000 = LocalDateTime.of(LocalDate.of(2020, 1, 14), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan15220000 = LocalDateTime.of(LocalDate.of(2020, 1, 15), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan16220000 = LocalDateTime.of(LocalDate.of(2020, 1, 16), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan17220000 = LocalDateTime.of(LocalDate.of(2020, 1, 17), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan18220000 = LocalDateTime.of(LocalDate.of(2020, 1, 18), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan19220000 = LocalDateTime.of(LocalDate.of(2020, 1, 19), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan20220000 = LocalDateTime.of(LocalDate.of(2020, 1, 20), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan21220000 = LocalDateTime.of(LocalDate.of(2020, 1, 21), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan22220000 = LocalDateTime.of(LocalDate.of(2020, 1, 22), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan23220000 = LocalDateTime.of(LocalDate.of(2020, 1, 23), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan24220000 = LocalDateTime.of(LocalDate.of(2020, 1, 24), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan25220000 = LocalDateTime.of(LocalDate.of(2020, 1, 25), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan26220000 = LocalDateTime.of(LocalDate.of(2020, 1, 26), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan27220000 = LocalDateTime.of(LocalDate.of(2020, 1, 27), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan28220000 = LocalDateTime.of(LocalDate.of(2020, 1, 28), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan29220000 = LocalDateTime.of(LocalDate.of(2020, 1, 29), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan30220000 = LocalDateTime.of(LocalDate.of(2020, 1, 30), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan31220000 = LocalDateTime.of(LocalDate.of(2020, 1, 31), LocalTime.of(22, 0));

		ValueDateTupel valuedatetupel1 = new ValueDateTupel(localDateTimeJan01220000, 0d);
		ValueDateTupel valuedatetupel2 = new ValueDateTupel(localDateTimeJan02220000, 0d);
		ValueDateTupel valuedatetupel3 = new ValueDateTupel(localDateTimeJan03220000, 0d);
		ValueDateTupel valuedatetupel4 = new ValueDateTupel(localDateTimeJan04220000, 0d);
		ValueDateTupel valuedatetupel5 = new ValueDateTupel(localDateTimeJan05220000, 0d);
		ValueDateTupel valuedatetupel6 = new ValueDateTupel(localDateTimeJan06220000, 0d);
		ValueDateTupel valuedatetupel7 = new ValueDateTupel(localDateTimeJan07220000, 0d);
		ValueDateTupel valuedatetupel8 = new ValueDateTupel(localDateTimeJan08220000, 0d);
		ValueDateTupel valuedatetupel9 = new ValueDateTupel(localDateTimeJan09220000, 0d);
		ValueDateTupel valuedatetupel10 = new ValueDateTupel(localDateTimeJan10220000, 0d);
		ValueDateTupel valuedatetupel11 = new ValueDateTupel(localDateTimeJan11220000, 0d);
		ValueDateTupel valuedatetupel12 = new ValueDateTupel(localDateTimeJan12220000, 0d);
		ValueDateTupel valuedatetupel13 = new ValueDateTupel(localDateTimeJan13220000, 0d);
		ValueDateTupel valuedatetupel14 = new ValueDateTupel(localDateTimeJan14220000, 0d);
		ValueDateTupel valuedatetupel15 = new ValueDateTupel(localDateTimeJan15220000, 0d);
		ValueDateTupel valuedatetupel16 = new ValueDateTupel(localDateTimeJan16220000, 0d);
		ValueDateTupel valuedatetupel17 = new ValueDateTupel(localDateTimeJan17220000, 0d);
		ValueDateTupel valuedatetupel18 = new ValueDateTupel(localDateTimeJan18220000, 0d);
		ValueDateTupel valuedatetupel19 = new ValueDateTupel(localDateTimeJan19220000, 0d);
		ValueDateTupel valuedatetupel20 = new ValueDateTupel(localDateTimeJan20220000, 0d);
		ValueDateTupel valuedatetupel21 = new ValueDateTupel(localDateTimeJan21220000, 0d);
		ValueDateTupel valuedatetupel22 = new ValueDateTupel(localDateTimeJan22220000, 0d);
		ValueDateTupel valuedatetupel23 = new ValueDateTupel(localDateTimeJan23220000, 0d);
		ValueDateTupel valuedatetupel24 = new ValueDateTupel(localDateTimeJan24220000, 0d);
		ValueDateTupel valuedatetupel25 = new ValueDateTupel(localDateTimeJan25220000, 0d);
		ValueDateTupel valuedatetupel26 = new ValueDateTupel(localDateTimeJan26220000, 0d);
		ValueDateTupel valuedatetupel27 = new ValueDateTupel(localDateTimeJan27220000, 0d);
		ValueDateTupel valuedatetupel28 = new ValueDateTupel(localDateTimeJan28220000, 0d);
		ValueDateTupel valuedatetupel29 = new ValueDateTupel(localDateTimeJan29220000, 0d);
		ValueDateTupel valuedatetupel30 = new ValueDateTupel(localDateTimeJan30220000, 0d);
		ValueDateTupel valuedatetupel31 = new ValueDateTupel(localDateTimeJan31220000, 0d);

		ValueDateTupel[] values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);
		values = ArrayUtils.add(values, valuedatetupel5);
		values = ArrayUtils.add(values, valuedatetupel6);
		values = ArrayUtils.add(values, valuedatetupel7);
		values = ArrayUtils.add(values, valuedatetupel8);
		values = ArrayUtils.add(values, valuedatetupel9);
		values = ArrayUtils.add(values, valuedatetupel10);
		values = ArrayUtils.add(values, valuedatetupel11);
		values = ArrayUtils.add(values, valuedatetupel12);
		values = ArrayUtils.add(values, valuedatetupel13);
		values = ArrayUtils.add(values, valuedatetupel14);
		values = ArrayUtils.add(values, valuedatetupel15);
		values = ArrayUtils.add(values, valuedatetupel16);
		values = ArrayUtils.add(values, valuedatetupel17);
		values = ArrayUtils.add(values, valuedatetupel18);
		values = ArrayUtils.add(values, valuedatetupel19);
		values = ArrayUtils.add(values, valuedatetupel20);
		values = ArrayUtils.add(values, valuedatetupel21);
		values = ArrayUtils.add(values, valuedatetupel22);
		values = ArrayUtils.add(values, valuedatetupel23);
		values = ArrayUtils.add(values, valuedatetupel24);
		values = ArrayUtils.add(values, valuedatetupel25);
		values = ArrayUtils.add(values, valuedatetupel26);
		values = ArrayUtils.add(values, valuedatetupel27);
		values = ArrayUtils.add(values, valuedatetupel28);
		values = ArrayUtils.add(values, valuedatetupel29);
		values = ArrayUtils.add(values, valuedatetupel30);
		values = ArrayUtils.add(values, valuedatetupel31);

		return new BaseValue(name, values);
	}

	public static final BaseValue jan1Feb05calcShort(String name) {
		LocalDateTime localDateTimeJan01220000 = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan02220000 = LocalDateTime.of(LocalDate.of(2020, 1, 2), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan03220000 = LocalDateTime.of(LocalDate.of(2020, 1, 3), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan04220000 = LocalDateTime.of(LocalDate.of(2020, 1, 4), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan05220000 = LocalDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan06220000 = LocalDateTime.of(LocalDate.of(2020, 1, 6), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan07220000 = LocalDateTime.of(LocalDate.of(2020, 1, 7), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan08220000 = LocalDateTime.of(LocalDate.of(2020, 1, 8), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan09220000 = LocalDateTime.of(LocalDate.of(2020, 1, 9), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan10220000 = LocalDateTime.of(LocalDate.of(2020, 1, 10), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan11220000 = LocalDateTime.of(LocalDate.of(2020, 1, 11), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan12220000 = LocalDateTime.of(LocalDate.of(2020, 1, 12), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan13220000 = LocalDateTime.of(LocalDate.of(2020, 1, 13), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan14220000 = LocalDateTime.of(LocalDate.of(2020, 1, 14), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan15220000 = LocalDateTime.of(LocalDate.of(2020, 1, 15), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan16220000 = LocalDateTime.of(LocalDate.of(2020, 1, 16), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan17220000 = LocalDateTime.of(LocalDate.of(2020, 1, 17), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan18220000 = LocalDateTime.of(LocalDate.of(2020, 1, 18), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan19220000 = LocalDateTime.of(LocalDate.of(2020, 1, 19), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan20220000 = LocalDateTime.of(LocalDate.of(2020, 1, 20), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan21220000 = LocalDateTime.of(LocalDate.of(2020, 1, 21), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan22220000 = LocalDateTime.of(LocalDate.of(2020, 1, 22), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan23220000 = LocalDateTime.of(LocalDate.of(2020, 1, 23), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan24220000 = LocalDateTime.of(LocalDate.of(2020, 1, 24), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan25220000 = LocalDateTime.of(LocalDate.of(2020, 1, 25), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan26220000 = LocalDateTime.of(LocalDate.of(2020, 1, 26), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan27220000 = LocalDateTime.of(LocalDate.of(2020, 1, 27), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan28220000 = LocalDateTime.of(LocalDate.of(2020, 1, 28), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan29220000 = LocalDateTime.of(LocalDate.of(2020, 1, 29), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan30220000 = LocalDateTime.of(LocalDate.of(2020, 1, 30), LocalTime.of(22, 0));
		LocalDateTime localDateTimeJan31220000 = LocalDateTime.of(LocalDate.of(2020, 1, 31), LocalTime.of(22, 0));
		LocalDateTime localDateTimeFeb01220000 = LocalDateTime.of(LocalDate.of(2020, 2, 1), LocalTime.of(22, 0));
		LocalDateTime localDateTimeFeb02220000 = LocalDateTime.of(LocalDate.of(2020, 2, 2), LocalTime.of(22, 0));
		LocalDateTime localDateTimeFeb03220000 = LocalDateTime.of(LocalDate.of(2020, 2, 3), LocalTime.of(22, 0));
		LocalDateTime localDateTimeFeb04220000 = LocalDateTime.of(LocalDate.of(2020, 2, 4), LocalTime.of(22, 0));
		LocalDateTime localDateTimeFeb05220000 = LocalDateTime.of(LocalDate.of(2020, 2, 5), LocalTime.of(22, 0));

		ValueDateTupel valuedatetupel1 = new ValueDateTupel(localDateTimeJan01220000, 200d);
		ValueDateTupel valuedatetupel2 = new ValueDateTupel(localDateTimeJan02220000, 400d);
		ValueDateTupel valuedatetupel3 = new ValueDateTupel(localDateTimeJan03220000, 500d);
		ValueDateTupel valuedatetupel4 = new ValueDateTupel(localDateTimeJan04220000, 200d);
		ValueDateTupel valuedatetupel5 = new ValueDateTupel(localDateTimeJan05220000, 100d);
		ValueDateTupel valuedatetupel6 = new ValueDateTupel(localDateTimeJan06220000, 150d);
		ValueDateTupel valuedatetupel7 = new ValueDateTupel(localDateTimeJan07220000, 200d);
		ValueDateTupel valuedatetupel8 = new ValueDateTupel(localDateTimeJan08220000, 250d);
		ValueDateTupel valuedatetupel9 = new ValueDateTupel(localDateTimeJan09220000, 300d);
		ValueDateTupel valuedatetupel10 = new ValueDateTupel(localDateTimeJan10220000, 200d);
		ValueDateTupel valuedatetupel11 = new ValueDateTupel(localDateTimeJan11220000, 400d);
		ValueDateTupel valuedatetupel12 = new ValueDateTupel(localDateTimeJan12220000, 300d);
		ValueDateTupel valuedatetupel13 = new ValueDateTupel(localDateTimeJan13220000, 200d);
		ValueDateTupel valuedatetupel14 = new ValueDateTupel(localDateTimeJan14220000, 250d);
		ValueDateTupel valuedatetupel15 = new ValueDateTupel(localDateTimeJan15220000, 300d);
		ValueDateTupel valuedatetupel16 = new ValueDateTupel(localDateTimeJan16220000, 400d);
		ValueDateTupel valuedatetupel17 = new ValueDateTupel(localDateTimeJan17220000, 500d);
		ValueDateTupel valuedatetupel18 = new ValueDateTupel(localDateTimeJan18220000, 900d);
		ValueDateTupel valuedatetupel19 = new ValueDateTupel(localDateTimeJan19220000, 500d);
		ValueDateTupel valuedatetupel20 = new ValueDateTupel(localDateTimeJan20220000, 450d);
		ValueDateTupel valuedatetupel21 = new ValueDateTupel(localDateTimeJan21220000, 400d);
		ValueDateTupel valuedatetupel22 = new ValueDateTupel(localDateTimeJan22220000, 450d);
		ValueDateTupel valuedatetupel23 = new ValueDateTupel(localDateTimeJan23220000, 400d);
		ValueDateTupel valuedatetupel24 = new ValueDateTupel(localDateTimeJan24220000, 350d);
		ValueDateTupel valuedatetupel25 = new ValueDateTupel(localDateTimeJan25220000, 300d);
		ValueDateTupel valuedatetupel26 = new ValueDateTupel(localDateTimeJan26220000, 200d);
		ValueDateTupel valuedatetupel27 = new ValueDateTupel(localDateTimeJan27220000, 150d);
		ValueDateTupel valuedatetupel28 = new ValueDateTupel(localDateTimeJan28220000, 175d);
		ValueDateTupel valuedatetupel29 = new ValueDateTupel(localDateTimeJan29220000, 175d);
		ValueDateTupel valuedatetupel30 = new ValueDateTupel(localDateTimeJan30220000, 200d);
		ValueDateTupel valuedatetupel31 = new ValueDateTupel(localDateTimeJan31220000, 180d);
		ValueDateTupel valuedatetupel32 = new ValueDateTupel(localDateTimeFeb01220000, 150d);
		ValueDateTupel valuedatetupel33 = new ValueDateTupel(localDateTimeFeb02220000, 160d);
		ValueDateTupel valuedatetupel34 = new ValueDateTupel(localDateTimeFeb03220000, 180d);
		ValueDateTupel valuedatetupel35 = new ValueDateTupel(localDateTimeFeb04220000, 190d);
		ValueDateTupel valuedatetupel36 = new ValueDateTupel(localDateTimeFeb05220000, 1d);

		ValueDateTupel[] values = ValueDateTupel.createEmptyArray();
		values = ArrayUtils.add(values, valuedatetupel1);
		values = ArrayUtils.add(values, valuedatetupel2);
		values = ArrayUtils.add(values, valuedatetupel3);
		values = ArrayUtils.add(values, valuedatetupel4);
		values = ArrayUtils.add(values, valuedatetupel5);
		values = ArrayUtils.add(values, valuedatetupel6);
		values = ArrayUtils.add(values, valuedatetupel7);
		values = ArrayUtils.add(values, valuedatetupel8);
		values = ArrayUtils.add(values, valuedatetupel9);
		values = ArrayUtils.add(values, valuedatetupel10);
		values = ArrayUtils.add(values, valuedatetupel11);
		values = ArrayUtils.add(values, valuedatetupel12);
		values = ArrayUtils.add(values, valuedatetupel13);
		values = ArrayUtils.add(values, valuedatetupel14);
		values = ArrayUtils.add(values, valuedatetupel15);
		values = ArrayUtils.add(values, valuedatetupel16);
		values = ArrayUtils.add(values, valuedatetupel17);
		values = ArrayUtils.add(values, valuedatetupel18);
		values = ArrayUtils.add(values, valuedatetupel19);
		values = ArrayUtils.add(values, valuedatetupel20);
		values = ArrayUtils.add(values, valuedatetupel21);
		values = ArrayUtils.add(values, valuedatetupel22);
		values = ArrayUtils.add(values, valuedatetupel23);
		values = ArrayUtils.add(values, valuedatetupel24);
		values = ArrayUtils.add(values, valuedatetupel25);
		values = ArrayUtils.add(values, valuedatetupel26);
		values = ArrayUtils.add(values, valuedatetupel27);
		values = ArrayUtils.add(values, valuedatetupel28);
		values = ArrayUtils.add(values, valuedatetupel29);
		values = ArrayUtils.add(values, valuedatetupel30);
		values = ArrayUtils.add(values, valuedatetupel31);
		values = ArrayUtils.add(values, valuedatetupel32);
		values = ArrayUtils.add(values, valuedatetupel33);
		values = ArrayUtils.add(values, valuedatetupel34);
		values = ArrayUtils.add(values, valuedatetupel35);
		values = ArrayUtils.add(values, valuedatetupel36);

		return new BaseValue(name, values);
	}

}
