package cn.dcube.ahead.soc.cep.service;

import com.espertech.esper.client.EventBean;

import cn.dcube.ahead.base.util.StringUtils;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.commons.proto.transport.EventTypeEnum;
import cn.dcube.ahead.soc.cep.CEPEngine;
import cn.dcube.ahead.soc.cep.model.CEPEvent;
import cn.dcube.ahead.soc.cep.output.OutPutInfor;
import cn.dcube.ahead.soc.util.IDGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractCepEvtHandler {

	protected CEPEventSrv cepEventSrv;

	protected CEPEngine cepEngine;

	protected OutPutInfor outputinfor;

	public AbstractCepEvtHandler(CEPEventSrv cepEventSrv, OutPutInfor outputinfor) {
		super();
		this.cepEventSrv = cepEventSrv;
		this.cepEngine = cepEventSrv.getCepEngine();
		this.outputinfor = outputinfor;
	}

	// 设置基本信息
	protected void setCepInfo(OutPutInfor outputinfor, CEPEvent cepevt) {
		// 引用规则
		cepevt.setRuleid(outputinfor.getRuleid());
		// cep_s
		cepevt.setCeps(outputinfor.getSubclass());
		// cep_f
		cepevt.setCepf(outputinfor.getFamily());
		// level
		cepevt.setCeplevel(outputinfor.getLevel());
		// tag
		cepevt.setTag(outputinfor.getRuleobject().getTag());
	}

	// 设置必填信息
	protected void setCepBaseInfo(EventBean event, OutPutInfor outputinfor, CEPEvent cepevt, String prefix) {
		setCepInfo(outputinfor, cepevt);

		if (outputinfor.getSubclass() == 2 && outputinfor.getFamily() == 0) {
			int level = Integer.parseInt(event.get("maxlevel").toString());
			if (level == 2 || level == 3) {
				level = 2;
			} else if (level == 4 || level == 5) {
				level = 3;
			}
			cepevt.setCeplevel(level);
		} else {
			// 对于非默认事件,如果原始事件的level较小,则使用原始事件定义的level
			int maxlevel = Integer.parseInt(event.get("maxlevel").toString());
			if (maxlevel < outputinfor.getLevel()) {
				cepevt.setCeplevel(maxlevel);
			}
		}

		// CEP事件必须输出项
		cepevt.setPlatformid(Integer.parseInt(event.get(prefix + "platformid").toString()));
		cepevt.setCustomerid(Integer.parseInt(event.get(prefix + "customerid").toString()));
		cepevt.setOrgid(Integer.parseInt(event.get(prefix + "orgid").toString()));
		cepevt.setClazz(Integer.parseInt(event.get(prefix + "classes").toString()));
		cepevt.setSubclass(Integer.parseInt(event.get(prefix + "subclass").toString()));
		cepevt.setFamily(Integer.parseInt(event.get(prefix + "family").toString()));
		// 开始时间为第一个事件的时间
		cepevt.setStarttime(event.get("minstarttime").toString());
		// 结束时间为最后一个事件的时间
		cepevt.setEndtime(event.get("maxendtime").toString());
		// 最后一条EventBase的sensormask
		cepevt.setSensormask(Integer.parseInt(event.get(prefix + "sensormask").toString()));

		// 最后一条EventBase的IA标记相关属性字段
		cepevt.setBadsrcip(event.get(prefix + "badsrcip").toString());
		cepevt.setBaddstip(event.get(prefix + "baddstip").toString());
		cepevt.setBadcode(event.get(prefix + "badcode").toString());
		cepevt.setBadurl(event.get(prefix + "badurl").toString());
		cepevt.setBadmailfrom(event.get(prefix + "badmailfrom").toString());
		cepevt.setBadmailto(event.get(prefix + "badmailto").toString());
		cepevt.setBadmailcc(event.get(prefix + "badmailcc").toString());
		cepevt.setBadmailbcc(event.get(prefix + "badmailbcc").toString());

		int eventNum = Integer.parseInt(event.get("num").toString());
		int eventCount = Integer.parseInt(event.get("event_count").toString());
		cepevt.setEventnum(eventNum);
		cepevt.setEventcount(eventCount);
		log.info("cep AggregateOutput event_num :" + eventNum + ", event_count :" + eventCount + ",start_time:"
				+ cepevt.getStarttime() + ",end_time:" + cepevt.getEndtime());
	}

	protected void setSrcInfo(EventBean event, CEPEvent cepevt, String prefix) {
		cepevt.setSrcorgid(Integer.parseInt(event.get(prefix + "srcorgid").toString()));
		cepevt.setSrcin(Integer.parseInt(event.get(prefix + "srcin").toString()));
		cepevt.setSrcGeo1Code(event.get(prefix + "srcGeo1Code").toString());
		cepevt.setSrcGeo2Code(event.get(prefix + "srcGeo2Code").toString());
		cepevt.setSrcGeo3Code(event.get(prefix + "srcGeo3Code").toString());
		cepevt.setSrcGeo1Name(event.get(prefix + "srcGeo1Name").toString());
		cepevt.setSrcGeo2Name(event.get(prefix + "srcGeo2Name").toString());
		cepevt.setSrcGeo3Name(event.get(prefix + "srcGeo3Name").toString());
		cepevt.setSrcLongitude(event.get(prefix + "srcLongitude").toString());
		cepevt.setSrcLatitude(event.get(prefix + "srcLatitude").toString());
		cepevt.setSrcassetid(Integer.parseInt(event.get(prefix + "srcassetid").toString()));
		cepevt.setSrcassetlevel(Integer.parseInt(event.get(prefix + "srcassetlevel").toString()));
		cepevt.setSrcbusiid(Integer.parseInt(event.get(prefix + "srcbusiid").toString()));
	}

	protected void setDstInfo(EventBean event, CEPEvent cepevt, String prefix) {
		cepevt.setDstorgid(Integer.parseInt(event.get(prefix + "dstorgid").toString()));
		cepevt.setDstin(Integer.parseInt(event.get(prefix + "dstin").toString()));
		cepevt.setDstGeo1Code(event.get(prefix + "dstGeo1Code").toString());
		cepevt.setDstGeo2Code(event.get(prefix + "dstGeo2Code").toString());
		cepevt.setDstGeo3Code(event.get(prefix + "dstGeo3Code").toString());
		cepevt.setDstGeo1Name(event.get(prefix + "dstGeo1Name").toString());
		cepevt.setDstGeo2Name(event.get(prefix + "dstGeo2Name").toString());
		cepevt.setDstGeo3Name(event.get(prefix + "dstGeo3Name").toString());
		cepevt.setDstLongitude(event.get(prefix + "dstLongitude").toString());
		cepevt.setDstLatitude(event.get(prefix + "dstLatitude").toString());
		cepevt.setDstassetid(Integer.parseInt(event.get(prefix + "dstassetid").toString()));
		cepevt.setDstassetlevel(Integer.parseInt(event.get(prefix + "dstassetlevel").toString()));
		cepevt.setDstbusiid(Integer.parseInt(event.get(prefix + "dstbusiid").toString()));
	}

	protected void setOutput(EventBean event, OutPutInfor outputinfor, CEPEvent cepevt, String prefix) {
		int count = 0;
		int srcipv4 = Integer.parseInt(event.get(prefix + "srcip").toString());
		String srcipv6 = event.get(prefix + "srcipv6").toString();

		int dstipv4 = Integer.parseInt(event.get(prefix + "dstip").toString());
		String dstipv6 = event.get(prefix + "dstipv6").toString();
		String[] outputArr = outputinfor.getOutput();
		for (int i = 0; i < outputArr.length; i++) {
			// CEP事件选择输出项
			String out = outputArr[i].trim();
			if ("srcip".equals(out)) {
				count = Integer.parseInt(event.get("ip4_count").toString());
				// 源IP\org\地理位置
				cepevt.setSrcip(srcipv4);
				setSrcInfo(event, cepevt, prefix);
				if (count == 0 || count == 1) { // count==0，按照源和目的合并；count==1，说明目的只有一个
					cepevt.setDstip(dstipv4);
					setDstInfo(event, cepevt, prefix);
				} else if (count > 1) {
					cepevt.setDstip(0);
				}
			} else if ("srcipv6".equals(out)) {
				count = Integer.parseInt(event.get("ip6_count").toString());
				cepevt.setSrcipv6(srcipv6);
				setSrcInfo(event, cepevt, prefix);

				if (count == 0 || count == 1) {
					cepevt.setDstipv6(dstipv6);
					setDstInfo(event, cepevt, prefix);
				} else if (count > 1) {
					cepevt.setDstipv6("");
				}
			} else if ("srcport".equals(out)) {
				cepevt.setSrcport(Integer.parseInt(event.get(prefix + "srcport").toString()));
			} else if ("srcorgid".equals(out)) {
				cepevt.setSrcorgid(Integer.parseInt(event.get(prefix + "srcorgid").toString()));
			} else if ("dstip".equals(out)) {
				count = Integer.parseInt(event.get("ip4_count").toString());
				cepevt.setDstip(dstipv4);
				setDstInfo(event, cepevt, prefix);
				if (count == 0 || count == 1) {
					cepevt.setSrcip(srcipv4);
					setSrcInfo(event, cepevt, prefix);
				} else if (count > 1) {
					cepevt.setSrcip(0);
				}
			} else if ("dstipv6".equals(out)) {
				count = Integer.parseInt(event.get("ip6_count").toString());
				cepevt.setDstipv6(dstipv6);
				setDstInfo(event, cepevt, prefix);

				if (count == 0 || count == 1) {
					cepevt.setSrcipv6(srcipv6);
					setSrcInfo(event, cepevt, prefix);
				} else if (count > 1) {
					cepevt.setSrcipv6("");
				}
			} else if ("dstport".equals(out)) {
				cepevt.setDstport(Integer.parseInt(event.get(prefix + "dstport").toString()));
			} else if ("dstorgid".equals(out)) {
				cepevt.setDstorgid(Integer.parseInt(event.get(prefix + "dstorgid").toString()));
			} else if ("sensormodel".equals(out)) {
				cepevt.setSensormodel(Integer.parseInt(event.get(prefix + "sensormodel").toString()));
				cepevt.setSensormask(Integer.parseInt(event.get(prefix + "sensormask").toString()));
			} else if ("sensorid".equals(out)) {
				cepevt.setSensorid(Integer.parseInt(event.get(prefix + "sensorid").toString()));
			} else if ("sensorip".equals(out)) {
				cepevt.setSensorip(event.get(prefix + "sensorip").toString());
			} else if ("account".equals(out)) {
				cepevt.setAccount(event.get(prefix + "account").toString());
			} else if ("protocol".equals(out)) {
				cepevt.setProtocol(Integer.parseInt(event.get(prefix + "protocol").toString()));
			} else if ("hole_mark".equals(out)) {
				cepevt.setHolemark(event.get(prefix + "holemark").toString());
			} else if ("badsrcip".equals(out)) {
				cepevt.setBadsrcip(event.get(prefix + "badsrcip").toString());
			} else if ("baddstip".equals(out)) {
				cepevt.setBaddstip(event.get(prefix + "baddstip").toString());
			} else if ("badurl".equals(out)) {
				cepevt.setBadurl(event.get(prefix + "badurl").toString());
			} else if ("badcode".equals(out)) {
				cepevt.setBadcode(event.get(prefix + "badcode").toString());
			} else if ("badmailfrom".equals(out)) {
				cepevt.setBadmailfrom(event.get(prefix + "badmailfrom").toString());
			} else if ("badmailto".equals(out)) {
				cepevt.setBadmailto(event.get(prefix + "badmailto").toString());
			} else if ("badmailcc".equals(out)) {
				cepevt.setBadmailcc(event.get(prefix + "badmailcc").toString());
			} else if ("badmailbcc".equals(out)) {
				cepevt.setBadmailbcc(event.get(prefix + "badmailbcc").toString());
			} else if ("badother".equals(out)) {
				cepevt.setBadother(event.get(prefix + "badother").toString());
			} else if ("c".equals(out)) {
				cepevt.setC(Integer.parseInt(event.get(prefix + "c").toString()));
			} else if ("i".equals(out)) {
				cepevt.setI(Integer.parseInt(event.get(prefix + "i").toString()));
			} else if ("a".equals(out)) {
				cepevt.setA(Integer.parseInt(event.get(prefix + "a").toString()));
			}
		}
	}

	protected void AggregateOutput(EventBean event, OutPutInfor outputinfor, CEPEvent cepevt) {
		log.info("聚合规则id为{}", outputinfor.getRuleid());
		setCepBaseInfo(event, outputinfor, cepevt, "");
		setOutput(event, outputinfor, cepevt, "");
	}

	protected boolean AggregateOutputComplex(EventBean event, OutPutInfor outputinfor, CEPEvent cepevt) {
		log.info("聚合规则id为{}", outputinfor.getRuleid());
		if (outputinfor.getSubclass() == 2 && outputinfor.getFamily() == 70) {
			// 非法改密,如果eventbase.id不存在,则获取T的信息作为cep基本信息
			if (event.get("eventbase.eventid") != null) {
				return false;
			}
			String prefix = "T" + outputinfor.getPreid().get(0) + ".";
			setCepBaseInfo(event, outputinfor, cepevt, prefix);
			setOutput(event, outputinfor, cepevt, prefix);
		} else {
			setCepBaseInfo(event, outputinfor, cepevt, "eventbase.");
			setOutput(event, outputinfor, cepevt, "eventbase.");
		}
		return true;
	}

	protected void setCepId(CEPEvent pbcepevt) {
		// 如果CEP存储cep对象的话，这里set cepid
		
		pbcepevt.setCepid(IDGenerator.getId((byte)cepEventSrv.getConfig().getId()));
	}

	protected void sendCepEvt(CEPEvent cepevt) {
		// 发送事件
		String sendTopics = cepEngine.getKafkaTopicConfig().getProducerTopic("cep-event", "sdap-eventbase-cep");
		for (String sendTopic : sendTopics.split(",")) {
			EventTransportEntity event = new EventTransportEntity(StringUtils.getUUID(), sendTopic,
					EventTypeEnum.CEPEVENT.getCode(), cepevt);
			cepEngine.getProducer().sendMessage(sendTopic, MessageType.TRANSPORT, event);
		}
	}

}
