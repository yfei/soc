package cn.dcube.ahead.soc.alarm.task;

import cn.dcube.ahead.soc.alarm.model.AlarmFrequency;
import cn.dcube.ahead.soc.alarm.service.AlarmService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmFrequencyTask implements Runnable {

	private AlarmService service;

	private AlarmFrequency alarmFrequency;

	public AlarmFrequencyTask(AlarmService service, AlarmFrequency alarmFrequency) {
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
			log.info("alarmFrequency id:{}", alarmFrequency.getId());
			service.getAlarmFrequencyMapper().insert(alarmFrequency);
		} catch (Exception e) {
			log.error("", e);
		}

	}
}
