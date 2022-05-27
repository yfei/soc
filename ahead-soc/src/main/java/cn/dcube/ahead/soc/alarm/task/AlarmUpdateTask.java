package cn.dcube.ahead.soc.alarm.task;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.dcube.ahead.soc.alarm.model.AlarmEvent;
import cn.dcube.ahead.soc.alarm.model.AlarmRule;
import cn.dcube.ahead.soc.alarm.service.AlarmService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmUpdateTask implements Runnable {
	private Logger errorLog = LogManager.getLogger("errorFile");

	private AlarmService service;

	private AlarmEvent alarmEvent;

	private AlarmRule alarmrule;

	private String match_key;

	private int flag;

	public AlarmUpdateTask(AlarmService service, AlarmEvent alarmEvent, AlarmRule alarmrule, String match_key,
			int flag) {
		this.service = service;
		this.alarmEvent = alarmEvent;
		this.alarmrule = alarmrule;
		this.match_key = match_key;
		this.flag = flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			// 如果父事件超长时间没有子事件，超过30分钟，则修改持续状态为2
			Date time = new Date();
			if ((time.getTime() - (alarmEvent.getStartTime().getTime())) > 1800 * 1000) {
				alarmEvent.setIscontinuous("2");
			}
			int auto = alarmrule.getAutoupdate();
			log.info("alarmevt 自动升级标志, id:" + auto);

			if (flag == 2) {
				if (alarmEvent.getLevel() <= 5) {// 事件级别小于等于5，修改告警事件内容
					try {
						long start = System.currentTimeMillis();
						service.getAlarmEventMapper().updateById(alarmEvent);
						long costTime = System.currentTimeMillis() - start;
						log.info(
								"AlarmEventMapper.updateByPrimaryKey(EventUpdateSrv->UpdateAlarm method) cost time:{} ms",
								costTime);
						log.info("alarmevt updatedb sucess.");
					} catch (Exception e) {
						log.error("AlarmEventMapper.updateByPrimaryKey:", e);
					}
				} else {// 告警事件大于5，将告警事件从内存删除，重新生成新的告警事件
					// alarm_event_father.remove(match_key);
				}
			}
		} catch (Exception e) {
			errorLog.error("UpdateAlarmTask error:", e);
		}

	}
}
