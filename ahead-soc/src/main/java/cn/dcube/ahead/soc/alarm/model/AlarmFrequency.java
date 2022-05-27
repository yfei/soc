package cn.dcube.ahead.soc.alarm.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "alarm_frequency")
public class AlarmFrequency {

	@TableId
	private Long id = 0L;

	private Float max = (float) 0;

	private Float min = (float) 0;

	private Float avg = (float) 0;

	private Float total = (float) 0;

	private Integer output = 0;

	private Date intime;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the max
	 */
	public Float getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(Float max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public Float getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(Float min) {
		this.min = min;
	}

	/**
	 * @return the avg
	 */
	public Float getAvg() {
		return avg;
	}

	/**
	 * @param avg
	 *            the avg to set
	 */
	public void setAvg(Float avg) {
		this.avg = avg;
	}

	/**
	 * @return the total
	 */
	public Float getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(Float total) {
		this.total = total;
	}

	/**
	 * @return the output
	 */
	public Integer getOutput() {
		return output;
	}

	/**
	 * @param output
	 *            the output to set
	 */
	public void setOutput(Integer output) {
		this.output = output;
	}

	/**
	 * @return the intime
	 */
	public Date getIntime() {
		return intime;
	}

	/**
	 * @param intime
	 *            the intime to set
	 */
	public void setIntime(Date intime) {
		this.intime = intime;
	}
}
