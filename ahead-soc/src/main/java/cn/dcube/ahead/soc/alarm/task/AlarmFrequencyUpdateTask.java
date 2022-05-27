package cn.dcube.ahead.soc.alarm.task;

import cn.dcube.ahead.soc.alarm.model.AlarmFrequency;
import cn.dcube.ahead.soc.alarm.service.AlarmService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmFrequencyUpdateTask implements Runnable {

	private AlarmService service;

	private AlarmFrequency alarmFrequency;

	public AlarmFrequencyUpdateTask(AlarmService service, AlarmFrequency alarmFrequency) {
		this.service = service;
		this.alarmFrequency = alarmFrequency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			service.getAlarmFrequencyMapper().updateById(alarmFrequency);
		} catch (Exception e) {
			log.error("UpdateAlarmFrequencyTask error:", e);
		}

	}
}
