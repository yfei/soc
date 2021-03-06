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
		// ?????????????????????
		boolean attack = false;
		try {
			// ?????????????????????
			log.info("??????cepevt,cep_s/f is {},srcip is {},dstip is {},tag is{}",
					cepevt.getSubclass() + "/" + cepevt.getFamily(), cepevt.getSrcip(), cepevt.getDstip(),
					cepevt.getTag());
			// ?????????????????????????????????????????????????????????????????????????????????????????????CEP????????????????????????????????????
			boolean notAnalysisEvent = this.handleNotAnalysisCepEvent(cepevt);
			if (notAnalysisEvent) {
				return;
			}
			// 2.??????CEP_S???CEP_F???????????????;???????????????????????????,???????????????CEP?????????????????????
			String rule_key = "cep_s=" + cepevt.getCeps() + ",cep_f=" + cepevt.getCepf();
			if (AlarmRuleManager.getInstance().getAlarmrulemap() == null) {
				log.warn("Alarmrulemap is null");
			}
			AlarmRule alarmRule = (AlarmRule) AlarmRuleManager.getInstance().getAlarmrulemap().get(rule_key);
			// cep_s???cep_f?????????alarmRule???,??????cep_s/f=2/0???????????????
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
			// ??????????????????
			if (alarmRule.getEnable() == 1) {
				log.warn("alarmRule.getEnable====" + alarmRule.getEnable());
				return;
			}
			if (!rule_key.equals("cep_s=2,cep_f=0")) {
				attack = true;
			} else if (cepevt.getClazz() == 3 && cepevt.getSubclass() == 4) {
				// ??????CEP?????????eventbase,class=3 and subclass=4
				attack = true;
			}
			// 0. ?????????????????????,??????cep_s/f=2/0,tag???????????????
			if (attack && cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
				cepevt.setTag(Constant.TAG_QDKY);
			}

			log.info("??????????????????{}??????,???????????????{}", alarmRule.getRuleId(), alarmRule.getMerge());
			// 3.????????????????????????alarmlog,?????????CEP
			Long alarmLogId = this.step4AlarmLog(cepevt, alarmRule);
			if (alarmLogId == 0) {
				log.warn("alarmLogId==0,??????????????????????????????CEP??????[{}]??????????????????!", cepevt.getCepid());
			}
			this.saveCepEvent(cepevt, alarmLogId);
			// ??????????????????CEP??????????????????????????????.
			List<SceneRule> sceneRuleList = AlarmRuleManager.getInstance().getScenerulemap()
					.get("cep_s=" + cepevt.getCeps() + ",cep_f=" + cepevt.getCepf());

			/* ??????CEP?????????????????????????????????????????? */
			if (sceneRuleList == null || sceneRuleList.isEmpty()) {
				String complex_rule_key = "c=2,s=0";
				// ??????????????? ???????????????????????????
				AlarmRule complex_alarmrule = (AlarmRule) AlarmRuleManager.getInstance().getAlarmrulemap()
						.get(complex_rule_key);
				// add by dai_hyan ???cep?????????????????????????????????????????????????????????
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
				if (cepevt.getCepf() != 0) { // ??????????????????SDAP??????
					// FIXME
					// sendevtSrv.sendRealAttackAlarmToDisplay(cepevt, alarmRule);
				}
			}
		} catch (Exception e) {
			log.error("EventMergeSrv MergeCep error:{} ", e);
		}
	}

	public boolean handleNotAnalysisCepEvent(CEPEvent cepevt) {
		// ?????????????????????,????????????
		if (cepevt.getCeps() == Constant.SUBCLASS_STATISTIC || cepevt.getCeps() == Constant.SUBCLASS_BASELINE) {
			log.info("cep_s==1 or cep_s==4,save cep directly");
			long cepid = alarmEngine.getMaxId();
			cepevt.setCepid(cepid);
			// cep????????????
			alarmEngine.submitESTask(new CepEventTask(esService, cepevt));
			return true;
		}
		return false;
	}

	/**
	 * ??????AlarmLog
	 * 
	 * @param cepevt
	 * @param alarmRule
	 * @return
	 */
	public Long step4AlarmLog(CEPEvent cepevt, AlarmRule alarmRule) {
		long alarmLogId = 0L;
		// ????????????????????????????????????
		String alarmLogMerge = alarmRule.getMerge();
		if (null == alarmLogMerge || alarmLogMerge.isEmpty()) {
			log.warn("alarmRule.getMerge is Empty!");
			return 0L;
		}

		// alarmrule?????????????????????SD?????????AlarmLogId???????????????cepevent???alarm_log_id??????
		if (alarmLogMerge.equals("SD")) {
			alarmLogId = createOrMergeAlarmLog(cepevt, alarmRule);
		}
		// cepevt??????Ip??????0???alarmrule?????????????????????S?????????AlarmLogId???????????????cepevent???alarm_log_id??????
		if (cepevt.getSrcip() != 0 && alarmLogMerge.equals("S")) {
			alarmLogId = createOrMergeAlarmLog(cepevt, alarmRule);
		}
		// cepevt?????????Ip??????0???alarmrule?????????????????????D?????????AlarmLogId???????????????cepevent???alarm_log_idd??????
		if (cepevt.getDstip() != 0 && alarmLogMerge.equals("D")) {
			alarmLogId = createOrMergeAlarmLog(cepevt, alarmRule);
		}
		log.info("??????AlarmLog??????,id???{}", alarmLogId);
		return alarmLogId;
	}

	/**
	 * ??????cepEvent
	 * 
	 * @param cepevt
	 * @param alarmLogId
	 */
	public void saveCepEvent(CEPEvent cepevt, Long alarmLogId) {
		long cepid = alarmEngine.getMaxId();
		cepevt.setCepid(cepid);
		cepevt.setAlarmlogid(alarmLogId);
		// cep????????????
		alarmEngine.submitESTask(new CepEventTask(esService, cepevt));
		log.info("CEPEvent id???{}", cepid);
	}

	/**
	 * ??????????????????????????????cep??????,??????????????????????????????S??????D??????S|D????????????
	 * 
	 * @param cepevt            cep??????
	 * @param complex_alarmrule rule_key???"c=2,s=0"???????????????
	 * @param alarmrule         ???cep???????????????????????????
	 * @param alarmLogId        ????????????Id
	 */
	public void mergeNoSceneProcess(CEPEvent cepevt, AlarmRule complex_alarmrule, AlarmRule alarmrule, Long alarmLogId,
			boolean attack) {
		// ?????????????????????key
		String complex_key = null;

		Long alarmEventId = 0L;
		String mergeStr = complex_alarmrule.getMerge();
		if (mergeStr == null) {
			return;
		}
		String merge[] = mergeStr.trim().split("\\|");// S|D
		for (int num = 0; num < merge.length; num++) {
			/* ?????????????????????Ip??????0??? ???????????????????????????Ip??????0 */
			if ((merge[num].equalsIgnoreCase("S") && cepevt.getSrcip() != 0 && alarmrule.getMerge().startsWith("S"))
					|| (merge[num].equalsIgnoreCase("D") && cepevt.getDstip() != 0
							&& alarmrule.getMerge().contains("D"))) {
				complex_key = MatchKeyUtil.getMatchKey4AlarmEventOfDefaultScene(cepevt, merge[num]);
				if (log.isDebugEnabled()) {
					log.debug("complex_key:{}", complex_key);
				}

				AlarmEvent alarmevt = MergeCacheManager.getInstance().getAlarm_event_complex().get(complex_key);
				// ?????????????????????????????????????????????
				if (alarmevt != null) {
					alarmEventId = alarmevt.getId();
					// alarmevent????????????
					mergeComplexEvent(complex_alarmrule, alarmrule, cepevt, alarmevt, complex_key, merge[num], attack);
					// alarm_log ??? alarm_event??????????????? alarm_event_log_r???????????????
					String key_event_log_r = alarmEventId + "/" + alarmLogId;
					if (null == MergeCacheManager.getInstance().getAlarm_event_log_rel().get(key_event_log_r)) {
						// ??????alarm???alarmlog?????????
						this.saveAlarmEventLogR(alarmevt.getId(), alarmLogId);
					}
				} else {
					alarmevt = createNewAlarm(cepevt, complex_alarmrule, alarmrule, complex_key, alarmLogId, 1,
							merge[num], attack);
				}
				// ???????????????????????????????????????,??????????????????
				if (Constant.TAG_CONFIRM.equals(alarmevt.getTag())
						|| Constant.TAG_CONFIRM_MULTI_DENY.equals(alarmevt.getTag())) {
					// ????????????IP????????????IP??????????????????IP??????
					addOrUpdateBadIp(alarmevt);
				}
			}
		}
	}

	// FIXME ??????????????????
	public void mergeScene(CEPEvent cepevt, AlarmRule scenerule, AlarmRule alarmrule, Long alarmLogId, boolean attack) {
		// ?????????????????????key
		String complex_key = MatchKeyUtil.getMatchKey4AlarmScene(scenerule, cepevt);
		if (log.isDebugEnabled()) {
			log.debug("complex_key:{}", complex_key);
		}

		AlarmEvent alarmevt = MergeCacheManager.getInstance().getAlarm_event_complex().get(complex_key);
		// ?????????????????????????????????????????????
		if (null != alarmevt) {
			// alarm_event_log_r??????
			String key_event_log_r = alarmevt.getId() + "/" + alarmLogId;
			if (null == MergeCacheManager.getInstance().getAlarm_event_log_rel().get(key_event_log_r)) {
				this.saveAlarmEventLogR(alarmevt.getId(), alarmLogId);
			}
			mergeComplexEvent(scenerule, alarmrule, cepevt, alarmevt, complex_key, null, attack);
		} else {
			createNewAlarm(cepevt, scenerule, alarmrule, complex_key, alarmLogId, 2, null, attack);
		}
	}

	/* ???????????????????????? */
	public AlarmEvent createNewAlarm(CEPEvent cepevt, AlarmRule complex_alarmrule, AlarmRule alarmrule,
			String complex_key, Long alarmLogId, int type, String mergeType, boolean attack) {
		AlarmEvent alarmevt = EventTran.cepToAlarm(cepevt, complex_alarmrule);
		// FIXME ??????????????????????????????cepToAlarm???,???????????????????????????????????????????????????
		// ?????????????????????alarm_c/s??????alarmevt???alarm_c/s
		alarmevt.setAlarmC(alarmrule.getC());
		alarmevt.setAlarmS(alarmrule.getS());
		alarmevt.setStage(alarmrule.getStage());
		alarmevt.setTag(cepevt.getTag());
		// 10???level
		alarmevt.setAlarmRuleLevel(alarmrule.getLevel());
		// ??????tag????????????
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
			// ????????????,??????cep????????????
			alarmevt.setLevel(cepevt.getCeplevel());
		}
		// ??????????????????cep????????????????????????????????????????????????????????????????????????
		if (attack) {
			// ?????????????????????????????????,cep?????????????????????,??????????????????
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

		if (mergeType == null) { // ???????????????,mergeType????????????????????????alarmRule???????????????
			mergeType = alarmrule.getMerge();
		}
		if (mergeType != null) {
			alarmevt.setMergeType(mergeType);
			// ?????????ip????????????????????????ip??????0????????????????????????ip
			if (mergeType.equalsIgnoreCase("S")) {
				if (attack) {
					String dstIp = "";
					if (cepevt.getDstip() != 0L) {
						dstIp = IpClassify.long2StrIp(cepevt.getDstip()) + ",";
					} else {
						dstIp = cepevt.getDstipv6() + ",";
					}
					// ????????????ipList
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
					// ????????????ipList
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

		// issendmail=1 ??????????????????????????????(????????????>=3)
		if (alarmevt.getLevel() >= MAIL_LEVEL) {
			alarmevt.setIssendmail("1");
		}
		// ??????alarm
		alarmEngine.submitTask(new AlarmEventTask(service, alarmevt));
		MergeCacheManager.getInstance().getAlarm_event_complex().put(complex_key, alarmevt);
		MergeCacheManager.getInstance().getAlarm_id_key().put(alarmevtId, complex_key);

		// ???????????????upgrade
		if (attack) {
			saveAlarmUpgrade(alarmevt,
					"????????????,??????" + getTagName(cepevt.getTag()) + "???????????????:"
							+ getAttackName(alarmrule.getC(), alarmrule.getS(), cepevt.getCeps(), cepevt.getCepf(),
									cepevt.getClazz(), cepevt.getSubclass(), cepevt.getFamily()),
					"INIT;ATTACK;");
		} else {
			saveAlarmUpgrade(alarmevt, "????????????", "INIT;");
		}

		// ??????alarm_event_log_r????????????
		if (alarmLogId != 0) {
			this.saveAlarmEventLogR(alarmevt.getId(), alarmLogId);
		}

		// ?????????alarm????????????
		long moveTime = cepevt.getEndtimeOfDate().getTime() - cepevt.getStarttimeOfDate().getTime();
		AlarmFrequency alarmfrequency = EventTran.cepToAlarmFrequency(cepevt, moveTime);
		alarmfrequency.setId(alarmevt.getId());
		alarmEngine.submitTask(new AlarmFrequencyTask(service, alarmfrequency));
		MergeCacheManager.getInstance().getAlarm_frequency_complex().put(complex_key, alarmfrequency);

		this.sendAlarm(alarmevt, cepevt, alarmrule, attack);
		return alarmevt;
	}

	/**
	 * ??????????????????
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
			// ???????????????,mergeType????????????????????????alarmRule???????????????
			if (mergeType == null) {
				mergeType = alarmrule.getMerge();
			}
			if (Constant.TAG_NORMAL.equals(cepevt.getTag())) {
				// ????????????,??????ceplevel??????,???????????????ceplevel????????????level
				if (Constant.TAG_NORMAL.equals(alarmevt.getTag())) {
					if (cepevt.getCeplevel() > alarmevt.getLevel()) {
						int beforeLevel = alarmevt.getLevel();
						alarmevt.setLevel(cepevt.getCeplevel());
						alarmevt.setAlarmRuleLevel(alarmrule.getLevel());
						String upgradeDesc = "???????????????????????????????????????,???????????????:"
								+ getCSFName(cepevt.getClazz(), cepevt.getSubclass(), cepevt.getFamily()) + ".?????????"
								+ beforeLevel + "????????????" + alarmevt.getLevel() + "???";
						alarmevt.setUpgradeTime(new Date());
						this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
					}
				}
			} else { // ??????/????????????
				// ????????????????????????
				if (alarmrule.getStage() >= alarmevt.getStage()) {
					alarmevt.setStage(alarmrule.getStage());
				}
				// ??????????????????????????????,??????????????????????????????CS?????????????????????tag
				if (alarmrule.getLevel() >= alarmevt.getAlarmRuleLevel()) {
					// ?????????????????????????????????,
					alarmevt.setAlarmC(alarmrule.getC());
					alarmevt.setAlarmS(alarmrule.getS());
					alarmevt.setAlarmRuleLevel(alarmrule.getLevel());
					alarmevt.setTag(cepevt.getTag());
					// ????????????????????????
					int beforeLevel = alarmevt.getLevel();
					// ??????tag????????????
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
						String upgradeDesc = "??????" + getTagName(cepevt.getTag()) + "???????????????:"
								+ getAttackName(alarmrule.getC(), alarmrule.getS(), cepevt.getCeps(), cepevt.getCepf(),
										cepevt.getClazz(), cepevt.getSubclass(), cepevt.getFamily())
								+ ",?????????" + beforeLevel + "????????????" + alarmevt.getLevel() + "???";
						alarmevt.setUpgradeTime(new Date());
						this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
					}
				}

			}
			// ??????????????????,?????????????????????????????????,??????upgrade
			this.mergeSrcOrDst(mergeType, alarmrule, cepevt, alarmevt, cepevt.getTag(), attack);
			// ??????????????????
			alarmevt.setStr1(JodaUtil.formatDate(new Date()));
			alarmevt.setCepNum(alarmevt.getCepNum() + 1);
			Date endTime = (cepevt.getEndtimeOfDate().after(alarmevt.getEndTime())) ? cepevt.getEndtimeOfDate()
					: alarmevt.getEndTime();
			alarmevt.setEndTime(endTime);
			alarmevt.setSensorMask(alarmevt.getSensorMask() | cepevt.getSensormask());

			// ??????direction????????????
			int updateDirection = alarmevt.getDirection() | DirectionUtil.GetDirection(cepevt);
			alarmevt.setDirection(updateDirection);

			// ??????alarmevent
			if (log.isDebugEnabled()) {
				log.debug("update alarmevt next step");
			}

			alarmEngine.submitTask(new AlarmUpdateTask(service, alarmevt, complex_alarmrule, complex_key, 2));
			MergeCacheManager.getInstance().getAlarm_event_complex().put(complex_key, alarmevt);

			// ????????????
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
			// ?????????ip???????????????
			if (mergeType.equalsIgnoreCase("S")) {
				// ??????????????????
				if (attack) {
					String cepTypeTemp = "cep_s=" + cepevt.getCeps() + ",cep_f=" + cepevt.getCepf() + ";";
					if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
						cepTypeTemp = "c=" + cepevt.getClazz() + ",s=" + cepevt.getSubclass() + ",f="
								+ cepevt.getFamily() + ";";
					}
					String cepType = alarmevt.getCepType() + ";";
					if (cepType.indexOf(cepTypeTemp) == -1) {
						// ????????????????????????,????????????
						cepType = cepTypeTemp + cepType;
						String cepTypeSubstr = cepType.substring(0, cepType.length() - 1);
						alarmevt.setCepType(cepTypeSubstr);
						alarmevt.setCeptypeCount(alarmevt.getCeptypeCount() + 1);
						String upgradeDesc = "??????" + getTagName(cepevt.getTag()) + "???????????????:"
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
								alarmevt.setAlarmRuleLevel(6); // ????????????,alarmrule?????????6???
								alarmevt.setLevel(3);
								if (beforeLevel < 3) {
									String upgradeDesc = "";
									if (alarmTypeCount == 3) {
										upgradeDesc = "????????????????????????????????????3,???" + beforeLevel + "????????????" + alarmevt.getLevel()
												+ "???";
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
								alarmevt.setAlarmRuleLevel(6); // ????????????,alarmrule?????????6???
								alarmevt.setLevel(3);
								if (beforeLevel < 3) {
									String upgradeDesc = "";
									if (alarmTypeCount == 3) {
										upgradeDesc = "?????????????????????????????????????????????3,???" + beforeLevel + "????????????" + alarmevt.getLevel()
												+ "???";
									}
									alarmevt.setUpgradeTime(new Date());
									this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
								}
							}
						}

					}
					// ????????????ipList
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
								// ??????ip??????
								String suspiciousDstIps = alarmevt.getSuspiciousDstList();
								int suspiciousDstIpCount = suspiciousDstIps == null ? 0
										: suspiciousDstIps.split(",").length;
								if (suspiciousDstIpCount == 50) {
									int beforeLevel = alarmevt.getLevel();
									alarmevt.setAlarmRuleLevel(6); // ????????????,alarmrule?????????6???
									alarmevt.setLevel(3);
									if (beforeLevel < 3) {
										String upgradeDesc = "";
										if (suspiciousDstIpCount == 50) {
											upgradeDesc = "??????????????????????????????IP??????????????????50,???" + beforeLevel + "????????????"
													+ alarmevt.getLevel() + "???";
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
									alarmevt.setAlarmRuleLevel(5); // ??????????????????,alarmrulelevel?????????5
									if (beforeLevel < 3) {
										String upgradeDesc = "";
										if (denyDstIpCount == 50) {
											upgradeDesc = "???????????????????????????????????????IP??????????????????50,???" + beforeLevel + "????????????"
													+ alarmevt.getLevel() + "???";
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
						// ????????????????????????,????????????
						cepType = cepTypeTemp + cepType;
						String cepTypeSubstr = cepType.substring(0, cepType.length() - 1);
						alarmevt.setCepType(cepTypeSubstr);
						alarmevt.setCeptypeCount(alarmevt.getCeptypeCount() + 1);
						String upgradeDesc = "??????" + getTagName(cepevt.getTag()) + "???????????????:"
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
								alarmevt.setAlarmRuleLevel(6); // ????????????,alarmrule?????????6???
								alarmevt.setLevel(3);
								if (beforeLevel < 3) {
									String upgradeDesc = "";
									if (alarmTypeCount == 3) {
										upgradeDesc = "????????????????????????????????????3,???" + beforeLevel + "????????????" + alarmevt.getLevel()
												+ "???";
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
								alarmevt.setAlarmRuleLevel(6); // ????????????,alarmrule?????????6???
								alarmevt.setLevel(3);
								if (beforeLevel < 3) {
									String upgradeDesc = "";
									if (alarmTypeCount == 3) {
										upgradeDesc = "?????????????????????????????????????????????3,???" + beforeLevel + "????????????" + alarmevt.getLevel()
												+ "???";
									}
									alarmevt.setUpgradeTime(new Date());
									this.saveAlarmUpgrade(alarmevt, upgradeDesc, "UPGRADE;");
								}
							}
						}

					}
					// ????????????ipList
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
								// ??????ip??????
								String suspiciousSrcIps = alarmevt.getSuspiciousSrcList();
								int suspiciousSrcIpCount = suspiciousSrcIps == null ? 0
										: suspiciousSrcIps.split(",").length;
								if (suspiciousSrcIpCount == 50) {
									int beforeLevel = alarmevt.getLevel();
									alarmevt.setAlarmRuleLevel(6); // ????????????,alarmrule?????????6???
									alarmevt.setLevel(3);
									if (beforeLevel < 3) {
										String upgradeDesc = "";
										if (suspiciousSrcIpCount == 50) {
											upgradeDesc = "????????????????????????IP??????????????????50,???" + beforeLevel + "????????????"
													+ alarmevt.getLevel() + "???";
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
									alarmevt.setAlarmRuleLevel(5); // ??????????????????,alarmrulelevel?????????5
									if (beforeLevel < 3) {
										String upgradeDesc = "";
										if (denySrcIpCount == 50) {
											upgradeDesc = "????????????????????????IP??????????????????50,???" + beforeLevel + "????????????"
													+ alarmevt.getLevel() + "???";
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
				// TODO ??????
			}
		}
	}

	private void addOrUpdateBadIp(AlarmEvent alarmEvent) {
		// TODO

	}

	public void suspiciousIpAnaly(CEPEvent cepEvent) {
		// TODO
	}

	/* ??????AlarmLog */
	public void mergeAlarmLog(AlarmLog alarmlog, CEPEvent cepevt, String alarmLogMerge) {
		int count = alarmlog.getCepNum();
		// ????????????????????????direction??????
		int updateDirection = alarmlog.getDirection() | DirectionUtil.GetDirection(cepevt);
		alarmlog.setCepNum(++count);
		// cep?????????endtime??????alarmlog???endtime?????????alarmlog???endtime
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
			// ???????????????????????????,????????????????????????????????????????????????
		}

		if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
			// ??????2-0??????,???cepevent???csf?????????describe???
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
	 * alarmlog???????????????
	 * 
	 * @param cepevt
	 * @param getinfo
	 * @param alarmrule
	 * @return
	 */
	private long createOrMergeAlarmLog(CEPEvent cepevt, AlarmRule alarmrule) {
		long alarmLogId = 0L;
		AlarmLog alarmLog;
		// cepevt??????Ip??????0???????????????/??????AlarmLog??? cepevt?????????Ip??????0??????????????????/??????AlarmLog
		String alarmlog_key = getMatchKeyOfAlarmlog(cepevt, alarmrule, alarmrule.getMerge());
		if (log.isDebugEnabled()) {
			log.debug("alarmlog_key:{}; merge:{}; SrcIpv4:{}; DstIpv4:{}", alarmlog_key, alarmrule.getMerge(),
					cepevt.getSrcip(), cepevt.getDstip());
		}
		alarmLog = MergeCacheManager.getInstance().getAlarm_event_statis().get(alarmlog_key);
		/* ???????????????????????????????????? */
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
			// ??????2-0??????,???cepevent???csf?????????describe???
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
	 * ??????????????????????????????????????????????????????????????????
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

		int i = subDescrib.indexOf("???");
		int j = subDescrib.length() - count;
		String[] describArr = subDescrib.split("???");
		k = j % i == 0 ? j / i : j / i + 1;
		k = k > (describArr.length - 1) ? describArr.length - 1 : k;

		int x = 0;
		for (String describStr : describArr) {
			if (x >= k) {
				describList.add(describStr);
			}
			x++;
		}

		String str = describList.toString().replace(",", "???").replace(" ", "");

		return prefixdesc + "???" + str.substring(1, str.length() - 1);
	}

	/**
	 * ??????CEP????????????????????????
	 * 
	 * @param cepevt
	 * @param desc   INIT ?????? UPGRADE ?????? ATTACK ??????????????????
	 * @return
	 */

	public Long saveAlarmUpgrade(AlarmEvent alarmevt, String upgradeDesc, String desc) {
		AlarmUpgrade alarmUpgrade = EventTran.alarmevtToUpgrade(alarmevt);
		alarmUpgrade.setUpgradeTime(new Date());
		alarmUpgrade.setUpgradeDesc(upgradeDesc);
		alarmUpgrade.setDescrib(desc);
		alarmUpgrade.setTag(alarmevt.getTag());
		alarmEngine.submitTask(new AlarmUpgradeTask(service, alarmUpgrade));
		// TODO ??????????????????
		return alarmUpgrade.getId();
	}

	private void saveAlarmEventLogR(Long alarmEventId, Long alarmLogId) {
		AlarmEventLogR alarmEventLogRel = new AlarmEventLogR();
		alarmEventLogRel.setAlarmEventId(alarmEventId);
		alarmEventLogRel.setAlarmLogId(alarmLogId);
		// alarmEvent-alarmLog ???????????????alarmEvent??????CEPEvent
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
			return "?????????????????????";
		} else if (Constant.TAG_QDKY.equals(tag)) {
			return "????????????";
		} else if (Constant.TAG_GDKY.equals(tag)) {
			return "????????????";
		} else if (Constant.TAG_CONFIRM.equals(tag)) {
			return "??????";
		} else if (Constant.TAG_CONFIRM_DENY.equals(tag)) {
			return "??????";
		} else if (Constant.TAG_CONFIRM_MULTI_DENY.equals(tag)) {
			return "????????????";
		}
		return tag;
	}

	private static String getMatchKeyOfAlarmlog(CEPEvent cepevt, AlarmRule alarmrule, String merge) {
		String match_key = null;
		if ("SD".equalsIgnoreCase(merge.trim())) {
			match_key = "alarm_c=" + alarmrule.getC() + ",alarm_s=" + alarmrule.getS() + ",sip="
					+ IpClassify.long2StrIp(cepevt.getSrcip()) + ",dip=" + IpClassify.long2StrIp(cepevt.getDstip())
					+ "srcorg_id=" + cepevt.getSrcorgid() + ",dstorg_id=" + cepevt.getDstorgid();
			// ??????CEP???????????????
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
			// ??????customid???platformid
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
