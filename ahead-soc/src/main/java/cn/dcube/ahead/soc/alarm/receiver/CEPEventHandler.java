package cn.dcube.ahead.soc.alarm.receiver;

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
import cn.dcube.ahead.soc.alarm.cache.CEPEventCache;
import cn.dcube.ahead.soc.alarm.config.AlarmConfig;
import cn.dcube.ahead.soc.cep.model.CEPEvent;
import cn.dcube.ahead.soc.kafka.handler.IKafkaEventHandler;
import cn.dcube.ahead.soc.kafka.model.KafkaTopic;
import lombok.extern.slf4j.Slf4j;

/**
 * DP接收Event的Kafka handler.
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
@ConditionalOnBean(AlarmConfig.class)
public class CEPEventHandler implements IKafkaEventHandler {

	@Autowired
	private AlarmConfig config;

	public CEPEventHandler() {
		log.info("Alarm的CEPEvent数据处理服务已开启!");
	}

	@Override
	public void handle(MessageType messageType, KafkaEvent event) {
		try {
			EventTransportEntity transportEvent = ByteMessageParser.deserializer(event, messageType, this.getEventType());
			// 将CEPEvent交给线程进行处理
			CEPEvent cepEvent = (CEPEvent)transportEvent.getEventData();
			CEPEventCache.add(cepEvent);
		}catch(Exception e) {
			log.error("",e);
		}

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
		return EventTypeEnum.CEPEVENT.getCode();
	}

}
