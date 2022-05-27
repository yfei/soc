package cn.dcube.ahead.soc.alarm.rule;

import java.util.List;
import java.util.Map;

import cn.dcube.ahead.soc.alarm.model.AlarmRule;
import cn.dcube.ahead.soc.alarm.model.SceneRule;

public class AlarmRuleManager {
	private AlarmRuleManager() {
	}

	private static final AlarmRuleManager alarmRuleManager = new AlarmRuleManager();

	public static AlarmRuleManager getInstance() {
		return alarmRuleManager;
	}

	private Map<String, AlarmRule> alarmrulemap;

	private Map<String, List<SceneRule>> scenerulemap;

	/**
	 * @return the alarmrulemap
	 */
	public Map<String, AlarmRule> getAlarmrulemap() {
		return alarmrulemap;
	}

	/**
	 * @param alarmrulemap the alarmrulemap to set
	 */
	public void setAlarmrulemap(Map<String, AlarmRule> alarmrulemap) {
		this.alarmrulemap = alarmrulemap;
	}

	/**
	 * @return the scenerulemap
	 */
	public Map<String, List<SceneRule>> getScenerulemap() {
		return scenerulemap;
	}

	/**
	 * @param scenerulemap the scenerulemap to set
	 */
	public void setScenerulemap(Map<String, List<SceneRule>> scenerulemap) {
		this.scenerulemap = scenerulemap;
	}

}
