package cn.dcube.ahead.soc.cep.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import cn.dcube.goku.commons.util.JodaUtil;

/**
 * CEP事件
 * 
 * @author yangfei
 *
 */
@Document(indexName = "cep-event")
public class CEPEvent {

	@Id
	@Field(type = FieldType.Long)
	private long cepid;

	@Field(type = FieldType.Long)
	private long alarmlogid;

	@Field(type = FieldType.Integer)
	private int platformid;

	@Field(type = FieldType.Integer)
	private int orgid;

	@Field(type = FieldType.Integer)
	private int customerid;

	@Field(type = FieldType.Integer)
	private int ceps;

	@Field(type = FieldType.Integer)
	private int cepf;

	@Field(type = FieldType.Integer)
	private int ruleid;

	@Field(type = FieldType.Integer)
	private int ceplevel;

	@Field(type = FieldType.Integer)
	private int clazz;

	@Field(type = FieldType.Integer)
	private int subclass;

	@Field(type = FieldType.Integer)
	private int family;

	@Field(type = FieldType.Long)
	private long srcip;

	@Field(type = FieldType.Text)
	private String srcipv6;

	@Field(type = FieldType.Integer)
	private int srcport;

	@Field(type = FieldType.Integer)
	private int srcin;

	@Field(type = FieldType.Integer)
	private int srcorgid = 0;

	@Field(type = FieldType.Integer)
	private int srcassetid;

	@Field(type = FieldType.Integer)
	private int srcassetlevel;

	@Field(type = FieldType.Integer)
	private int srcbusiid;

	@Field(type = FieldType.Text)
	private String srcbusilink;

	@Field(type = FieldType.Long)
	private long dstip;

	@Field(type = FieldType.Text)
	private String dstipv6;

	@Field(type = FieldType.Integer)
	private int dstport;

	@Field(type = FieldType.Integer)
	private int dstin;

	@Field(type = FieldType.Integer)
	private int dstorgid = 0;

	@Field(type = FieldType.Integer)
	private int dstassetid;

	@Field(type = FieldType.Integer)
	private int dstassetlevel;

	@Field(type = FieldType.Integer)
	private int dstbusiid;

	@Field(type = FieldType.Text)
	private String dstbusilink;

	@Field(type = FieldType.Long)
	private long sensormask;

	@Field(type = FieldType.Integer)
	private int sensormodel;

	@Field(type = FieldType.Text)
	private String sensorip;

	@Field(type = FieldType.Integer)
	private int sensorid;

	@Field(type = FieldType.Text)
	private String account;

	@Field(type = FieldType.Integer)
	private int eventcount;

	@Field(type = FieldType.Integer)
	private int eventnum;

	@Field(type = FieldType.Text)
	private String starttime;

	@Field(type = FieldType.Text)
	private String endtime;

	@Field(type = FieldType.Integer)
	private int protocol;

	@Field(type = FieldType.Integer)
	private int c;

	@Field(type = FieldType.Integer)
	private int i;

	@Field(type = FieldType.Integer)
	private int a;

	@Field(type = FieldType.Text)
	private String holemark;

	@Field(type = FieldType.Text)
	private String badurl;

	@Field(type = FieldType.Text)
	private String badsrcip;

	@Field(type = FieldType.Text)
	private String baddstip;

	@Field(type = FieldType.Text)
	private String badcode;

	@Field(type = FieldType.Text)
	private String badmailfrom;

	@Field(type = FieldType.Text)
	private String badmailto;

	@Field(type = FieldType.Text)
	private String badmailcc;

	@Field(type = FieldType.Text)
	private String badmailbcc;

	@Field(type = FieldType.Text)
	private String badother;

	@Field(type = FieldType.Integer)
	private int num1 = 0;

	@Field(type = FieldType.Integer)
	private int num2 = 0;

	@Field(type = FieldType.Integer)
	private int num3 = 0;

	@Field(type = FieldType.Integer)
	private int num4 = 0;

	@Field(type = FieldType.Integer)
	private int num5 = 0;

	@Field(type = FieldType.Text)
	private String str1 = "";

	@Field(type = FieldType.Text)
	private String str2 = "";

	@Field(type = FieldType.Text)
	private String str3 = "";

	@Field(type = FieldType.Text)
	private String str4 = "";

	@Field(type = FieldType.Text)
	private String str5 = "";

	@Field(type = FieldType.Double)
	private double df1 = 0;

	@Field(type = FieldType.Double)
	private double df2 = 0;

	@Field(type = FieldType.Double)
	private double df3 = 0;

	@Field(type = FieldType.Double)
	private double df4 = 0;

	@Field(type = FieldType.Double)
	private double df5 = 0;

	@Field(type = FieldType.Text)
	private String srcGeo1Code;
	@Field(type = FieldType.Text)
	private String srcGeo1Name;
	@Field(type = FieldType.Text)
	private String srcGeo2Code;
	@Field(type = FieldType.Text)
	private String srcGeo2Name;
	@Field(type = FieldType.Text)
	private String srcGeo3Code;
	@Field(type = FieldType.Text)
	private String srcGeo3Name;
	@Field(type = FieldType.Text)
	private String dstGeo1Code;
	@Field(type = FieldType.Text)
	private String dstGeo1Name;
	@Field(type = FieldType.Text)
	private String dstGeo2Code;
	@Field(type = FieldType.Text)
	private String dstGeo2Name;
	@Field(type = FieldType.Text)
	private String dstGeo3Code;
	@Field(type = FieldType.Text)
	private String dstGeo3Name;

	// 源ip所在的经度坐标
	@Field(type = FieldType.Text)
	private String srcLongitude;
	// 源ip所在的纬度坐标
	@Field(type = FieldType.Text)
	private String srcLatitude;
	// 目的ip所在的经度坐标
	@Field(type = FieldType.Text)
	private String dstLongitude;
	// 目的ip所在的纬度坐标
	@Field(type = FieldType.Text)
	private String dstLatitude;

	@Field(type = FieldType.Text)
	private String info;

	@Field(type = FieldType.Text)
	private String tag;

	public Long getCepid() {
		return cepid;
	}

	public void setCepid(Long cepid) {
		this.cepid = cepid;
	}

	public Long getAlarmlogid() {
		return alarmlogid;
	}

	public void setAlarmlogid(Long alarmlogid) {
		this.alarmlogid = alarmlogid;
	}

	public int getPlatformid() {
		return platformid;
	}

	public void setPlatformid(int platformid) {
		this.platformid = platformid;
	}

	public int getOrgid() {
		return orgid;
	}

	public void setOrgid(int orgid) {
		this.orgid = orgid;
	}

	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

	public int getCeps() {
		return ceps;
	}

	public void setCeps(int ceps) {
		this.ceps = ceps;
	}

	public int getCepf() {
		return cepf;
	}

	public void setCepf(int cepf) {
		this.cepf = cepf;
	}

	public int getRuleid() {
		return ruleid;
	}

	public void setRuleid(int ruleid) {
		this.ruleid = ruleid;
	}

	public int getCeplevel() {
		return ceplevel;
	}

	public void setCeplevel(int ceplevel) {
		this.ceplevel = ceplevel;
	}

	public int getClazz() {
		return clazz;
	}

	public void setClazz(int clazz) {
		this.clazz = clazz;
	}

	public int getSubclass() {
		return subclass;
	}

	public void setSubclass(int subclass) {
		this.subclass = subclass;
	}

	public int getFamily() {
		return family;
	}

	public void setFamily(int family) {
		this.family = family;
	}

	public long getSrcip() {
		return srcip;
	}

	public void setSrcip(long srcip) {
		this.srcip = srcip;
	}

	public String getSrcipv6() {
		return srcipv6;
	}

	public void setSrcipv6(String srcipv6) {
		this.srcipv6 = srcipv6;
	}

	public int getSrcport() {
		return srcport;
	}

	public void setSrcport(int srcport) {
		this.srcport = srcport;
	}

	public int getSrcin() {
		return srcin;
	}

	public void setSrcin(int srcin) {
		this.srcin = srcin;
	}

	public int getSrcorgid() {
		return srcorgid;
	}

	public void setSrcorgid(int srcorgid) {
		this.srcorgid = srcorgid;
	}

	public int getSrcassetid() {
		return srcassetid;
	}

	public void setSrcassetid(int srcassetid) {
		this.srcassetid = srcassetid;
	}

	public int getSrcassetlevel() {
		return srcassetlevel;
	}

	public void setSrcassetlevel(int srcassetlevel) {
		this.srcassetlevel = srcassetlevel;
	}

	public int getSrcbusiid() {
		return srcbusiid;
	}

	public void setSrcbusiid(int srcbusiid) {
		this.srcbusiid = srcbusiid;
	}

	public String getSrcbusilink() {
		return srcbusilink;
	}

	public void setSrcbusilink(String srcbusilink) {
		this.srcbusilink = srcbusilink;
	}

	public long getDstip() {
		return dstip;
	}

	public void setDstip(long dstip) {
		this.dstip = dstip;
	}

	public String getDstipv6() {
		return dstipv6;
	}

	public void setDstipv6(String dstipv6) {
		this.dstipv6 = dstipv6;
	}

	public int getDstport() {
		return dstport;
	}

	public void setDstport(int dstport) {
		this.dstport = dstport;
	}

	public int getDstin() {
		return dstin;
	}

	public void setDstin(int dstin) {
		this.dstin = dstin;
	}

	public int getDstorgid() {
		return dstorgid;
	}

	public void setDstorgid(int dstorgid) {
		this.dstorgid = dstorgid;
	}

	public int getDstassetid() {
		return dstassetid;
	}

	public void setDstassetid(int dstassetid) {
		this.dstassetid = dstassetid;
	}

	public int getDstassetlevel() {
		return dstassetlevel;
	}

	public void setDstassetlevel(int dstassetlevel) {
		this.dstassetlevel = dstassetlevel;
	}

	public int getDstbusiid() {
		return dstbusiid;
	}

	public void setDstbusiid(int dstbusiid) {
		this.dstbusiid = dstbusiid;
	}

	public String getDstbusilink() {
		return dstbusilink;
	}

	public void setDstbusilink(String dstbusilink) {
		this.dstbusilink = dstbusilink;
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

	public String getSensorip() {
		return sensorip;
	}

	public void setSensorip(String sensorip) {
		this.sensorip = sensorip;
	}

	public int getSensorid() {
		return sensorid;
	}

	public void setSensorid(int sensorid) {
		this.sensorid = sensorid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getEventcount() {
		return eventcount;
	}

	public void setEventcount(int eventcount) {
		this.eventcount = eventcount;
	}

	public int getEventnum() {
		return eventnum;
	}

	public void setEventnum(int eventnum) {
		this.eventnum = eventnum;
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

	public Date getEndtimeOfDate() {
		if (null != this.endtime && !"".equals(endtime)) {
			return JodaUtil.parseStrToDateSimple(endtime);
		}
		return null;
	}

	public Date getStarttimeOfDate() {
		if (null != this.starttime && !"".equals(starttime)) {
			return JodaUtil.parseStrToDateSimple(starttime);
		}
		return null;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public String getHolemark() {
		return holemark;
	}

	public void setHolemark(String holemark) {
		this.holemark = holemark;
	}

	public String getBadurl() {
		return badurl;
	}

	public void setBadurl(String badurl) {
		this.badurl = badurl;
	}

	public String getBadsrcip() {
		return badsrcip;
	}

	public void setBadsrcip(String badsrcip) {
		this.badsrcip = badsrcip;
	}

	public String getBaddstip() {
		return baddstip;
	}

	public void setBaddstip(String baddstip) {
		this.baddstip = baddstip;
	}

	public String getBadcode() {
		return badcode;
	}

	public void setBadcode(String badcode) {
		this.badcode = badcode;
	}

	public String getBadmailfrom() {
		return badmailfrom;
	}

	public void setBadmailfrom(String badmailfrom) {
		this.badmailfrom = badmailfrom;
	}

	public String getBadmailto() {
		return badmailto;
	}

	public void setBadmailto(String badmailto) {
		this.badmailto = badmailto;
	}

	public String getBadmailcc() {
		return badmailcc;
	}

	public void setBadmailcc(String badmailcc) {
		this.badmailcc = badmailcc;
	}

	public String getBadother() {
		return badother;
	}

	public void setBadother(String badother) {
		this.badother = badother;
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

	public double getDf1() {
		return df1;
	}

	public void setDf1(double df1) {
		this.df1 = df1;
	}

	public double getDf2() {
		return df2;
	}

	public void setDf2(double df2) {
		this.df2 = df2;
	}

	public double getDf3() {
		return df3;
	}

	public void setDf3(double df3) {
		this.df3 = df3;
	}

	public double getDf4() {
		return df4;
	}

	public void setDf4(double df4) {
		this.df4 = df4;
	}

	public double getDf5() {
		return df5;
	}

	public void setDf5(double df5) {
		this.df5 = df5;
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

	public String getBadmailbcc() {
		return badmailbcc;
	}

	public void setBadmailbcc(String badmailbcc) {
		this.badmailbcc = badmailbcc;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
