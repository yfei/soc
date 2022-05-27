package cn.dcube.ahead.soc.cep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.soc.cep.config.CEPConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务
 * 
 * @author yangfei
 *
 */
@Service
@ConditionalOnBean(CEPConfig.class)
@Slf4j
public class CEPScheduleService {

	@Autowired
	private CEPStatisticsSrv statisticsSrv;
	
	public CEPScheduleService() {
		log.info("启动CEP定时任务服务!");
	}

	// TODO
	/**
	 * 定时删除统计信息
	 */
	@Scheduled(cron = "${soc.cep.statistics.clean-cron}")
	@ConditionalOnExpression("${soc.cep.statistics.clean:false}")
	public void cleanStatistics() {
		statisticsSrv.clean();
	}

}
