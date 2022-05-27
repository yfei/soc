package cn.dcube.ahead.soc.alarm.merge;

import cn.dcube.ahead.soc.alarm.cache.CEPEventCache;
import cn.dcube.ahead.soc.cep.model.CEPEvent;

public class EventMergeThread extends Thread {

	private IEventMergeSrv evtmerge;

	public EventMergeThread(IEventMergeSrv evtmerge) {
		this.evtmerge = evtmerge;
		this.setName("event merge");
	}

	@Override
	public void run() {
		while (true) {
			CEPEvent cepevt = CEPEventCache.take();
			if (evtmerge != null) {
				evtmerge.mergeCep(cepevt);
			}
		}
	}
}
