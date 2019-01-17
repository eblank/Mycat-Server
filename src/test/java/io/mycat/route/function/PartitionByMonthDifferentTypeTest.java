package io.mycat.route.function;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author luxp
 * @date 2019/1/17
 */
public class PartitionByMonthDifferentTypeTest {
	@Test
	public void test() {
		PartitionByMonthDifferentType partition = new PartitionByMonthDifferentType();

		partition.setDateFormat("yyyyMMdd");
		partition.setsBeginDate("201801");
		partition.setTypeCount(3);

		partition.init();

		Assert.assertEquals(true, 0 == partition.calculate("2018010"));
		Assert.assertEquals(true, 1 == partition.calculate("2018011"));
		Assert.assertEquals(true, 2 == partition.calculate("2018012"));
		Assert.assertEquals(true, 14 == partition.calculate("2018052"));
		Assert.assertEquals(true, 32 == partition.calculate("2018112"));
		Assert.assertEquals(true, 37 == partition.calculate("2019011"));
		Assert.assertEquals(true, 43 == partition.calculate("2019031"));
		Assert.assertEquals(true, 67 == partition.calculate("2019111"));
		Assert.assertEquals(true, 70 == partition.calculate("2019121"));

		partition.setDateFormat("yyyyMMdd");
		partition.setsBeginDate("201801");
		partition.setsEndDate("201812");
		partition.setTypeCount(3);

		partition.init();

		/**
		 *  0 : 2016-01-01~31, 2015-01-01~31, 2014-01-01~31
		 *  1 : 2016-02-01~28, 2015-02-01~28, 2014-02-01~28
		 *  5 : 2016-06-01~30, 2015-06-01~30, 2014-06-01~30
		 * 11 : 2016-12-01~31, 2015-12-01~31, 2014-12-01~31
		 */

		Assert.assertEquals(true, 0 == partition.calculate("2018010"));
		Assert.assertEquals(true, 0 == partition.calculate("2019010"));
		Assert.assertEquals(true, 0 == partition.calculate("2020010"));

		Assert.assertEquals(true, 1 == partition.calculate("2018011"));
		Assert.assertEquals(true, 1 == partition.calculate("2019011"));
		Assert.assertEquals(true, 1 == partition.calculate("2020011"));

		Assert.assertEquals(true, 5 == partition.calculate("2018022"));
		Assert.assertEquals(true, 5 == partition.calculate("2019022"));

		Assert.assertEquals(true, 33 == partition.calculate("2018120"));
		Assert.assertEquals(true, 34 == partition.calculate("2018121"));
		Assert.assertEquals(true, 35 == partition.calculate("2018122"));

	}
}