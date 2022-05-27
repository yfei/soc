package cn.dcube.ahead.soc.dp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.soc.dp.config.DPConfig;
import cn.dcube.ahead.soc.dp.config.DPEventModuleConfig;
import cn.dcube.ahead.soc.dp.service.handler.IRefillHandler;

@Service
@ConditionalOnBean(DPConfig.class)
public class RefillContext {

	// 回填策略,key为moduleName
	private Map<String, IRefillHandler> handlerCategory = new HashMap<String, IRefillHandler>();

	@Autowired
	private List<IRefillHandler> handlers;

	@PostConstruct
	public void init() {
		for (IRefillHandler handler : handlers) {
			handlerCategory.put(handler.getCategory(), handler);
		}
	}

	public void handle(String moduleName, DPEventModuleConfig module, String topic, EventTransportEntity event) {
		if (handlerCategory.containsKey(moduleName)) {
			handlerCategory.get(moduleName).handle(moduleName, module, topic, event);
		} else {
			// 默认使用Common
			handlerCategory.get("COMMON").handle(moduleName, module, topic, event);
		}

	}

}
