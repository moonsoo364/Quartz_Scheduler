package org.example.job;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class MyJobScheduler {

    private final Scheduler scheduler;

    @Bean
    public JobDetail myJobDetail(){
        return JobBuilder
                .newJob(MyJob.class)
                .withIdentity("myJob","group1")
                .usingJobData("jobSays", "Hello World!") //JobDataMap형식으로 데이터를 넣음
                .usingJobData("myFloatValue", 3.141f)
                .storeDurably()// JobDetail을 영구적으로 저장하도록 설정하는 메서드, 트리거가 삭제되더라도 JobDetail이 삭제되지 않고 스케줄러에 남아있다
                .build();
    }

    @Bean
    public Trigger myJobTrigger(JobDetail myJobDetail){
        return TriggerBuilder.newTrigger()
                .forJob(myJobDetail)
                .withIdentity("myTrigger","group1")
                .startAt(new Date())// 즉시 시작
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(10) // 10초 마다 실행
                                .repeatForever())
                .build();
    }

    @Bean
    public Trigger addTrigger(JobDetail myJobDetail, Trigger myJobTrigger) throws SchedulerException {
        // schedulerJob 메서드가 호출되어야 실제로 스케줄이 실행됨
        scheduler.scheduleJob(myJobDetail, myJobTrigger);
        return myJobTrigger; // 반환 타입을 Trigger로 변경
    }

}
