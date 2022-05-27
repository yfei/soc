package cn.dcube.ahead.soc.ia.receiver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.commons.proto.transport.EventTypeEnum;
import cn.dcube.ahead.kafka.coder.ByteMessageParser;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.ia.IAEngine;
import cn.dcube.ahead.soc.ia.config.IAConfig;
import cn.dcube.ahead.soc.kafka.handler.IKafkaEventHandler;
import cn.dcube.ahead.soc.kafka.model.KafkaTopic;
import lombok.extern.slf4j.Slf4j;

/**
 * DP回填处理器
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
@ConditionalOnBean(IAConfig.class)
public class IAKafkaEventHandler implements IKafkaEventHandler {

	@Autowired
	private IAConfig config;

	@Autowired
	private IAEngine iaContext;

	public IAKafkaEventHandler() {
		log.info("IA的Event数据处理服务已开启!");
	}

	@Override
	public void handle(MessageType type, KafkaEvent event) {
		EventTransportEntity transportEvent = ByteMessageParser.deserializer(event, type, this.getEventType());
		iaContext.handle(transportEvent, event.getTopic());
	}

	@Override
	public List<KafkaTopic> getTopic() {
		List<KafkaTopic> topics = new ArrayList<KafkaTopic>();
		config.getTopics().forEach(topic -> {
			if (this.getEventType().equals(topic.getEventType())) {
				topics.add(topic);
			}
		});
		return topics;
	}

	@Override
	public String getEventType() {
		return EventTypeEnum.EVENT.getCode();
	}

}
