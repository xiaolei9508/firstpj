package com.cmft.slas.cmuop.quartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cmft.slas.cmuop.service.UserService;
import com.cmft.slas.cmuop.service.impl.OaAuthorityServiceImpl;

/**
 * 
 * 同步oa权限
 */

@DisallowConcurrentExecution
public class SynOaAuthorityJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${nuc.callerModule}")
    private String callerModule;

    @Value("${nuc.app-key}")
    private String appKey;

    @Value("${nuc.secret-key}")
    private String secretKey;

    @Value("${nuc.organizeFile}")
    private String nucOrganizeFile;

    @Value("${oa.data-path}")
    private String oaDataPath;

    @Autowired
    private OaAuthorityServiceImpl oaAuthorityService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("################## start syn oa data ##################");
        synOaFile();
        synOaData();
    }

    /**
     * 同步oa文件
     */
    private void synOaFile() {
        // oaAuthorityService.getNucDataFile();
    }

    /**
     * 同步oa数据
     */
    private void synOaData() {
        // List<String> userUidList = userService.queryUserUidList();
        // logger.info("开始同步用户信息，同步的用户个数为：" + userUidList.size());
        // for (String uid : userUidList) {
        // oaAuthorityService.addOaAuthority(uid);
        // }
    }
}
