package cn.dcube.ahead.soc.cep.service.handler;

import java.util.List;

import com.espertech.esper.client.EventBean;

import cn.dcube.ahead.soc.cep.model.CEPEvent;
import cn.dcube.ahead.soc.cep.output.OutPutInfor;
import cn.dcube.ahead.soc.cep.service.AbstractCepEvtHandler;
import cn.dcube.ahead.soc.cep.service.CEPEventSrv;
import cn.dcube.goku.net.protobuf.PB_Base;
import cn.dcube.goku.net.protobuf.PB_Query;
import lombok.extern.slf4j.Slf4j;

/**
 * 复杂关联事件
 * 
 * @author yangfei
 *
 */
@Slf4j
public class ComplexAnalysisHandler extends AbstractCepEvtHandler implements Runnable {

	public static final String SPLIT = ",";

	public ComplexAnalysisHandler(CEPEventSrv cepEventSrv, OutPutInfor outputinfor) {
		super(cepEventSrv, outputinfor);
	}

	@Override
	public void run() {
		try {
			EventBean[] events = outputinfor.getEvtbean();
			log.info("esper output. EventBean:{}", events.toString());
			int num = events.length;
			log.info("newEvents size :" + num);

			CEPEvent pb_cepevt = new CEPEvent();
			for (int i = 0; i < num; i++) {
				EventBean event = events[i];
				boolean flag = AggregateOutputComplex(event, outputinfor, pb_cepevt);
				if (!flag) {
					// 如果使用left join的话，如果没有关联的，则直接返回
					continue;
				}
				setCepId(pb_cepevt);
				List<String> preid = outputinfor.getPreid();
				String str1 = "";
				String str2 = "";
				if (event.get("eventbase.eventid") != null) {
					str2 = str2 + event.get("eventbase.eventid").toString() + SPLIT;
				}
				for (String str : preid) {
					str1 = str1 + str + SPLIT;
					String key = "T" + str + ".eventid";
					String eventid = event.get(key).toString();
					str2 = str2 + eventid + SPLIT;
				}
				// 前置规则
				pb_cepevt.setStr1(str1);
				// 关联事件的id
				pb_cepevt.setStr2(str2);

				setCepId(pb_cepevt);
				sendCepEvt(pb_cepevt);
				log.info("send complex cep to alarm, subclass:" + pb_cepevt.getCeps() + ", family:"
						+ pb_cepevt.getCepf());
			}
		} catch (Exception e) {
			log.error("", e);
		}

	}

}
