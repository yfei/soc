package cn.dcube.ahead.soc.alarm.task;

import cn.dcube.ahead.soc.alarm.model.AlarmUpgrade;
import cn.dcube.ahead.soc.alarm.service.AlarmService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmUpgradeTask implements Runnable {

	private AlarmService service;

	private AlarmUpgrade alarmUpgrade;

	public AlarmUpgradeTask(AlarmService service, AlarmUpgrade alarmUpgrade) {
		this.service = service;
		this.alarmUpgrade = alarmUpgrade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			service.getAlarmUpgradeMapper().insert(alarmUpgrade);
		} catch (Exception e) {
			log.error("", e);
		}

	}
}
