package cn.dcube.ahead.soc.alarm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "alarm_rule2")
public class AlarmRule {

	@TableId
	@TableField(value = "rule_id")
	private Integer ruleId;

	@TableField(value = "c")
	private Integer c;

	@TableField(value = "s")
	private Integer s;

	@TableField(value = "name")
	private String name;


	@TableField(value = "describ")
	private String describ;

	@TableField(value = "cep_s")
	private Integer cepS;

	@TableField(value = "cep_f")
	private Integer cepF;

	@TableField(value = "merge")
	private String merge;

	@TableField(value = "level")	
	private Integer level;
	// 10级level 20190711 add by 杨飞
	@TableField(value = "level10")	
	private Integer level10;

	@TableField(value = "stage")
	private Integer stage;

	@TableField(value = "confidence")	
	private Integer confidence;

	@TableField(value = "matchs")	
	private Integer matchs;

	@TableField(value = "hit")	
	private Integer hit;

	@TableField(value = "continue_condition")	
	private Integer continueCondition;

	@TableField(value = "wintype")	
	private Integer wintype;

	@TableField(value = "winsize")	
	private Integer winsize;

	@TableField(value = "preevent")	
	private Integer preevent;

	@TableField(value = "autoupdate")	
	private Integer autoupdate;

	@TableField(value = "enable")	
	private Integer enable;

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getC() {
		return c;
	}

	public void setC(Integer c) {
		this.c = c;
	}

	public Integer getS() {
		return s;
	}

	public void setS(Integer s) {
		this.s = s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? "" : name.trim();
	}

	public String getDescrib() {
		return describ;
	}

	public void setDescrib(String describ) {
		this.describ = describ == null ? "" : describ.trim();
	}

	public Integer getCepS() {
		return cepS;
	}

	public void setCepS(Integer cepS) {
		this.cepS = cepS;
	}

	public Integer getCepF() {
		return cepF;
	}

	public void setCepF(Integer cepF) {
		this.cepF = cepF;
	}

	public String getMerge() {
		return merge;
	}

	public void setMerge(String merge) {
		this.merge = merge == null ? "" : merge.trim();
	}


	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public Integer getConfidence() {
		return confidence;
	}

	public void setConfidence(Integer confidence) {
		this.confidence = confidence;
	}

	public Integer getMatchs() {
		return matchs;
	}

	public void setMatchs(Integer matchs) {
		this.matchs = matchs;
	}

	public Integer getHit() {
		return hit;
	}

	public void setHit(Integer hit) {
		this.hit = hit;
	}

	public Integer getContinueCondition() {
		return continueCondition;
	}

	public void setContinueCondition(Integer continueCondition) {
		this.continueCondition = continueCondition;
	}

	public Integer getWintype() {
		return wintype;
	}

	public void setWintype(Integer wintype) {
		this.wintype = wintype;
	}

	public Integer getWinsize() {
		return winsize;
	}

	public void setWinsize(Integer winsize) {
		this.winsize = winsize;
	}

	public Integer getPreevent() {
		return preevent;
	}

	public void setPreevent(Integer preevent) {
		this.preevent = preevent;
	}

	public Integer getAutoupdate() {
		return autoupdate;
	}

	public void setAutoupdate(Integer autoupdate) {
		this.autoupdate = autoupdate;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

}
