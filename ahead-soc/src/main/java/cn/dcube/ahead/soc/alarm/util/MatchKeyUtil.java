package cn.dcube.ahead.soc.alarm.util;

import cn.dcube.ahead.soc.alarm.model.AlarmEvent;
import cn.dcube.ahead.soc.alarm.model.AlarmRule;
import cn.dcube.ahead.soc.cep.model.CEPEvent;
import cn.dcube.goku.commons.ip.IpClassify;

/**
 * 
 * 描述：
 * <p>
 * 获取信息类
 * </p>
 * 创建日期：2014-3-25 上午9:25:48<br>
 * 
 * @author：dai_hyan<br>
 * @update：$Date$<br>
 * @version：$Revision$<br>
 * 
 * @since 3.1.0
 */
public class MatchKeyUtil {

	/**
	 * 场景规则key
	 * 
	 * @param sceneRule
	 * @param cepevt
	 * @param merge
	 * @return
	 */
	public static String getMatchKey4AlarmScene(AlarmRule sceneRule, CEPEvent cepevt) {
		String merge = sceneRule.getMerge();
		String match_key = null;
		if ("SD".equalsIgnoreCase(merge.trim())) {
			match_key = "c=" + sceneRule.getC() + ",s=" + sceneRule.getS() + ",sip="
					+ IpClassify.long2StrIp(cepevt.getSrcip()) + ",dip=" + IpClassify.long2StrIp(cepevt.getDstip())
					+ ",srcorg_id=" + cepevt.getSrcorgid() + ",dstorg_id=" + cepevt.getDstorgid();
			if (cepevt.getCeps() == 2 && cepevt.getSrcorgid() == 0) {
				match_key = "c=" + cepevt.getClazz() + ",s=" + cepevt.getSubclass() + ",f=" + cepevt.getFamily()
						+ ",sip=" + IpClassify.long2StrIp(cepevt.getSrcip()) + ",dip="
						+ IpClassify.long2StrIp(cepevt.getDstip()) + ",srcorg_id=" + cepevt.getSrcorgid()
						+ ",dstorg_id=" + cepevt.getDstorgid();
			}
		} else if ("S".equalsIgnoreCase(merge.trim())) {
			match_key = "c=" + sceneRule.getC() + ",s=" + sceneRule.getS() + ",sip="
					+ IpClassify.long2StrIp(cepevt.getSrcip()) + ",srcorg_id=" + cepevt.getSrcorgid();
			if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
				match_key = "c=" + cepevt.getClazz() + ",s=" + cepevt.getSubclass() + ",f=" + cepevt.getFamily()
						+ ",sip=" + IpClassify.long2StrIp(cepevt.getSrcip()) + ",srcorg_id=" + cepevt.getSrcorgid();
			}
		} else if ("D".equalsIgnoreCase(merge.trim())) {
			match_key = "c=" + sceneRule.getC() + ",s=" + sceneRule.getS() + ",dip="
					+ IpClassify.long2StrIp(cepevt.getDstip()) + ",dstorg_id=" + cepevt.getDstorgid();
			if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
				match_key = "c=" + cepevt.getClazz() + ",s=" + cepevt.getSubclass() + ",f=" + cepevt.getFamily()
						+ ",dip=" + IpClassify.long2StrIp(cepevt.getDstip()) + ",dstorg_id=" + cepevt.getDstorgid();
			}
		} else {
			match_key = "c=" + sceneRule.getC() + ",s=" + sceneRule.getS() + ",srcorg_id=" + cepevt.getSrcorgid()
					+ ",dstorg_id=" + cepevt.getDstorgid();
		}
		if (match_key != null) {
			// 增加customid和platformid
			match_key += ",customid=" + cepevt.getCustomerid();
			match_key += ",platformid=" + cepevt.getPlatformid();
		}
		return match_key;
	}

	/**
	 * 默认场景聚合时的key
	 * 
	 * @param alarmrule
	 * @param cepevt
	 * @param keytype
	 * @param merge
	 * @return
	 */
	public static String getMatchKey4AlarmEventOfDefaultScene(CEPEvent cepevt, String merge) {
		String match_key = "c=2" + ",s=0" + ",sip=" + IpClassify.long2StrIp(cepevt.getSrcip()) + ",dip="
				+ IpClassify.long2StrIp(cepevt.getDstip()) + ",srcorg_id=" + cepevt.getSrcorgid() + ",dstorg_id="
				+ cepevt.getDstorgid();
		if ("SD".equalsIgnoreCase(merge.trim())) {
			match_key = "c=2" + ",s=0" + ",sip=" + IpClassify.long2StrIp(cepevt.getSrcip()) + ",dip="
					+ IpClassify.long2StrIp(cepevt.getDstip()) + ",srcorg_id=" + cepevt.getSrcorgid() + ",dstorg_id="
					+ cepevt.getDstorgid();
		} else if ("S".equalsIgnoreCase(merge.trim())) {
			match_key = "c=2" + ",s=0" + ",sip=" + IpClassify.long2StrIp(cepevt.getSrcip()) + ",srcorg_id="
					+ cepevt.getSrcorgid();
		} else if ("D".equalsIgnoreCase(merge.trim())) {
			match_key = "c=2" + ",s=0" + ",dip=" + IpClassify.long2StrIp(cepevt.getDstip()) + ",dstorg_id="
					+ cepevt.getDstorgid();
		}
		if (match_key != null) {
			// 增加customid和platformid
			match_key += ",customid=" + cepevt.getCustomerid();
			match_key += ",platformid=" + cepevt.getPlatformid();
			// match_key += ",org_id=" + cepevt.getOrgId();
		}
		return match_key;
	}

	/**
	 * alarmLog聚合时的key.默认事件时，使用csf
	 * 
	 * @param cepevt
	 * @param merge
	 * @return
	 */
	public static String getMatchKeyOfAlarmlog(CEPEvent cepevt, String merge) {
		String match_key = null;
		if ("SD".equalsIgnoreCase(merge.trim())) {
			match_key = "cep_s=" + cepevt.getCeps() + ",cep_f=" + cepevt.getCepf() + ",sip="
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
			match_key = "cep_s=" + cepevt.getCeps() + ",cep_f=" + cepevt.getCepf() + ",sip="
					+ IpClassify.long2StrIp(cepevt.getSrcip()) + ",srcorg_id=" + cepevt.getDstorgid();
			if (cepevt.getCeps() == 2 && cepevt.getCepf() == 0) {
				match_key = "c=" + cepevt.getClazz() + ",s=" + cepevt.getSubclass() + ",f=" + cepevt.getFamily()
						+ ",sip=" + IpClassify.long2StrIp(cepevt.getSrcip()) + ",srcorg_id=" + cepevt.getDstorgid();
			}
		} else if ("D".equalsIgnoreCase(merge.trim())) {
			match_key = "cep_s=" + cepevt.getCeps() + ",cep_f=" + cepevt.getCepf() + ",dip="
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

	/**
	 * 告警加载时的key
	 * 
	 * @param alarmEvent
	 * @param merge
	 * @return
	 */
	public static String getMatchKey4AlarmLoad(AlarmEvent alarmEvent) {
		int alarmC = alarmEvent.getAlarmC();
		int alarmS = alarmEvent.getAlarmS();
		if (alarmEvent.getRuleId() == 36) {
			// FIXME
			alarmC = 2;
			alarmS = 0;
		}
		String merge = alarmEvent.getMergeType();
		String match_key = "c=" + alarmC + ",s=" + alarmS + ",sip=" + IpClassify.long2StrIp(alarmEvent.getSrcIpv4())
				+ ",dip=" + IpClassify.long2StrIp(alarmEvent.getDstIpv4()) + ",srcorg_id=" + alarmEvent.getSrcOrgId()
				+ ",dstorg_id=" + alarmEvent.getDstOrgId();
		if ("SD".equalsIgnoreCase(merge.trim())) {
			match_key = "c=" + alarmC + ",s=" + alarmS + ",sip=" + IpClassify.long2StrIp(alarmEvent.getSrcIpv4()) + ",dip="
					+ IpClassify.long2StrIp(alarmEvent.getDstIpv4()) + ",srcorg_id=" + alarmEvent.getSrcOrgId()
					+ ",dstorg_id=" + alarmEvent.getDstOrgId();
		} else if ("S".equalsIgnoreCase(merge.trim())) {
			match_key = "c=" + alarmC + ",s=" + alarmS + ",sip=" + IpClassify.long2StrIp(alarmEvent.getSrcIpv4())
					+ ",srcorg_id=" + alarmEvent.getSrcOrgId();
		} else if ("D".equalsIgnoreCase(merge.trim())) {
			match_key = "c=" + alarmC + ",s=" + alarmS + ",dip=" + IpClassify.long2StrIp(alarmEvent.getDstIpv4())
					+ ",dstorg_id=" + alarmEvent.getDstOrgId();
		}
		if (match_key != null) {
			// 增加customid和platformid
			match_key += ",customid=" + alarmEvent.getCustomerId() + ",platformid=" + alarmEvent.getPlatformid();
		}
		return match_key;
	}

}