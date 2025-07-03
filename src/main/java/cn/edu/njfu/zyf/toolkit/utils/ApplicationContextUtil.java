package cn.edu.njfu.zyf.toolkit.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationContextUtil {
    
    private static Logger logger = LoggerFactory.getLogger(ApplicationContextUtil.class);
    private static ConfigurableApplicationContext ac = null;

    public static ApplicationContext getApplicationContext() {
        return ac;
    }

    public static void setApplicationContext(ConfigurableApplicationContext ac1) {
        ac = ac1;
        logger.info("ApplicationContext set into ApplicationContextUtil.");
    }
    
    public static <T> T getBean(Class<T> clazz) {
        return ac == null ? null : ac.getBean(clazz);
    }
}
