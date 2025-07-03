package cn.edu.njfu.zyf.toolkit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import cn.edu.njfu.zyf.toolkit.quartz.QuartzSchedulerStarter;
import cn.edu.njfu.zyf.toolkit.utils.ApplicationContextUtil;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {
    public static void main( String[] args ){
        ConfigurableApplicationContext cac = SpringApplication.run(App.class, args);
        ApplicationContextUtil.setApplicationContext(cac);
    }
}
