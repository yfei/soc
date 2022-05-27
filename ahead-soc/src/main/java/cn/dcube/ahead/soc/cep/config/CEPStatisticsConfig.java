package cn.dcube.ahead.soc.cep.config;

import lombok.Data;

@Data
public class CEPStatisticsConfig {

	// 统计小类
	private int subclass;

	// 统计子类
	private int family;

	// 是否存储到es
	private StatisticsESConfig es;

	// 是否定期清除
	private boolean clean = false;

	// 定期清除的cron表达式
	private String cleanCron = "0 0 0 * * ?";

}
