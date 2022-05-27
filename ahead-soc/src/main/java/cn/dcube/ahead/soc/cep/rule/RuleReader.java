package cn.dcube.ahead.soc.cep.rule;

import cn.dcube.ahead.soc.util.Constant;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class RuleReader {

	protected Map<String, Properties> sections = new HashMap<String, Properties>();

	private transient String currentSecion;

	private transient Properties current;

	private CB_RuleObject ruleobject;

	/*
	 * add by yu_jwei 2014-06-26 10:00 解决复杂关联规则加载之前 前置规则未加载进来，导致espser解析报错
	 * HashMap是一种无序的存储结构， 当从HashMap中读取数据时与放入数据的先后顺序不一致； 修改为用LinkedHashMap来存放关联规则
	 */
	public static Map<Integer, CB_RuleObject> ruleobjectsOfAnalysis = new LinkedHashMap<Integer, CB_RuleObject>();
	
	/*
	 * add by yu_jwei 2014-06-26 10:00 解决复杂关联规则加载之前 前置规则未加载进来，导致espser解析报错
	 * HashMap是一种无序的存储结构， 当从HashMap中读取数据时与放入数据的先后顺序不一致； 修改为用LinkedHashMap来存放关联规则
	 */
	public static Map<Integer, CB_RuleObject> ruleobjectsOfStatistics = new LinkedHashMap<Integer, CB_RuleObject>();

	public RuleReader(String filename,String type) throws IOException {
		if(Constant.RULE_STATISTICS.equals(type)){
			ruleobjectsOfStatistics.clear();
		}else{
			ruleobjectsOfAnalysis.clear();
		}
		InputStreamReader insReader = new InputStreamReader(new FileInputStream(filename), "UTF-8");
		BufferedReader reader = new BufferedReader(insReader);
		read(reader,type);
		reader.close();
	}

	protected void read(BufferedReader reader,String type) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			parseLine(line,type);
		}
	}

	protected void parseLine(String line,String type) {
		line = line.trim();
		if (line.matches("\\[.*\\]")) {
			this.ruleobject = new CB_RuleObject();
			currentSecion = line.replaceFirst("\\[(.*)\\]", "$1");
			ruleobject.setDiscrib(currentSecion);
			ruleobject.setTag(Constant.TAG_QDKY);
			current = new Properties();
			sections.put(currentSecion, current);
		} else if (line.matches(".*=.*")) {
			if (current != null) {
				int i = line.indexOf('=');
				String name = line.substring(0, i);
				String value = line.substring(i + 1);
				if (name.equals("id")) {
					ruleobject.setRuleid(Integer.parseInt(value));
				} else if (name.equals("type")) {
					ruleobject.setType(Integer.valueOf(value));
				} else if (name.equals("rule")) {
					ruleobject.setRules(value);
				} else if (name.equals("subclass")) {
					ruleobject.setSubclass(Integer.parseInt(value));
				} else if (name.equals("family")) {
					ruleobject.setFamily(Integer.parseInt(value));
				} else if (name.equals("isGene")) {
					ruleobject.setIsGene(Integer.parseInt(value));
				}else if (name.equals("level")) {
					ruleobject.setLevel(Integer.parseInt(value));
				} else if (name.equals("preid")) {
					if (!"".equals(value) && !"0".equals(value)) {
						String[] preid = value.split(",");
						for (int j = 0; j < preid.length; j++)
							ruleobject.setPreid(preid[j]);
					} else {
						ruleobject.setPreid(value);
					}
				} else if (name.equals("colmapping")) {
					ruleobject.setColmapping(value);
				} else if (name.equals("accumulative")) {
					ruleobject.setAccumulative(Integer.valueOf(value));
				} else if (name.equals("accumulationUnit")) {
					ruleobject.setAccumulationUnit(value);
				}else if (name.equals("accumulationGroupkey")) {
					ruleobject.setAccumulationGroupkey(value);
				} else if (name.equals("accumulationCol")) {
					ruleobject.setAccumulationCol(value);
				}  else if (name.equals("tag")) {
					ruleobject.setTag(value);
				} else {
					ruleobject.setOutput(value);
					if(Constant.RULE_STATISTICS.equals(type)){
						ruleobjectsOfStatistics.put(ruleobject.getRuleid(), ruleobject);
					}else{
						ruleobjectsOfAnalysis.put(ruleobject.getRuleid(), ruleobject);
					}
				}
				current.setProperty(name, value);
			}
		}
	}

	public String getValue(String section, String name) {
		Properties p = (Properties) sections.get(section);

		if (p == null) {
			return null;
		}
		String value = p.getProperty(name);

		return value;
	}

	public Map<Integer, CB_RuleObject> getRules(String type) {
		if(Constant.RULE_STATISTICS.equals(type)){
			return ruleobjectsOfStatistics;
		}else{
			return ruleobjectsOfAnalysis;
		}
	}

	public static Map<Integer, CB_RuleObject> getRuleObjects(String type) {
		if(Constant.RULE_STATISTICS.equals(type)){
			return ruleobjectsOfStatistics;
		}else{
			return ruleobjectsOfAnalysis;
		}
	}

}
