package cn.dcube.ahead.soc.ia.config;

import java.util.List;

import lombok.Data;

@Data
public class IAEventModuleConfig {

	private boolean enable;

	private String type = "redis";

	private String redisKey;

	private String redisKeyJoin = IAConfig.KEY_SPLIT;

	private String redisIndex;

	private List<String> fields;

	private List<String> excludeTopics;

}
