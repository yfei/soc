package cn.dcube.ahead.soc.cep.service;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.dcube.ahead.elastic.service.ElasticService;
import cn.dcube.ahead.soc.cep.config.CEPConfig;
import cn.dcube.ahead.soc.cep.mapper.CEPStatisticsMapper;
import cn.dcube.ahead.soc.cep.model.CEPStatistics;
import cn.dcube.goku.commons.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * CEP统计服务
 * 
 * @author yangfei
 *
 */
@Service
@ConditionalOnBean(CEPConfig.class)
@Slf4j
public class CEPStatisticsSrv {

	@Autowired
	private CEPConfig config;

	@Autowired
	private CEPStatisticsMapper mapper;

	@Autowired
	private ElasticService esService;

	public void save(CEPStatistics cepStatistics) {
		try {
			log.info("save cepStatistics of rule[{}]", cepStatistics.getRuleId());
			mapper.insert(cepStatistics);
			// 同时存储es
			if (config.getStatistics().getEs().isEnable()) {
				esService.getElasticTemplate().save(cepStatistics);
			}

		} catch (Exception e) {
			log.error("save cepStatistics of rule[{}] error!{}", cepStatistics.getRuleId(), e);
		}
	}

	public void saveAccumulative(Map<String, Object> params, CEPStatistics cepStatistics) {
		try {
			QueryWrapper<CEPStatistics> qw = new QueryWrapper<CEPStatistics>();
			for(Entry<String,Object> entry:params.entrySet()) {
				String colName = entry.getKey();
				TableField tf = cepStatistics.getClass().getDeclaredField(entry.getKey()).getAnnotation(TableField.class);
				if (tf!=null) {
					colName = tf.value();
				}
				qw.eq(colName, entry.getValue());
			}
			CEPStatistics db = mapper.selectOne(qw);
			if (db == null) {
				// insert
				this.save(cepStatistics);
			} else {
				// update 累加列
				cepStatistics.setId(db.getId());
				log.info("update cepStatistics of rule[{}]", cepStatistics.getRuleId());
				String accumulativeCols = cepStatistics.getAccumulationCol();
				String[] accumulativeColArr = accumulativeCols.split("\\|");
				for (String accumulativeCol : accumulativeColArr) {
					String setMethod = "set" + StringUtils.upperFirst(accumulativeCol);
					String getMethod = "get" + StringUtils.upperFirst(accumulativeCol);
					Class<?> clazz = cepStatistics.getClass().getDeclaredField(accumulativeCol).getType();
					Object existValue = db.getClass().getMethod(getMethod).invoke(db);
					Object currentValue = cepStatistics.getClass().getMethod(getMethod).invoke(cepStatistics);
					if ("java.lang.Short".equals(clazz.getName()) || "java.lang.Integer".equals(clazz.getName())
							|| "java.lang.Long".equals(clazz.getName())) {
						cepStatistics.getClass().getMethod(setMethod, clazz).invoke(cepStatistics,
								(Long) existValue + (Long) currentValue);

					} else if ("java.lang.Float".equals(clazz.getName())
							|| "java.lang.Double".equals(clazz.getName())) {
						cepStatistics.getClass().getMethod(setMethod, clazz).invoke(cepStatistics,
								(Double) existValue + (Double) currentValue);
					} else {
						log.warn("累加字段为{},不是有效的数值类型", accumulativeCol);
					}
				}
				mapper.updateById(cepStatistics);

			}
		} catch (Exception e) {
			log.error("saveAccumulative cepStatistics of rule[{}] error!{}", cepStatistics.getRuleId(), e);
		}
	}

	public void clean() {
		try {
			log.info(">>>>>>> clean cepStatistics");
			mapper.clean();
		} catch (Exception e) {
			log.error("clean cepStatistics error!{}", e);
		}
	}
}
