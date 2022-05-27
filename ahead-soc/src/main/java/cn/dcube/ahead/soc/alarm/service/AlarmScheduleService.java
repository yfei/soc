package cn.dcube.ahead.soc.alarm.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.soc.alarm.config.AlarmConfig;
import cn.dcube.ahead.soc.alarm.merge.MergeCacheManager;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务service
 * 
 * @author yangfei
 *
 */
@Service
@ConditionalOnBean(AlarmConfig.class)
@Slf4j
public class AlarmScheduleService {

	@Autowired
	private AlarmService service;

	@Autowired
	private AlarmConfig config;

	/**
	 * 定时关闭告警
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	@ConditionalOnExpression("${ahead.soc.alarm.closeConfig.enable: false}")
	public void closeAlarmEvent() {
		log.info("定时关闭告警");
		log.info("{} 每天关闭告警事件，清理Alarm内存--start", new Date());

		// 修改告警的状态，state置为10，人工关闭状态
		service.closeAlarmEvent();
		// 清理alarm内存，不再对告警持续合并
		cleanAlarmCache();
		log.info("{} 每天0点关闭告警事件，清理Alarm内存--成功！", new Date());
		// FIXME 没有清理的告警不再持续聚合
	}

	private void cleanAlarmCache() {
		// 清理内存
		MergeCacheManager.getInstance().cleanCacheWithPeriod(config.getCloseConfig().getPeriod() * 24 * 60 * 60);
	}
}
