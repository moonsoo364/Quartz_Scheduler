package org.example.scheduler;

import org.example.job.MyJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Date;

@Configuration
public class MyJobScheduler extends JobScheduler {

    public MyJobScheduler(Scheduler scheduler) {
        super(scheduler);
    }

    @Bean(name="myJobDetail")
    public JobDetail getJobDetail() {
        return JobBuilder
                .newJob(MyJob.class)
                .withIdentity("myJob","group1")
                .usingJobData("jobName", "Spring App Management") //JobDataMap형식으로 데이터를 넣음
                .usingJobData("jobHourPerWeek", 40.0f)
                .withDescription("The number of hours I work per week at the company")
                .storeDurably()// JobDetail을 영구적으로 저장하도록 설정하는 메서드, 트리거가 삭제되더라도 JobDetail이 삭제되지 않고 스케줄러에 남아있다
                .build();
    }

    @Bean(name="myJobTrigger")
    public Trigger getJobTrigger(@Qualifier("myJobDetail") JobDetail jobDetail) throws SchedulerException {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("myTrigger","group1")
                .startAt(new Date())// 즉시 시작
                .withSchedule(
                        //CronScheduleBuilder.cronSchedule("0/10 * * * * ?")
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(10) // 10초 마다 실행
                                .repeatForever())
                .build();
        addTrigger(jobDetail, trigger);
        return trigger;
    }

}
