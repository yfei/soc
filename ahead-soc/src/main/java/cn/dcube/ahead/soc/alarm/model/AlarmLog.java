package cn.dcube.ahead.soc.alarm.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "alarm_log")
public class AlarmLog {

	@TableId
	@TableField(value = "alarm_log_id")
	private Long alarmLogId = 0L;

	// 数据来源平台
	@TableField(value = "platform_id")
	private Integer platformid;

	@TableField(value = "customer_id")
	private Integer customerId = 0;

	@TableField(value = "org_id")
	private Integer orgId = 0;

	@TableField(value = "merge_type")
	private String merge;

	@TableField(value = "alarm_c")
	private Integer alarmC = 0;

	@TableField(value = "alarm_s")
	private Integer alarmS = 0;

	@TableField(value = "level")
	private Integer level = 0;

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

	@TableField(value = "start_time")
	private Date startTime;

	@TableField(value = "end_time")
	private Date endTime;

	@TableField(value = "cep_num")
	private Integer cepNum = 0;

	@TableField(value = "direction")
	private Integer direction = 0;

	@TableField(value = "stage")
	private Integer stage = 0;

	@TableField(value = "impact")
	private Integer impact = 0;

	@TableField(value = "sensor_mask")
	private Long sensorMask = 0L;

	@TableField(value = "sensor_model")
	private Integer sensorModel = 0;

	@TableField(value = "describ")
	private String describ = "";

	@TableField(value = "hole_mark")
	private String holeMark = "";

	@TableField(value = "bad_srcip")
	private String badSrcIp = "";

	@TableField(value = "bad_dstip")
	private String badDstIp = "";

	@TableField(value = "bad_url")
	private String badUrl = "";

	@TableField(value = "bad_code")
	private String badCode = "";

	@TableField(value = "bad_mailfrom")
	private String badMailFrom = "";

	@TableField(value = "bad_mailto")
	private String badMailTo = "";

	@TableField(value = "bad_mailcc")
	private String badMailCc = "";

	@TableField(value = "bad_mailbcc")
	private String badMailBcc = "";

	@TableField(value = "bad_other")
	private String badOther = "";

	@TableField(value = "src_geo1_code")
	private String srcGeo1Code;
	@TableField(value = "src_geo1_name")
	private String srcGeo1Name;
	@TableField(value = "src_geo2_code")
	private String srcGeo2Code;
	@TableField(value = "src_geo2_name")
	private String srcGeo2Name;
	@TableField(value = "src_geo3_code")
	private String srcGeo3Code;
	@TableField(value = "src_geo3_name")
	private String srcGeo3Name;
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

	public Integer getPlatformid() {
		return platformid;
	}

	public void setPlatformid(Integer platformid) {
		this.platformid = platformid;
	}

	/**
	 * @return the badDstIp
	 */
	public String getBadDstIp() {
		return badDstIp;
	}

	/**
	 * @param badDstIp the badDstIp to set
	 */
	public void setBadDstIp(String badDstIp) {
		this.badDstIp = badDstIp;
	}

	/**
	 * @return the badSrcIp
	 */
	public String getBadSrcIp() {
		return badSrcIp;
	}

	/**
	 * @param badSrcIp the badSrcIp to set
	 */
	public void setBadSrcIp(String badSrcIp) {
		this.badSrcIp = badSrcIp;
	}

	/**
	 * @return the badMailFrom
	 */
	public String getBadMailFrom() {
		return badMailFrom;
	}

	/**
	 * @param badMailFrom the badMailFrom to set
	 */
	public void setBadMailFrom(String badMailFrom) {
		this.badMailFrom = badMailFrom;
	}

	/**
	 * @return the badMailTo
	 */
	public String getBadMailTo() {
		return badMailTo;
	}

	/**
	 * @param badMailTo the badMailTo to set
	 */
	public void setBadMailTo(String badMailTo) {
		this.badMailTo = badMailTo;
	}

	/**
	 * @return the badMailCc
	 */
	public String getBadMailCc() {
		return badMailCc;
	}

	/**
	 * @param badMailCc the badMailCc to set
	 */
	public void setBadMailCc(String badMailCc) {
		this.badMailCc = badMailCc;
	}

	/**
	 * @return the badMailBcc
	 */
	public String getBadMailBcc() {
		return badMailBcc;
	}

	/**
	 * @param badMailBcc the badMailBcc to set
	 */
	public void setBadMailBcc(String badMailBcc) {
		this.badMailBcc = badMailBcc;
	}

	public Long getAlarmLogId() {
		return alarmLogId;
	}

	public void setAlarmLogId(Long alarmLogId) {
		this.alarmLogId = alarmLogId;
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

	public Integer getImpact() {
		return impact;
	}

	public void setImpact(Integer impact) {
		this.impact = impact;
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

	public String getDescrib() {
		return describ;
	}

	public void setDescrib(String describ) {
		this.describ = describ == null ? "" : describ.trim();
	}

	public String getHoleMark() {
		return holeMark;
	}

	public void setHoleMark(String holeMark) {
		this.holeMark = holeMark == null ? "0" : holeMark.trim();
	}

	public String getBadUrl() {
		return badUrl;
	}

	public void setBadUrl(String badUrl) {
		this.badUrl = badUrl == null ? "0" : badUrl.trim();
	}

	public String getBadCode() {
		return badCode;
	}

	public void setBadCode(String badCode) {
		this.badCode = badCode == null ? "0" : badCode.trim();
	}

	public String getBadOther() {
		return badOther;
	}

	public void setBadOther(String badOther) {
		this.badOther = badOther == null ? "0" : badOther.trim();
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


	public String getMerge() {
		return merge;
	}

	public void setMerge(String merge) {
		this.merge = merge;
	}
}
