package cn.dcube.ahead.soc.alarm.task;

import cn.dcube.ahead.elastic.service.ElasticService;
import cn.dcube.ahead.soc.cep.model.CEPEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CepEventTask implements Runnable {

	private CEPEvent cepevt;

	private ElasticService esService;

	public CepEventTask(ElasticService esService, CEPEvent cepevt) {
		this.cepevt = cepevt;
		this.esService = esService;
	}

	@Override
	public void run() {
		try {
			log.debug("cepevt save,cepevtid:{}, alarmlogid:{}", cepevt.getCepid(), cepevt.getAlarmlogid());
			esService.getElasticTemplate().save(cepevt);
		} catch (Exception e) {
			log.error("CepEventTask save cep error:", e);
		}
	}
}
