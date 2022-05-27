package cn.dcube.ahead.soc.dp;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.kafka.config.KafkaTopicConfig;
import cn.dcube.ahead.kafka.producer.KafkaProducer;
import cn.dcube.ahead.redis.service.RedisService;
import cn.dcube.ahead.soc.dp.config.DPConfig;
import cn.dcube.ahead.soc.dp.service.RefillContext;
import cn.dcube.ahead.soc.dp.task.RefillTask;
import cn.dcube.ahead.soc.util.IDGenerator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ConditionalOnBean(DPConfig.class)
public class DPEngine {

	@Getter
	private EventLoopGroup eventLoop;

	@Autowired
	private DPConfig config;

	@Autowired
	private KafkaTopicConfig kafkaTopicConfig;

	@Autowired
	private KafkaProducer producer;

	@Autowired
	private RedisService redisService;

	@Autowired
	private RefillContext refillHandlerContext;

	@PostConstruct
	public void init() {
		eventLoop = new NioEventLoopGroup(config.getRefillThread(), new DefaultThreadFactory("dp-handle"));
		log.info("初始化DPContext,线程池数量为" + config.getRefillThread());
	}

	public void handle(EventTransportEntity event, String consumerTopic) {
		eventLoop.submit(new RefillTask(event, consumerTopic, this));
	}

	public DPConfig getConfig() {
		return config;
	}

	public void setConfig(DPConfig config) {
		this.config = config;
	}

	public KafkaProducer getProducer() {
		return producer;
	}

	public void setProducer(KafkaProducer producer) {
		this.producer = producer;
	}

	public RedisService getRedisService() {
		return redisService;
	}

	public void setRedisService(RedisService redisService) {
		this.redisService = redisService;
	}

	public RefillContext getRefillHandlerContext() {
		return refillHandlerContext;
	}

	public void setRefillHandlerContext(RefillContext refillHandlerContext) {
		this.refillHandlerContext = refillHandlerContext;
	}

	public KafkaTopicConfig getKafkaTopicConfig() {
		return kafkaTopicConfig;
	}

	public void setKafkaTopicConfig(KafkaTopicConfig kafkaTopicConfig) {
		this.kafkaTopicConfig = kafkaTopicConfig;
	}

}
