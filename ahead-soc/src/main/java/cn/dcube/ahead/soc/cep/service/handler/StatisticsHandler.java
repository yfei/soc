package cn.dcube.ahead.soc.cep.service.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.espertech.esper.client.EventBean;

import cn.dcube.ahead.soc.cep.model.CEPStatistics;
import cn.dcube.ahead.soc.cep.output.OutPutInfor;
import cn.dcube.ahead.soc.cep.service.AbstractCepEvtHandler;
import cn.dcube.ahead.soc.cep.service.CEPEventSrv;
import cn.dcube.goku.commons.ip.IpClassify;
import cn.dcube.goku.commons.util.JodaUtil;
import cn.dcube.goku.commons.util.StringUtils;
import cn.dcube.goku.net.protobuf.PB_Query;
import lombok.extern.slf4j.Slf4j;

/**
 * 统计handler。output以&开头，subclass=0,主要针对仪表盘和5分钟统计表
 * 
 * @author yangfei
 *
 */
@Slf4j
public class StatisticsHandler extends AbstractCepEvtHandler implements Runnable {

	public StatisticsHandler(CEPEventSrv cepEventSrv, OutPutInfor outputinfor) {
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

			PB_Query.CepEvent.Builder pb_cepevt = PB_Query.CepEvent.newBuilder();

			for (int i = 0; i < num; i++) { // 处理事件统计结果
				event = events[i];
				if (outputinfor.getSubclass() == cepEventSrv.getConfig().getStatistics().getSubclass()) {
					if (outputinfor.getFamily() == cepEventSrv.getConfig().getStatistics().getFamily()) {
						// 统计结果存储5分钟统计表
						statistics(event, outputinfor);
					}
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}

	}

	private void statistics(EventBean event, OutPutInfor outputinfor) throws Exception {
		// 构造统计对象
		CEPStatistics cepStatistics = new CEPStatistics();
		cepStatistics.setTime(new Date());
		cepStatistics.setPlatformId(Integer.parseInt(event.get("platformid").toString()));
		cepStatistics.setCustomerId(Integer.parseInt(event.get("customerid").toString()));
		cepStatistics.setOrgId(Integer.parseInt(event.get("orgid").toString()));
		cepStatistics.setRuleId(outputinfor.getRuleid());
		if (event.get("srcorgid") != null) {
			cepStatistics.setSrcOrgid(Integer.parseInt(event.get("srcorgid").toString()));
		}
		if (event.get("dstorgid") != null) {
			cepStatistics.setDstOrgid(Integer.parseInt(event.get("dstorgid").toString()));
		}

		String mapping = outputinfor.getRuleobject().getColmapping();
		if (StringUtils.isEmpty(mapping)) {
			log.warn("colmapping for rule[{}] is empty!", outputinfor.getRuleid());
		}
		JSONObject mappingObj = JSON.parseObject(mapping);
		String[] outputArr = outputinfor.getOutput();
		for (int i = 0; i < outputArr.length; i++) {
			// CEP事件选择输出项
			String out = outputArr[i].trim();
			if (mappingObj.containsKey(out) && event.get(out) != null) {
				String eventValue = event.get(out).toString();
				if (out.equals("srcip") || out.equals("dstip")) {
					eventValue = IpClassify.int2StrIp(Integer.valueOf(eventValue));
				}
				String col = mappingObj.getString(out);
				Object value = null;
				Class<?> clazz = cepStatistics.getClass().getDeclaredField(col).getType();
				if ("java.lang.String".equals(clazz.getName())) {
					value = String.valueOf(eventValue);
				} else if ("java.lang.Byte".equals(clazz.getName())) {
					value = Byte.valueOf(eventValue);
				} else if ("java.lang.Short".equals(clazz.getName())) {
					value = Short.valueOf(eventValue);
				} else if ("java.lang.Integer".equals(clazz.getName())) {
					value = Integer.valueOf(eventValue);
				} else if ("java.lang.Long".equals(clazz.getName())) {
					value = Long.valueOf(eventValue);
				} else if ("java.lang.Float".equals(clazz.getName())) {
					value = Float.valueOf(eventValue);
				} else if ("java.lang.Double".equals(clazz.getName())) {
					value = Double.valueOf(eventValue);
				} else if ("java.lang.Boolean".equals(clazz.getName())) {
					value = Boolean.valueOf(eventValue);
				} else {
					throw new Exception("不支持的数据类型!");
				}
				String method = "set" + StringUtils.upperFirst(col);
				cepStatistics.getClass().getMethod(method, clazz).invoke(cepStatistics, value);
			}
		}

		// 判断规则是否是累加的
		if (1 == outputinfor.getRuleobject().getAccumulative()) {
			String accumulationUnit = outputinfor.getRuleobject().getAccumulationUnit();
			if (accumulationUnit == null) { // 默认年
				accumulationUnit = "Y";
			}
			if ("Y".equals(accumulationUnit)) {
				cepStatistics.setAccumulationTime(JodaUtil.currentDate("yyyy"));
			} else if ("M".equals(accumulationUnit)) {
				cepStatistics.setAccumulationTime(JodaUtil.currentDate("yyyy-MM"));
			} else if ("D".equals(accumulationUnit)) {
				cepStatistics.setAccumulationTime(JodaUtil.currentDate("yyyy-MM-dd"));
			} else {
				throw new Exception("不支持的累加周期!");
			}
			String accumulativeCols = outputinfor.getRuleobject().getAccumulationCol();
			String[] accumulativeColArr = accumulativeCols.split("\\|");
			String accumulativeColAfterTrans = "";
			for (String accumulativeCol : accumulativeColArr) {
				if (StringUtils.isNotBlank(accumulativeCol)) {
					if (mappingObj.containsKey(accumulativeCol)) {
						accumulativeColAfterTrans += mappingObj.getString(accumulativeCol) + "|";
					} else {
						accumulativeColAfterTrans += accumulativeCol + "|";
					}
				} else {
					throw new Exception("不明累加列【" + accumulativeCol + "】!");
				}
			}
			if (accumulativeColAfterTrans.endsWith("|")) {
				accumulativeColAfterTrans = accumulativeColAfterTrans.substring(0,
						accumulativeColAfterTrans.length() - 1);
			}
			cepStatistics.setAccumulationCol(accumulativeColAfterTrans);
			// 累加统计列
			String groupkey = outputinfor.getRuleobject().getAccumulationGroupkey();
			String[] groupkeyArr = new String[] {};
			if (StringUtils.isNotEmpty(groupkey)) {
				groupkeyArr = groupkey.split("\\|");
			}
			Map<String, Object> params = new HashMap<>();
			params.put("ruleId", outputinfor.getRuleid());
			params.put("accumulationTime", cepStatistics.getAccumulationTime());
			for (String groupkeyStr : groupkeyArr) {
				if (mappingObj.containsKey(groupkeyStr)) {
					params.put(mappingObj.getString(groupkeyStr), event.get(groupkeyStr));
				} else {
					params.put(groupkeyStr, event.get(groupkeyStr));
				}
			}
			cepEventSrv.getStatisticsSrv().saveAccumulative(params, cepStatistics);
		} else {
			cepEventSrv.getStatisticsSrv().save(cepStatistics);
		}
	}

}
