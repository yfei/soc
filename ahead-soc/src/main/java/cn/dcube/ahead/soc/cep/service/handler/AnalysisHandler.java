package cn.dcube.ahead.soc.cep.service.handler;

import com.espertech.esper.client.EventBean;

import cn.dcube.ahead.soc.cep.model.CEPEvent;
import cn.dcube.ahead.soc.cep.output.OutPutInfor;
import cn.dcube.ahead.soc.cep.service.AbstractCepEvtHandler;
import cn.dcube.ahead.soc.cep.service.CEPEventSrv;
import cn.dcube.ahead.soc.util.Constant;
import cn.dcube.goku.net.protobuf.PB_Base;
import lombok.extern.slf4j.Slf4j;

/**
 * 统计分析事件handler。output的内容为*,生成的CEP有基线事件、告警事件、统计事件(给第三方准备，本平台不需要入库)、异常事件。
 * 
 * @author yangfei
 *
 */
// FIXME 理论上基线事件、告警事件、统计事件、异常事件应该分开策略处理。但是历史原因，都是*开头的,暂不分开
@Slf4j
public class AnalysisHandler extends AbstractCepEvtHandler implements Runnable {

	public AnalysisHandler(CEPEventSrv cepEventSrv, OutPutInfor outputinfor) {
		super(cepEventSrv, outputinfor);
	}

	@Override
	public void run() {
		try {
			EventBean[] events = outputinfor.getEvtbean();
			log.info("esper output. EventBean:{}", events.toString());
			int num = events.length;
			log.info("newEvents size :" + num);

			EventBean event = null;

			CEPEvent pb_cepevt = new CEPEvent();

			// output的内容为*,生成的CEP有基线事件、告警事件、统计事件(给第三方准备)、异常事件。
			// 对于基线类,一般(活跃资产部分不是)生成CEP但是告警引擎不分析;告警事件和异常事件,生成CEP并且告警引擎分析
			// 统计事件,直接发送给第三方
			for (int i = 0; i < num; i++) {
				event = events[i];
				if (outputinfor.getSubclass() == Constant.SUBCLASS_BASELINE) { // 基线类
					// 基线事件,生成正常的CEP,但是在Alarm那里直接入库,不分析
					if (outputinfor.getRuleobject().getIsGene() != 0) {
						AggregateOutput(event, outputinfor, pb_cepevt);
						setCepId(pb_cepevt);
						sendCepEvt(pb_cepevt);
						log.info("send aggregate cep to alarm, cep_s:" + pb_cepevt.getCeps() + ", cep_f:"
								+ pb_cepevt.getCepf() + ", startTime:" + pb_cepevt.getStarttime() + ", endTime:"
								+ pb_cepevt.getEndtime());
					}
				} else { // 发送给alarm，让alarm入库
					if (outputinfor.getRuleobject().getIsGene() != 0) {
						AggregateOutput(event, outputinfor, pb_cepevt);
						setCepId(pb_cepevt);
						sendCepEvt(pb_cepevt);
						log.info("send aggregate cep event of rule[" + outputinfor.getRuleid() + "] to alarm, subclass:"
								+ pb_cepevt.getSubclass() + ", family:" + pb_cepevt.getFamily() + ", cep_id:"
								+ pb_cepevt.getCepid() + ", startTime:" + pb_cepevt.getStarttime() + ", endTime:"
								+ pb_cepevt.getEndtime() + ",tag:" + pb_cepevt.getTag());
					}
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
