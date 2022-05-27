package cn.dcube.ahead.soc.alarm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 
 * 描述：
 * <p>
 * 告警事件表和告警日志表关联类
 * </p>
 * 创建日期：2014年7月9日 下午3:56:04<br>
 * 
 * @author：yu_jwei<br>
 * @update：$Date$<br>
 * @version：$Revision$<br>
 * @since 3.1.0
 */
@TableName(value = "alarm_event_log_r")
public class AlarmEventLogR {

	@TableId
	@TableField(value = "id")
	private Long id = 0L;

	@TableField(value = "alarm_event_id")
	private Long alarmEventId = 0L;

	@TableField(value = "alarm_log_id")
	private Long alarmLogId = 0L;

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
	 * @return the alarmEventId
	 */
	public Long getAlarmEventId() {
		return alarmEventId;
	}

	/**
	 * @param alarmEventId
	 *            the alarmEventId to set
	 */
	public void setAlarmEventId(Long alarmEventId) {
		this.alarmEventId = alarmEventId;
	}

	/**
	 * @return the alarmLogId
	 */
	public Long getAlarmLogId() {
		return alarmLogId;
	}

	/**
	 * @param alarmLogId
	 *            the alarmLogId to set
	 */
	public void setAlarmLogId(Long alarmLogId) {
		this.alarmLogId = alarmLogId;
	}
}
