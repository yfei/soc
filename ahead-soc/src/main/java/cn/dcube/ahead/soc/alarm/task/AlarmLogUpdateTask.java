package cn.dcube.ahead.soc.alarm.task;

import cn.dcube.ahead.soc.alarm.model.AlarmLog;
import cn.dcube.ahead.soc.alarm.service.AlarmService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmLogUpdateTask implements Runnable {

	private AlarmService service;

	private AlarmLog alarmlog;

	public AlarmLogUpdateTask(AlarmService service, AlarmLog alarmlog) {
		this.service = service;
		this.alarmlog = alarmlog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			service.getAlarmLogMapper().updateById(alarmlog);
		} catch (Exception e) {
			log.error("UpdateAlarmLogTask error:{}", e);
		}

	}
}
