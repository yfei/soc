package cn.dcube.ahead.soc.cep.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.soc.cep.CEPEngine;
import cn.dcube.ahead.soc.cep.config.CEPConfig;
import cn.dcube.ahead.soc.cep.output.OutPutInfor;
import cn.dcube.ahead.soc.cep.service.handler.AnalysisHandler;
import cn.dcube.ahead.soc.cep.service.handler.ComplexAnalysisHandler;
import cn.dcube.ahead.soc.cep.service.handler.StatisticsHandler;
import cn.dcube.goku.commons.util.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * esper输出事件的业务处理
 * 
 * @author yangfei
 *
 */
@Slf4j
@Service
@ConditionalOnBean(CEPConfig.class)
public class CEPEventSrv {

	@Autowired
	@Getter
	private CEPConfig config;

	@Autowired
	@Getter
	private CEPEngine cepEngine;

	@Autowired
	@Getter
	private CEPStatisticsSrv statisticsSrv;
	

	public void cepEvtDeal(OutPutInfor outputinfor) {
		String[] outputArr = outputinfor.getOutput();
		log.info("output :" + outputArr[0]);
		try {
			if ("*".equals(outputArr[0])) {
				boolean complex = false;
				if (outputinfor.getPreid() != null) {
					for (String preid : outputinfor.getPreid()) {
						if (StringUtils.isNotEmpty(preid) && !"0".equals(preid)) {
							complex = true;
						}
					}
				}
				if (complex) {
					log.info("使用复杂规则{}分析", outputinfor.getRuleid());
					// handlerMap.get("complex").handle(outputinfor);
					cepEngine.getEpserOutEventLoop().submit(new ComplexAnalysisHandler(this, outputinfor));
				} else {
					cepEngine.getEpserOutEventLoop().submit(new AnalysisHandler(this, outputinfor));
				}
			} else if ("&".equals(outputArr[0])) {
				cepEngine.getEpserOutEventLoop().submit(new StatisticsHandler(this, outputinfor));
			} else {
				log.error("非法的输出前置符号!");
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
