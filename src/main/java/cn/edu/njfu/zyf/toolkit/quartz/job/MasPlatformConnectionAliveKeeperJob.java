package cn.edu.njfu.zyf.toolkit.quartz.job;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.njfu.zyf.toolkit.service.MasPlatformConnectionAliveService;
import cn.edu.njfu.zyf.toolkit.utils.ApplicationContextUtil;

public class MasPlatformConnectionAliveKeeperJob implements Job {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        MasPlatformConnectionAliveService service = ApplicationContextUtil.getBean(MasPlatformConnectionAliveService.class);
        boolean connectionAlive = service.keepConnectionAlive();
        if (connectionAlive) {
            logger.info("Connection to MAS platform alive. Good news.");

        } else {
            logger.error("Connection to MAS platform dead. Set a new JSESSIONID ASAP.");

        }
    }

    public static JobDetail getJobDetail() {
        return JobBuilder.newJob(MasPlatformConnectionAliveKeeperJob.class)
                .withIdentity("connectionKeepAlive")
                .build();
    }
    
    public static Trigger getTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(MasPlatformConnectionAliveKeeperJob.getJobDetail())
                .withIdentity("connectionKeepAlive")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 0/1 ? * * *"))
                .build();
    }

}
