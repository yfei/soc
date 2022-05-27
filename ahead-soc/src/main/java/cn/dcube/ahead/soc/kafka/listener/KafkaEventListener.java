package cn.dcube.ahead.soc.kafka.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.kafka.event.KafkaEvent;
import cn.dcube.ahead.kafka.listener.DefaultKafkaEventListener;
import cn.dcube.ahead.soc.kafka.KafkaEventHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * kafka消息监听器.<br/>
 * 当监听到kafka消息后,会将kafka消息通过Spring ApplicationListener进行发布和接收.<br/>
 * 接收到ApplicationEvent后,KafkaEventHandlerContext会根据各个节点订阅的topic策略进行处理.
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
public class KafkaEventListener extends DefaultKafkaEventListener {

	@Autowired
	private KafkaEventHandlerContext context;

	@Override
	protected void handle(KafkaEvent event) {
		try {
			context.handle(event);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	protected void statistics(KafkaEvent event) {
		// TODO 对接收到的event进行统计
		
	}

}
