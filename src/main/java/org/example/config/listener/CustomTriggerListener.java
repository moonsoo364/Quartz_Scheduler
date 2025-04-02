package org.example.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

@Slf4j
public class CustomTriggerListener implements TriggerListener {
    @Override
    public String getName() {
        return "CustomTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.info("[{}] TRIGGER START", trigger.getKey());
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        // 실행을 막을 경우 false로 반환
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        log.info("[{}] TRIGGER FAIL",trigger.getKey());
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        log.info("[{}] TRIGGER SUCCESS",trigger.getKey());
    }
}
