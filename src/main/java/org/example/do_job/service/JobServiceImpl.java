package org.example.do_job.service;

import lombok.RequiredArgsConstructor;
import org.example.do_job.vo.JobRequestVO;
import org.example.job.MyJob;
import org.quartz.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements  JobService{

    private final Scheduler scheduler;


    @Override
    public void startJob(JobRequestVO param) {
         JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                 .withIdentity(param.getJobName(), param.getJobGroup())
                 .usingJobData("jobName", param.getJobName())
                 .usingJobData("jobHourPerWeek", param.getJobHourPerWeek())
                 .build();

         Trigger trigger = TriggerBuilder.newTrigger()
                 .withIdentity(param.getTriggerName(), param.getTriggerGroup())
                 .startNow()
                 .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                         .withIntervalInSeconds(10)
                         .repeatForever())
                 .build();

         //scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    public void stopJob(JobRequestVO param) {

    }

    @Override
    public void updateJob(JobRequestVO param) {

    }

    @Override
    public void deleteJob(JobRequestVO param) {

    }
}
