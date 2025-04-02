package org.example.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

@Slf4j
public class CustomSchedulerListener implements SchedulerListener {
    @Override
    public void jobScheduled(Trigger trigger) {
        log.info("[{}] JOB SCHEDULED", trigger.getKey());
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        log.info("[{}] JOB이 스케줄에서 제거됨", triggerKey);
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {

    }

    @Override
    public void triggersPaused(String s) {

    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {

    }

    @Override
    public void triggersResumed(String s) {

    }

    @Override
    public void jobAdded(JobDetail jobDetail) {

    }

    @Override
    public void jobDeleted(JobKey jobKey) {

    }

    @Override
    public void jobPaused(JobKey jobKey) {

    }

    @Override
    public void jobsPaused(String s) {

    }

    @Override
    public void jobResumed(JobKey jobKey) {

    }

    @Override
    public void jobsResumed(String s) {

    }

    @Override
    public void schedulerError(String s, SchedulerException e) {
        log.info("[{}] SCHEDULE ERROR {}",e.getCause() ,s);
    }

    @Override
    public void schedulerInStandbyMode() {

    }

    @Override
    public void schedulerStarted() {
        log.info("SCHEDULE START");
    }

    @Override
    public void schedulerStarting() {

    }

    @Override
    public void schedulerShutdown() {
        log.info("SCHEDULE END");
    }

    @Override
    public void schedulerShuttingdown() {

    }

    @Override
    public void schedulingDataCleared() {

    }
}
