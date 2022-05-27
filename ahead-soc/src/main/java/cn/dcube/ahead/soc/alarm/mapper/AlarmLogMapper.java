package cn.dcube.ahead.soc.alarm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.dcube.ahead.soc.alarm.config.AlarmConfig;
import cn.dcube.ahead.soc.alarm.model.AlarmLog;

@Mapper
@Repository
@ConditionalOnBean(AlarmConfig.class)
public interface AlarmLogMapper extends BaseMapper<AlarmLog>{

}
