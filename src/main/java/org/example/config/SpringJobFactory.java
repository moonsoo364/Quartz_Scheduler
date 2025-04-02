package org.example.config;

import lombok.RequiredArgsConstructor;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringJobFactory extends SpringBeanJobFactory  {

    @Autowired
    private ApplicationContext applicationContext;

    /** Quartz 작업이 Spring의 관리 하에 있는 Bean으로 동작할 수 있도록 보장한다. */
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        /**Quartz 작업의 인스턴스를 생성한다. */
        Object job = super.createJobInstance(bundle);
        /**생성된 인스턴스에 Spring 의존성을 주입한다. */
        applicationContext.getAutowireCapableBeanFactory().autowireBean(job);
        return job;
    }
}
