package cn.dcube.ahead.soc.dp.task;

import java.util.Map;
import java.util.Map.Entry;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.soc.dp.DPEngine;
import cn.dcube.ahead.soc.dp.config.DPEventModuleConfig;
import cn.dcube.ahead.soc.dp.config.DPKafkaTopic;
import cn.dcube.ahead.soc.util.IDGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RefillTask implements Runnable {

	private EventTransportEntity event;

	private DPEngine context;

	private String topic;

	public RefillTask(EventTransportEntity event, String topic, DPEngine context) {
		this.event = event;
		this.context = context;
		this.topic = topic;
	}

	@Override
	public void run() {
		// 处理数据
		Map<String, Object> eventData = (Map<String, Object>) event.getEventData();
		if (eventData == null) {
			log.warn("事件为空,忽略处理!");
			return;
		} else {
			Long id = IDGenerator.getId((byte)context.getConfig().getId());
			// 设值id
			event.setEventId(id + "");
			// 设置id为eventid
			eventData.put("id", id);
		}
		// 对于每个模块进行处理
		for (Entry<String, DPEventModuleConfig> moduleEntry : context.getConfig().getModules().entrySet()) {
			context.getRefillHandlerContext().handle(moduleEntry.getKey(), moduleEntry.getValue(), topic, event);
		}
		try {
			// 发送事件
			String sendTopics = null;
			for (DPKafkaTopic topic : context.getConfig().getTopics()) {
				if (topic.getTopic().equals(this.topic)) {
					sendTopics = topic.getProducer();
				}
			}
			if (sendTopics == null) {
				sendTopics = context.getKafkaTopicConfig().getProducerTopic("dp-event", "sdap-eventbase-dp");
			}
			for (String sendTopic : sendTopics.split(",")) {
				context.getProducer().sendMessage(sendTopic, MessageType.TRANSPORT, event);
			}
		} catch (Exception e) {
			log.error("发送消息{}到topic{}失败!", event.getEventId(), event.getEventName());
		}
	}
}
