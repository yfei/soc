package cn.dcube.ahead.soc.cep.rule;

import cn.dcube.ahead.soc.cep.config.CEPConfig;
import cn.dcube.ahead.soc.cep.output.EventListener;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPStatement;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改单个EPL语句，并创建新的结果监听
 * @author yangfei
 *
 */
@Service
@ConditionalOnBean(CEPConfig.class)
public class CEPRuleService {
	
	private Map<Integer,String> ruleMap = new HashMap<>();

	public CEPRuleService() {}

	public int add(EPAdministrator admin, CB_RuleObject ruleobj) {
		for (String rule : ruleobj.getRules()) {
			Integer ruleid = ruleobj.getRuleid();
			// 逐个加入 每组关联规则
			EPStatement statement = admin.createEPL(rule);
			if (null != statement) {
				statement.addListener(new EventListener(ruleobj));
				ruleMap.put(ruleid, statement.getName());
			}
			
		}

		return 0;
	}

	public int update(EPAdministrator admin, CB_RuleObject ruleobj) {
		this.delete(admin, ruleobj);
		add(admin, ruleobj);
		return 0;
	}

	public int delete(EPAdministrator admin, CB_RuleObject ruleobj) {
		Integer ruleid = ruleobj.getRuleid();
		String statementName = ruleMap.get(ruleid);
		EPStatement oldstmt = admin.getStatement(statementName);
		if (null != oldstmt) {
			oldstmt.destroy(); // 销毁statement的同时，会将statemnt设置的监听等信息一并销毁
			ruleMap.remove(ruleid);
		}
		return 0;
	}

	public Map<Integer, String> getRuleMap() {
		return ruleMap;
	}

}
