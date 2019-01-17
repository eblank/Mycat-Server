package io.mycat.route.function;

import io.mycat.config.model.rule.RuleAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;


/**
 * 按月份和差异类型分区
 * <p>
 * 例子
 * month differentType => index
 * 201801 0 => 0
 * 201801 1 => 1
 * 201802 0 => 3
 *
 * @author luxp
 * @date 2019/1/17
 */
public class PartitionByMonthDifferentType extends AbstractPartitionAlgorithm implements
		RuleAlgorithm {
	private static final Logger LOGGER = Logger.getLogger(PartitionByMonthDifferentType.class);

	private String sBeginDate;
	private String dateFormat;
	private String sEndDate;
	private LocalDate beginDate;
	private LocalDate endDate;
	private int nPartition;
	private int typeCount;
	private DateTimeFormatter formatter;

	@Override
	public void init() {
		dateFormat = "yyyyMMdd";
		formatter = DateTimeFormatter.ofPattern(dateFormat);
		beginDate = LocalDate.parse(sBeginDate + "01", formatter);
		if (StringUtils.isNotBlank(sEndDate)) {
			endDate = LocalDate.parse(sEndDate + "01", formatter);
			Period period = beginDate.until(endDate);
			nPartition = (period.getYears() * 12 + period.getMonths()) + 1;
			if (nPartition < 0) {
				throw new IllegalArgumentException("起始日期必须小于结束日期.");
			}
		} else {
			nPartition = -1;
		}

		if (nPartition > 0) {
			nPartition *= typeCount;
		}
	}

	@Override
	public Integer calculate(String columnValue) {
		int dateLength = 6;
		String sDate = columnValue.substring(0, dateLength) + "01";
		String sType = columnValue.substring(dateLength);

		int type = Integer.parseInt(sType);

		LocalDate date = LocalDate.parse(sDate, formatter);
		Period period = beginDate.until(date);
		int targetPartition = (period.getYears() * 12 + period.getMonths()) * typeCount + type;
		if (nPartition > 0) {
			targetPartition = reCalculatePartition(targetPartition);
		}
		return targetPartition;
	}

	/**
	 * For circulatory partition, calculated value of target partition needs to be
	 * rotated to fit the partition range
	 */
	private int reCalculatePartition(int targetPartition) {
		/**
		 * If target date is previous of start time of partition setting, shift
		 * the delta range between target and start date to be positive value
		 */
		if (targetPartition < 0) {
			targetPartition = nPartition - (-targetPartition) % nPartition;
		}

		if (targetPartition >= nPartition) {
			targetPartition =  targetPartition % nPartition;
		}

		return targetPartition;
	}


	@Override
	public int getPartitionNum() {
		int nPartition = this.nPartition;
		return nPartition;
	}

	public void setsBeginDate(String sBeginDate) {
		this.sBeginDate = sBeginDate;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setsEndDate(String sEndDate) {
		this.sEndDate = sEndDate;
	}

	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}
}
