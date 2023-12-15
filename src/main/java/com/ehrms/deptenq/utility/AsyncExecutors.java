package com.ehrms.deptenq.utility;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class AsyncExecutors {
	
//	 final int cpus = Runtime.getRuntime().availableProcessors();
	
	@Bean
	public ExecutorService getExe() {
		
		return Executors.newWorkStealingPool();
	}

}
