package org.example.scheduler;

import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class JobScheduler {

    protected final Scheduler scheduler;

    public abstract JobDetail getJobDetail();

    public abstract Trigger getJobTrigger(JobDetail myJobDetail) throws SchedulerException;

    public void addTrigger(JobDetail myJobDetail, Trigger myJobTrigger) throws SchedulerException {
        scheduler.scheduleJob(myJobDetail, myJobTrigger);
    }
}
