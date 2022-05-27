package cn.dcube.ahead.soc.cep.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.dcube.ahead.soc.cep.config.CEPConfig;
import cn.dcube.ahead.soc.cep.model.CEPStatistics;

@Mapper
@Repository
@ConditionalOnBean(CEPConfig.class)
public interface CEPStatisticsMapper extends BaseMapper<CEPStatistics> {

	// TODO ERROR
	@Select("{call delete_cep_statistics()}")
	@Options(statementType = StatementType.CALLABLE)
	public void clean();

}
