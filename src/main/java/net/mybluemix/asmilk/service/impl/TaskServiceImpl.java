package net.mybluemix.asmilk.service.impl;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import net.mybluemix.asmilk.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

	private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

	@Override
//	@Scheduled(fixedDelay = 5000)
	public void check() {
		long now = System.currentTimeMillis();
		LOG.info("now: {}", now);
		LOG.info("!!!TaskServiceImpl.check():{} -- start!!!", now);
		if(now % 2 == 0) {
			this.sync(now);
		}	
		LOG.info("!!!TaskServiceImpl.check():{} -- end!!!", now);
	}

	@Override
	@Async
	public void sync(long now) {
		LOG.info("!!!TaskServiceImpl.sync({}) -- start!!!", now);
		long millis = new Random().nextInt(20000) + 10000L;
		LOG.info("sleep: {}", millis);
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e.getCause());
		}
		LOG.info("!!!TaskServiceImpl.sync({}) -- end!!!", now);
	}

}
