package cn.edu.njfu.zyf.toolkit.quartz;


import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cn.edu.njfu.zyf.toolkit.quartz.job.TeachingStaffBirthdayCongratDailySendingJob;


@Component
public class TeachingStaffBirthdayCongratDailySendingScheduler implements ApplicationListener<ApplicationReadyEvent> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        JobDetail jobDetail = TeachingStaffBirthdayCongratDailySendingJob.getJobDetail();
        Trigger trigger = TeachingStaffBirthdayCongratDailySendingJob.getTrigger();
        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();        
            logger.info("TeachingStaffBirthdayCongratDailySendingScheduler has started birthday jobs.");

        } catch(SchedulerException e) {
            logger.error("** Failed to schedule birthday jobs. **");
            logger.error("{}", e);
        }
    }

}
