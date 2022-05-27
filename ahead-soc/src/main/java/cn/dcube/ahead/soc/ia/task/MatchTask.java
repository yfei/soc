package cn.dcube.ahead.soc.ia.task;

import java.util.Map;
import java.util.Map.Entry;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.soc.ia.IAEngine;
import cn.dcube.ahead.soc.ia.config.IAEventModuleConfig;
import cn.dcube.ahead.soc.ia.config.IAKafkaTopic;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchTask implements Runnable {

	private EventTransportEntity event;

	private IAEngine context;
	
	private String topic;

	public MatchTask(EventTransportEntity event, String topic,IAEngine context) {
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
		}
		// 对于每个模块进行处理
		for (Entry<String, IAEventModuleConfig> moduleEntry : context.getConfig().getModules().entrySet()) {
			context.getMatchService().handle(moduleEntry.getKey(), moduleEntry.getValue(), topic, event);
		}
		try {
			// 发送事件
			String sendTopics = null;
			for (IAKafkaTopic topic : context.getConfig().getTopics()) {
				if (topic.getTopic().equals(this.topic)) {
					sendTopics = topic.getProducer();
				}
			}
			if (sendTopics == null) {
				sendTopics = context.getKafkaTopicConfig().getProducerTopic("ia-event", "sdap-eventbase-ia");
			}
			for (String sendTopic : sendTopics.split(",")) {
				context.getProducer().sendMessage(sendTopic, MessageType.TRANSPORT, event);
			}
		} catch (Exception e) {
			log.error("发送消息{}到topic{}失败!", event.getEventId(), event.getEventName());
		}
	}
}
