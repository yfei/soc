package cn.dcube.ahead.soc.cep.config;

import lombok.Data;

@Data
public class StatisticsESConfig {
	
	private boolean enable;
	
	private String index;
	
	private int shards;
	
	private int replicas;

}
