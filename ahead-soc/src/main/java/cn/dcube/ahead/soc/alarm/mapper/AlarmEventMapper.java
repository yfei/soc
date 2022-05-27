package cn.dcube.ahead.soc.alarm.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.dcube.ahead.soc.alarm.config.AlarmConfig;
import cn.dcube.ahead.soc.alarm.model.AlarmEvent;

@Mapper
@Repository
@ConditionalOnBean(AlarmConfig.class)
public interface AlarmEventMapper extends BaseMapper<AlarmEvent>{
	
	@Update("<script> update alarm_event "
			+ "set state = 9 "
			+ "where state !=4 and state!=8 and state!=9 and state!=10 "
			+ "and (unix_timestamp(#{currentTime,jdbcType=TIMESTAMP})-unix_timestamp(start_time))>=#{limit,jdbcType=BIGINT}"
			+"</script>")
	public void updateStatus(@Param("currentTime")Date currentTime, @Param("limit")long limit);
	
	
	@Select("<script>select a.*, c.max, c.min, c.avg, c.total,"
			+ "c.output "
			+ "from ("
			+ "select "
			+ "id,platform_id,merge_type,customer_id,org_id,rule_id,level,"
			+ "src_ipv4, src_ipv6, src_orgid,src_assetid,src_assetlevel,src_in,src_busiid,src_busilink,src_list,"
			+ "dst_ipv4, dst_ipv6, dst_orgid,dst_assetid,dst_assetlevel,dst_in,dst_busiid,dst_busilink,dst_list,"
			+ "alarm_c,alarm_s,start_time,end_time,cep_num,state,upgrade_time,cep_type,ceptype_count,"
			+ "describ,ability,impact,direction,stage,sensor_mask,sensor_model,sensor_id,sensor_ip,"
			+ "iscontinuous,ip_count,sensor_count,confidence,relevancy,"
			+ "case when issendmail = '1' then '0' else issendmail end	issendmail,"
			+ "isupholemark,isupbadip,isupbadurl,isupbadcode,isupbadmail,isupbadother,isupasset,isupbusiness,"
			+ "num1,num2,num3,num4,num5,str1,str2,str3,str4,str5,"
			+ "src_geo1_code,src_geo1_name,src_geo2_code,src_geo2_name,src_geo3_code,src_geo3_name,"
			+ "dst_geo1_code,dst_geo1_name,dst_geo2_code,dst_geo2_name,dst_geo3_code,dst_geo3_name,"
			+ "src_longitude,src_latitude,dst_longitude,dst_latitude "
			+ "from alarm_event "
			+ "where "
			+ "state in (1, 2, 3, 6, 7)) a "
			+ "left join alarm_frequency c on c.id = a.id"
			+ "</script>")
	public List<AlarmEvent> selectComplexEvent();

}
