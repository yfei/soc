package cn.dcube.ahead.soc.cep.output;

import cn.dcube.ahead.soc.cep.rule.CB_RuleObject;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Esper输出监听
 * @author yangfei
 *
 */
public class EventListener implements UpdateListener {

	private static final Logger logger = LogManager.getLogger(EventListener.class);
	
	private CB_RuleObject ruleobject;

	private static LinkedBlockingQueue<OutPutInfor> evtdeq = new LinkedBlockingQueue<OutPutInfor>();

	public EventListener(CB_RuleObject ruleobject) {
		this.ruleobject = ruleobject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.espertech.esper.client.UpdateListener#update(com.espertech.esper.client
	 * .EventBean[], com.espertech.esper.client.EventBean[])
	 */
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents == null) {
			// we don't care about events leaving the window (old events)
			return;
		}
		OutPutInfor outputinfor = new OutPutInfor(newEvents, ruleobject);
		try {
			evtdeq.put(outputinfor);
			logger.info("EventLisenter put, outputinfor:{}, newEvents:{}", outputinfor, newEvents.toString());
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static OutPutInfor getRecEvent() {
		OutPutInfor outputinfor = null;
		try {
			outputinfor = evtdeq.take();
		} catch (Exception e) {
			logger.error("", e);
		}

		return outputinfor;
	}
}
