package cn.dcube.ahead.soc.cep.output;

import cn.dcube.ahead.soc.cep.rule.CB_RuleObject;
import com.espertech.esper.client.EventBean;

import java.util.ArrayList;
import java.util.List;

/**
 * CEP规则输出对象
 * @author yangfei
 *
 */
public class OutPutInfor {

	private EventBean[] evtbean;

	private String[] output;

	private int ruleid;

	private int subclass;

	private int family;

	private int level;

	private CB_RuleObject ruleobject;

	private List<String> preid = new ArrayList<String>();

	public OutPutInfor(EventBean[] evtbean, CB_RuleObject ruleobject) {
		this.evtbean = evtbean;
		this.output = ruleobject.ParseOutput();
		this.ruleid = ruleobject.getRuleid();
		this.subclass = ruleobject.getSubclass();
		this.family = ruleobject.getFamily();
		this.level = ruleobject.getLevel();
		this.preid = ruleobject.getPreid();
		this.ruleobject = ruleobject;
	}

	public EventBean[] getEvtbean() {
		return evtbean;
	}

	public void setEvtbean(EventBean[] evtbean) {
		this.evtbean = evtbean;
	}

	public String[] getOutput() {
		return output;
	}

	public void setOutput(String[] output) {
		this.output = output;
	}

	public int getRuleid() {
		return ruleid;
	}

	public void setRuleid(int ruleid) {
		this.ruleid = ruleid;
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
	 * @return the ruleobject
	 */
	public CB_RuleObject getRuleobject() {
		return ruleobject;
	}

	/**
	 * @param ruleobject
	 *            the ruleobject to set
	 */
	public void setRuleobject(CB_RuleObject ruleobject) {
		this.ruleobject = ruleobject;
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

}
