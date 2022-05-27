package cn.dcube.ahead.soc.alarm.merge;

import cn.dcube.ahead.soc.alarm.AlarmEngine;
import cn.dcube.ahead.soc.cep.model.CEPEvent;

public interface IEventMergeSrv {
	
	public void mergeCep(CEPEvent cepevt) ;
	
	public void setAlarmEngine(AlarmEngine alarmEngine);

}
