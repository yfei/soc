package cn.dcube.ahead.soc.alarm.config;

import lombok.Data;

/**
 * 告警关闭配置
 * @author yangfei
 *
 */
@Data
public class AlarmCloseConfig {
	
	// 是否定时关闭告警
	private boolean enable = false;
	
	// 关闭周期
	private int period = 7;

}
