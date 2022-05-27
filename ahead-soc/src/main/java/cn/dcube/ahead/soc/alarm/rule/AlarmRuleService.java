package cn.dcube.ahead.soc.alarm.rule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.soc.alarm.config.AlarmConfig;
import cn.dcube.ahead.soc.alarm.mapper.AlarmRuleMapper;
import cn.dcube.ahead.soc.alarm.mapper.SceneRuleMapper;
import cn.dcube.ahead.soc.alarm.model.AlarmRule;
import cn.dcube.ahead.soc.alarm.model.SceneRule;
import lombok.extern.slf4j.Slf4j;

/**
 * alarm规则service
 * 
 * @author yangfei
 *
 */
@Service
@ConditionalOnBean(AlarmConfig.class)
@Slf4j
public class AlarmRuleService {
	
	@Autowired
	private AlarmRuleMapper alarmRuleMapper;
	
	@Autowired
	private SceneRuleMapper sceneRuleMapper;
	
	
	private Map<String, AlarmRule> alarmrulemap = new ConcurrentHashMap<String, AlarmRule>();
	// key 为cep_s,cep_f。value为对应的SceneRule
	private Map<String, List<SceneRule>> scenerulemap = new ConcurrentHashMap<String, List<SceneRule>>();

	
	/**
	 * 加载告警规则
	 */
	public void loadRule() {
		try {
			long start = System.currentTimeMillis();
			List<AlarmRule> rulelist = alarmRuleMapper.selectList(null);
			long costTime = System.currentTimeMillis() - start;
			log.info("AlarmRuleMapper.selectallrule(LoadAlarmRule->loadRule method) cost time:{} ms", costTime);
			String key = null;
			for (AlarmRule alarmrule : rulelist) {
				key = "cep_s=" + alarmrule.getCepS() + ",cep_f=" + alarmrule.getCepF();
				// 场景
				if (alarmrule.getCepS() == 0 && alarmrule.getCepF() == 0) {
					key = "c=" + alarmrule.getC() + ",s=" + alarmrule.getS();
				}
				alarmrulemap.put(key, alarmrule);
			}
			AlarmRuleManager.getInstance().setAlarmrulemap(alarmrulemap);
		} catch (Exception e) {
			log.error("loadAlarmRule从数据库加载告警规则异常!" + e);
		}
		log.info("Alarm加载告警策略成功！");
	}
	
	/**
	 * 加载场景规则
	 */
	public void loadSceneRule() {
		try {
			long start = System.currentTimeMillis();
			List<SceneRule> list = sceneRuleMapper.selectList(null);
			long costTime = System.currentTimeMillis() - start;
			log.info("SceneRuleMapper.selectallrule(LoadAlarmRule->loadSceneRule method) cost time:{} ms", costTime);
			String cepkey = null;
			List<SceneRule> sceneRuleList = null;
			for (SceneRule scenerule : list) {
				boolean flag = true;
				cepkey = "cep_s=" + scenerule.getCepS() + ",cep_f=" + scenerule.getCepF();

				sceneRuleList = scenerulemap.get(cepkey);
				if (null == sceneRuleList) {
					sceneRuleList = new ArrayList<SceneRule>();
				}

				Iterator<SceneRule> it = sceneRuleList.iterator();
				while (it.hasNext()) {
					SceneRule rule = it.next();
					// 说明cep_s/f对应的场景已经存在
					if (rule.getC() == scenerule.getC() && rule.getS() == scenerule.getS()) {
						flag = false;
						break;
					}
				}
				if (flag) {
					sceneRuleList.add(scenerule);
				}

				scenerulemap.put(cepkey, sceneRuleList);
			}
			AlarmRuleManager.getInstance().setScenerulemap(scenerulemap);
		} catch (Exception e) {
			log.error("loadSceneRule从数据库加载复杂规则异常!" + e);
		}
		log.info("Alarm加载复杂规则成功！");
	}

}
