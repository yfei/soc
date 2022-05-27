package cn.dcube.ahead.soc.dw.config;

import lombok.Data;

@Data
public class DWIndexConfig {
	
	// 名称
	private String name;
	
	// 是否按月分表
	private boolean split;
	
	// 分表字段
	private String splitField;

}
