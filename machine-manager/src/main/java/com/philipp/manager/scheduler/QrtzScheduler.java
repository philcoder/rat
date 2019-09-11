package com.philipp.manager.scheduler;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.philipp.manager.util.AutoWiringSpringBeanJobFactory;

/**
 * Configuration do Quartz Scheduler
 * 
 * more info see: https://dzone.com/articles/adding-quartz-to-spring-boot
 * 
 * @author phili
 *
 */
@Configuration
public class QrtzScheduler {

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public SpringBeanJobFactory springBeanJobFactory() {
		AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();

		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean
	public JobDetailFactoryBean removeOldEntriesJob() {
		return createJobDetail(CheckOfflineMachines.class, "Remove Old Entries Job");
	}

	@Bean
	public SimpleTriggerFactoryBean removeOldEntriesTrigger(@Qualifier("removeOldEntriesJob") JobDetail job) {
		int frequencyRepeatInterval = 15;// 15s

		SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
		trigger.setJobDetail(job);
		trigger.setStartDelay(0L);

		trigger.setRepeatInterval(frequencyRepeatInterval * 1000);
		trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		trigger.setName("Remove Old Entries Job Trigger");
		return trigger;
	}

	private JobDetailFactoryBean createJobDetail(Class<? extends Job> jobClass, String jobName) {
		JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
		factoryBean.setName(jobName);
		factoryBean.setJobClass(jobClass);
		factoryBean.setDurability(true);
		return factoryBean;
	}
}
