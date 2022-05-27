package cn.dcube.ahead.soc.alarm;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.soc.alarm.config.AlarmConfig;
import cn.dcube.ahead.soc.alarm.merge.EventMergeSrv;
import cn.dcube.ahead.soc.alarm.merge.EventMergeThread;
import cn.dcube.ahead.soc.alarm.merge.MergeCacheManager;
import cn.dcube.ahead.soc.alarm.rule.AlarmRuleService;
import cn.dcube.ahead.soc.alarm.service.AlarmService;
import cn.dcube.ahead.soc.util.IDGenerator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Alarm引擎
 * 
 * @author yangfei
 *
 */
@Service
@ConditionalOnBean(AlarmConfig.class)
@Slf4j
public class AlarmEngine {

	@Autowired
	@Getter
	private AlarmConfig config;

	@Autowired
	private AlarmService alarmEventService;

	@Autowired
	private AlarmRuleService alarmRuleService;
	
	@Autowired
	private EventMergeSrv eventMergeSrv;

	// 数据库操作eventloop
	@Getter
	private EventLoopGroup dbOptEventLoopGroup;

	// es操作eventloop
	@Getter
	private EventLoopGroup esOptEventLoopGroup;

	@PostConstruct
	public void start() {
		// 0. 缓存初始化
		MergeCacheManager.getInstance().init(config);
		log.info("01.初始化alarm的内存成功!");
		// 02.加载告警规则,从数据库读取alarmevent最大id到内存
		loadAlarmRules();

		// 03.启动告警业务处理线程
		startThread();

		// 数据库操作的eventLoop
		dbOptEventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("alarm-dboper"));
		esOptEventLoopGroup = new NioEventLoopGroup(4, new DefaultThreadFactory("alarm-esoper"));
	}

	/**
	 * 加载告警规则和告警信息
	 */
	public void loadAlarmRules() {
		if (log.isInfoEnabled()) {
			log.info("02.加载告警规则");
		}

		// 加载告警规则
		alarmRuleService.loadRule();
		alarmRuleService.loadSceneRule();

		// 关闭当天0点之前的未关闭的alarmevent,防止程序当前0点之前down掉，未及时关闭alarmevent
		alarmEventService.closeAlarmEvent();

		// 加载复杂告警事件
		alarmEventService.loadAlarmEvent();
		if (log.isInfoEnabled()) {
			log.info("02.加载告警规则成功");
		}
	}

	/**
	 * 启动告警业务处理线程
	 */
	public void startThread() {

		// 启动事件合并线程
		EventMergeThread evtmergeThread = new EventMergeThread(eventMergeSrv);
		evtmergeThread.start();

		if (log.isInfoEnabled()) {
			log.info("03.启动业务处理线程成功!");
		}

	}
	
	public Long getMaxId() {
		return IDGenerator.getId((byte)config.getId()); 
	}
	
	
	public void submitTask(Runnable task) {
		dbOptEventLoopGroup.submit(task);
	}

	public void submitESTask(Runnable task) {
		esOptEventLoopGroup.next().submit(task);
	}


}
