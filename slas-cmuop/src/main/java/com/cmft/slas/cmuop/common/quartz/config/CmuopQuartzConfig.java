package com.cmft.slas.cmuop.common.quartz.config;

import com.cmft.slas.cmuop.common.quartz.CmuopQuartzJobFactory;
import com.cmft.slas.cmuop.common.utils.EncryptUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置任务调度中心 [QRTZ_JOB_DETAILS], [QRTZ_TRIGGERS] and [QRTZ_CRON_TRIGGERS]
 *
 * 应用启动时自动加载job
 *
 * @author lance
 */
@Configuration
public class CmuopQuartzConfig {
    private final static Logger logger = LoggerFactory.getLogger(CmuopQuartzConfig.class);

    static String noPrefix = Hex.encodeHexString("NOPFX:".getBytes());

    @Autowired
    private CmuopQuartzJobFactory quartzJobFactory;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String jdbcUser;

    @Value("${spring.datasource.password}")
    private String jdbcpwd;


    @Bean("cmuopSchedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        // 延时启动(秒)
        schedulerFactoryBean.setStartupDelay(10);
        // 设置quartz的配置文件
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        // 设置自定义Job Factory，用于Spring管理Job bean
        schedulerFactoryBean.setJobFactory(quartzJobFactory);

        schedulerFactoryBean.setAutoStartup(true);

        return schedulerFactoryBean;
    }

    @Bean("cmuopScheduler")
    public Scheduler scheduler() throws IOException {
        return schedulerFactoryBean().getScheduler();
    }

    /**
     * 设置quartz属性
     *
     * @throws IOException 2016年10月8日下午2:39:05
     */
    @Bean("cmuopProperties")
    public Properties quartzProperties() throws IOException {
        /*
         * 密码解密
         */
        if (StringUtils.isNotBlank(jdbcpwd) && jdbcpwd.startsWith("encrypt:")) {
            String cipherPwd = jdbcpwd.split(":")[1];
            jdbcpwd = decodePassword(cipherPwd);
        }

        Properties prop = new Properties();
        prop.put("quartz.scheduler.instanceName", "ServerScheduler");
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        // prop.put("org.quartz.scheduler.instanceId", "NON_CLUSTERED");

        prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
        prop.put("org.quartz.scheduler.jobFactory.class", "org.quartz.simpl.SimpleJobFactory");
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        prop.put("org.quartz.jobStore.dataSource", "quartzCmuopDataSource");
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        prop.put("org.quartz.jobStore.isClustered", "true");
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        // 多线程
        prop.put("org.quartz.threadPool.threadCount", "3");

        prop.put("org.quartz.dataSource.quartzCmuopDataSource.driver", "com.mysql.jdbc.Driver");
        prop.put("org.quartz.dataSource.quartzCmuopDataSource.URL", jdbcUrl);
        prop.put("org.quartz.dataSource.quartzCmuopDataSource.user", jdbcUser);
        prop.put("org.quartz.dataSource.quartzCmuopDataSource.password", jdbcpwd);
        prop.put("org.quartz.dataSource.quartzCmuopDataSource.maxConnections", "10");
//        prop.put("org.quartz.dataSource.quartzCmuopDataSource.discardIdleConnectionsSeconds", "60");
        return prop;
    }

    protected static String decodePassword(String cipherPwd) {
        String subStr = cipherPwd.substring(noPrefix.length());
        try {
            byte[] data = EncryptUtil.decryptByPrivateKey(Hex.decodeHex(subStr.toCharArray()),
                    EncryptUtil.getPrivateKey("UMPassword"));
            return new String(data);
        } catch (Exception e) {
            logger.error("store password decode error ", e);
            System.exit(2);
        }
        return null;

    }
}
