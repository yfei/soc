package cn.dcube.ahead.soc.cep.config;

import java.util.Map;

import cn.dcube.ahead.soc.kafka.model.KafkaTopic;
import lombok.Data;

@Data
public class CEPKafkaTopic extends KafkaTopic{
	
	// 对应的event类型
	private String eventClazz;
	
	// 字段映射到eventbase
	private Map<String,String> fields;
	
	// 生产者topic
	private String producer;

}
