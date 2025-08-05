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

	private static final String IDENTITY = "connectionKeepAlive";
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        MasPlatformConnectionAliveService service = ApplicationContextUtil.getBean(MasPlatformConnectionAliveService.class);
        boolean connectionAlive = service.checkOrKeepConnectionAlive();
        if (connectionAlive) {
            logger.info("Kept connection to MAS platform alive. Good news.");

        } else {
            logger.error("Connection to MAS platform dead. Set a new JSESSIONID ASAP.");

        }
    }

    public static JobDetail getJobDetail() {
        return JobBuilder.newJob(MasPlatformConnectionAliveKeeperJob.class)
                .withIdentity(IDENTITY)
                .build();
    }
    
    /**
     * 测试结果：
     * 每30分钟访问一次，MAS平台的session会过期；
     * 每20分钟访问一次，MAS平台的session会过期；
     * 每15分钟访问一次，MAS平台的session不会过期。
     * 看起来过期时间在15~20min之间。
     * 设置每15分钟访问一次。
     * @return a cron trigger
     */
    public static Trigger getTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(MasPlatformConnectionAliveKeeperJob.getJobDetail())
                .withIdentity(IDENTITY)
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/15 0/1 ? * * *"))
                .build();
    }

}
