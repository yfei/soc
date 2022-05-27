package cn.dcube.ahead.soc.cep.config;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * DP配置
 * 
 * @author yangfei
 *
 */
@Data
@Configuration(value = "CEPConfig")
@ConfigurationProperties(prefix = "soc.cep")
@ConditionalOnExpression("${soc.cep.enable:false}")
public class CEPConfig {
	
	private boolean enable;
	
	private int id;

	// 处理线程数
	private int handleThread = Runtime.getRuntime().availableProcessors();;
	
	// 统计结果
	private CEPStatisticsConfig statistics;

	// 规则类型 ANALYSIS/STATISTICS/ALL
	private String ruleType = "ALL";
	
	// 消费主题
	private List<CEPKafkaTopic> topics;

	// 规则中存在的CSF
	private List<String> csf;
	
	public boolean isHandleAnalysisRule() {
		if ("ANALYSIS".equals(ruleType) || "ALL".equals(ruleType)) {
			return true;
		}
		return false;
	}

	public boolean isHandleStatisticsRule() {
		if ("STATISTICS".equals(ruleType) || "ALL".equals(ruleType)) {
			return true;
		}
		return false;
	}

}
