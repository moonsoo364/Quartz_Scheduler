package org.example.scheduler;

import org.example.job.HomeJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HomeJobScheduler extends JobScheduler{
    public HomeJobScheduler(Scheduler scheduler) {
        super(scheduler);
    }

    @Bean(name="homeJobDetail")
    public JobDetail getJobDetail() {
        return JobBuilder
                .newJob(HomeJob.class)
                .withIdentity("homeJob","group2")
                .usingJobData("jobName","Dish wash")
                .usingJobData("jobHourPerWeek",4.55f)
                .withDescription("The number of hours I work per week at the Home")
                .storeDurably()
                .build();
    }

    @Bean(name="homeJobTrigger")
    public Trigger getJobTrigger(@Qualifier("homeJobDetail") JobDetail jobDetail) throws SchedulerException {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("homeTrigger", "group2")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(5)
                                .repeatForever())
                .build();
        addTrigger(jobDetail, trigger);
        return trigger;
    }
}
