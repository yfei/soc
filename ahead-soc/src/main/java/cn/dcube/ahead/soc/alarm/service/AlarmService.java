package cn.dcube.ahead.soc.alarm.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.soc.alarm.config.AlarmConfig;
import cn.dcube.ahead.soc.alarm.mapper.AlarmEventLogRMapper;
import cn.dcube.ahead.soc.alarm.mapper.AlarmEventMapper;
import cn.dcube.ahead.soc.alarm.mapper.AlarmFrequencyMapper;
import cn.dcube.ahead.soc.alarm.mapper.AlarmLogMapper;
import cn.dcube.ahead.soc.alarm.mapper.AlarmUpgradeMapper;
import cn.dcube.ahead.soc.alarm.merge.MergeCacheManager;
import cn.dcube.ahead.soc.alarm.model.AlarmEvent;
import cn.dcube.ahead.soc.alarm.model.AlarmFrequency;
import cn.dcube.ahead.soc.alarm.util.MatchKeyUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@ConditionalOnBean(AlarmConfig.class)
@Slf4j
public class AlarmService {

	@Autowired
	private AlarmConfig config;

	@Autowired
	@Getter
	private AlarmEventMapper alarmEventMapper;

	@Autowired
	@Getter
	private AlarmLogMapper alarmLogMapper;

	@Autowired
	@Getter
	private AlarmUpgradeMapper alarmUpgradeMapper;

	@Autowired
	@Getter
	private AlarmFrequencyMapper alarmFrequencyMapper;

	@Autowired
	@Getter
	private AlarmEventLogRMapper alarmEventLogRMapper;

	/**
	 * 加载未关闭的告警事件到内存中
	 */
	public void loadAlarmEvent() {
		try {
			Calendar calendar = new GregorianCalendar();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			long start = System.currentTimeMillis();
			// 导入当天0点之后的alarmevent进行持续合并
			List<AlarmEvent> evtlist = alarmEventMapper.selectComplexEvent();
			long costTime = System.currentTimeMillis() - start;
			log.info("AlarmEventMapper.selectcomplexevent(LoadAlarm->LoadAlarmEvent method) cost time:{} ms", costTime);
			AlarmEvent alarmevt = new AlarmEvent();
			Iterator<AlarmEvent> it = evtlist.iterator();
			while (it.hasNext()) {
				alarmevt = it.next();
				String complex_key = MatchKeyUtil.getMatchKey4AlarmLoad(alarmevt);
				MergeCacheManager.getInstance().getAlarm_event_complex().put(complex_key, alarmevt);
				MergeCacheManager.getInstance().getAlarm_id_key().put(alarmevt.getId(), complex_key);
				AlarmFrequency alarmfrequency = new AlarmFrequency();
				alarmfrequency.setId(alarmevt.getId());
				alarmfrequency.setMax(alarmevt.getMax());
				alarmfrequency.setMin(alarmevt.getMin());
				alarmfrequency.setAvg(alarmevt.getAvg());
				alarmfrequency.setTotal(alarmevt.getTotal());
				alarmfrequency.setOutput(alarmevt.getOutput());
				MergeCacheManager.getInstance().getAlarm_frequency_complex().put(complex_key, alarmfrequency);
			}
		} catch (Exception e) {
			log.error("alarm加载复杂告警事件异常!", e);
		}
		log.info("alarm加载复杂告警事件成功!");
	}

	/**
	 * 关闭告警事件
	 */
	public void closeAlarmEvent() {
		try {
			log.info("Close AlarmEvent job start, timeLimit:{} Day", config.getCloseConfig().getPeriod());

			long limit = config.getCloseConfig().getPeriod() * 24 * 60 * 60L;
			alarmEventMapper.updateStatus(new Date(), limit);
			long start = System.currentTimeMillis();
			long costTime = System.currentTimeMillis() - start;
			log.info("AlarmEventMapper.closeAlarmEvent(EventUpdateSrv->updateAlarmEventState method) cost time:{} ms",
					costTime);
			log.info("Close AlarmEvent success");
		} catch (Exception e) {
			log.error("Close AlarmEvent job failed:", e);
		}
	}

}
