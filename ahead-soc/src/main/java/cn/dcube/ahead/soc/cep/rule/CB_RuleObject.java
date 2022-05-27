package cn.dcube.ahead.soc.cep.rule;

import java.util.ArrayList;
import java.util.List;

/**
 * CEP规则对象
 * @author yangfei
 *
 */
public class CB_RuleObject {

	private int ruleid;
	
	private List<String> rules = new ArrayList<String>();

	private String output;

	private int subclass;

	private int family;

	private int level;

	private String discrib;
	
	private String colmapping;
	// 規則類型 0/1 分析 2/3统计
	private int type;
	
	// 0 是不累加 1 累加
	private int accumulative = 0;
	// Y 年 M 月  D 日(自然年月日)
	private String accumulationUnit;
	
	 private String accumulationCol;
	
	private String accumulationGroupkey;
	// 是否生成CEP事件，对于分析规则生效  0 不生成  1 生成
	private int isGene = 1;
	
	private String tag;

	/**
	 * 前置规则id集合
	 */
	private List<String> preid = new ArrayList<String>();

	public CB_RuleObject() {

	}

	public String[] ParseOutput() {
		String[] outputs = output.split("[|]");
		return outputs;
	}

	public int getRuleid() {
		return ruleid;
	}

	public void setRuleid(int ruleid) {
		this.ruleid = ruleid;
	}

	public List<String> getRules() {
		return rules;
	}

	public void setRules(String rule) {
		this.rules.add(rule);
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public int getSubclass() {
		return subclass;
	}

	public void setSubclass(int subclass) {
		this.subclass = subclass;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the preid
	 */
	public List<String> getPreid() {
		return preid;
	}

	/**
	 * @param preid
	 *            the preid to set
	 */
	public void setPreid(List<String> preid) {
		this.preid = preid;
	}

	/**
	 * @param preid
	 *            the preid to set
	 */
	public void setPreid(String preid) {
		this.preid.add(preid);
	}

	/**
	 * @param rules
	 *            the rules to set
	 */
	public void setRules(List<String> rules) {
		this.rules = rules;
	}

	/**
	 * @return the family
	 */
	public int getFamily() {
		return family;
	}

	/**
	 * @param family
	 *            the family to set
	 */
	public void setFamily(int family) {
		this.family = family;
	}

	/**
	 * @return the discrib
	 */
	public String getDiscrib() {
		return discrib;
	}

	/**
	 * @param discrib
	 *            the discrib to set
	 */
	public void setDiscrib(String discrib) {
		this.discrib = discrib;
	}

	public String getColmapping() {
		return colmapping;
	}

	public void setColmapping(String colmapping) {
		this.colmapping = colmapping;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAccumulative() {
		return accumulative;
	}

	public void setAccumulative(int accumulative) {
		this.accumulative = accumulative;
	}

	public String getAccumulationUnit() {
		return accumulationUnit;
	}

	public void setAccumulationUnit(String accumulationUnit) {
		this.accumulationUnit = accumulationUnit;
	}


	public String getAccumulationGroupkey() {
		return accumulationGroupkey;
	}

	public void setAccumulationGroupkey(String accumulationGroupkey) {
		this.accumulationGroupkey = accumulationGroupkey;
	}

	public String getAccumulationCol() {
		return accumulationCol;
	}

	public void setAccumulationCol(String accumulationCol) {
		this.accumulationCol = accumulationCol;
	}

	public int getIsGene() {
		return isGene;
	}

	public void setIsGene(int isGene) {
		this.isGene = isGene;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
