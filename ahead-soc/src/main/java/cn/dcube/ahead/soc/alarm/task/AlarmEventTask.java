package cn.dcube.ahead.soc.alarm.task;

import cn.dcube.ahead.soc.alarm.model.AlarmEvent;
import cn.dcube.ahead.soc.alarm.service.AlarmService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmEventTask implements Runnable {

	private AlarmService service;

	private AlarmEvent alarmEvent;

	public AlarmEventTask(AlarmService service, AlarmEvent alarmEvent) {
		this.service = service;
		this.alarmEvent = alarmEvent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			service.getAlarmEventMapper().insert(alarmEvent);
		} catch (Exception e) {
			log.error("AlarmEventTask save alarmevent error:", e);
		}

	}
}
