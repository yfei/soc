package cn.dcube.ahead.soc.cep.model;

/**
 * 告警事件
 * @author yangfei
 *
 */
public class AlarmEvt {
	private Long eventid = 0L;
	
	private int platformid;

	private int srcorgid = 0;

	private int dstorgid = 0;

	private int customerid = 0;

	private int ruleid = 0;

	private int level = 0;

	private int srcip = 0;

	private String srcipv6 = "";

	private int srcassetid = 0;

	private int dstip = 0;

	private String dstipv6 = "";

	private int dstassetid = 0;

	private int classes = 0;

	private int subclass = 0;

	private String starttime = "";

	private String endtime = "";

	private int cepnum = 0;

	private int state = 0;

	private String upgradetime = "";

	private String ceptype = "";

	private int ceptypecount = 0;

	private String describ = "";

	private int ability = 0;

	private int impact = 0;

	private int direction = 0;

	private int stage = 0;

	private long sensormask = 0;

	private int sensormodel = 0;

	private int sensorid = 0;

	private String sensorip = "";

	private String iscontinuous = "";

	private int ipcount = 0;

	private int sensorcount = 0;

	private int confidence = 0;

	private int relevancy = 0;

	private String issendmail = "";

	private String isupholemark = "";

	private String isupbadip = "";

	private String isupbadurl = "";

	private String isupbadcode = "";

	private String isupbadmail = "";

	private String isupbadother = "";

	private String isupasset = "";

	private String isupbusiness = "";

	private int num1 = 0;

	private int num2 = 0;

	private int num3 = 0;

	private int num4 = 0;

	private int num5 = 0;

	private String str1 = "";

	private String str2 = "";

	private String str3 = "";

	private String str4 = "";

	private String str5 = "";
	
	private String srcGeo1Code;
	private String srcGeo1Name;
	private String srcGeo2Code;
	private String srcGeo2Name;
	private String srcGeo3Code;
	private String srcGeo3Name;
	private String dstGeo1Code;
	private String dstGeo1Name;
	private String dstGeo2Code;
	private String dstGeo2Name;
	private String dstGeo3Code;
	private String dstGeo3Name;

	// 源ip所在的经度坐标
	private String srcLongitude;
	// 源ip所在的纬度坐标
	private String srcLatitude;
	// 目的ip所在的经度坐标
	private String dstLongitude;
	// 目的ip所在的纬度坐标
	private String dstLatitude;

	public Long getEventid() {
		return eventid;
	}

	public void setEventid(Long eventid) {
		this.eventid = eventid;
	}

	public int getSrcorgid() {
		return srcorgid;
	}

	public void setSrcorgid(int srcorgid) {
		this.srcorgid = srcorgid;
	}

	public int getDstorgid() {
		return dstorgid;
	}

	public void setDstorgid(int dstorgid) {
		this.dstorgid = dstorgid;
	}

	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

	public int getRuleid() {
		return ruleid;
	}

	public void setRuleid(int ruleid) {
		this.ruleid = ruleid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSrcip() {
		return srcip;
	}

	public void setSrcip(int srcip) {
		this.srcip = srcip;
	}

	public String getSrcipv6() {
		return srcipv6;
	}

	public void setSrcipv6(String srcipv6) {
		this.srcipv6 = srcipv6;
	}

	public int getSrcassetid() {
		return srcassetid;
	}

	public void setSrcassetid(int srcassetid) {
		this.srcassetid = srcassetid;
	}

	public int getDstip() {
		return dstip;
	}

	public void setDstip(int dstip) {
		this.dstip = dstip;
	}

	public String getDstipv6() {
		return dstipv6;
	}

	public void setDstipv6(String dstipv6) {
		this.dstipv6 = dstipv6;
	}

	public int getDstassetid() {
		return dstassetid;
	}

	public void setDstassetid(int dstassetid) {
		this.dstassetid = dstassetid;
	}

	public int getClasses() {
		return classes;
	}

	public void setClasses(int classes) {
		this.classes = classes;
	}

	public int getSubclass() {
		return subclass;
	}

	public void setSubclass(int subclass) {
		this.subclass = subclass;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public int getCepnum() {
		return cepnum;
	}

	public void setCepnum(int cepnum) {
		this.cepnum = cepnum;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getUpgradetime() {
		return upgradetime;
	}

	public void setUpgradetime(String upgradetime) {
		this.upgradetime = upgradetime;
	}

	public String getCeptype() {
		return ceptype;
	}

	public void setCeptype(String ceptype) {
		this.ceptype = ceptype;
	}

	public int getCeptypecount() {
		return ceptypecount;
	}

	public void setCeptypecount(int ceptypecount) {
		this.ceptypecount = ceptypecount;
	}

	public String getDescrib() {
		return describ;
	}

	public void setDescrib(String describ) {
		this.describ = describ;
	}

	public int getAbility() {
		return ability;
	}

	public void setAbility(int ability) {
		this.ability = ability;
	}

	public int getImpact() {
		return impact;
	}

	public void setImpact(int impact) {
		this.impact = impact;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public long getSensormask() {
		return sensormask;
	}

	public void setSensormask(long sensormask) {
		this.sensormask = sensormask;
	}

	public int getSensormodel() {
		return sensormodel;
	}

	public void setSensormodel(int sensormodel) {
		this.sensormodel = sensormodel;
	}

	public int getSensorid() {
		return sensorid;
	}

	public void setSensorid(int sensorid) {
		this.sensorid = sensorid;
	}

	public String getSensorip() {
		return sensorip;
	}

	public void setSensorip(String sensorip) {
		this.sensorip = sensorip;
	}

	public String getIscontinuous() {
		return iscontinuous;
	}

	public void setIscontinuous(String iscontinuous) {
		this.iscontinuous = iscontinuous;
	}

	public int getIpcount() {
		return ipcount;
	}

	public void setIpcount(int ipcount) {
		this.ipcount = ipcount;
	}

	public int getSensorcount() {
		return sensorcount;
	}

	public void setSensorcount(int sensorcount) {
		this.sensorcount = sensorcount;
	}

	public int getConfidence() {
		return confidence;
	}

	public void setConfidence(int confidence) {
		this.confidence = confidence;
	}

	public int getRelevancy() {
		return relevancy;
	}

	public void setRelevancy(int relevancy) {
		this.relevancy = relevancy;
	}

	public String getIssendmail() {
		return issendmail;
	}

	public void setIssendmail(String issendmail) {
		this.issendmail = issendmail;
	}

	public String getIsupholemark() {
		return isupholemark;
	}

	public void setIsupholemark(String isupholemark) {
		this.isupholemark = isupholemark;
	}

	public String getIsupbadip() {
		return isupbadip;
	}

	public void setIsupbadip(String isupbadip) {
		this.isupbadip = isupbadip;
	}

	public String getIsupbadurl() {
		return isupbadurl;
	}

	public void setIsupbadurl(String isupbadurl) {
		this.isupbadurl = isupbadurl;
	}

	public String getIsupbadcode() {
		return isupbadcode;
	}

	public void setIsupbadcode(String isupbadcode) {
		this.isupbadcode = isupbadcode;
	}

	public String getIsupbadmail() {
		return isupbadmail;
	}

	public void setIsupbadmail(String isupbadmail) {
		this.isupbadmail = isupbadmail;
	}

	public String getIsupbadother() {
		return isupbadother;
	}

	public void setIsupbadother(String isupbadother) {
		this.isupbadother = isupbadother;
	}

	public String getIsupasset() {
		return isupasset;
	}

	public void setIsupasset(String isupasset) {
		this.isupasset = isupasset;
	}

	public String getIsupbusiness() {
		return isupbusiness;
	}

	public void setIsupbusiness(String isupbusiness) {
		this.isupbusiness = isupbusiness;
	}

	public int getNum1() {
		return num1;
	}

	public void setNum1(int num1) {
		this.num1 = num1;
	}

	public int getNum2() {
		return num2;
	}

	public void setNum2(int num2) {
		this.num2 = num2;
	}

	public int getNum3() {
		return num3;
	}

	public void setNum3(int num3) {
		this.num3 = num3;
	}

	public int getNum4() {
		return num4;
	}

	public void setNum4(int num4) {
		this.num4 = num4;
	}

	public int getNum5() {
		return num5;
	}

	public void setNum5(int num5) {
		this.num5 = num5;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}

	public String getStr3() {
		return str3;
	}

	public void setStr3(String str3) {
		this.str3 = str3;
	}

	public String getStr4() {
		return str4;
	}

	public void setStr4(String str4) {
		this.str4 = str4;
	}

	public String getStr5() {
		return str5;
	}

	public void setStr5(String str5) {
		this.str5 = str5;
	}

	public int getPlatformid() {
		return platformid;
	}

	public void setPlatformid(int platformid) {
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
	
}
