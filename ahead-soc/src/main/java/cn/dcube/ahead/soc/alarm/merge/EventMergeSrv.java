package cn.dcube.ahead.soc.alarm.merge;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.base.date.JodaUtil;
import cn.dcube.ahead.elastic.service.ElasticService;
import cn.dcube.ahead.soc.alarm.AlarmEngine;
import cn.dcube.ahead.soc.alarm.config.AlarmConfig;
import cn.dcube.ahead.soc.alarm.model.AlarmEvent;
import cn.dcube.ahead.soc.alarm.model.AlarmEventLogR;
import cn.dcube.ahead.soc.alarm.model.AlarmFrequency;
import cn.dcube.ahead.soc.alarm.model.AlarmLog;
import cn.dcube.ahead.soc.alarm.model.AlarmRule;
import cn.dcube.ahead.soc.alarm.model.AlarmUpgrade;
import cn.dcube.ahead.soc.alarm.model.SceneRule;
import cn.dcube.ahead.soc.alarm.rule.AlarmRuleManager;
import cn.dcube.ahead.soc.alarm.service.AlarmService;
import cn.dcube.ahead.soc.alarm.task.AlarmEventLogRelTask;
import cn.dcube.ahead.soc.alarm.task.AlarmEventTask;
import cn.dcube.ahead.soc.alarm.task.AlarmFrequencyTask;
import cn.dcube.ahead.soc.alarm.task.AlarmFrequencyUpdateTask;
import cn.dcube.ahead.soc.alarm.task.AlarmLogTask;
import cn.dcube.ahead.soc.alarm.task.AlarmLogUpdateTask;
import cn.dcube.ahead.soc.alarm.task.AlarmUpdateTask;
import cn.dcube.ahead.soc.alarm.task.AlarmUpgradeTask;
import cn.dcube.ahead.soc.alarm.task.CepEventTask;
import cn.dcube.ahead.soc.alarm.util.DirectionUtil;
import cn.dcube.ahead.soc.alarm.util.EventTran;
import cn.dcube.ahead.soc.alarm.util.MatchKeyUtil;
import cn.dcube.ahead.soc.cep.model.CEPEvent;
import cn.dcube.ahead.soc.util.Constant;
import cn.dcube.goku.commons.ip.IpClassify;
import lombok.extern.slf4j.Slf4j;

@Service
@ConditionalOnBean(AlarmConfig.class)
@Slf4j
public class EventMergeSrv implements IEventMergeSrv {

	public static final int MAIL_LEVEL = 3;

	@Autowired
	private AlarmEngine alarmEngine;

	@Autowired
	private AlarmConfig config;

	@Autowired
	private AlarmService service;

	@Autowired
	private ElasticService esService;

	public static final int MAIL_MIN_LEVEL = 3;

	public void mergeCep(CEPEvent cepevt) {
		// 是否是实时攻击
		boolean attack = false;
		try {
			// 是否是实时攻击
			log.info("处理cepevt,cep_s/f is {},srcip is {},dstip is {},tag is{}",
					cepevt.getSubclass() + "/" + cepevt.getFamily(), cepevt.getSrcip(), cepevt.getDstip(),
					cepevt.getTag());
			// 处理非告警分析事件。这类事件一般是统计展示和基线事件，直接存储CEP数据即可，不参与后续分析
			boolean notAnalysisEvent = this.handleNotAnalysisCepEvent(cepevt);
			if (notAnalysisEvent) {
				return;
			}
			// 2.根据CEP_S和CEP_F找告警规则;如果告警规则不存在,则使用默认CEP事件的告警规则
			String rule_key = "cep_s=" + cepevt.getCeps() + ",cep_f=" + cepevt.getCepf();
			if (AlarmRuleManager.getInstance().getAlarmrulemap() == null) {
				log.warn("Alarmrulemap is null");
			}
			AlarmRule alarmRule = (AlarmRule) AlarmRuleManager.getInstance().getAlarmrulemap().get(rule_key);
			// cep_s和cep_f找不到alarmRule时,使用cep_s/f=2/0的默认告警
			if (alarmRule == null) {
				rule_key = "cep_s=2,cep_f=0";
				alarmRule = (AlarmRule) AlarmRuleManager.getInstance().getAlarmrulemap().get(rule_key);
				if (alarmRule == null) {
					if (log.isDebugEnabled()) {
						log.debug("cep eventtype:{} no alarm rule.", rule_key);
					}
					return;
				}
			}
			log.info("the alarm rule key is {}", rule_key);
			// 告警规则禁用
			if (alarmRule.getEnable() == 1) {
				log.warn("alarmRule.getEnable====" + alarmRule.getEnable());
				return;
			}
			if (!rule_key.equals("cep_s=2,cep_f=0")) {
				attack = true;
			} else if (cepevt.getClazz() == 3 && cepevt.getSubclass() == 4) {
				// 没有CEP规则的eventbase,class=3 and subclass=4
				attack = true;
			}
			// 0. 如果是攻击事件,但是cep_s/f=2/0,tag设置为可疑
			if (attack && cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
				cepevt.setTag(Constant.TAG_QDKY);
			}

			log.info("根据告警规则{}聚合,聚合策略为{}", alarmRule.getRuleId(), alarmRule.getMerge());
			// 3.根据告警规则聚合alarmlog,并存储CEP
			Long alarmLogId = this.step4AlarmLog(cepevt, alarmRule);
			if (alarmLogId == 0) {
				log.warn("alarmLogId==0,请校验聚合策略是否与CEP事件[{}]聚合策略一致!", cepevt.getCepid());
			}
			this.saveCepEvent(cepevt, alarmLogId);
			// 查找是否存在CEP事件所在攻击场景列表.
			List<SceneRule> sceneRuleList = AlarmRuleManager.getInstance().getScenerulemap()
					.get("cep_s=" + cepevt.getCeps() + ",cep_f=" + cepevt.getCepf());

			/* 判断CEP事件所在攻击场景列表是否为空 */
			if (sceneRuleList == null || sceneRuleList.isEmpty()) {
				String complex_rule_key = "c=2,s=0";
				// 获取到的是 默认聚合攻击的规则
				AlarmRule complex_alarmrule = (AlarmRule) AlarmRuleManager.getInstance().getAlarmrulemap()
						.get(complex_rule_key);
				// add by dai_hyan 若cep事件未在任何场景中，合并到默认复杂攻击
				mergeNoSceneProcess(cepevt, complex_alarmrule, alarmRule, alarmLogId, attack);
			} else {
				for (SceneRule sceneRule : sceneRuleList) {
					String complex_rule_key = "c=" + sceneRule.getC() + ",s=" + sceneRule.getS();
					if (log.isDebugEnabled()) {
						log.debug("get scenerule rule_key:{}", complex_rule_key);
					}
					AlarmRule scenerule = (AlarmRule) AlarmRuleManager.getInstance().getAlarmrulemap()
							.get(complex_rule_key);
					mergeScene(cepevt, scenerule, alarmRule, alarmLogId, attack);
				}
			}
			if (cepevt.getSrcip() != 0 && cepevt.getDstip() != 0) {
				if (cepevt.getCepf() != 0) { // 实时攻击发送SDAP展示
					// FIXME
					// sendevtSrv.sendRealAttackAlarmToDisplay(cepevt, alarmRule);
				}
			}
		} catch (Exception e) {
			log.error("EventMergeSrv MergeCep error:{} ", e);
		}
	}

	public boolean handleNotAnalysisCepEvent(CEPEvent cepevt) {
		// 基线和统计事件,直接入库
		if (cepevt.getCeps() == Constant.SUBCLASS_STATISTIC || cepevt.getCeps() == Constant.SUBCLASS_BASELINE) {
			log.info("cep_s==1 or cep_s==4,save cep directly");
			long cepid = alarmEngine.getMaxId();
			cepevt.setCepid(cepid);
			// cep事件入库
			alarmEngine.submitESTask(new CepEventTask(esService, cepevt));
			return true;
		}
		return false;
	}

	/**
	 * 聚合AlarmLog
	 * 
	 * @param cepevt
	 * @param alarmRule
	 * @return
	 */
	public Long step4AlarmLog(CEPEvent cepevt, AlarmRule alarmRule) {
		long alarmLogId = 0L;
		// 按源聚合还是按照目的聚合
		String alarmLogMerge = alarmRule.getMerge();
		if (null == alarmLogMerge || alarmLogMerge.isEmpty()) {
			log.warn("alarmRule.getMerge is Empty!");
			return 0L;
		}

		// alarmrule的合并策略是“SD”时，AlarmLogId对应记录到cepevent的alarm_log_id字段
		if (alarmLogMerge.equals("SD")) {
			alarmLogId = createOrMergeAlarmLog(cepevt, alarmRule);
		}
		// cepevt的源Ip不为0，alarmrule的合并策略是“S”时，AlarmLogId对应记录到cepevent的alarm_log_id字段
		if (cepevt.getSrcip() != 0 && alarmLogMerge.equals("S")) {
			alarmLogId = createOrMergeAlarmLog(cepevt, alarmRule);
		}
		// cepevt的目的Ip不为0，alarmrule的合并策略是“D”时，AlarmLogId对应记录到cepevent的alarm_log_idd字段
		if (cepevt.getDstip() != 0 && alarmLogMerge.equals("D")) {
			alarmLogId = createOrMergeAlarmLog(cepevt, alarmRule);
		}
		log.info("聚合AlarmLog完成,id为{}", alarmLogId);
		return alarmLogId;
	}

	/**
	 * 保存cepEvent
	 * 
	 * @param cepevt
	 * @param alarmLogId
	 */
	public void saveCepEvent(CEPEvent cepevt, Long alarmLogId) {
		long cepid = alarmEngine.getMaxId();
		cepevt.setCepid(cepid);
		cepevt.setAlarmlogid(alarmLogId);
		// cep事件入库
		alarmEngine.submitESTask(new CepEventTask(esService, cepevt));
		log.info("CEPEvent id为{}", cepid);
	}

	/**
	 * 合并非基于复杂场景的cep事件,合并策略暂时只支持按S、按D、按S|D中的一种
	 * 
	 * @param cepevt            cep事件
	 * @param complex_alarmrule rule_key为"c=2,s=0"的告警规则
	 * @param alarmrule         该cep事件对应的告警规则
	 * @param alarmLogId        告警日志Id
	 */
	public void mergeNoSceneProcess(CEPEvent cepevt, AlarmRule complex_alarmrule, AlarmRule alarmrule, Long alarmLogId,
			boolean attack) {
		// 告警事件合并的key
		String complex_key = null;

		Long alarmEventId = 0L;
		String mergeStr = complex_alarmrule.getMerge();
		if (mergeStr == null) {
			return;
		}
		String merge[] = mergeStr.trim().split("\\|");// S|D
		for (int num = 0; num < merge.length; num++) {
			/* 按源合并时，源Ip不为0； 按目的合并时，目的Ip不为0 */
			if ((merge[num].equalsIgnoreCase("S") && cepevt.getSrcip() != 0 && alarmrule.getMerge().startsWith("S"))
					|| (merge[num].equalsIgnoreCase("D") && cepevt.getDstip() != 0
							&& alarmrule.getMerge().contains("D"))) {
				complex_key = MatchKeyUtil.getMatchKey4AlarmEventOfDefaultScene(cepevt, merge[num]);
				if (log.isDebugEnabled()) {
					log.debug("complex_key:{}", complex_key);
				}

				AlarmEvent alarmevt = MergeCacheManager.getInstance().getAlarm_event_complex().get(complex_key);
				// 告警事件已经存在，合并告警事件
				if (alarmevt != null) {
					alarmEventId = alarmevt.getId();
					// alarmevent事件合并
					mergeComplexEvent(complex_alarmrule, alarmrule, cepevt, alarmevt, complex_key, merge[num], attack);
					// alarm_log 和 alarm_event的关联管理 alarm_event_log_r是否已存在
					String key_event_log_r = alarmEventId + "/" + alarmLogId;
					if (null == MergeCacheManager.getInstance().getAlarm_event_log_rel().get(key_event_log_r)) {
						// 保存alarm和alarmlog的关联
						this.saveAlarmEventLogR(alarmevt.getId(), alarmLogId);
					}
				} else {
					alarmevt = createNewAlarm(cepevt, complex_alarmrule, alarmrule, complex_key, alarmLogId, 1,
							merge[num], attack);
				}
				// 确认事件和多种确认阻断事件,产生内部情报
				if (Constant.TAG_CONFIRM.equals(alarmevt.getTag())
						|| Constant.TAG_CONFIRM_MULTI_DENY.equals(alarmevt.getTag())) {
					// 将攻击源IP视为恶意IP，添加到恶意IP库中
					addOrUpdateBadIp(alarmevt);
				}
			}
		}
	}

	// FIXME 场景规则待定
	public void mergeScene(CEPEvent cepevt, AlarmRule scenerule, AlarmRule alarmrule, Long alarmLogId, boolean attack) {
		// 告警事件合并的key
		String complex_key = MatchKeyUtil.getMatchKey4AlarmScene(scenerule, cepevt);
		if (log.isDebugEnabled()) {
			log.debug("complex_key:{}", complex_key);
		}

		AlarmEvent alarmevt = MergeCacheManager.getInstance().getAlarm_event_complex().get(complex_key);
		// 告警事件已经存在，合并告警事件
		if (null != alarmevt) {
			// alarm_event_log_r入库
			String key_event_log_r = alarmevt.getId() + "/" + alarmLogId;
			if (null == MergeCacheManager.getInstance().getAlarm_event_log_rel().get(key_event_log_r)) {
				this.saveAlarmEventLogR(alarmevt.getId(), alarmLogId);
			}
			mergeComplexEvent(scenerule, alarmrule, cepevt, alarmevt, complex_key, null, attack);
		} else {
			createNewAlarm(cepevt, scenerule, alarmrule, complex_key, alarmLogId, 2, null, attack);
		}
	}

	/* 创建新的告警事件 */
	public AlarmEvent createNewAlarm(CEPEvent cepevt, AlarmRule complex_alarmrule, AlarmRule alarmrule,
			String complex_key, Long alarmLogId, int type, String mergeType, boolean attack) {
		AlarmEvent alarmevt = EventTran.cepToAlarm(cepevt, complex_alarmrule);
		// FIXME 以下内容其实可以放到cepToAlarm中,但是为了兼容以前版本，这里单独处理
		// 根据告警规则的alarm_c/s定义alarmevt的alarm_c/s
		alarmevt.setAlarmC(alarmrule.getC());
		alarmevt.setAlarmS(alarmrule.getS());
		alarmevt.setStage(alarmrule.getStage());
		alarmevt.setTag(cepevt.getTag());
		// 10级level
		alarmevt.setAlarmRuleLevel(alarmrule.getLevel());
		// 根据tag确定级别
		if (Constant.TAG_QDKY.equals(cepevt.getTag())) {
			alarmevt.setLevel(2);
		} else if (Constant.TAG_GDKY.equals(cepevt.getTag())) {
			alarmevt.setLevel(3);
		} else if (Constant.TAG_CONFIRM.equals(cepevt.getTag())) {
			alarmevt.setLevel(4);
		} else if (Constant.TAG_CONFIRM_DENY.equals(cepevt.getTag())) {
			alarmevt.setLevel(2);
		} else if (Constant.TAG_CONFIRM_MULTI_DENY.equals(cepevt.getTag())) {
			alarmevt.setLevel(3);
		} else {
			// 一般事件,使用cep事件级别
			alarmevt.setLevel(cepevt.getCeplevel());
		}
		// 只统计具体的cep事件类型，默认统计输出的事件类型，不作为统计对象
		if (attack) {
			// 这里使用告警规则的类型,cep类型数量比较多,这里做了收缩
			if (Constant.TAG_QDKY.equals(cepevt.getTag())) {
				alarmevt.setSuspiciousCepType(alarmevt.getCepType());
				alarmevt.setSuspiciousCeptypeCount(1);
			} else if (Constant.TAG_CONFIRM_DENY.equals(cepevt.getTag())) {
				alarmevt.setSuspiciousCepType(alarmevt.getCepType());
				alarmevt.setDenyCeptypeCount(1);
			}
		}

		Long alarmevtId = alarmEngine.getMaxId();
		alarmevt.setId(alarmevtId);

		if (mergeType == null) { // 对于场景的,mergeType使用场景中对应的alarmRule的聚合策略
			mergeType = alarmrule.getMerge();
		}
		if (mergeType != null) {
			alarmevt.setMergeType(mergeType);
			// 按照源ip进行的统计，目标ip设为0，表示有多个目的ip
			if (mergeType.equalsIgnoreCase("S")) {
				if (attack) {
					String dstIp = "";
					if (cepevt.getDstip() != 0L) {
						dstIp = IpClassify.long2StrIp(cepevt.getDstip()) + ",";
					} else {
						dstIp = cepevt.getDstipv6() + ",";
					}
					// 设置目的ipList
					if (!"".equals(dstIp)) {
						alarmevt.setDstList(dstIp);
						if (Constant.TAG_QDKY.equals(cepevt.getTag())) {
							alarmevt.setSuspiciousDstList(dstIp);
						} else if (Constant.TAG_CONFIRM_DENY.equals(cepevt.getTag())) {
							alarmevt.setDenyDstList(dstIp);
						}
					}
				}
			} else if (mergeType.equalsIgnoreCase("D")) {
				if (attack) {
					String srcIp = "";
					if (cepevt.getSrcip() != 0L) {
						srcIp = IpClassify.long2StrIp(cepevt.getSrcip()) + ",";
					} else {
						srcIp = cepevt.getSrcipv6() + ",";
					}
					// 设置目的ipList
					if (!"".equals(srcIp)) {
						alarmevt.setSrcList(srcIp);
						if (Constant.TAG_QDKY.equals(cepevt.getTag())) {
							alarmevt.setSuspiciousSrcList(srcIp);
						} else if (Constant.TAG_CONFIRM_DENY.equals(cepevt.getTag())) {
							alarmevt.setDenySrcList(srcIp);
						}
					}
				}
			} else if (alarmrule.getMerge().equals("SD")) {
			}
		}

		// issendmail=1 标识告警事件发送邮件(告警级别>=3)
		if (alarmevt.getLevel() >= MAIL_LEVEL) {
			alarmevt.setIssendmail("1");
		}
		// 存储alarm
		alarmEngine.submitTask(new AlarmEventTask(service, alarmevt));
		MergeCacheManager.getInstance().getAlarm_event_complex().put(complex_key, alarmevt);
		MergeCacheManager.getInstance().getAlarm_id_key().put(alarmevtId, complex_key);

		// 初始化生成upgrade
		if (attack) {
			saveAlarmUpgrade(alarmevt,
					"告警生成,发生" + getTagName(cepevt.getTag()) + "的攻击事件:"
							+ getAttackName(alarmrule.getC(), alarmrule.getS(), cepevt.getCeps(), cepevt.getCepf(),
									cepevt.getClazz(), cepevt.getSubclass(), cepevt.getFamily()),
					"INIT;ATTACK;");
		} else {
			saveAlarmUpgrade(alarmevt, "告警生成", "INIT;");
		}

		// 记录alarm_event_log_r关联关系
		if (alarmLogId != 0) {
			this.saveAlarmEventLogR(alarmevt.getId(), alarmLogId);
		}

		// 初始化alarm攻击频率
		long moveTime = cepevt.getEndtimeOfDate().getTime() - cepevt.getStarttimeOfDate().getTime();
		AlarmFrequency alarmfrequency = EventTran.cepToAlarmFrequency(cepevt, moveTime);
		alarmfrequency.setId(alarmevt.getId());
		alarmEngine.submitTask(new AlarmFrequencyTask(service, alarmfrequency));
		MergeCacheManager.getInstance().getAlarm_frequency_complex().put(complex_key, alarmfrequency);

		this.sendAlarm(alarmevt, cepevt, alarmrule, attack);
		return alarmevt;
	}

	/**
	 * 合并告警事件
	 * 
	 * @param complex_alarmrule
	 * @param cepevt
	 * @param alarmevt
	 * @param complex_key
	 * @param rel_key
	 * @param alarmLogId
	 * @param mergeType
	 * @param attack
	 */
	public void mergeComplexEvent(AlarmRule complex_alarmrule, AlarmRule alarmrule, CEPEvent cepevt,
			AlarmEvent alarmevt, String complex_key, String mergeType, boolean attack) {
		AlarmFrequency alarmfrequency = MergeCacheManager.getInstance().getAlarm_frequency_complex().get(complex_key);
		synchronized (alarmevt) {
			// 对于场景的,mergeType使用场景中对应的alarmRule的聚合策略
			if (mergeType == null) {
				mergeType = alarmrule.getMerge();
			}
			if (Constant.TAG_NORMAL.equals(cepevt.getTag())) {
				// 一般事件,如果ceplevel较高,使用较高的ceplevel作为告警level
				if (Constant.TAG_NORMAL.equals(alarmevt.getTag())) {
					if (cepevt.getCeplevel() > alarmevt.getLevel()) {
						int beforeLevel = alarmevt.getLevel();
						alarmevt.setLevel(cepevt.getCeplevel());
						alarmevt.setAlarmRuleLevel(alarmrule.getLevel());
						String upgradeDesc = "发生高等级的非安全告警事件,事件类型为:"
								+ getCSFName(cepevt.getClazz(), cepevt.getSubclass(), cepevt.getFamily()) + ".告警从"
								+ beforeLevel + "级升级到" + alarmevt.getLevel() + "级";
						alarmevt.setUpgradeTime(new Date());
						this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
					}
				}
			} else { // 攻击/异常事件
				// 使用高的攻击阶段
				if (alarmrule.getStage() >= alarmevt.getStage()) {
					alarmevt.setStage(alarmrule.getStage());
				}
				// 出现高等级的告警事件,使用高等级告警事件的CS和攻击阶段以及tag
				if (alarmrule.getLevel() >= alarmevt.getAlarmRuleLevel()) {
					// 出现同等级的告警事件时,
					alarmevt.setAlarmC(alarmrule.getC());
					alarmevt.setAlarmS(alarmrule.getS());
					alarmevt.setAlarmRuleLevel(alarmrule.getLevel());
					alarmevt.setTag(cepevt.getTag());
					// 判断是否发生升级
					int beforeLevel = alarmevt.getLevel();
					// 根据tag设置级别
					if (Constant.TAG_QDKY.equals(alarmevt.getTag())) {
						alarmevt.setLevel(2);
					} else if (Constant.TAG_GDKY.equals(alarmevt.getTag())) {
						alarmevt.setLevel(3);
					} else if (Constant.TAG_CONFIRM.equals(alarmevt.getTag())) {
						alarmevt.setLevel(4);
					} else if (Constant.TAG_CONFIRM_DENY.equals(alarmevt.getTag())) {
						alarmevt.setLevel(2);
					} else if (Constant.TAG_CONFIRM_MULTI_DENY.equals(alarmevt.getTag())) {
						alarmevt.setLevel(3);
					}
					if (alarmevt.getLevel() > beforeLevel) {
						String upgradeDesc = "出现" + getTagName(cepevt.getTag()) + "的告警事件:"
								+ getAttackName(alarmrule.getC(), alarmrule.getS(), cepevt.getCeps(), cepevt.getCepf(),
										cepevt.getClazz(), cepevt.getSubclass(), cepevt.getFamily())
								+ ",告警从" + beforeLevel + "级升级到" + alarmevt.getLevel() + "级";
						alarmevt.setUpgradeTime(new Date());
						this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
					}
				}

			}
			// 聚合源和目的,出现未出现的攻击类型时,记录upgrade
			this.mergeSrcOrDst(mergeType, alarmrule, cepevt, alarmevt, cepevt.getTag(), attack);
			// 更新更新时间
			alarmevt.setStr1(JodaUtil.formatDate(new Date()));
			alarmevt.setCepNum(alarmevt.getCepNum() + 1);
			Date endTime = (cepevt.getEndtimeOfDate().after(alarmevt.getEndTime())) ? cepevt.getEndtimeOfDate()
					: alarmevt.getEndTime();
			alarmevt.setEndTime(endTime);
			alarmevt.setSensorMask(alarmevt.getSensorMask() | cepevt.getSensormask());

			// 更新direction字段的值
			int updateDirection = alarmevt.getDirection() | DirectionUtil.GetDirection(cepevt);
			alarmevt.setDirection(updateDirection);

			// 更新alarmevent
			if (log.isDebugEnabled()) {
				log.debug("update alarmevt next step");
			}

			alarmEngine.submitTask(new AlarmUpdateTask(service, alarmevt, complex_alarmrule, complex_key, 2));
			MergeCacheManager.getInstance().getAlarm_event_complex().put(complex_key, alarmevt);

			// 更新频度
			if (log.isDebugEnabled()) {
				log.debug("update alarmfrequency next step");
			}

			long moveTime = cepevt.getEndtimeOfDate().getTime() - cepevt.getStarttimeOfDate().getTime();
			if (alarmfrequency != null) {
				alarmfrequency = EventTran.CompareToAlarmFrequency(cepevt, alarmfrequency, moveTime);
				alarmEngine.submitTask(new AlarmFrequencyUpdateTask(service, alarmfrequency));
				MergeCacheManager.getInstance().getAlarm_frequency_complex().put(complex_key, alarmfrequency);
			}
			this.sendAlarm(alarmevt, cepevt, alarmrule, attack);
		}
	}

	private void mergeSrcOrDst(String mergeType, AlarmRule alarmrule, CEPEvent cepevt, AlarmEvent alarmevt, String tag,
			boolean attack) {
		if (mergeType != null) {
			// 按照源ip进行的合并
			if (mergeType.equalsIgnoreCase("S")) {
				// 处理告警类型
				if (attack) {
					String cepTypeTemp = "cep_s=" + cepevt.getCeps() + ",cep_f=" + cepevt.getCepf() + ";";
					if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
						cepTypeTemp = "c=" + cepevt.getClazz() + ",s=" + cepevt.getSubclass() + ",f="
								+ cepevt.getFamily() + ";";
					}
					String cepType = alarmevt.getCepType() + ";";
					if (cepType.indexOf(cepTypeTemp) == -1) {
						// 出现新的攻击类型,记录攻击
						cepType = cepTypeTemp + cepType;
						String cepTypeSubstr = cepType.substring(0, cepType.length() - 1);
						alarmevt.setCepType(cepTypeSubstr);
						alarmevt.setCeptypeCount(alarmevt.getCeptypeCount() + 1);
						String upgradeDesc = "出现" + getTagName(cepevt.getTag()) + "的告警事件:"
								+ getAttackName(alarmrule.getC(), alarmrule.getS(), cepevt.getCeps(), cepevt.getCepf(),
										cepevt.getClazz(), cepevt.getSubclass(), cepevt.getFamily());
						alarmevt.setUpgradeTime(new Date());
						this.saveAlarmUpgrade(alarmevt, upgradeDesc, "ATTACK;");
					}
					if (Constant.TAG_QDKY.equals(tag)) {
						String alarmType = alarmevt.getSuspiciousCepType() + ";";
						int alarmTypeCount = alarmevt.getSuspiciousCeptypeCount();
						if (alarmType.indexOf(cepTypeTemp) == -1) {
							alarmType = cepTypeTemp + alarmType;
							alarmType = alarmType.substring(0, alarmType.length() - 1);
							alarmTypeCount = alarmTypeCount + 1;
							alarmevt.setSuspiciousCepType(alarmType);
							alarmevt.setSuspiciousCeptypeCount(alarmTypeCount);
							if (alarmTypeCount == 3) {
								int beforeLevel = alarmevt.getLevel();
								alarmevt.setAlarmRuleLevel(6); // 高度可疑,alarmrule级别为6级
								alarmevt.setLevel(3);
								if (beforeLevel < 3) {
									String upgradeDesc = "";
									if (alarmTypeCount == 3) {
										upgradeDesc = "可疑攻击手段数量达到阈值3,从" + beforeLevel + "级升级为" + alarmevt.getLevel()
												+ "级";
									}
									alarmevt.setUpgradeTime(new Date());
									this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
								}
							}
						}
					} else if (Constant.TAG_CONFIRM_DENY.equals(tag)) {
						String alarmType = alarmevt.getDenyCepType() + ";";
						int alarmTypeCount = alarmevt.getDenyCeptypeCount();
						if (alarmType.indexOf(cepTypeTemp) == -1) {
							alarmType = cepTypeTemp + alarmType;
							alarmType = alarmType.substring(0, alarmType.length() - 1);
							alarmTypeCount = alarmTypeCount + 1;
							alarmevt.setDenyCepType(alarmType);
							alarmevt.setDenyCeptypeCount(alarmTypeCount);
							if (alarmTypeCount == 3) {
								int beforeLevel = alarmevt.getLevel();
								alarmevt.setAlarmRuleLevel(6); // 高度可疑,alarmrule级别为6级
								alarmevt.setLevel(3);
								if (beforeLevel < 3) {
									String upgradeDesc = "";
									if (alarmTypeCount == 3) {
										upgradeDesc = "确认阻断的攻击类型数量达到阈值3,从" + beforeLevel + "级升级为" + alarmevt.getLevel()
												+ "级";
									}
									alarmevt.setUpgradeTime(new Date());
									this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
								}
							}
						}

					}
					// 设置目的ipList
					String dstIp = "";
					if (cepevt.getDstip() != 0L) {
						dstIp = IpClassify.long2StrIp(cepevt.getDstip());
					} else {
						dstIp = cepevt.getDstipv6();
					}
					if (dstIp != null && !"".equals(dstIp)) {
						dstIp = dstIp + ",";
						if (alarmevt.getDstList() != null && alarmevt.getDstList().indexOf(dstIp) == -1) {
							alarmevt.setDstList(alarmevt.getDstList() + dstIp);
						}
						if (Constant.TAG_QDKY.equals(tag)) {
							if (alarmevt.getSuspiciousDstList() != null
									&& alarmevt.getSuspiciousDstList().indexOf(dstIp) == -1) {
								alarmevt.setSuspiciousDstList(alarmevt.getSuspiciousDstList() + dstIp);
								// 处理ip数量
								String suspiciousDstIps = alarmevt.getSuspiciousDstList();
								int suspiciousDstIpCount = suspiciousDstIps == null ? 0
										: suspiciousDstIps.split(",").length;
								if (suspiciousDstIpCount == 50) {
									int beforeLevel = alarmevt.getLevel();
									alarmevt.setAlarmRuleLevel(6); // 高度可疑,alarmrule级别为6级
									alarmevt.setLevel(3);
									if (beforeLevel < 3) {
										String upgradeDesc = "";
										if (suspiciousDstIpCount == 50) {
											upgradeDesc = "可疑攻击源的攻击目的IP数量达到阈值50,从" + beforeLevel + "级升级为"
													+ alarmevt.getLevel() + "级";
										}
										alarmevt.setUpgradeTime(new Date());
										this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
									}
								}
							}
						} else if (Constant.TAG_CONFIRM_DENY.equals(tag)) {
							if (alarmevt.getDenyDstList() != null && alarmevt.getDenyDstList().indexOf(dstIp) == -1) {
								alarmevt.setDenyDstList(alarmevt.getDenyDstList() + dstIp);
								String denyDstIps = alarmevt.getDenyDstList();
								int denyDstIpCount = denyDstIps == null ? 0 : denyDstIps.split(",").length;
								if (denyDstIpCount == 50) {
									int beforeLevel = alarmevt.getLevel();
									alarmevt.setLevel(3);
									alarmevt.setAlarmRuleLevel(5); // 多种确认阻断,alarmrulelevel设置为5
									if (beforeLevel < 3) {
										String upgradeDesc = "";
										if (denyDstIpCount == 50) {
											upgradeDesc = "确认阻断的攻击源的攻击目的IP数量达到阈值50,从" + beforeLevel + "级升级为"
													+ alarmevt.getLevel() + "级";
										}
										alarmevt.setUpgradeTime(new Date());
										this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
									}
								}
							}
						}
					}

				}
			} else if (mergeType.equalsIgnoreCase("D")) {
				if (attack) {

					String cepTypeTemp = "cep_s=" + cepevt.getCeps() + ",cep_f=" + cepevt.getCepf() + ";";
					if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
						cepTypeTemp = "c=" + cepevt.getClazz() + ",s=" + cepevt.getSubclass() + ",f="
								+ cepevt.getFamily() + ";";
					}
					String cepType = alarmevt.getCepType() + ";";
					if (cepType.indexOf(cepTypeTemp) == -1) {
						// 出现新的攻击类型,记录攻击
						cepType = cepTypeTemp + cepType;
						String cepTypeSubstr = cepType.substring(0, cepType.length() - 1);
						alarmevt.setCepType(cepTypeSubstr);
						alarmevt.setCeptypeCount(alarmevt.getCeptypeCount() + 1);
						String upgradeDesc = "出现" + getTagName(cepevt.getTag()) + "的告警事件:"
								+ getAttackName(alarmrule.getC(), alarmrule.getS(), cepevt.getCeps(), cepevt.getCepf(),
										cepevt.getClazz(), cepevt.getSubclass(), cepevt.getFamily());
						alarmevt.setUpgradeTime(new Date());
						this.saveAlarmUpgrade(alarmevt, upgradeDesc, "ATTACK;");
					}
					if (Constant.TAG_QDKY.equals(tag)) {
						String alarmType = alarmevt.getSuspiciousCepType() + ";";
						int alarmTypeCount = alarmevt.getSuspiciousCeptypeCount();
						if (alarmType.indexOf(cepTypeTemp) == -1) {
							alarmType = cepTypeTemp + alarmType;
							alarmType = alarmType.substring(0, alarmType.length() - 1);
							alarmTypeCount = alarmTypeCount + 1;
							alarmevt.setSuspiciousCepType(alarmType);
							alarmevt.setSuspiciousCeptypeCount(alarmTypeCount);
							if (alarmTypeCount == 3) {
								int beforeLevel = alarmevt.getLevel();
								alarmevt.setAlarmRuleLevel(6); // 高度可疑,alarmrule级别为6级
								alarmevt.setLevel(3);
								if (beforeLevel < 3) {
									String upgradeDesc = "";
									if (alarmTypeCount == 3) {
										upgradeDesc = "可疑攻击手段数量达到阈值3,从" + beforeLevel + "级升级为" + alarmevt.getLevel()
												+ "级";
									}
									alarmevt.setUpgradeTime(new Date());
									this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
								}
							}
						}
					} else if (Constant.TAG_CONFIRM_DENY.equals(tag)) {
						String alarmType = alarmevt.getDenyCepType() + ";";
						int alarmTypeCount = alarmevt.getDenyCeptypeCount();
						if (alarmType.indexOf(cepTypeTemp) == -1) {
							alarmType = cepTypeTemp + alarmType;
							alarmType = alarmType.substring(0, alarmType.length() - 1);
							alarmTypeCount = alarmTypeCount + 1;
							alarmevt.setDenyCepType(alarmType);
							alarmevt.setDenyCeptypeCount(alarmTypeCount);
							if (alarmTypeCount == 3) {
								int beforeLevel = alarmevt.getLevel();
								alarmevt.setAlarmRuleLevel(6); // 高度可疑,alarmrule级别为6级
								alarmevt.setLevel(3);
								if (beforeLevel < 3) {
									String upgradeDesc = "";
									if (alarmTypeCount == 3) {
										upgradeDesc = "确认阻断的攻击类型数量达到阈值3,从" + beforeLevel + "级升级为" + alarmevt.getLevel()
												+ "级";
									}
									alarmevt.setUpgradeTime(new Date());
									this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
								}
							}
						}

					}
					// 设置目的ipList
					String srcIp = "";
					if (cepevt.getSrcip() != 0L) {
						srcIp = IpClassify.long2StrIp(cepevt.getSrcip());
					} else {
						srcIp = cepevt.getSrcipv6();
					}
					if (srcIp != null && !"".equals(srcIp)) {
						srcIp = srcIp + ",";
						if (alarmevt.getSrcList() != null && alarmevt.getSrcList().indexOf(srcIp) == -1) {
							alarmevt.setSrcList(alarmevt.getSrcList() + srcIp);
						}
						if (Constant.TAG_QDKY.equals(tag)) {
							if (alarmevt.getSuspiciousSrcList() != null
									&& alarmevt.getSuspiciousSrcList().indexOf(srcIp) == -1) {
								alarmevt.setSuspiciousSrcList(alarmevt.getSuspiciousSrcList() + srcIp);
								// 处理ip数量
								String suspiciousSrcIps = alarmevt.getSuspiciousSrcList();
								int suspiciousSrcIpCount = suspiciousSrcIps == null ? 0
										: suspiciousSrcIps.split(",").length;
								if (suspiciousSrcIpCount == 50) {
									int beforeLevel = alarmevt.getLevel();
									alarmevt.setAlarmRuleLevel(6); // 高度可疑,alarmrule级别为6级
									alarmevt.setLevel(3);
									if (beforeLevel < 3) {
										String upgradeDesc = "";
										if (suspiciousSrcIpCount == 50) {
											upgradeDesc = "可疑攻击的攻击源IP数量达到阈值50,从" + beforeLevel + "级升级为"
													+ alarmevt.getLevel() + "级";
										}
										alarmevt.setUpgradeTime(new Date());
										this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
									}
								}
							}
						} else if (Constant.TAG_CONFIRM_DENY.equals(tag)) {
							if (alarmevt.getDenySrcList() != null && alarmevt.getDenySrcList().indexOf(srcIp) == -1) {
								alarmevt.setDenySrcList(alarmevt.getDenySrcList() + srcIp);
								String denySrcIps = alarmevt.getDenySrcList();
								int denySrcIpCount = denySrcIps == null ? 0 : denySrcIps.split(",").length;
								if (denySrcIpCount == 50) {
									int beforeLevel = alarmevt.getLevel();
									alarmevt.setLevel(3);
									alarmevt.setAlarmRuleLevel(5); // 多种确认阻断,alarmrulelevel设置为5
									if (beforeLevel < 3) {
										String upgradeDesc = "";
										if (denySrcIpCount == 50) {
											upgradeDesc = "确认阻断的攻击源IP数量达到阈值50,从" + beforeLevel + "级升级为"
													+ alarmevt.getLevel() + "级";
										}
										alarmevt.setUpgradeTime(new Date());
										this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
									}
								}
							}
						}
					}

				}
			} else if (alarmrule.getMerge().equals("SD")) {
				// TODO 場景
			}
		}
	}

	private void addOrUpdateBadIp(AlarmEvent alarmEvent) {
		// TODO

	}

	public void suspiciousIpAnaly(CEPEvent cepEvent) {
		// TODO
	}

	/* 合并AlarmLog */
	public void mergeAlarmLog(AlarmLog alarmlog, CEPEvent cepevt, String alarmLogMerge) {
		int count = alarmlog.getCepNum();
		// 更新合并统计事件direction的值
		int updateDirection = alarmlog.getDirection() | DirectionUtil.GetDirection(cepevt);
		alarmlog.setCepNum(++count);
		// cep事件的endtime晚于alarmlog的endtime，更新alarmlog的endtime
		if (cepevt.getEndtimeOfDate() != null && cepevt.getEndtimeOfDate().after(alarmlog.getEndTime())) {
			alarmlog.setEndTime(cepevt.getEndtimeOfDate());
		}
		alarmlog.setDirection(updateDirection);

		if (alarmLogMerge.equals("S")) {
			if (cepevt.getCeplevel() >= alarmlog.getLevel()) {
				this.setAlarmLogDstInfo(alarmlog, cepevt);
			}
		} else if (alarmLogMerge.equals("D")) {
			if (cepevt.getCeplevel() >= alarmlog.getLevel()) {
				log.debug("set alarmlog srcip.");
				this.setAlarmLogSrcInfo(alarmlog, cepevt);
			}
		} else if (alarmLogMerge.equals("SD")) {
			// 按源和目的聚合的话,源和目的都是唯一的，所以无需更改
		}

		if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
			// 对于2-0事件,将cepevent的csf添加到describe中
			String describe = alarmlog.getDescrib();
			String csf = cepevt.getClazz() + "|" + cepevt.getSubclass() + "|" + cepevt.getFamily() + ";";
			if (describe == null) {
				describe = csf;
			} else if (describe.indexOf(csf) == -1) {
				describe += csf;
			}
			alarmlog.setDescrib(describe);
		}
		alarmEngine.submitTask(new AlarmLogUpdateTask(service, alarmlog));
	}

	/**
	 * alarmlog新增或合并
	 * 
	 * @param cepevt
	 * @param getinfo
	 * @param alarmrule
	 * @return
	 */
	private long createOrMergeAlarmLog(CEPEvent cepevt, AlarmRule alarmrule) {
		long alarmLogId = 0L;
		AlarmLog alarmLog;
		// cepevt的源Ip不为0，按源生成/合并AlarmLog； cepevt的目的Ip不为0，按目的生成/合并AlarmLog
		String alarmlog_key = getMatchKeyOfAlarmlog(cepevt, alarmrule, alarmrule.getMerge());
		if (log.isDebugEnabled()) {
			log.debug("alarmlog_key:{}; merge:{}; SrcIpv4:{}; DstIpv4:{}", alarmlog_key, alarmrule.getMerge(),
					cepevt.getSrcip(), cepevt.getDstip());
		}
		alarmLog = MergeCacheManager.getInstance().getAlarm_event_statis().get(alarmlog_key);
		/* 判断统计合并事件是否存在 */
		if (null != alarmLog) {
			mergeAlarmLog(alarmLog, cepevt, alarmrule.getMerge());
		} else {
			alarmLog = createAlarmLog(cepevt, alarmrule);
		}

		alarmLogId = alarmLog.getAlarmLogId();
		MergeCacheManager.getInstance().getAlarm_event_statis().put(alarmlog_key, alarmLog);

		return alarmLogId;
	}

	private AlarmLog createAlarmLog(CEPEvent cepevt, AlarmRule alarmRule) {
		AlarmLog alarmLog = new AlarmLog();
		alarmLog = EventTran.cepToAlarmLog(cepevt, alarmRule, DirectionUtil.GetDirection(cepevt));
		long alarmLogId = alarmEngine.getMaxId();
		alarmLog.setAlarmLogId(alarmLogId);
		//
		if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
			// 对于2-0事件,将cepevent的csf添加到describe中
			String describe = alarmLog.getDescrib();
			String csf = cepevt.getClazz() + "|" + cepevt.getSubclass() + "|" + cepevt.getFamily() + ";";
			if (describe == null) {
				describe = csf;
			} else if (describe.indexOf(csf) == -1) {
				describe += csf;
			}
			alarmLog.setDescrib(describe);
		}
		alarmEngine.submitTask(new AlarmLogTask(service, alarmLog));

		return alarmLog;
	}

	/**
	 * 告警事件描述信息追加且不会超出数据库定义长度
	 * 
	 * @param describ
	 * @param count
	 * @param prefixdesc
	 * @return
	 */
	public String describ(String describ, int count, String prefixdesc) {
		int k = 0;
		List<String> describList = new ArrayList<String>();

		String subDescrib = describ.substring(prefixdesc.length() + 1);

		count = count - (prefixdesc.length() + 1);
		count = (count > subDescrib.length()) ? subDescrib.length() : count;

		int i = subDescrib.indexOf("，");
		int j = subDescrib.length() - count;
		String[] describArr = subDescrib.split("，");
		k = j % i == 0 ? j / i : j / i + 1;
		k = k > (describArr.length - 1) ? describArr.length - 1 : k;

		int x = 0;
		for (String describStr : describArr) {
			if (x >= k) {
				describList.add(describStr);
			}
			x++;
		}

		String str = describList.toString().replace(",", "，").replace(" ", "");

		return prefixdesc + "，" + str.substring(1, str.length() - 1);
	}

	/**
	 * 获取CEP事件的输出时间窗
	 * 
	 * @param cepevt
	 * @param desc   INIT 初始 UPGRADE 升级 ATTACK 新的攻击事件
	 * @return
	 */

	public Long saveAlarmUpgrade(AlarmEvent alarmevt, String upgradeDesc, String desc) {
		AlarmUpgrade alarmUpgrade = EventTran.alarmevtToUpgrade(alarmevt);
		alarmUpgrade.setUpgradeTime(new Date());
		alarmUpgrade.setUpgradeDesc(upgradeDesc);
		alarmUpgrade.setDescrib(desc);
		alarmUpgrade.setTag(alarmevt.getTag());
		alarmEngine.submitTask(new AlarmUpgradeTask(service, alarmUpgrade));
		// TODO 发送升级记录
		return alarmUpgrade.getId();
	}

	private void saveAlarmEventLogR(Long alarmEventId, Long alarmLogId) {
		AlarmEventLogR alarmEventLogRel = new AlarmEventLogR();
		alarmEventLogRel.setAlarmEventId(alarmEventId);
		alarmEventLogRel.setAlarmLogId(alarmLogId);
		// alarmEvent-alarmLog 关系表根据alarmEvent反查CEPEvent
		alarmEngine.submitTask(new AlarmEventLogRelTask(service, alarmEventLogRel));
		String key_event_log_r = alarmEventId + "/" + alarmLogId;
		MergeCacheManager.getInstance().getAlarm_event_log_rel().put(key_event_log_r, alarmEventLogRel);
	}

	private void setAlarmLogSrcInfo(AlarmLog alarmlog, CEPEvent cepevt) {
		log.debug("set alarmlog srcip:", cepevt.getDstip());
		alarmlog.setSrcIpv4(cepevt.getSrcip());
		alarmlog.setSrcIpv6(cepevt.getSrcipv6());
		alarmlog.setSrcOrgId(cepevt.getSrcorgid());
		alarmlog.setSrcIn(cepevt.getSrcin());
		alarmlog.setSrcAssetid(cepevt.getSrcassetid());
		alarmlog.setSrcAssetLevel(cepevt.getSrcassetlevel());
		alarmlog.setSrcBusiId(cepevt.getSrcbusiid());
		alarmlog.setSrcBusiLink(cepevt.getSrcbusilink());
		alarmlog.setSrcGeo1Code(cepevt.getSrcGeo1Code());
		alarmlog.setSrcGeo1Name(cepevt.getSrcGeo1Name());
		alarmlog.setSrcGeo2Code(cepevt.getSrcGeo2Code());
		alarmlog.setSrcGeo2Name(cepevt.getSrcGeo2Name());
		alarmlog.setSrcGeo3Code(cepevt.getSrcGeo3Code());
		alarmlog.setSrcGeo3Name(cepevt.getSrcGeo3Name());
		alarmlog.setSrcLatitude(cepevt.getSrcLatitude());
		alarmlog.setSrcLongitude(cepevt.getSrcLongitude());
	}

	private void setAlarmLogDstInfo(AlarmLog alarmlog, CEPEvent cepevt) {
		log.debug("set alarmlog dstip:", cepevt.getDstip());
		alarmlog.setDstIpv4(cepevt.getDstip());
		alarmlog.setDstIpv6(cepevt.getDstipv6());
		alarmlog.setDstOrgId(cepevt.getDstorgid());
		alarmlog.setDstIn(cepevt.getDstin());
		alarmlog.setDstAssetid(cepevt.getDstassetid());
		alarmlog.setDstAssetLevel(cepevt.getDstassetlevel());
		alarmlog.setDstBusiId(cepevt.getDstbusiid());
		alarmlog.setDstBusiLink(cepevt.getDstbusilink());
		alarmlog.setDstGeo1Code(cepevt.getDstGeo1Code());
		alarmlog.setDstGeo1Name(cepevt.getDstGeo1Name());
		alarmlog.setDstGeo2Code(cepevt.getDstGeo2Code());
		alarmlog.setDstGeo2Name(cepevt.getDstGeo2Name());
		alarmlog.setDstGeo3Code(cepevt.getDstGeo3Code());
		alarmlog.setDstGeo3Name(cepevt.getDstGeo3Name());
		alarmlog.setDstLatitude(cepevt.getDstLatitude());
		alarmlog.setDstLongitude(cepevt.getDstLongitude());
	}

	/**
	 * @param alarmEngine the alarmEngine to set
	 */
	public void setAlarmEngine(AlarmEngine alarmEngine) {
		this.alarmEngine = alarmEngine;
	}

	/**
	 * @return the alarmEngine
	 */
	public AlarmEngine getAlarmEngine() {
		return alarmEngine;
	}

	private static String getAttackName(int alarmC, int alarmS, int cepS, int cepF, int clazz, int subclass,
			int family) {
		if (cepS == 2 && cepF == 0) {
			return getCSFName(clazz, subclass, family);
		} else {
			String cepType = cepS + "-" + cepF;
			String alarmType = alarmC + "-" + alarmS;
			// TODO redis

			return alarmType.split("-")[0] + "-" + cepType.split("-")[1];
		}

	}

	private static String getCSFName(int clazz, int subclass, int family) {
		String csf = clazz + "-" + subclass + "-" + family;
		// TODO redis
		return clazz + "-" + subclass + "-" + family;
	}

	private static String getTagName(String tag) {
		if (Constant.TAG_NORMAL.equals(tag)) {
			return "非安全告警事件";
		} else if (Constant.TAG_QDKY.equals(tag)) {
			return "轻度可疑";
		} else if (Constant.TAG_GDKY.equals(tag)) {
			return "高度可疑";
		} else if (Constant.TAG_CONFIRM.equals(tag)) {
			return "确认";
		} else if (Constant.TAG_CONFIRM_DENY.equals(tag)) {
			return "阻断";
		} else if (Constant.TAG_CONFIRM_MULTI_DENY.equals(tag)) {
			return "确认阻断";
		}
		return tag;
	}

	private static String getMatchKeyOfAlarmlog(CEPEvent cepevt, AlarmRule alarmrule, String merge) {
		String match_key = null;
		if ("SD".equalsIgnoreCase(merge.trim())) {
			match_key = "alarm_c=" + alarmrule.getC() + ",alarm_s=" + alarmrule.getS() + ",sip="
					+ IpClassify.long2StrIp(cepevt.getSrcip()) + ",dip=" + IpClassify.long2StrIp(cepevt.getDstip())
					+ "srcorg_id=" + cepevt.getSrcorgid() + ",dstorg_id=" + cepevt.getDstorgid();
			// 对于CEP非攻击事件
			if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
				match_key = "c=" + cepevt.getClazz() + ",s=" + cepevt.getSubclass() + ",f=" + cepevt.getFamily()
						+ ",sip=" + IpClassify.long2StrIp(cepevt.getSrcip()) + ",dip="
						+ IpClassify.long2StrIp(cepevt.getDstip()) + "srcorg_id=" + cepevt.getSrcorgid() + ",dstorg_id="
						+ cepevt.getDstorgid();
			}
		} else if ("S".equalsIgnoreCase(merge.trim())) {
			match_key = "alarm_c=" + alarmrule.getC() + ",alarm_s=" + alarmrule.getS() + ",sip="
					+ IpClassify.long2StrIp(cepevt.getSrcip()) + ",srcorg_id=" + cepevt.getDstorgid();
			if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
				match_key = "c=" + cepevt.getClazz() + ",s=" + cepevt.getSubclass() + ",f=" + cepevt.getFamily()
						+ ",sip=" + IpClassify.long2StrIp(cepevt.getSrcip()) + ",srcorg_id=" + cepevt.getDstorgid();
			}
		} else if ("D".equalsIgnoreCase(merge.trim())) {
			match_key = "alarm_c=" + alarmrule.getC() + ",alarm_s=" + alarmrule.getS() + ",dip="
					+ IpClassify.long2StrIp(cepevt.getDstip()) + ",dstorg_id=" + cepevt.getDstorgid();
			if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
				match_key = "c=" + cepevt.getClazz() + ",s=" + cepevt.getSubclass() + ",f=" + cepevt.getFamily()
						+ ",dip=" + IpClassify.long2StrIp(cepevt.getDstip()) + ",dstorg_id=" + cepevt.getDstorgid();
			}
		}
		if (match_key != null) {
			// 增加customid和platformid
			match_key += ",customid=" + cepevt.getCustomerid();
			match_key += ",platformid=" + cepevt.getPlatformid();
			// match_key += ",org_id=" + cepevt.getOrgId();
		}
		return match_key;
	}

	private void sendAlarm(AlarmEvent alarmevt, CEPEvent cepevt, AlarmRule alarmrule, boolean attack) {
		// TODO
	}

}
