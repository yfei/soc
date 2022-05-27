package cn.dcube.ahead.soc.alarm.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "alarm_event")
public class AlarmEvent {

	@TableId
	@TableField(value = "id")
	private Long id = 0L;

	@TableField(value = "platform_id")
	private Integer platformid;


	@TableField(value = "org_id")
	private Integer orgId = 0;

	@TableField(value = "customer_id")
	private Integer customerId = 0;

	@TableField(value = "rule_id")
	private Integer ruleId = 0;

	// 5级level
	@TableField(value = "level")
	private Integer level = 0;

	// 10级level
	@TableField(value = "level10")
	private Integer alarmRuleLevel = 0;

	@TableField(value = "src_ipv4")
	private Long srcIpv4 = 0L;

	@TableField(value = "src_ipv6")
	private String srcIpv6 = "";

	@TableField(value = "src_assetid")
	private Integer srcAssetid = 0;

	@TableField(value = "src_assetlevel")
	private Integer srcAssetLevel = 0;

	@TableField(value = "src_in")
	private Integer srcIn = 0;

	@TableField(value = "src_orgid")
	private Integer srcOrgId = 0;

	@TableField(value = "src_busiid")
	private Integer srcBusiId = 0;

	@TableField(value = "src_busilink")
	private String srcBusiLink = "";

	@TableField(value = "dst_ipv4")
	private Long dstIpv4 = 0L;

	@TableField(value = "dst_ipv6")
	private String dstIpv6 = "";

	@TableField(value = "dst_assetid")
	private Integer dstAssetid = 0;

	@TableField(value = "dst_assetlevel")
	private Integer dstAssetLevel = 0;

	@TableField(value = "dst_in")
	private Integer dstIn = 0;

	@TableField(value = "dst_orgid")
	private Integer dstOrgId = 0;

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

	@TableField(value = "cep_type")
	private String cepType = "";

	// 轻度可疑CEP Type
	@TableField(exist = false) // 非数据库字段
	private String suspiciousCepType = "";

	// 阻断CEP Type
	@TableField(exist = false) // 非数据库字段
	private String denyCepType = "";

	@TableField(value = "ceptype_count")
	private Integer ceptypeCount = 0;

	@Deprecated
	@TableField(exist = false) // 非数据库字段
	private Integer suspiciousCeptypeCount = 0;

	@Deprecated
	@TableField(exist = false) // 非数据库字段
	private Integer denyCeptypeCount = 0;

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

	// 数字冰雹功能使用：是否满足level>=limitLevel(config.xml),满足置为1
	@TableField(value = "num1")
	private Integer num1 = 0;

	// 数字冰雹功能使用：是否是第一次发送给cep做统计，是第一次发送置为1
	@TableField(value = "num2")
	private Integer num2 = 0;

	@TableField(value = "num3")
	private Integer num3 = 0;

	// num4为1，alarmevent是按源合并的（S）；为2，按目的合并的（D）；为3，按源和目的合并（SD）
	@TableField(value = "num4")
	private Integer num4 = 0;

	@TableField(value = "num5")
	private Integer num5 = 0;

	// 作为updatetime字段
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

	// 重启加载告警事件使用
	@TableField(value = "merge_type")
	private String mergeType = "";

	@TableField(exist = false) // 非数据库字段
	private Float max = (float) 0;

	@TableField(exist = false) // 非数据库字段
	private Float min = (float) 0;

	@TableField(exist = false) // 非数据库字段
	private Float avg = (float) 0;

	@TableField(exist = false) // 非数据库字段
	private Float total = (float) 0;

	@TableField(exist = false) // 非数据库字段
	private Integer output = 0;

	// 告警升级使用
	@TableField(exist = false) // 非数据库字段
	private Long upbadsrcipId = 0L;

	@TableField(exist = false) // 非数据库字段
	private Long upbaddstipId = 0L;

	@TableField(exist = false) // 非数据库字段
	private Long upsrcassetId = 0L;

	@TableField(exist = false) // 非数据库字段
	private Long updstassetId = 0L;

	@TableField(exist = false) // 非数据库字段
	private Long upbadipId = 0L;

	@TableField(exist = false) // 非数据库字段
	private Long upassetId = 0L;

	@TableField(value = "src_geo1_code")
	private String srcGeo1Code = "";

	@TableField(value = "src_geo1_name")
	private String srcGeo1Name = "";

	@TableField(value = "src_geo2_code")
	private String srcGeo2Code = "";

	@TableField(value = "src_geo2_name")
	private String srcGeo2Name = "";

	@TableField(value = "src_geo3_code")
	private String srcGeo3Code = "";

	@TableField(value = "src_geo3_name")
	private String srcGeo3Name = "";

	@TableField(value = "dst_geo1_code")
	private String dstGeo1Code = "";

	@TableField(value = "dst_geo1_name")
	private String dstGeo1Name = "";

	@TableField(value = "dst_geo2_code")
	private String dstGeo2Code = "";

	@TableField(value = "dst_geo2_name")
	private String dstGeo2Name = "";

	@TableField(value = "dst_geo3_code")
	private String dstGeo3Code = "";

	@TableField(value = "dst_geo3_name")
	private String dstGeo3Name = "";

	// 源ip所在的经度坐标
	@TableField(value = "src_longitude")
	private String srcLongitude = "";

	// 源ip所在的纬度坐标
	@TableField(value = "src_latitude")
	private String srcLatitude = "";

	// 目的ip所在的经度坐标
	@TableField(value = "dst_longitude")
	private String dstLongitude = "";

	// 目的ip所在的纬度坐标
	@TableField(value = "dst_latitude")
	private String dstLatitude = "";

	@TableField(value = "src_list")
	private String srcList = "";

	@TableField(value = "dst_list")
	private String dstList = "";

	@TableField(exist = false) // 非数据库字段
	private String suspiciousSrcList = "";

	@TableField(exist = false) // 非数据库字段
	private String suspiciousDstList = "";

	@TableField(exist = false) // 非数据库字段
	private String denySrcList = "";

	@TableField(exist = false) // 非数据库字段
	private String denyDstList = "";

	@TableField(exist = false) // 非数据库字段
	private String tag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		if (level <= 5) {
			this.level = level;
		}
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

	public String getMergeType() {
		return mergeType;
	}

	public void setMergeType(String mergeType) {
		this.mergeType = mergeType;
	}

	public Float getMax() {
		return max;
	}

	public void setMax(Float max) {
		this.max = max;
	}

	public Float getMin() {
		return min;
	}

	public void setMin(Float min) {
		this.min = min;
	}

	public Float getAvg() {
		return avg;
	}

	public void setAvg(Float avg) {
		this.avg = avg;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public Integer getOutput() {
		return output;
	}

	public void setOutput(Integer output) {
		this.output = output;
	}

	public Long getUpbadsrcipId() {
		return upbadsrcipId;
	}

	public void setUpbadsrcipId(Long upbadsrcipId) {
		this.upbadsrcipId = upbadsrcipId;
	}

	public Long getUpbaddstipId() {
		return upbaddstipId;
	}

	public void setUpbaddstipId(Long upbaddstipId) {
		this.upbaddstipId = upbaddstipId;
	}

	public Long getUpsrcassetId() {
		return upsrcassetId;
	}

	public void setUpsrcassetId(Long upsrcassetId) {
		this.upsrcassetId = upsrcassetId;
	}

	public Long getUpdstassetId() {
		return updstassetId;
	}

	public void setUpdstassetId(Long updstassetId) {
		this.updstassetId = updstassetId;
	}

	/**
	 * @param upbadipid the upbadipid to set
	 */
	public void setUpbadipId(Long upbadipId) {
		this.upbadipId = upbadipId;
	}

	/**
	 * @param upassetid the upassetid to set
	 */
	public void setUpassetId(Long upassetId) {
		this.upassetId = upassetId;
	}

	/**
	 * @return the upbadipid
	 */
	public Long getUpbadipId() {
		return upbadipId;
	}

	/**
	 * @return the upassetid
	 */
	public Long getUpassetId() {
		return upassetId;
	}

	public Integer getPlatformid() {
		return platformid;
	}

	public void setPlatformid(Integer platformid) {
		this.platformid = platformid;
	}

	public String getSrcGeo1Code() {
		return srcGeo1Code;
	}

	public void setSrcGeo1Code(String srcGeo1Code) {
		this.srcGeo1Code = srcGeo1Code;
	}

	public String getSrcGeo1Name() {
		return srcGeo1Name;
	}

	public void setSrcGeo1Name(String srcGeo1Name) {
		this.srcGeo1Name = srcGeo1Name;
	}

	public String getSrcGeo2Code() {
		return srcGeo2Code;
	}

	public void setSrcGeo2Code(String srcGeo2Code) {
		this.srcGeo2Code = srcGeo2Code;
	}

	public String getSrcGeo2Name() {
		return srcGeo2Name;
	}

	public void setSrcGeo2Name(String srcGeo2Name) {
		this.srcGeo2Name = srcGeo2Name;
	}

	public String getSrcGeo3Code() {
		return srcGeo3Code;
	}

	public void setSrcGeo3Code(String srcGeo3Code) {
		this.srcGeo3Code = srcGeo3Code;
	}

	public String getSrcGeo3Name() {
		return srcGeo3Name;
	}

	public void setSrcGeo3Name(String srcGeo3Name) {
		this.srcGeo3Name = srcGeo3Name;
	}

	public String getDstGeo1Code() {
		return dstGeo1Code;
	}

	public void setDstGeo1Code(String dstGeo1Code) {
		this.dstGeo1Code = dstGeo1Code;
	}

	public String getDstGeo1Name() {
		return dstGeo1Name;
	}

	public void setDstGeo1Name(String dstGeo1Name) {
		this.dstGeo1Name = dstGeo1Name;
	}

	public String getDstGeo2Code() {
		return dstGeo2Code;
	}

	public void setDstGeo2Code(String dstGeo2Code) {
		this.dstGeo2Code = dstGeo2Code;
	}

	public String getDstGeo2Name() {
		return dstGeo2Name;
	}

	public void setDstGeo2Name(String dstGeo2Name) {
		this.dstGeo2Name = dstGeo2Name;
	}

	public String getDstGeo3Code() {
		return dstGeo3Code;
	}

	public void setDstGeo3Code(String dstGeo3Code) {
		this.dstGeo3Code = dstGeo3Code;
	}

	public String getDstGeo3Name() {
		return dstGeo3Name;
	}

	public void setDstGeo3Name(String dstGeo3Name) {
		this.dstGeo3Name = dstGeo3Name;
	}

	public String getSrcLongitude() {
		return srcLongitude;
	}

	public void setSrcLongitude(String srcLongitude) {
		this.srcLongitude = srcLongitude;
	}

	public String getSrcLatitude() {
		return srcLatitude;
	}

	public void setSrcLatitude(String srcLatitude) {
		this.srcLatitude = srcLatitude;
	}

	public String getDstLongitude() {
		return dstLongitude;
	}

	public void setDstLongitude(String dstLongitude) {
		this.dstLongitude = dstLongitude;
	}

	public String getDstLatitude() {
		return dstLatitude;
	}

	public void setDstLatitude(String dstLatitude) {
		this.dstLatitude = dstLatitude;
	}

	public Integer getSrcIn() {
		return srcIn;
	}

	public void setSrcIn(Integer srcIn) {
		this.srcIn = srcIn;
	}

	public Integer getDstIn() {
		return dstIn;
	}

	public void setDstIn(Integer dstIn) {
		this.dstIn = dstIn;
	}

	public String getSrcList() {
		return srcList;
	}

	public void setSrcList(String srcList) {
		this.srcList = srcList;
	}

	public String getDstList() {
		return dstList;
	}

	public void setDstList(String dstList) {
		this.dstList = dstList;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public String getSuspiciousCepType() {
		return suspiciousCepType;
	}

	public void setSuspiciousCepType(String suspiciousCepType) {
		this.suspiciousCepType = suspiciousCepType;
	}

	public String getDenyCepType() {
		return denyCepType;
	}

	public void setDenyCepType(String denyCepType) {
		this.denyCepType = denyCepType;
	}

	public Integer getSuspiciousCeptypeCount() {
		return suspiciousCeptypeCount;
	}

	public void setSuspiciousCeptypeCount(Integer suspiciousCeptypeCount) {
		this.suspiciousCeptypeCount = suspiciousCeptypeCount;
	}

	public Integer getDenyCeptypeCount() {
		return denyCeptypeCount;
	}

	public void setDenyCeptypeCount(Integer denyCeptypeCount) {
		this.denyCeptypeCount = denyCeptypeCount;
	}

	public String getSuspiciousSrcList() {
		return suspiciousSrcList;
	}

	public void setSuspiciousSrcList(String suspiciousSrcList) {
		this.suspiciousSrcList = suspiciousSrcList;
	}

	public String getSuspiciousDstList() {
		return suspiciousDstList;
	}

	public void setSuspiciousDstList(String suspiciousDstList) {
		this.suspiciousDstList = suspiciousDstList;
	}

	public String getDenySrcList() {
		return denySrcList;
	}

	public void setDenySrcList(String denySrcList) {
		this.denySrcList = denySrcList;
	}

	public String getDenyDstList() {
		return denyDstList;
	}

	public void setDenyDstList(String denyDstList) {
		this.denyDstList = denyDstList;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getAlarmRuleLevel() {
		return alarmRuleLevel;
	}

	public void setAlarmRuleLevel(Integer alarmRuleLevel) {
		this.alarmRuleLevel = alarmRuleLevel;
	}
}
