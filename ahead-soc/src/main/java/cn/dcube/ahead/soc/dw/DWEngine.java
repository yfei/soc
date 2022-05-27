package cn.dcube.ahead.soc.dw;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import cn.dcube.ahead.elastic.service.ElasticService;
import cn.dcube.ahead.soc.dw.config.DWConfig;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@ConditionalOnBean(DWConfig.class)
@Slf4j
public class DWEngine {

	@Getter
	private EventLoopGroup eventLoop;

	@Autowired
	@Getter
	private DWConfig config;

	// 已经存在的索引
	@Getter
	private List<String> indices;

	@Autowired
	@Getter
	private ElasticService esService;

	@Autowired
	RestHighLevelClient highLevelClient;

	@PostConstruct
	public void init() throws IOException {
		eventLoop = new NioEventLoopGroup(config.getThreads(), new DefaultThreadFactory("dw-handle"));
		log.info("初始化DWContext,线程池数量为" + config.getThreads());
		initIndex();
	}

	public void initIndex() throws IOException {
		indices = esService.getIndices("*");
	}

	/**
	 * 索引是否存在
	 * 
	 * @param index
	 * @return
	 */
	public boolean indexExists(String index) {
		if (indices != null && indices.contains(index)) {
			return true;
		}
		return false;
	}

}
