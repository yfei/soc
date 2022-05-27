package cn.dcube.ahead.soc.dw.task;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import com.github.pagehelper.util.StringUtil;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.soc.dw.DWEngine;
import cn.dcube.ahead.soc.dw.config.DWConfig;
import cn.dcube.ahead.soc.dw.config.DWKafkaTopic;
import lombok.extern.slf4j.Slf4j;

@ConditionalOnBean(DWConfig.class)
@Slf4j
public class ESSaveTask implements Runnable {

	private EventTransportEntity event;

	private MessageType messageType;

	private DWEngine engine;

	private String topic;

	public ESSaveTask(EventTransportEntity event, MessageType messageType, String topic, DWEngine engine) {
		this.event = event;
		this.messageType = messageType;
		this.engine = engine;
		this.topic = topic;
	}

	@Override
	public void run() {
		if (messageType.compareTo(MessageType.TRANSPORT) == 0) {
			Map<String, Object> data = (Map<String, Object>) event.getEventData();
			if (data == null) {
				log.warn("事件为空,不进行保存!");
				return;
			}
			try {
				String indexName = this.getIndex(topic, data);
				if (!engine.indexExists(indexName)) {
					// TODO 处理索引!
				}

				IndexCoordinates index = IndexCoordinates.of(indexName);
				engine.getEsService().getElasticTemplate().save(data, index);
				log.debug("保存数据{}到ES成功!", event.getEventId());
			} catch (Exception e) {
				log.warn("保存数据{}到ES失败!", event.getEventId());
				log.error("", e);
			}
		}
	}

	/**
	 * 根据事件主题得到对应的索引
	 * 
	 * @param topic
	 * @param time  标准时间格式
	 * @return
	 */
	public String getIndex(String topic, Map<String, Object> data) {
		String index = "event-base";
		for (DWKafkaTopic ktopic : engine.getConfig().getTopics()) {
			if (ktopic.getTopic().equals(topic)) {
				index = ktopic.getIndex().getName();
				if (ktopic.getIndex().isSplit()) {
					// FIXME 支持Object
					String time = data.get(ktopic.getIndex().getSplitField()).toString();
					if (StringUtil.isNotEmpty(time)) {
						index += "-" + time.substring(0, 7).replace("-", "");
					}
				}

			}
		}
		return index;
	}
}
