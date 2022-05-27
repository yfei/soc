package cn.dcube.ahead.soc.dw.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.soc.dw.config.DWConfig;
import lombok.extern.slf4j.Slf4j;

@Service
@ConditionalOnBean(DWConfig.class)
@Slf4j
public class DWScheduleService {

	/**
	 * 定时触发索引列表更新
	 */
	// TODO
	public void initIndices() {
		log.debug("定时触发更新索引列表");
	}

}
