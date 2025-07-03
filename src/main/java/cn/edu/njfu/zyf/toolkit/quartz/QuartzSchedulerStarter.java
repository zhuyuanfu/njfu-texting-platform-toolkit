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

import cn.edu.njfu.zyf.toolkit.quartz.job.MasPlatformConnectionAliveKeeperJob;


@Component
public class QuartzSchedulerStarter implements ApplicationListener<ApplicationReadyEvent> {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        JobDetail jobDetail = MasPlatformConnectionAliveKeeperJob.getJobDetail();
        Trigger trigger = MasPlatformConnectionAliveKeeperJob.getTrigger();
        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();        
            logger.info("Quartz scheduler has been started.");

        } catch(SchedulerException e) {
            logger.error("{}", e);
        }
    }

}
