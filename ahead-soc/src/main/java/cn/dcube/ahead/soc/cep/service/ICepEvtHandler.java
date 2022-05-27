package cn.dcube.ahead.soc.cep.service;

import cn.dcube.ahead.soc.cep.output.OutPutInfor;

public interface ICepEvtHandler {
	
	public void handle(OutPutInfor outputinfor) throws Exception;

}
