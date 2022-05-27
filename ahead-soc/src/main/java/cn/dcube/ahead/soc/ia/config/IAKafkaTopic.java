package cn.dcube.ahead.soc.ia.config;

import cn.dcube.ahead.soc.kafka.model.KafkaTopic;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class IAKafkaTopic extends KafkaTopic {

	// 消费主题对应的生产主题,多个逗号分隔
	private String producer;

}
