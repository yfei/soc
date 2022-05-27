package cn.dcube.ahead.soc.alarm.merge;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import cn.dcube.ahead.soc.alarm.config.AlarmConfig;
import cn.dcube.ahead.soc.alarm.model.AlarmEvent;
import cn.dcube.ahead.soc.alarm.model.AlarmEventLogR;
import cn.dcube.ahead.soc.alarm.model.AlarmFrequency;
import cn.dcube.ahead.soc.alarm.model.AlarmLog;

/**
 * 告警引擎缓存
 * 
 * @author yangfei
 *
 */
public class MergeCacheManager {


	private MergeCacheManager() {

	}

	private static final MergeCacheManager cacheManager = new MergeCacheManager();

	public static MergeCacheManager getInstance() {
		return cacheManager;
	}

	public void init(AlarmConfig config) {
		alarm_event_statis = new ConcurrentHashMap<String, AlarmLog>();
		alarm_event_complex = new ConcurrentHashMap<String, AlarmEvent>();
		alarm_frequency_complex = new ConcurrentHashMap<String, AlarmFrequency>();
		alarm_id_key = new ConcurrentHashMap<Long, String>();
		alarm_event_log_rel = new ConcurrentHashMap<String, AlarmEventLogR>();
	}

	/**
	 * 每月初执行定时任务，清理alarm内存
	 */
	public void cleanCache() {
		if (!alarm_event_statis.isEmpty()) {
			alarm_event_statis.clear();
		}
		if (!alarm_event_complex.isEmpty()) {
			alarm_event_complex.clear();
		}
		if (!alarm_frequency_complex.isEmpty()) {
			alarm_frequency_complex.clear();
		}
		if (!alarm_id_key.isEmpty()) {
			alarm_id_key.clear();
		}
		if (!alarm_event_log_rel.isEmpty()) {
			alarm_event_log_rel.clear();
		}
	}

	public void cleanCacheWithPeriod(long period) {
		long now = new Date().getTime();
		if (!alarm_event_complex.isEmpty()) {
			for (Entry<String, AlarmEvent> entry : alarm_event_complex.entrySet()) {
				AlarmEvent alarmEvent = entry.getValue();
				String complex_key = entry.getKey();
				Long alarmId = alarmEvent.getId();
				if (now - alarmEvent.getStartTime().getTime() > period) {
					// 超过关闭周期,关闭相关内存
					alarm_id_key.remove(alarmId);
					alarm_frequency_complex.remove(complex_key);
					alarm_event_complex.remove(complex_key);
				}
			}
		}
		// 清理alarmlog
		if (!alarm_event_statis.isEmpty()) {
			alarm_event_statis.clear();
		}
		// 清理告警和alarmlog的关联关系
		if (!alarm_event_log_rel.isEmpty()) {
			alarm_event_log_rel.clear();
		}
	}

	private Map<String, AlarmFrequency> alarm_frequency_complex;

	/**
	 * 单类型告警事件的统计
	 */
	private Map<String, AlarmLog> alarm_event_statis;

	// 告警事件合并Map
	private Map<String, AlarmEvent> alarm_event_complex;

	private Map<Long, String> alarm_id_key;

	// key: alarmEventId + "/" + alarmLogId
	private Map<String, AlarmEventLogR> alarm_event_log_rel;

	/**
	 * @return the alarm_frequency_complex
	 */
	public Map<String, AlarmFrequency> getAlarm_frequency_complex() {
		return alarm_frequency_complex;
	}

	/**
	 * @param alarm_frequency_complex the alarm_frequency_complex to set
	 */
	public void setAlarm_frequency_complex(Map<String, AlarmFrequency> alarm_frequency_complex) {
		this.alarm_frequency_complex = alarm_frequency_complex;
	}

	/**
	 * @return the alarm_event_statis
	 */
	public Map<String, AlarmLog> getAlarm_event_statis() {
		return alarm_event_statis;
	}

	/**
	 * @param alarm_event_statis the alarm_event_statis to set
	 */
	public void setAlarm_event_statis(Map<String, AlarmLog> alarm_event_statis) {
		this.alarm_event_statis = alarm_event_statis;
	}

	/**
	 * @return the alarm_event_complex
	 */
	public Map<String, AlarmEvent> getAlarm_event_complex() {
		return alarm_event_complex;
	}

	/**
	 * @param alarm_event_complex the alarm_event_complex to set
	 */
	public void setAlarm_event_complex(Map<String, AlarmEvent> alarm_event_complex) {
		this.alarm_event_complex = alarm_event_complex;
	}

	/**
	 * @return the alarm_id_key
	 */
	public Map<Long, String> getAlarm_id_key() {
		return alarm_id_key;
	}

	/**
	 * @param alarm_id_key the alarm_id_key to set
	 */
	public void setAlarm_id_key(Map<Long, String> alarm_id_key) {
		this.alarm_id_key = alarm_id_key;
	}

	/**
	 * @return the alarm_event_log_rel
	 */
	public Map<String, AlarmEventLogR> getAlarm_event_log_rel() {
		return alarm_event_log_rel;
	}

	/**
	 * @param alarm_event_log_rel the alarm_event_log_rel to set
	 */
	public void setAlarm_event_log_rel(Map<String, AlarmEventLogR> alarm_event_log_rel) {
		this.alarm_event_log_rel = alarm_event_log_rel;
	}

}
