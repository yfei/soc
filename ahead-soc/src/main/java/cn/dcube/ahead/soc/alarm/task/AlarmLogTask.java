package cn.dcube.ahead.soc.alarm.task;

import cn.dcube.ahead.soc.alarm.model.AlarmLog;
import cn.dcube.ahead.soc.alarm.service.AlarmService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmLogTask implements Runnable {

	private AlarmService service;

	private AlarmLog alarmLog;

	public AlarmLogTask(AlarmService service, AlarmLog alarmLog) {
		this.service = service;
		this.alarmLog = alarmLog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			service.getAlarmLogMapper().insert(alarmLog);
		} catch (Exception e) {
			log.error("AlarmLogTask save alarmlog error:", e);
		}

	}
}
