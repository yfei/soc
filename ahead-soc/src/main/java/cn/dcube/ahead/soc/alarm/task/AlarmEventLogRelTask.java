package cn.dcube.ahead.soc.alarm.task;

import cn.dcube.ahead.soc.alarm.model.AlarmEventLogR;
import cn.dcube.ahead.soc.alarm.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class AlarmEventLogRelTask implements Runnable {

	private AlarmService service;

	private AlarmEventLogR alarmEventLogRel;

	public AlarmEventLogRelTask(AlarmService service, AlarmEventLogR alarmEventLogRel) {
		this.service = service;
		this.alarmEventLogRel = alarmEventLogRel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			service.getAlarmEventLogRMapper().insert(alarmEventLogRel);
		} catch (Exception e) {
			log.error("", e);
		}

	}
}
