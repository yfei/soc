package cn.dcube.ahead.soc.alarm.config;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.dcube.ahead.soc.kafka.model.KafkaTopic;
import lombok.Data;

/**
 * DP配置
 * 
 * @author yangfei
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "soc.alarm")
@ConditionalOnExpression("${soc.alarm.enable:false}")
public class AlarmConfig {
	
	private boolean enable;
	
	private int id;

	// 消费主题
	private List<KafkaTopic> topics;
	
	private AlarmCloseConfig closeConfig;
}
