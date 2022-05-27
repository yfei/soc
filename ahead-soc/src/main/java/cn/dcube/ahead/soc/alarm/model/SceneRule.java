package cn.dcube.ahead.soc.alarm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "scene_rule")
public class SceneRule {

	@TableId
	@TableField(value = "id")
	private Integer id;

	private Integer c;

	private Integer s;

	private String name;

	private String describ;

	@TableField(value = "cep_s")
	private Integer cepS;

	@TableField(value = "cep_f")
	private Integer cepF;

	private Integer stage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		this.name = name == null ? null : name.trim();
	}

	public String getDescrib() {
		return describ;
	}

	public void setDescrib(String describ) {
		this.describ = describ == null ? null : describ.trim();
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

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
}
