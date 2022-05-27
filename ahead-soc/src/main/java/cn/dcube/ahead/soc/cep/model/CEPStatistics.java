package cn.dcube.ahead.soc.cep.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * CEP统计结果
 * @author yangfei
 *
 */
@Document(indexName = "cep-statistics")
@TableName(value = "cep_statistics")
public class CEPStatistics {
	
	@Id
	@Field(type = FieldType.Long)
	@TableId
    private Long id;

	@Field(type = FieldType.Date)
    private Date time;
    
    @Field(type = FieldType.Text)
    @TableField(value = "accumulation_time")
    private String accumulationTime;

    @Field(type = FieldType.Integer)
    @TableField(value = "platform_id")
    private int platformId;

    @Field(type = FieldType.Integer)
    @TableField(value = "customer_id")
    private int customerId;

    @Field(type = FieldType.Integer)
    @TableField(value = "org_id")
    private int orgId;

    @Field(type = FieldType.Integer)
    @TableField(value = "rule_id")
    private int ruleId;

    @Field(type = FieldType.Integer)
    @TableField(value = "src_orgid")
    private int srcOrgid;

    @Field(type = FieldType.Integer)
    @TableField(value = "dst_orgid")
    private int dstOrgid;

    @Field(type = FieldType.Text)
    private String gc1;

    @Field(type = FieldType.Text)
    private String gc2;

    @Field(type = FieldType.Text)
    private String gc3;

    @Field(type = FieldType.Text)
    private String gc4;

    @Field(type = FieldType.Text)
    private String gc5;

    @Field(type = FieldType.Text)
    private String gc6;

    @Field(type = FieldType.Text)
    private String gc7;

    @Field(type = FieldType.Text)
    private String gc8;

    @Field(type = FieldType.Text)
    private String gc9;

    @Field(type = FieldType.Text)
    private String gc10;

    @Field(type = FieldType.Long)
    private Long s1;

    @Field(type = FieldType.Long)
    private Long s2;

    @Field(type = FieldType.Long)
    private Long s3;
    
    @Field(type = FieldType.Long)
    private Long s4;

    @Field(type = FieldType.Long)
    private Long s5;

    @Field(type = FieldType.Double)
    private Double s6;

    @Field(type = FieldType.Double)
    private Double s7;

    @Field(type = FieldType.Double)
    private Double s8;

    @Field(type = FieldType.Double)
    private Double s9;

    @Field(type = FieldType.Double)
    private Double s10;
    
    @Field(type = FieldType.Text)
    @TableField(exist = false) // 非数据库字段
    private String accumulationCol;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    

    public String getAccumulationTime() {
		return accumulationTime;
	}

	public void setAccumulationTime(String accumulationTime) {
		this.accumulationTime = accumulationTime;
	}

	public int getPlatformId() {
		return platformId;
	}

	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public int getSrcOrgid() {
		return srcOrgid;
	}

	public void setSrcOrgid(int srcOrgid) {
		this.srcOrgid = srcOrgid;
	}

	public int getDstOrgid() {
		return dstOrgid;
	}

	public void setDstOrgid(int dstOrgid) {
		this.dstOrgid = dstOrgid;
	}

	public String getGc1() {
		return gc1;
	}

	public void setGc1(String gc1) {
		this.gc1 = gc1;
	}

	public String getGc2() {
		return gc2;
	}

	public void setGc2(String gc2) {
		this.gc2 = gc2;
	}

	public String getGc3() {
		return gc3;
	}

	public void setGc3(String gc3) {
		this.gc3 = gc3;
	}

	public String getGc4() {
		return gc4;
	}

	public void setGc4(String gc4) {
		this.gc4 = gc4;
	}

	public String getGc5() {
		return gc5;
	}

	public void setGc5(String gc5) {
		this.gc5 = gc5;
	}

	public String getGc6() {
		return gc6;
	}

	public void setGc6(String gc6) {
		this.gc6 = gc6;
	}

	public String getGc7() {
		return gc7;
	}

	public void setGc7(String gc7) {
		this.gc7 = gc7;
	}

	public String getGc8() {
		return gc8;
	}

	public void setGc8(String gc8) {
		this.gc8 = gc8;
	}

	public String getGc9() {
		return gc9;
	}

	public void setGc9(String gc9) {
		this.gc9 = gc9;
	}

	public String getGc10() {
        return gc10;
    }

    public void setGc10(String gc10) {
        this.gc10 = gc10 == null ? null : gc10.trim();
    }


	public Long getS1() {
		return s1;
	}

	public void setS1(Long s1) {
		this.s1 = s1;
	}

	public Long getS2() {
		return s2;
	}

	public void setS2(Long s2) {
		this.s2 = s2;
	}

	public Long getS3() {
		return s3;
	}

	public void setS3(Long s3) {
		this.s3 = s3;
	}

	public Long getS4() {
		return s4;
	}

	public void setS4(Long s4) {
		this.s4 = s4;
	}

	public Long getS5() {
		return s5;
	}

	public void setS5(Long s5) {
		this.s5 = s5;
	}

	public Double getS6() {
		return s6;
	}

	public void setS6(Double s6) {
		this.s6 = s6;
	}

	public Double getS7() {
		return s7;
	}

	public void setS7(Double s7) {
		this.s7 = s7;
	}

	public Double getS8() {
		return s8;
	}

	public void setS8(Double s8) {
		this.s8 = s8;
	}

	public Double getS9() {
		return s9;
	}

	public void setS9(Double s9) {
		this.s9 = s9;
	}

	public Double getS10() {
		return s10;
	}

	public void setS10(Double s10) {
		this.s10 = s10;
	}

	public String getAccumulationCol() {
		return accumulationCol;
	}

	public void setAccumulationCol(String accumulationCol) {
		this.accumulationCol = accumulationCol;
	}
}