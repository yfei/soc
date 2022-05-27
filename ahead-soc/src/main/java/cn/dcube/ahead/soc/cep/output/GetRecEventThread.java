package cn.dcube.ahead.soc.cep.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.dcube.ahead.soc.cep.service.CEPEventSrv;

/**
 * 事件处理线程
 * 
 * @author yangfei
 *
 */
public class GetRecEventThread extends Thread {

	private static final Logger LOGError = LogManager.getLogger("errorFile");

	private Logger logger = LogManager.getLogger("kernelFile");

	private CEPEventSrv cepevtsrv = null;

	private volatile boolean running = true;

	public GetRecEventThread(CEPEventSrv cepevtsrv) {
		this.setName("GetRecEventThread");
		this.cepevtsrv = cepevtsrv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (running) {
			try {
				OutPutInfor out = EventListener.getRecEvent();
				logger.info("GetRecEventThread run get OutputInfor:{}", out);
				// CEP的处理服务
				 cepevtsrv.cepEvtDeal(out);
			} catch (Exception e) {
				LOGError.error("GetRecEventThread run method error:", e);
			}
		}
	}

	public void shutdown() {
		running = false;
	}
}
