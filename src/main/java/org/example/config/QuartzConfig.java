package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.config.listener.CustomJobListener;
import org.example.config.listener.CustomSchedulerListener;
import org.example.config.listener.CustomTriggerListener;
import org.quartz.ListenerManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final DataSource dataSource;

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setJobFactory(jobFactory); // Bean으로 등록된 jobFactory 사용
        factoryBean.setDataSource(dataSource);

        /** 기존 Job이 등록되어 있더라도 덮어쓰기 */
        factoryBean.setOverwriteExistingJobs(true);

        /** 애플리케이션 종료 시 작업 완료까지 대기하는 설정 */
        factoryBean.setWaitForJobsToCompleteOnShutdown(true);

        return factoryBean;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean factory) throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        ListenerManager listenerManager = scheduler.getListenerManager();

        listenerManager.addJobListener(new CustomJobListener());
        listenerManager.addTriggerListener(new CustomTriggerListener());
        listenerManager.addSchedulerListener(new CustomSchedulerListener());

        return scheduler;
    }
}
