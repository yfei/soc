package cn.dcube.ahead.soc.cep;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.stereotype.Service;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

import cn.dcube.ahead.kafka.config.KafkaTopicConfig;
import cn.dcube.ahead.kafka.producer.KafkaProducer;
import cn.dcube.ahead.soc.cep.config.CEPConfig;
import cn.dcube.ahead.soc.cep.model.AlarmEvt;
import cn.dcube.ahead.soc.cep.model.CEPEvent;
import cn.dcube.ahead.soc.cep.model.EventBase;
import cn.dcube.ahead.soc.cep.output.GetRecEventThread;
import cn.dcube.ahead.soc.cep.rule.CB_RuleObject;
import cn.dcube.ahead.soc.cep.rule.CEPRuleService;
import cn.dcube.ahead.soc.cep.rule.RuleReader;
import cn.dcube.ahead.soc.cep.service.CEPEventSrv;
import cn.dcube.ahead.soc.util.Constant;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * CEP引擎
 * 
 * @author yangfei
 *
 */
@Service
@ConditionalOnBean(CEPConfig.class)
@Slf4j
public class CEPEngine {

	@Autowired
	@Getter
	private CEPConfig config;
	
	@Autowired
	@Getter
	private KafkaTopicConfig kafkaTopicConfig;
	
	@Autowired
	@Getter
	private KafkaProducer producer;
	
	@Autowired
	private CEPRuleService ruleService;
	
	@Autowired
	private CEPEventSrv cepEventSrv;
	
	@Getter
	private EPServiceProvider epService = null;

	// esper输出事件处理线程池
	@Getter
	private EventLoopGroup epserOutEventLoop;

	@PostConstruct
	public void start() {
		// 1.启动ESPER
		initEspEngine();
		// 2. 加载CEP规则
		loadCEPRule();
		// 3. 启动处理任务
		startThread();
	}

	/**
	 * 1.启动ESPER
	 */
	private void initEspEngine() {
		if (log.isInfoEnabled()) {
			log.info("初始化esper引擎");
		}

		Configuration configuration = new Configuration();
		configuration.addEventType("EventBase", EventBase.class.getName());
		configuration.addEventType("AlarmEvt", AlarmEvt.class.getName());
		configuration.addEventType("CEPEvt", CEPEvent.class.getName());
		// Get engine instance
		epService = EPServiceProviderManager.getProvider("cep", configuration);
		if (log.isInfoEnabled()) {
			log.info("完成初始化esper引擎");
		}
	}

	/**
	 * 2.加载ESPER分析规则
	 */
	private void loadCEPRule() {
		if (log.isInfoEnabled()) {
			log.info("加载CEP规则文件");
		}
		if (config.isHandleAnalysisRule()) {
			try {
				this.loadCEPRule(Constant.RULE_ANALYSIS);
			} catch (Exception e) {
				log.error("", e);
			}
		}
		if (config.isHandleStatisticsRule()) {
			try {
				this.loadCEPRule(Constant.RULE_STATISTICS);
			} catch (Exception e) {
				log.error("", e);
			}
		}
		if (log.isInfoEnabled()) {
			log.info("完成加载CEP规则文件");
		}
	}

	private void loadCEPRule(String type) throws IOException {
		// 获取关联规则文件路径
		String rulefile = new FileSystemResourceLoader().getResource("classpath:rule-analysis.ini").getFile()
				.getPath();
		if (Constant.RULE_STATISTICS.equals(type)) {
			rulefile = new FileSystemResourceLoader().getResource("classpath:rule-statistics.ini").getFile().getPath();
		}
		if (log.isInfoEnabled()) {
			log.info("加载规则文件:{}", rulefile);
		}

		Map<Integer, CB_RuleObject> ruleobjectsMap = new LinkedHashMap<Integer, CB_RuleObject>();
		try {
			RuleReader rulereader = new RuleReader(rulefile, type);
			// 获取关联规则 对象列表
			ruleobjectsMap = rulereader.getRules(type);
			for (Integer i : ruleobjectsMap.keySet()) {
				CB_RuleObject ruleobject = ruleobjectsMap.get(i);

				if (log.isInfoEnabled()) {
					log.info("加载规则,规则id为:{}", ruleobject.getRuleid());
				}
				ruleService.add(epService.getEPAdministrator(), ruleobject);
			}
		} catch (Exception e) {
			log.error(type + "规则加载失败:{}", e);
			log.error(type + "规则加载失败:{}", e);
		}
	}

	private void startThread() {
		if (log.isInfoEnabled()) {
			log.info("启动关联分析事件处理线程");
		}

		epserOutEventLoop = new NioEventLoopGroup(config.getHandleThread(),
				new DefaultThreadFactory("cep-esper-handle"));
		GetRecEventThread getrecevt = new GetRecEventThread(cepEventSrv);
		getrecevt.start();
		log.info("完成启动关联分析事件处理线程");
	}

}
