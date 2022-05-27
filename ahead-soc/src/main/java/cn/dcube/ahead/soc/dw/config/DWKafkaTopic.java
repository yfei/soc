package cn.dcube.ahead.soc.dw.config;

import cn.dcube.ahead.soc.kafka.model.KafkaTopic;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DWKafkaTopic extends KafkaTopic {

	// 对应的索引
	private DWIndexConfig index;

}
