package cn.dcube.ahead.soc.alarm.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "alarm_upgrade")
public class AlarmUpgrade {

	@TableId
	@TableField(value = "id")
	private Long id = 0L;

	@TableField(value = "platform_id")
	private Integer platformid;

	@TableField(value = "org_id")
	private Integer orgId;

	@TableField(value = "alarm_event_id")
	private Long alarmEventId = 0L;

	@TableField(value = "customer_id")
	private Integer customerId = 0;

	@TableField(value = "rule_id")
	private Integer ruleId = 0;

	@TableField(value = "level")
	private Integer level = 0;

	@TableField(value = "level10")
	private Integer level10 = 0;

	@TableField(value = "src_ipv4")
	private Long srcIpv4 = 0L;

	@TableField(value = "src_ipv6")
	private String srcIpv6 = "";

	@TableField(value = "src_orgid")
	private Integer srcOrgId = 0;

	@TableField(value = "src_in")
	private Integer srcIn = 0;

	@TableField(value = "src_assetid")
	private Integer srcAssetid = 0;

	@TableField(value = "src_assetlevel")
	private Integer srcAssetLevel = 0;

	@TableField(value = "src_busiid")
	private Integer srcBusiId = 0;

	@TableField(value = "src_busilink")
	private String srcBusiLink = "";

	@TableField(value = "dst_ipv4")
	private Long dstIpv4 = 0L;

	@TableField(value = "dst_ipv6")
	private String dstIpv6 = "";

	@TableField(value = "dst_orgid")
	private Integer dstOrgId = 0;

	@TableField(value = "dst_in")
	private Integer dstIn = 0;

	@TableField(value = "dst_assetid")
	private Integer dstAssetid = 0;

	@TableField(value = "dst_assetlevel")
	private Integer dstAssetLevel = 0;

	@TableField(value = "dst_busiid")
	private Integer dstBusiId = 0;

	@TableField(value = "dst_busilink")
	private String dstBusiLink = "";

	@TableField(value = "alarm_c")
	private Integer alarmC = 0;

	@TableField(value = "alarm_s")
	private Integer alarmS = 0;

	@TableField(value = "start_time")
	private Date startTime;

	@TableField(value = "end_time")
	private Date endTime;

	@TableField(value = "cep_num")
	private Integer cepNum = 0;

	@TableField(value = "state")
	private Integer state = 0;

	@TableField(value = "upgrade_time")
	private Date upgradeTime;

	@TableField(value = "upgrade_desc")
	private String upgradeDesc = "";

	@TableField(value = "cep_type")
	private String cepType = "";

	@TableField(value = "ceptype_count")
	private Integer ceptypeCount = 0;

	@TableField(value = "describ")
	private String describ = "";

	@TableField(value = "ability")
	private Integer ability = 0;

	@TableField(value = "impact")
	private Integer impact = 0;

	@TableField(value = "direction")
	private Integer direction = 0;

	@TableField(value = "stage")
	private Integer stage = 0;

	@TableField(value = "sensor_mask")
	private Long sensorMask = 0L;

	@TableField(value = "sensor_model")
	private Integer sensorModel = 0;

	@TableField(value = "sensor_id")
	private Integer sensorId = 0;

	@TableField(value = "sensor_ip")
	private String sensorIp = "";

	@TableField(value = "iscontinuous")
	private String iscontinuous = "";

	@TableField(value = "ip_count")
	private Integer ipCount = 0;

	@TableField(value = "sensor_count")
	private Integer sensorCount = 0;

	@TableField(value = "confidence")
	private Integer confidence = 0;

	@TableField(value = "relevancy")
	private Integer relevancy = 0;

	@TableField(value = "issendmail")
	private String issendmail = "";

	@TableField(value = "isupholemark")
	private String isupholemark = "";

	@TableField(value = "isupbadip")
	private String isupbadip = "";

	@TableField(value = "isupbadurl")
	private String isupbadurl = "";

	@TableField(value = "isupbadcode")
	private String isupbadcode = "";

	@TableField(value = "isupbadmail")
	private String isupbadmail = "";

	@TableField(value = "isupbadother")
	private String isupbadother = "";

	@TableField(value = "isupasset")
	private String isupasset = "";

	@TableField(value = "isupbusiness")
	private String isupbusiness = "";

	@TableField(value = "num1")
	private Integer num1 = 0;

	@TableField(value = "num2")
	private Integer num2 = 0;

	@TableField(value = "num3")
	private Integer num3 = 0;

	@TableField(value = "num4")
	private Integer num4 = 0;

	@TableField(value = "num5")
	private Integer num5 = 0;

	@TableField(value = "str1")
	private String str1 = "";

	@TableField(value = "str2")
	private String str2 = "";

	@TableField(value = "str3")
	private String str3 = "";

	@TableField(value = "str4")
	private String str4 = "";

	@TableField(value = "str5")
	private String str5 = "";

	@TableField(value = "tag")
	private String tag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAlarmEventId() {
		return alarmEventId;
	}

	public void setAlarmEventId(Long alarmEventId) {
		this.alarmEventId = alarmEventId;
	}

	public Integer getSrcOrgId() {
		return srcOrgId;
	}

	public void setSrcOrgId(Integer srcOrgId) {
		this.srcOrgId = srcOrgId;
	}

	public Integer getDstOrgId() {
		return dstOrgId;
	}

	public void setDstOrgId(Integer dstOrgId) {
		this.dstOrgId = dstOrgId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getSrcIpv4() {
		return srcIpv4;
	}

	public void setSrcIpv4(Long srcIpv4) {
		this.srcIpv4 = srcIpv4;
	}

	public String getSrcIpv6() {
		return srcIpv6;
	}

	public void setSrcIpv6(String srcIpv6) {
		this.srcIpv6 = srcIpv6 == null ? "" : srcIpv6.trim();
	}

	public Integer getSrcAssetid() {
		return srcAssetid;
	}

	public void setSrcAssetid(Integer srcAssetid) {
		this.srcAssetid = srcAssetid;
	}

	public Long getDstIpv4() {
		return dstIpv4;
	}

	public void setDstIpv4(Long dstIpv4) {
		this.dstIpv4 = dstIpv4;
	}

	public String getDstIpv6() {
		return dstIpv6;
	}

	public void setDstIpv6(String dstIpv6) {
		this.dstIpv6 = dstIpv6 == null ? "" : dstIpv6.trim();
	}

	public Integer getDstAssetid() {
		return dstAssetid;
	}

	public void setDstAssetid(Integer dstAssetid) {
		this.dstAssetid = dstAssetid;
	}

	public Integer getAlarmC() {
		return alarmC;
	}

	public void setAlarmC(Integer alarmC) {
		this.alarmC = alarmC;
	}

	public Integer getAlarmS() {
		return alarmS;
	}

	public void setAlarmS(Integer alarmS) {
		this.alarmS = alarmS;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getCepNum() {
		return cepNum;
	}

	public void setCepNum(Integer cepNum) {
		this.cepNum = cepNum;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getUpgradeTime() {
		return upgradeTime;
	}

	public void setUpgradeTime(Date upgradeTime) {
		this.upgradeTime = upgradeTime;
	}

	public String getUpgradeDesc() {
		return upgradeDesc;
	}

	public void setUpgradeDesc(String upgradeDesc) {
		this.upgradeDesc = upgradeDesc;
	}

	public String getCepType() {
		return cepType;
	}

	public void setCepType(String cepType) {
		this.cepType = cepType == null ? "" : cepType.trim();
	}

	public Integer getCeptypeCount() {
		return ceptypeCount;
	}

	public void setCeptypeCount(Integer ceptypeCount) {
		this.ceptypeCount = ceptypeCount;
	}

	public String getDescrib() {
		return describ;
	}

	public void setDescrib(String describ) {
		this.describ = describ == null ? "" : describ.trim();
	}

	public Integer getAbility() {
		return ability;
	}

	public void setAbility(Integer ability) {
		this.ability = ability;
	}

	public Integer getImpact() {
		return impact;
	}

	public void setImpact(Integer impact) {
		this.impact = impact;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public Long getSensorMask() {
		return sensorMask;
	}

	public void setSensorMask(Long sensorMask) {
		this.sensorMask = sensorMask;
	}

	public Integer getSensorModel() {
		return sensorModel;
	}

	public void setSensorModel(Integer sensorModel) {
		this.sensorModel = sensorModel;
	}

	public Integer getSensorId() {
		return sensorId;
	}

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}

	public String getSensorIp() {
		return sensorIp;
	}

	public void setSensorIp(String sensorIp) {
		this.sensorIp = sensorIp == null ? "" : sensorIp.trim();
	}

	public String getIscontinuous() {
		return iscontinuous;
	}

	public void setIscontinuous(String iscontinuous) {
		this.iscontinuous = iscontinuous == null ? "0" : iscontinuous.trim();
	}

	public Integer getIpCount() {
		return ipCount;
	}

	public void setIpCount(Integer ipCount) {
		this.ipCount = ipCount;
	}

	public Integer getSensorCount() {
		return sensorCount;
	}

	public void setSensorCount(Integer sensorCount) {
		this.sensorCount = sensorCount;
	}

	public Integer getConfidence() {
		return confidence;
	}

	public void setConfidence(Integer confidence) {
		this.confidence = confidence;
	}

	public Integer getRelevancy() {
		return relevancy;
	}

	public void setRelevancy(Integer relevancy) {
		this.relevancy = relevancy;
	}

	public String getIssendmail() {
		return issendmail;
	}

	public void setIssendmail(String issendmail) {
		this.issendmail = issendmail == null ? "0" : issendmail.trim();
	}

	public String getIsupholemark() {
		return isupholemark;
	}

	public void setIsupholemark(String isupholemark) {
		this.isupholemark = isupholemark == null ? "0" : isupholemark.trim();
	}

	public String getIsupbadip() {
		return isupbadip;
	}

	public void setIsupbadip(String isupbadip) {
		this.isupbadip = isupbadip == null ? "0" : isupbadip.trim();
	}

	public String getIsupbadurl() {
		return isupbadurl;
	}

	public void setIsupbadurl(String isupbadurl) {
		this.isupbadurl = isupbadurl == null ? "0" : isupbadurl.trim();
	}

	public String getIsupbadcode() {
		return isupbadcode;
	}

	public void setIsupbadcode(String isupbadcode) {
		this.isupbadcode = isupbadcode == null ? "0" : isupbadcode.trim();
	}

	public String getIsupbadmail() {
		return isupbadmail;
	}

	public void setIsupbadmail(String isupbadmail) {
		this.isupbadmail = isupbadmail == null ? "0" : isupbadmail.trim();
	}

	public String getIsupbadother() {
		return isupbadother;
	}

	public void setIsupbadother(String isupbadother) {
		this.isupbadother = isupbadother == null ? "0" : isupbadother.trim();
	}

	public String getIsupasset() {
		return isupasset;
	}

	public void setIsupasset(String isupasset) {
		this.isupasset = isupasset == null ? "0" : isupasset.trim();
	}

	public String getIsupbusiness() {
		return isupbusiness;
	}

	public void setIsupbusiness(String isupbusiness) {
		this.isupbusiness = isupbusiness == null ? "0" : isupbusiness.trim();
	}

	public Integer getNum1() {
		return num1;
	}

	public void setNum1(Integer num1) {
		this.num1 = num1;
	}

	public Integer getNum2() {
		return num2;
	}

	public void setNum2(Integer num2) {
		this.num2 = num2;
	}

	public Integer getNum3() {
		return num3;
	}

	public void setNum3(Integer num3) {
		this.num3 = num3;
	}

	public Integer getNum4() {
		return num4;
	}

	public void setNum4(Integer num4) {
		this.num4 = num4;
	}

	public Integer getNum5() {
		return num5;
	}

	public void setNum5(Integer num5) {
		this.num5 = num5;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1 == null ? "" : str1.trim();
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2 == null ? "" : str2.trim();
	}

	public String getStr3() {
		return str3;
	}

	public void setStr3(String str3) {
		this.str3 = str3 == null ? "" : str3.trim();
	}

	public String getStr4() {
		return str4;
	}

	public void setStr4(String str4) {
		this.str4 = str4 == null ? "" : str4.trim();
	}

	public String getStr5() {
		return str5;
	}

	public void setStr5(String str5) {
		this.str5 = str5 == null ? "" : str5.trim();
	}

	public Integer getPlatformid() {
		return platformid;
	}

	public void setPlatformid(Integer platformid) {
		this.platformid = platformid;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getSrcIn() {
		return srcIn;
	}

	public void setSrcIn(Integer srcIn) {
		this.srcIn = srcIn;
	}

	public Integer getSrcAssetLevel() {
		return srcAssetLevel;
	}

	public void setSrcAssetLevel(Integer srcAssetLevel) {
		this.srcAssetLevel = srcAssetLevel;
	}

	public Integer getSrcBusiId() {
		return srcBusiId;
	}

	public void setSrcBusiId(Integer srcBusiId) {
		this.srcBusiId = srcBusiId;
	}

	public String getSrcBusiLink() {
		return srcBusiLink;
	}

	public void setSrcBusiLink(String srcBusiLink) {
		this.srcBusiLink = srcBusiLink;
	}

	public Integer getDstIn() {
		return dstIn;
	}

	public void setDstIn(Integer dstIn) {
		this.dstIn = dstIn;
	}

	public Integer getDstAssetLevel() {
		return dstAssetLevel;
	}

	public void setDstAssetLevel(Integer dstAssetLevel) {
		this.dstAssetLevel = dstAssetLevel;
	}

	public Integer getDstBusiId() {
		return dstBusiId;
	}

	public void setDstBusiId(Integer dstBusiId) {
		this.dstBusiId = dstBusiId;
	}

	public String getDstBusiLink() {
		return dstBusiLink;
	}

	public void setDstBusiLink(String dstBusiLink) {
		this.dstBusiLink = dstBusiLink;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getLevel10() {
		return level10;
	}

	public void setLevel10(Integer level10) {
		this.level10 = level10;
	}
}
