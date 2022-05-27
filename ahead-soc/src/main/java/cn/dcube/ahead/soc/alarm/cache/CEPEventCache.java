package cn.dcube.ahead.soc.alarm.cache;

import java.util.concurrent.LinkedBlockingQueue;

import cn.dcube.ahead.soc.cep.model.CEPEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * CEP事件缓存队列。kafka接收到消息后,放到缓存队列中
 * 
 * @author yangfei
 *
 */
@Slf4j
public class CEPEventCache {

	private static LinkedBlockingQueue<CEPEvent> eventQueue = new LinkedBlockingQueue<CEPEvent>();

	private CEPEventCache() {}

	private static final CEPEventCache cache = new CEPEventCache();

	public static CEPEventCache getInstance() {
		return cache;
	}

	public static void add(CEPEvent cepEvent) {
		try {
			eventQueue.add(cepEvent);
			log.debug("receive cepevt:" + cepEvent.getCepid());
		} catch (Exception e) {
			log.error("Alarm 接收cep事件队列已满，cepevt id：" + cepEvent.getCepid());
		}
	}

	public static CEPEvent take() {
		try {
			return eventQueue.take();
		} catch (InterruptedException e) {
			return null;
		}
	}

}
