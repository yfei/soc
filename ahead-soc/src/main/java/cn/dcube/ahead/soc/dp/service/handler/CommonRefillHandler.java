package cn.dcube.ahead.soc.dp.service.handler;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.redis.service.RedisService;
import cn.dcube.ahead.soc.dp.config.DPConfig;
import cn.dcube.ahead.soc.dp.config.DPEventModuleConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用回填服务
 * 
 * @author yangfei
 *
 */
@Service
@Slf4j
@ConditionalOnBean(DPConfig.class)
public class CommonRefillHandler implements IRefillHandler {

	@Autowired
	private RedisService redisService;

	@Override
	public String getCategory() {
		return "COMMON";
	}

	@Override
	public void handle(String moduleName, DPEventModuleConfig module, String topic, EventTransportEntity event) {
		try {
			if (module != null && module.isEnable()) {
				if (module.getExcludeTopics() != null && module.getExcludeTopics().contains(topic)) {
					log.debug("忽略Event:{}-{}的{}信息丰富化", topic, event.getEventId(), moduleName);
					return;
				}
				log.debug("处理Event:{}-{}的{}信息丰富化", topic, event.getEventId(), moduleName);
				// 处理数据
				Map<String, Object> eventData = (Map<String, Object>) event.getEventData();

				Map<String, String> fileds = module.getFields();
				// 判断是否为null,如果需要回填的属性都为空,或者需要覆盖回填时才回填
				boolean allNull = true; // 是否字段都为空,如果都为空的话
				for (Entry<String, String> filedEntry : fileds.entrySet()) {
					if (eventData.containsKey(filedEntry.getKey()) && eventData.get(filedEntry.getValue()) != null) {
						allNull = false;
					}
				}

				if (module.isFillIfNotNull() || !allNull) {
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
					if (redisCache != null && !redisCache.isEmpty()) {
						JSONObject json = JSON.parseObject(redisCache);
						for (Entry<String, String> propEntry : fileds.entrySet()) {
							try {
								// event属性
								String eventProp = propEntry.getKey();
								// redis属性
								String jsonProp = propEntry.getValue();
								if (json.get(jsonProp) != null) {
									// 属性字段
									eventData.put(eventProp, json.get(jsonProp));
								}
							} catch (Exception e) {
								log.warn("{}字段回填异常!{}", propEntry.getKey(), e.getMessage());
								log.warn("", e);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.warn("处理Event:{}的{}信息丰富化失败!", event.getEventId(), moduleName);
			log.error("", e);
		}
	}

}
