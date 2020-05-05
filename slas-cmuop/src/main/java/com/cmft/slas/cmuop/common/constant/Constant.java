package com.cmft.slas.cmuop.common.constant;

public interface Constant {

    /**
     * 系统英文简称 编码 配置中心文件夹和文件名称
     */
    String SYSTEM_CODE = "orgm";

    String Y = "Y";

    String N = "N";

    String ZHAOWEN = "000";

    String ZHAOWENNAME = "招闻天下";
    /**
     * 系统日志类型常量.
     * 
     * @author xiaojp001
     *
     */
    interface SysLogType {
        String ADD = "ADD";
        String UPDATE = "UPDATE";
        String DELETE = "DELETE";
        String DOWNLOAD = "DOWNLOAD";
        // String VIEW = "VIEW";
        // String QUERYPAGE = "QUERYPAGE";
        // String QUERYALL = "QUERYALL";
    }
}
