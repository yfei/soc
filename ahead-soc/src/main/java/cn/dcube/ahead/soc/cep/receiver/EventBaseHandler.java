package cn.dcube.ahead.soc.cep.receiver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.commons.proto.transport.EventTypeEnum;
import cn.dcube.ahead.kafka.coder.ByteMessageParser;
import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.soc.cep.CEPEngine;
import cn.dcube.ahead.soc.cep.config.CEPConfig;
import cn.dcube.ahead.soc.cep.config.CEPKafkaTopic;
import cn.dcube.ahead.soc.cep.model.EventBase;
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
@ConditionalOnBean(CEPConfig.class)
public class EventBaseHandler implements IKafkaEventHandler {

	@Autowired
	private CEPConfig config;

	@Autowired
	private CEPEngine engine;

	public EventBaseHandler() {
		log.info("CEP的Event数据处理服务已开启!");
	}

	@Override
	public void handle(MessageType messageType, KafkaEvent event) {
		EventTransportEntity transportEvent = ByteMessageParser.deserializer(event, messageType, this.getEventType());
		// send to esper engine
		CEPKafkaTopic topicConfig = null;
		for (CEPKafkaTopic tConfig : config.getTopics()) {
			if (tConfig.getTopic().equals(event.getTopic())) {
				topicConfig = tConfig;
			}
		}
		if (topicConfig != null) {
			try {
				String eclazz = topicConfig.getEventClazz();
				Class<?> clazz = Class.forName(eclazz);
				Object eventObj = clazz.newInstance();
				Map<String, String> fields = topicConfig.getFields();
				if (fields != null) {
					// 进行字段转换
					Map<String, Object> data = (Map<String, Object>) transportEvent.getEventData();
					for (Entry<String, String> filedEntry : fields.entrySet()) {
						String srcField = filedEntry.getKey();
						String dstField = filedEntry.getValue();
						if (data.containsKey(srcField)) {
							Object value = data.get(srcField);
							try {
								Field eventField = eventObj.getClass().getDeclaredField(dstField);
								if (eventField != null) {
									eventField.setAccessible(true);
									eventField.set(eventObj, value);
								}
							} catch (Exception e) {
								log.warn("事件转换异常!{}", e);
							}
						}
					}
				}
				if (eventObj instanceof EventBase) {
					EventBase eventBase = (EventBase) eventObj;
					setCsfFlag(eventBase);
				}
				engine.getEpService().getEPRuntime().sendEvent(eventObj);
			} catch (Exception e) {
				log.error("", e);
			}
		} else {
			log.warn("{}对应的topicConfig为空", event.getTopic());
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
		return EventTypeEnum.EVENT.getCode();
	}

	private void setCsfFlag(EventBase evtbase) {
		List<String> csfList = config.getCsf();
		String cs = evtbase.getClasses() + "|" + evtbase.getSubclass();
		String csf = evtbase.getClasses() + "|" + evtbase.getSubclass() + "|" + evtbase.getFamily();

		log.debug("evtbase cs = {}, csf = {}, srcin = {}, dstin = {}, srcorgid = {}, dstorgid = {},receive_time={}", cs,
				csf, evtbase.getSrcin(), evtbase.getDstin(), evtbase.getSrcorgid(), evtbase.getDstorgid(),
				evtbase.getReceivetime());

		if (csfList.contains(csf)) {
			evtbase.setCsfflag(1);
		} else if (csfList.contains(cs)) {
			evtbase.setCsfflag(1);
		}
		if (log.isDebugEnabled()) {
			log.debug("evtbase csf flag = {}", evtbase.getCsfflag());
		}
	}

}
