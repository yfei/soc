package cn.dcube.ahead.soc.cep.model;

/**
 * 归一化事件
 * @author yangfei
 *
 */
public class EventBase {
    public static final String NOTBAD = "0";

    private Long eventid = 0L;

    private int platformid;

    private int customerid = 0;

    private int orgid = 0;

    private int classes = 0;

    private int subclass = 0;

    private int family = 0;

    private String account = "";

    private int rawid = 0;

    private String rawlevel = "";

    private String rawname = "";

    private int level = 0;

    private long sensormask = 0;

    private int sensormodel = 0;

    private String sensorip = "";

    private int srcip = 0;

    private String srcipv6 = "";

    private String srchost = "";

    private int srcorgid = 0;

    private int srcport = 0;

    private int srcin = 0;

    private int dstip = 0;

    private String dstipv6 = "";

    private String dsthost = "";

    private int dstorgid = 0;

    private int dstport = 0;

    private int dstin = 0;

    private int protocol = 0;

    private String starttime = "";
    // 2018-4-12 从starttime中解析出时间来
    private String time = "";

    private String endtime = "";

    private String receivetime = "";

    private int counts = 0;

    private String cve = "";

    private String bugtraq = "";

    private String url = "";

    private String dns = "";

    private String mailform = "";

    private String mailto = "";

    private String mailcc = "";

    private String mailbcc = "";

    private int num1 = 0;

    private int num2 = 0;

    private int num3 = 0;

    private int num4 = 0;

    private int num5 = 0;

    private int num6 = 0;

    private int num7 = 0;

    private int num8 = 0;

    private int num9 = 0;

    private int num10 = 0;

    private String str1 = "";

    private String str2 = "";

    private String str3 = "";

    private String str4 = "";

    private String str5 = "";

    private String str6 = "";

    private String str7 = "";

    private String str8 = "";

    private String str9 = "";

    private String str10 = "";

    private double df1;

    private double df2;

    private double df3;

    private double df4;

    private double df5;


    private int c = 0;

    private int i = 0;

    private int a = 0;

    /**
     * 源IP是否是恶意IP
     */
    private String badsrcip = NOTBAD;

    /**
     * 目的IP是否是恶意IP
     */
    private String baddstip = NOTBAD;

    /**
     * 是否是恶意代码
     */
    private String badcode = NOTBAD;

    /**
     * 是否是恶意url
     */
    private String badurl = NOTBAD;

    /**
     * MailFrom是否是恶意的
     */
    private String badmailfrom = NOTBAD;

    /**
     * MailTo是否是恶意的
     */
    private String badmailto = NOTBAD;

    /**
     * MailCc是否是恶意的
     */
    private String badmailcc = NOTBAD;

    /**
     * MailBcc是否是恶意的
     */
    private String badmailbcc = NOTBAD;

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

    // 黑白名单标记
    private int bwlFlag;

    private int srcassetid;

    private int srcassetlevel;

    private int srcbusiid;

    private String srcbusilink;

    private int dstassetid;

    private int dstassetlevel;

    private int dstbusiid;

    private String dstbusilink;

    private int csfflag = 0;

    // 20180815 增加summary字段
    private String summary;

    public Long getEventid() {
        return eventid;
    }

    public void setEventid(Long eventid) {
        this.eventid = eventid;
    }

    public int getPlatformid() {
        return platformid;
    }

    public void setPlatformid(int platformid) {
        this.platformid = platformid;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
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

    public int getFamily() {
        return family;
    }

    public void setFamily(int family) {
        this.family = family;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getRawid() {
        return rawid;
    }

    public void setRawid(int rawid) {
        this.rawid = rawid;
    }

    public String getRawlevel() {
        return rawlevel;
    }

    public void setRawlevel(String rawlevel) {
        this.rawlevel = rawlevel;
    }

    public String getRawname() {
        return rawname;
    }

    public void setRawname(String rawname) {
        this.rawname = rawname;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public int getSrcorgid() {
        return srcorgid;
    }

    public void setSrcorgid(int srcorgid) {
        this.srcorgid = srcorgid;
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

    public int getDstorgid() {
        return dstorgid;
    }

    public void setDstorgid(int dstorgid) {
        this.dstorgid = dstorgid;
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

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(String receivetime) {
        this.receivetime = receivetime;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public String getBugtraq() {
        return bugtraq;
    }

    public void setBugtraq(String bugtraq) {
        this.bugtraq = bugtraq;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getMailform() {
        return mailform;
    }

    public void setMailform(String mailform) {
        this.mailform = mailform;
    }

    public String getMailto() {
        return mailto;
    }

    public void setMailto(String mailto) {
        this.mailto = mailto;
    }

    public String getMailcc() {
        return mailcc;
    }

    public void setMailcc(String mailcc) {
        this.mailcc = mailcc;
    }

    public String getMailbcc() {
        return mailbcc;
    }

    public void setMailbcc(String mailbcc) {
        this.mailbcc = mailbcc;
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

    public int getNum6() {
        return num6;
    }

    public void setNum6(int num6) {
        this.num6 = num6;
    }

    public int getNum7() {
        return num7;
    }

    public void setNum7(int num7) {
        this.num7 = num7;
    }

    public int getNum8() {
        return num8;
    }

    public void setNum8(int num8) {
        this.num8 = num8;
    }

    public int getNum9() {
        return num9;
    }

    public void setNum9(int num9) {
        this.num9 = num9;
    }

    public int getNum10() {
        return num10;
    }

    public void setNum10(int num10) {
        this.num10 = num10;
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

    public String getStr6() {
        return str6;
    }

    public void setStr6(String str6) {
        this.str6 = str6;
    }

    public String getStr7() {
        return str7;
    }

    public void setStr7(String str7) {
        this.str7 = str7;
    }

    public String getStr8() {
        return str8;
    }

    public void setStr8(String str8) {
        this.str8 = str8;
    }

    public String getStr9() {
        return str9;
    }

    public void setStr9(String str9) {
        this.str9 = str9;
    }

    public String getStr10() {
        return str10;
    }

    public void setStr10(String str10) {
        this.str10 = str10;
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

    public String getBadurl() {
        return badurl;
    }

    public void setBadurl(String badurl) {
        this.badurl = badurl;
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

    public String getBadmailbcc() {
        return badmailbcc;
    }

    public void setBadmailbcc(String badmailbcc) {
        this.badmailbcc = badmailbcc;
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

    public int getBwlFlag() {
        return bwlFlag;
    }

    public void setBwlFlag(int bwlFlag) {
        this.bwlFlag = bwlFlag;
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

    public int getCsfflag() {
        return csfflag;
    }

    public void setCsfflag(int csfflag) {
        this.csfflag = csfflag;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSrchost() {
        return srchost;
    }

    public void setSrchost(String srchost) {
        this.srchost = srchost;
    }

    public String getDsthost() {
        return dsthost;
    }

    public void setDsthost(String dsthost) {
        this.dsthost = dsthost;
    }
}
