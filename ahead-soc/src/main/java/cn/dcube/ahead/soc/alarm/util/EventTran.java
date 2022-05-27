package cn.dcube.ahead.soc.alarm.util;

import java.math.BigDecimal;
import java.util.Date;

import org.joou.UInteger;

import cn.dcube.ahead.soc.alarm.model.AlarmEvent;
import cn.dcube.ahead.soc.alarm.model.AlarmFrequency;
import cn.dcube.ahead.soc.alarm.model.AlarmLog;
import cn.dcube.ahead.soc.alarm.model.AlarmRule;
import cn.dcube.ahead.soc.alarm.model.AlarmUpgrade;
import cn.dcube.ahead.soc.cep.model.CEPEvent;
import cn.dcube.goku.commons.util.JodaUtil;

/**
 * 
 * 描述：
 * <p>
 * 事件转换类
 * </p>
 * 创建日期：2013-6-21 下午1:02:19<br>
 * 
 * @author：daihaiyan<br>
 * @update：$Date$<br>
 * @version：$Revision$<br>
 * 
 * @since 3.1.0
 */
public class EventTran {

	/**
	 * CEP对象转换为AlarmLog
	 * 
	 * @param cepEvent
	 * @param alarmrule
	 * @param direction
	 * @return
	 */
	public static AlarmLog cepToAlarmLog(CEPEvent cepEvent, AlarmRule alarmrule, int direction) {
		AlarmLog alarmLog = new AlarmLog();
		alarmLog.setPlatformid(cepEvent.getPlatformid());
		alarmLog.setCustomerId(cepEvent.getCustomerid());
		alarmLog.setOrgId(cepEvent.getOrgid());
		alarmLog.setMerge(alarmrule.getMerge());
		alarmLog.setAlarmC(alarmrule.getC());
		alarmLog.setAlarmS(alarmrule.getS());
		alarmLog.setLevel(cepEvent.getCeplevel());
		alarmLog.setSrcIpv4(cepEvent.getSrcip());
		alarmLog.setSrcIpv6(cepEvent.getSrcipv6());
		alarmLog.setSrcIn(cepEvent.getSrcin());
		alarmLog.setSrcOrgId(cepEvent.getSrcorgid());
		alarmLog.setSrcAssetid(cepEvent.getSrcassetid());
		alarmLog.setSrcAssetLevel(cepEvent.getSrcassetlevel());
		alarmLog.setSrcBusiId(cepEvent.getSrcbusiid());
		alarmLog.setSrcBusiLink(cepEvent.getSrcbusilink());
		alarmLog.setDstIpv4(cepEvent.getDstip());
		alarmLog.setDstIpv6(cepEvent.getDstipv6());
		alarmLog.setDstIn(cepEvent.getDstin());
		alarmLog.setDstOrgId(cepEvent.getDstorgid());
		alarmLog.setDstAssetid(cepEvent.getDstassetid());
		alarmLog.setDstAssetLevel(cepEvent.getDstassetlevel());
		alarmLog.setDstBusiId(cepEvent.getDstbusiid());
		alarmLog.setDstBusiLink(cepEvent.getDstbusilink());
		if (null != cepEvent.getStarttime() && !"".equals(cepEvent.getStarttime())) {
			alarmLog.setStartTime(JodaUtil.parseStrToDateSimple(cepEvent.getStarttime()));
		}
		if (null != cepEvent.getEndtime() && !"".equals(cepEvent.getEndtime())) {
			alarmLog.setEndTime(JodaUtil.parseStrToDateSimple(cepEvent.getEndtime()));
		}
		alarmLog.setCepNum(1);
		alarmLog.setDirection(direction);
		alarmLog.setStage(alarmrule.getStage());
		alarmLog.setImpact(0);
		alarmLog.setSensorMask(cepEvent.getSensormask());
		alarmLog.setSensorModel(cepEvent.getSensormodel());
		alarmLog.setDescrib(cepEvent.getInfo());
		alarmLog.setHoleMark(cepEvent.getHolemark());
		alarmLog.setBadSrcIp(cepEvent.getBadsrcip());
		alarmLog.setBadDstIp(cepEvent.getBaddstip());
		alarmLog.setBadUrl(cepEvent.getBadurl());
		alarmLog.setBadCode(cepEvent.getBadcode());
		alarmLog.setBadMailFrom(cepEvent.getBadmailfrom());
		alarmLog.setBadMailTo(cepEvent.getBadmailto());
		alarmLog.setBadMailCc(cepEvent.getBadmailcc());
		alarmLog.setBadMailBcc(cepEvent.getBadmailbcc());
		alarmLog.setBadOther(cepEvent.getBadother());

		alarmLog.setSrcGeo1Code(cepEvent.getSrcGeo1Code());
		alarmLog.setSrcGeo1Name(cepEvent.getSrcGeo1Name());
		alarmLog.setSrcGeo2Code(cepEvent.getSrcGeo2Code());
		alarmLog.setSrcGeo2Name(cepEvent.getSrcGeo2Name());
		alarmLog.setSrcGeo3Code(cepEvent.getSrcGeo3Code());
		alarmLog.setSrcGeo3Name(cepEvent.getSrcGeo3Name());
		alarmLog.setDstGeo1Code(cepEvent.getDstGeo1Code());
		alarmLog.setDstGeo1Name(cepEvent.getDstGeo1Name());
		alarmLog.setDstGeo2Code(cepEvent.getDstGeo2Code());
		alarmLog.setDstGeo2Name(cepEvent.getDstGeo2Name());
		alarmLog.setDstGeo3Code(cepEvent.getDstGeo3Code());
		alarmLog.setDstGeo3Name(cepEvent.getDstGeo3Name());
		alarmLog.setSrcLatitude(cepEvent.getSrcLatitude());
		alarmLog.setSrcLongitude(cepEvent.getSrcLongitude());
		alarmLog.setDstLatitude(cepEvent.getDstLatitude());
		alarmLog.setDstLongitude(cepEvent.getDstLongitude());
		return alarmLog;
	}

	/**
	 * CEP对象转换为告警对象
	 * 
	 * @param cepevt
	 * @param alarmrule
	 * @return
	 */
	public static AlarmEvent cepToAlarm(CEPEvent cepevt, AlarmRule alarmrule) {
		AlarmEvent alarmEvent = new AlarmEvent();
		alarmEvent.setPlatformid(cepevt.getPlatformid());
		alarmEvent.setOrgId(cepevt.getOrgid());
		alarmEvent.setCustomerId(cepevt.getCustomerid());
		alarmEvent.setRuleId(alarmrule.getRuleId());
		alarmEvent.setLevel(cepevt.getCeplevel());
		alarmEvent.setSrcIpv4(cepevt.getSrcip());
		alarmEvent.setSrcIpv6(cepevt.getSrcipv6());
		alarmEvent.setSrcOrgId(cepevt.getSrcorgid());
		alarmEvent.setSrcIn(cepevt.getSrcin());
		alarmEvent.setSrcAssetid(cepevt.getSrcassetid());
		alarmEvent.setSrcAssetLevel(cepevt.getSrcassetlevel());
		alarmEvent.setSrcBusiId(cepevt.getSrcbusiid());
		alarmEvent.setSrcBusiLink(cepevt.getSrcbusilink());
		alarmEvent.setDstIpv4(cepevt.getDstip());
		alarmEvent.setDstIpv6(cepevt.getDstipv6());
		alarmEvent.setDstOrgId(cepevt.getDstorgid());
		alarmEvent.setDstIn(cepevt.getDstin());
		alarmEvent.setDstAssetid(cepevt.getDstassetid());
		alarmEvent.setDstAssetLevel(cepevt.getDstassetlevel());
		alarmEvent.setDstBusiId(cepevt.getDstbusiid());
		alarmEvent.setDstBusiLink(cepevt.getDstbusilink());
		alarmEvent.setAlarmC(alarmrule.getC());
		alarmEvent.setAlarmS(alarmrule.getS());
		if (null != cepevt.getStarttime() && !"".equals(cepevt.getStarttime())) {
			alarmEvent.setStartTime(JodaUtil.parseStrToDateSimple(cepevt.getStarttime()));
		}
		if (null != cepevt.getEndtime() && !"".equals(cepevt.getEndtime())) {
			alarmEvent.setEndTime(JodaUtil.parseStrToDateSimple(cepevt.getEndtime()));
		}
		alarmEvent.setCepNum(1);
		alarmEvent.setState(1);
		if (null != cepevt.getEndtime() && !"".equals(cepevt.getEndtime())) {
			alarmEvent.setUpgradeTime(JodaUtil.parseStrToDateSimple(cepevt.getEndtime()));
		}
		// 更新updatetime
		alarmEvent.setStr1(JodaUtil.dateToStr(new Date()));
		String cepType = "cep_s=" + cepevt.getCeps() + ",cep_f=" + cepevt.getCepf();
		if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
			cepType = "c=" + cepevt.getClazz() + ",s=" + cepevt.getSubclass() + ",f=" + cepevt.getFamily();
		}
		alarmEvent.setCepType(cepType);
		// 只统计具体的cep事件类型，默认统计输出的事件类型，不作为统计对象
		if (cepevt.getCeps() != 2 || cepevt.getCepf() != 0) {
			alarmEvent.setCeptypeCount(1);
		}
		alarmEvent.setDescrib(alarmrule.getDescrib());
		alarmEvent.setAbility(alarmrule.getHit());
		alarmEvent.setImpact(0);
		alarmEvent.setDirection(DirectionUtil.GetDirection(cepevt));
		alarmEvent.setStage(alarmrule.getStage());
		alarmEvent.setSensorMask(cepevt.getSensormask());
		alarmEvent.setSensorModel(cepevt.getSensormodel());
		alarmEvent.setSensorId(cepevt.getSensorid());
		alarmEvent.setSensorIp(cepevt.getSensorip());
		alarmEvent.setIscontinuous("0");
		alarmEvent.setIssendmail("0");
		alarmEvent.setIsupholemark("0");
		alarmEvent.setIsupbadip("0");
		alarmEvent.setIsupbadurl("0");
		alarmEvent.setIsupbadcode("0");
		alarmEvent.setIsupbadmail("0");
		alarmEvent.setIsupbadother("0");
		alarmEvent.setIsupasset("0");
		alarmEvent.setIsupbusiness("0");
		// 地理位置
		alarmEvent.setSrcGeo1Code(cepevt.getSrcGeo1Code());
		alarmEvent.setSrcGeo1Name(cepevt.getSrcGeo1Name());
		alarmEvent.setSrcGeo2Code(cepevt.getSrcGeo2Code());
		alarmEvent.setSrcGeo2Name(cepevt.getSrcGeo2Name());
		alarmEvent.setSrcGeo3Code(cepevt.getSrcGeo3Code());
		alarmEvent.setSrcGeo3Name(cepevt.getSrcGeo3Name());
		alarmEvent.setDstGeo1Code(cepevt.getDstGeo1Code());
		alarmEvent.setDstGeo1Name(cepevt.getDstGeo1Name());
		alarmEvent.setDstGeo2Code(cepevt.getDstGeo2Code());
		alarmEvent.setDstGeo2Name(cepevt.getDstGeo2Name());
		alarmEvent.setDstGeo3Code(cepevt.getDstGeo3Code());
		alarmEvent.setDstGeo3Name(cepevt.getDstGeo3Name());
		alarmEvent.setSrcLatitude(cepevt.getSrcLatitude());
		alarmEvent.setSrcLongitude(cepevt.getSrcLongitude());
		alarmEvent.setDstLatitude(cepevt.getDstLatitude());
		alarmEvent.setDstLongitude(cepevt.getDstLongitude());

		return alarmEvent;
	}

	/**
	 * 计算告警的攻击频率
	 * 
	 * @param cepevt
	 * @param moveTime
	 * @return
	 */
	public static AlarmFrequency cepToAlarmFrequency(CEPEvent cepevt, long moveTime) {
		float freq = 0;
		AlarmFrequency alarmfrequency = new AlarmFrequency();
		if (moveTime != 0) {
			freq = getFloatDivVal(cepevt.getEventnum(), moveTime);
		}
		alarmfrequency.setMax(freq);
		alarmfrequency.setMin(freq);
		alarmfrequency.setAvg(freq);
		alarmfrequency.setTotal(freq);
		alarmfrequency.setOutput(1);
		alarmfrequency.setIntime(new Date());
		return alarmfrequency;
	}

	/**
	 * 更新
	 * 
	 * @param cepevt
	 * @param alarmfrequency
	 * @param moveTime
	 * @return
	 */
	public static AlarmFrequency CompareToAlarmFrequency(CEPEvent cepevt, AlarmFrequency alarmfrequency,
			long moveTime) {
		float freq = 0;
		float max = alarmfrequency.getMax();
		float min = alarmfrequency.getMin();
		float avg = alarmfrequency.getAvg();
		float total = alarmfrequency.getTotal();
		int output = alarmfrequency.getOutput();
		if (moveTime != 0) {
			freq = getFloatDivVal(cepevt.getEventnum(), moveTime);
		}
		max = (freq > max) ? freq : max;
		min = (freq < min) ? freq : min;
		total = getFloatAddVal(total, freq);
		output += 1;
		alarmfrequency.setMax(max);
		alarmfrequency.setMin(min);
		avg = getFloatDivVal(total, output);
		alarmfrequency.setAvg(avg);
		alarmfrequency.setTotal(total);
		alarmfrequency.setOutput(output);
		alarmfrequency.setIntime(new Date());
		return alarmfrequency;
	}

	/**
	 * alarmEvent转换为升级对象
	 * 
	 * @param alarmEvent
	 * @return
	 */
	public static AlarmUpgrade alarmevtToUpgrade(AlarmEvent alarmEvent) {
		AlarmUpgrade alarmUpgrade = new AlarmUpgrade();
		alarmUpgrade.setPlatformid(alarmEvent.getPlatformid());
		alarmUpgrade.setOrgId(alarmEvent.getOrgId());
		alarmUpgrade.setAlarmEventId(alarmEvent.getId());
		alarmUpgrade.setCustomerId(alarmEvent.getCustomerId());
		alarmUpgrade.setRuleId(alarmEvent.getRuleId());
		alarmUpgrade.setLevel(alarmEvent.getLevel());
		alarmUpgrade.setSrcIpv4(UInteger.valueOf(alarmEvent.getSrcIpv4()).longValue());
		alarmUpgrade.setSrcIpv6(alarmEvent.getSrcIpv6());
		alarmUpgrade.setSrcOrgId(alarmEvent.getSrcOrgId());
		alarmUpgrade.setSrcIn(alarmEvent.getSrcIn());
		alarmUpgrade.setSrcAssetid(alarmEvent.getSrcAssetid());
		alarmUpgrade.setSrcAssetLevel(alarmEvent.getSrcAssetLevel());
		alarmUpgrade.setSrcBusiId(alarmEvent.getSrcBusiId());
		alarmUpgrade.setSrcBusiLink(alarmEvent.getSrcBusiLink());
		alarmUpgrade.setDstIpv4(UInteger.valueOf(alarmEvent.getDstIpv4()).longValue());
		alarmUpgrade.setDstIpv6(alarmEvent.getDstIpv6());
		alarmUpgrade.setDstOrgId(alarmEvent.getDstOrgId());
		alarmUpgrade.setDstIn(alarmEvent.getDstIn());
		alarmUpgrade.setDstAssetid(alarmEvent.getDstAssetid());
		alarmUpgrade.setDstAssetLevel(alarmEvent.getDstAssetLevel());
		alarmUpgrade.setDstBusiId(alarmEvent.getDstBusiId());
		alarmUpgrade.setDstBusiLink(alarmEvent.getDstBusiLink());
		alarmUpgrade.setAlarmC(alarmEvent.getAlarmC());
		alarmUpgrade.setAlarmS(alarmEvent.getAlarmS());
		alarmUpgrade.setStartTime(alarmEvent.getStartTime());
		alarmUpgrade.setEndTime(alarmEvent.getEndTime());
		alarmUpgrade.setCepNum(alarmEvent.getCepNum());
		alarmUpgrade.setState(alarmEvent.getState());
		alarmUpgrade.setUpgradeTime(alarmEvent.getUpgradeTime());
		alarmUpgrade.setCepType(alarmEvent.getCepType());
		alarmUpgrade.setCeptypeCount(alarmEvent.getCeptypeCount());
		alarmUpgrade.setDescrib("");
		alarmUpgrade.setAbility(alarmEvent.getAbility());
		alarmUpgrade.setImpact(alarmEvent.getImpact());
		alarmUpgrade.setDirection(alarmEvent.getDirection());
		alarmUpgrade.setStage(alarmEvent.getStage());
		alarmUpgrade.setSensorMask(alarmEvent.getSensorMask());
		alarmUpgrade.setSensorModel(alarmEvent.getSensorModel());
		alarmUpgrade.setSensorId(alarmEvent.getSensorId());
		alarmUpgrade.setSensorIp(alarmEvent.getSensorIp());
		alarmUpgrade.setIscontinuous(alarmEvent.getIscontinuous());
		alarmUpgrade.setIpCount(alarmEvent.getIpCount());
		alarmUpgrade.setSensorCount(alarmEvent.getSensorCount());
		alarmUpgrade.setConfidence(alarmEvent.getConfidence());
		alarmUpgrade.setRelevancy(alarmEvent.getRelevancy());
		alarmUpgrade.setIssendmail(alarmEvent.getIssendmail());
		alarmUpgrade.setIsupholemark(alarmEvent.getIsupholemark());
		alarmUpgrade.setIsupbadip(alarmEvent.getIsupbadip());
		alarmUpgrade.setIsupbadurl(alarmEvent.getIsupbadurl());
		alarmUpgrade.setIsupbadcode(alarmEvent.getIsupbadcode());
		alarmUpgrade.setIsupbadmail(alarmEvent.getIsupbadmail());
		alarmUpgrade.setIsupbadother(alarmEvent.getIsupbadother());
		alarmUpgrade.setIsupasset(alarmEvent.getIsupasset());
		alarmUpgrade.setIsupbusiness(alarmEvent.getIsupbusiness());
		alarmUpgrade.setNum1(alarmEvent.getNum1());
		alarmUpgrade.setNum2(alarmEvent.getNum2());
		alarmUpgrade.setNum3(alarmEvent.getNum3());
		alarmUpgrade.setNum4(alarmEvent.getNum4());
		alarmUpgrade.setNum5(alarmEvent.getNum5());
		alarmUpgrade.setStr1(alarmEvent.getStr1());
		alarmUpgrade.setStr2(alarmEvent.getStr2());
		alarmUpgrade.setStr3(alarmEvent.getStr3());
		alarmUpgrade.setStr4(alarmEvent.getStr4());
		alarmUpgrade.setStr5(alarmEvent.getStr5());

		return alarmUpgrade;
	}


	public static float getFloatDivVal(Integer eventnum, long time) {
		float result = eventnum.floatValue();
		if (time > 60) {
			eventnum = eventnum * 60;
			BigDecimal a = new BigDecimal(Integer.toString(eventnum));
			BigDecimal b = new BigDecimal(time);
			result = a.divide(b, 2, BigDecimal.ROUND_HALF_DOWN).floatValue();
		}
		return result;
	}

	public static float getFloatDivVal(Float total, Integer output) {
		BigDecimal a = new BigDecimal(Float.toString(total));
		BigDecimal b = new BigDecimal(Integer.toString(output));
		return a.divide(b, 2, BigDecimal.ROUND_HALF_DOWN).floatValue();
	}

	public static float getFloatAddVal(float x, float y) {
		BigDecimal a = new BigDecimal(Float.toString(x));
		BigDecimal b = new BigDecimal(Float.toString(y));
		return a.add(b).floatValue();
	}

	// 判断是否攻击事件
	public static boolean isAttackEvent(CEPEvent cep) {
		if (cep.getCeps() == 2 && cep.getCepf() == 0) {
			if (cep.getClazz() == 3 && cep.getSubclass() == 4) {
				return true;
			}
			return false;
		}
		return true;
	}

}
