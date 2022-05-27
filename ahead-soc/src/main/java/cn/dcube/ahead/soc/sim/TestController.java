package cn.dcube.ahead.soc.sim;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cn.dcube.ahead.base.date.JodaUtil;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity;
import cn.dcube.ahead.commons.proto.transport.EventTransportEntity.MessageType;
import cn.dcube.ahead.kafka.producer.KafkaProducer;
import cn.dcube.goku.net.protobuf.PB_Query.DOMAIN_TYPE;
import cn.dcube.goku.net.protobuf.PB_Query.Event;

@RestController
public class TestController {

	@Autowired
	private KafkaProducer producer;

	@Autowired
	private RestTemplate restTemplate;

	@ResponseBody
	@RequestMapping("/test")
	public void send() {
		Event.Builder b = Event.newBuilder();
		b.setId(Long.valueOf(1));
		b.setPlatformId(1);
		b.setCustomerId(2);
		b.setOrgId(3);
		b.setClazz(3);
		b.setSubclass(4);
		b.setFamily(12);
		b.setAccount("");
		b.setRawId(10180001);
		b.setRawLevel("");
		b.setRawName("");
		b.setLevel(2);
		b.setCollectorId(93);
		b.setCollectorIp("192.168.2.122");
		b.setSensorMask(8);
		b.setSensorModel(131072);
		b.setSensorId(10);
		b.setSensorIp("192.168.8.111");
		b.setSrcIpv4(993270914);
		b.setSrcIpv6("");
		b.setSrcOrgId(0);
		b.setSrcDomain(DOMAIN_TYPE.WAN);
		b.setSrcPort(38826);
		b.setDstIpv4(134744072);
		b.setDstIpv6("");
		b.setDstOrgId(0);
		b.setDstPort(53);
		b.setProtocol(4606);
		b.setStartTime(JodaUtil.formatDate(new Date()));
		b.setEndTime("2018-08-01 15:59:37");
		b.setReceiveTime("2018-08-01 16:06:36");
		b.setCounts(1);
		b.setSummary("malicious-domain-dns-query");
		b.setOriginal(
				"<14>1 2018-08-01T07:59:37.954Z 172.16.40.210 notice - - - 1533110377954^ATD^172.16.40.205^NDE^81712114-b43a-4a69-b121-14b43a0a69e6^p6p1^10180001^threat-intelligence-alarm^malicious-domain-dns-query^^59.52.28.130^38826^^8.8.8.8^53^dns^udp^dns^remote-control^^^3^^palevo (malware)^ilo.brenz.pl^family:palevo (malware);query:ilo.brenz.pl;answers:;qtype_name:A;^^China^Nanchang^28.55,115.9333^CN^United States^^37.751,-97.822^US^^^^^^^^^^^^^^^^^^ilo.brenz.pl^A^^^^^^^^^^^^^^^^^^^^^^^^^");
		b.setCve("无");
		b.setBugtraq("无");
		b.setUrl("ilo.brenz.pl");
		b.setDns("");
		b.setMailFrom("");
		b.setMailTo("");
		b.setMailCc("");
		b.setMailBcc("");

		b.setStr1("threat-intelligence-alarm");
		b.setStr2("");
		b.setStr3("");
		b.setStr4("");
		b.setStr5("dns");
		b.setStr6("dns");
		b.setStr7("");
		b.setStr8("");
		b.setStr9("");
		b.setStr10("");
		b.setNum1(0);
		b.setNum2(0);
		b.setNum3(0);
		b.setNum4(0);
		b.setNum5(0);
		b.setNum6(0);
		b.setNum7(0);
		b.setNum8(0);
		b.setNum9(0);
		b.setNum10(0);

		b.setC(0);
		b.setI(0);
		b.setA(0);
		b.setBadUrl("");
		b.setBadSrcip("");
		b.setBadDstip("");
		b.setBadMailFrom("");
		b.setBadMailTo("");
		b.setBadMailCc("");
		b.setBadMailBcc("");
		b.setBadCode("");

		b.setSrcGeo1Name("中国");
		b.setSrcGeo1Code("CN");
		b.setSrcGeo2Name("江西");
		b.setSrcGeo2Code("360000");
		b.setSrcGeo3Name("南昌");
		b.setSrcGeo3Code("360100");
		b.setDstGeo1Name("美国");
		b.setDstGeo1Code("US");
		b.setDstGeo2Name("加利福尼亚州");
		b.setDstGeo2Code("US05");
		b.setDstGeo3Name("芒廷维尤");
		b.setDstGeo3Code("US051486");
		b.setSrcLongitude("115.9333");
		b.setSrcLatitude("28.5500");
		b.setDstLongitude("-122.0838");
		b.setDstLatitude("37.3860");

		b.setBwlFlag(0);
		EventTransportEntity event = new EventTransportEntity(b.getId() + "", b.getSummary(),
				MessageType.PROTOBUF.name(), b.build());
		producer.sendProtobufMessage("ahead-soc-eventbase-collector", event);
	}

	@ResponseBody
	@RequestMapping("/echo")
	public String discover(String info) {
		System.out.println("i am provider>>>>" + info);
		return "provicer" + info;
	}

	@ResponseBody
	@RequestMapping("/echo2")
	public void discover2(String info) {
		String ss = restTemplate.getForObject("http://ahead-soc/echo?info=" + info, String.class);
		System.out.println(ss);
	}

}
