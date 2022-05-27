package cn.dcube.ahead.soc.ia.service.handler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.redis.service.RedisService;
import cn.dcube.ahead.soc.dp.config.DPConfig;
import cn.dcube.ahead.soc.ia.config.IAConfig;
import cn.dcube.ahead.soc.ia.config.IAEventModuleConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用匹配服务,包括恶意IP/恶意URL等
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
@ConditionalOnBean(IAConfig.class)
public class IAMatchHandler implements IMatchHandler {

	@Autowired
	private IAConfig config;

	@Autowired
	private RedisService redisService;

	@Override
	public String getCategory() {
		return "COMMON";
	}

	@Override
	public void handle(String moduleName, IAEventModuleConfig module, String topic, EventTransportEntity event) {
		try {
			if (module != null && module.isEnable()) {
				if (module.getExcludeTopics() != null && module.getExcludeTopics().contains(topic)) {
					log.debug("忽略Event:{}-{}的{}信息丰富化", topic, event.getEventId(), moduleName);
					return;
				}
				log.info("处理Event:{}的{}信息丰富化", event.getEventId(), moduleName);
				// 处理数据
				Map<String, Object> eventData = (Map<String, Object>) event.getEventData();

				List<String> fields = module.getFields();
				// 根据fields查询redis
				String[] redisKeys = module.getRedisKey().split(DPConfig.FIELD_JOIN_SPLIT);
				String redisKeyValue = "";
				for (String redisKey : redisKeys) {
					redisKeyValue += eventData.get(redisKey) + module.getRedisKeyJoin();
				}
				if (!redisKeyValue.isEmpty()) {
					redisKeyValue = redisKeyValue.substring(0,
							redisKeyValue.length() - module.getRedisKeyJoin().length());
				}
				String redisCache = redisService.getCacheMapValue(module.getRedisIndex(), redisKeyValue);
				for (String field : fields) {
					// if (!redisCache.isEmpty()) {
					eventData.put(field, config.getMatchValue());
//					} else {
//						eventData.remove(field);
//					}
				}
			}
		} catch (Exception e) {
			log.info("处理Event:{}的{}信息丰富化失败!", event.getEventId(), moduleName);
			log.error("", e);
		}
	}

}
