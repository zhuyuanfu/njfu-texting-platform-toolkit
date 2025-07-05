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

import cn.edu.njfu.zyf.toolkit.service.TeachingStaffService;
import cn.edu.njfu.zyf.toolkit.utils.ApplicationContextUtil;

public class TeachingStaffBirthdayCongratDailySendingJob implements Job {

    Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Will send daily birthday congratulation message to all related teaching staff.");

		TeachingStaffService service = ApplicationContextUtil.getBean(TeachingStaffService.class);
		String result = null;
		//result = service.sendTextMessageToBirthdayTeachingStaff();
		
		logger.info("Have sent daily birthday congratulation message to all related teaching staff. Result: {}", result);
	}

    public static JobDetail getJobDetail() {
        return JobBuilder.newJob(TeachingStaffBirthdayCongratDailySendingJob.class)
                .withIdentity("TeachingStaffBirthdayCongratDailySending")
                .build();
    }
    
    public static Trigger getTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(TeachingStaffBirthdayCongratDailySendingJob.getJobDetail())
                .withIdentity("TeachingStaffBirthdayCongratDailySending")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 8 ? * * *"))
                .build();
    }
}
