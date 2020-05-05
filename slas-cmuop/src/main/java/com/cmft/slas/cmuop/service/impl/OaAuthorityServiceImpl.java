package com.cmft.slas.cmuop.service.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmft.slas.cmuop.common.utils.DESEncryptUtil;
import com.cmft.slas.cmuop.common.utils.DownloadFileUtil;
import com.cmft.slas.cmuop.common.utils.HttpUtil;
import com.cmft.slas.cmuop.common.utils.SignVerifyUtil;
import com.cmft.slas.cmuop.dto.OaAuthorityDTO;
import com.cmft.slas.cmuop.entity.OaAuthority;
import com.cmft.slas.cmuop.mapper.OaAuthorityMapper;
import com.cmft.slas.cmuop.service.OaAuthorityService;
import com.cmft.slas.common.utils.BeanMapper;

@Service("oaAuthorityService")
public class OaAuthorityServiceImpl implements OaAuthorityService {

    private static final Logger logger = LoggerFactory.getLogger(OaAuthorityServiceImpl.class);

    @Autowired
    private OaAuthorityMapper oaAuthorityMapper;

    @Value("${oa.password-url}")
    private String oaPasswordUrl;

    @Value("${oa.auth-url}")
    private String oaAuthUrl;

    @Value("${oa.data-path}")
    private String oaDataPath;

    @Value("${nuc.callerModule}")
    private String callerModule;

    @Value("${nuc.app-key}")
    private String appKey;

    @Value("${nuc.secret-key}")
    private String secretKey;

    @Value("${nuc.organizeFile}")
    private String nucOrganizeFile;

    @Override
    public int deleteOaAuthority(Long id) {
        return oaAuthorityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int addOaAuthority(String uid) {
        ResultSet psSet = null;
        ResultSet oaSet = null;
        Connection connection = null;
        try {
            String dirver = "org.sqlite.JDBC";
            Class.forName(dirver);
            String filePath = oaDataPath.concat(File.separator).concat("origin_bak.db");
            File file = new File(filePath);
            if (!file.exists()) {
                getNucDataFile();
            }
            String url = "jdbc:sqlite:".concat(filePath);
            connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            psSet = statement.executeQuery("select ps_id from paas_uc_staff where uid = '" + uid + "'");
            oaAuthorityMapper.updateByUid(uid);
            while (psSet.next()) {
                String psId = psSet.getString("ps_id");
                oaSet = statement.executeQuery("select account_id from nuc_oa_account where user_id = '" + psId + "'");
                while (oaSet.next()) {
                    String accountId = oaSet.getString("account_id");
                    String oaPwd = getOaPassword(accountId);
                    int i = oaPwd.indexOf("<result>");
                    int j = oaPwd.indexOf("</result>");
                    String pwdResult = oaPwd.substring(i + 8, j);
                    String auth = getAuth(accountId, pwdResult.trim());
                    int i1 = auth.indexOf("&lt;authAreaId>");
                    int j1 = auth.indexOf("&lt;/authAreaId>");
                    String authAreaId = auth.substring(i1 + 15, j1);
                    String[] authAreaIds = new String[0];
                    if (StringUtils.isNotBlank(authAreaId)) {
                        authAreaIds = authAreaId.split(",");
                    }
                    int i2 = auth.indexOf("&lt;authReader>");
                    int j2 = auth.indexOf("&lt;/authReader>");
                    String authReader = auth.substring(i2 + 15, j2);
                    String[] authReaders = new String[0];
                    if (StringUtils.isNotBlank(authReader)) {
                        authReaders = authReader.split(",");
                    }
                    for (String authArea : authAreaIds) {
                        for (String authRead : authReaders) {
                            OaAuthorityDTO oaAuthorityDTO = new OaAuthorityDTO();
                            oaAuthorityDTO.setUid(uid);
                            oaAuthorityDTO.setPsId(psId);
                            oaAuthorityDTO.setOaId(accountId);
                            oaAuthorityDTO.setAuthAreaId(authArea.trim());
                            oaAuthorityDTO.setAuthReader(authRead.trim());
                            oaAuthorityMapper.insertSelective(BeanMapper.map(oaAuthorityDTO, OaAuthority.class));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("通过解密文件查询用户的oa权限发生错误: " + ExceptionUtils.getStackTrace(e));
        } finally {
            closeConnection(oaSet, psSet, connection);
        }
        return 1;
    }

    @Override
    public List<OaAuthorityDTO> queryOaAuthority() {
        return BeanMapper.mapList(oaAuthorityMapper.queryByCondition(new OaAuthorityDTO()), OaAuthorityDTO.class);
    }

    @Override
    public void synNucDataFile() {
        getNucDataFile();
    }

    // 获取oa账号密码
    private String getOaPassword(String accountId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.pda.third.kmss.landray.com/\">")
            .append("<soapenv:Header/>").append("<soapenv:Body>").append("<ser:ekp_getNewsPaawd>").append("<userName>")
            .append(accountId).append("</userName>").append("</ser:ekp_getNewsPaawd>").append("</soapenv:Body>")
            .append("</soapenv:Envelope>");
        return HttpUtil.postXMLAuthWithHttpPost(oaPasswordUrl, stringBuilder.toString(), null);
    }

    // 获取文章权限
    private String getAuth(String oaUser, String pwd) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.pda.third.kmss.landray.com/\">")
            .append("<soapenv:Header>").append("<tns:RequestSOAPHeader xmlns:tns=\"http://sys.webservice.client\">")
            .append("<tns:user xmlns=\"http://sys.webservice.client\">").append(oaUser).append("</tns:user>")
            .append("<tns:password xmlns=\"http://sys.webservice.client\">").append(pwd).append("</tns:password>")
            .append("</tns:RequestSOAPHeader>").append("</soapenv:Header>").append("<soapenv:Body>")
            .append("<ser:ekp_getAuthlist>").append("</ser:ekp_getAuthlist>").append("</soapenv:Body>")
            .append("</soapenv:Envelope>");
        return HttpUtil.postXMLAuthWithHttpPost(oaAuthUrl, stringBuilder.toString(), "NEWS");
    }

    // 关闭流
    private void closeConnection(ResultSet psSet, ResultSet oaSet, Connection connection) {
        try {
            if (null != psSet) {
                psSet.close();
            }
            if (null != oaSet) {
                oaSet.close();
            }
            if (null != connection) {
                connection.close();
            }
        } catch (Exception e) {
            logger.error("close connection is error");
        }
    }

    public void getNucDataFile() {
        String filePathBak = "";
        Map bodyMap = new HashMap();
        String timestamp = System.currentTimeMillis() + "";
        String sign = SignVerifyUtil.generateSign(bodyMap, timestamp, appKey, secretKey);
        Map headerMap = new HashMap<>();
        headerMap.put("signature", sign);
        headerMap.put("timestamp", timestamp);
        headerMap.put("CallerModule", callerModule);
        headerMap.put("App-Key", appKey);
        try {
            String organizeDataFileString = HttpUtil.get(nucOrganizeFile, headerMap);
            logger.info("调用nuc组织接口的返回数据为：" + organizeDataFileString);
            if (StringUtils.isNotBlank(organizeDataFileString)) {
                JSONObject jsonObject = JSON.parseObject(organizeDataFileString);
                if ("1".equals(jsonObject.getString("status"))) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (null != data) {
                        String fileSecretKey = data.getString("fileSecretKey");
                        String fileDownloadUrl = data.getString("fileDownloadUrl");
                        logger.info("nuc组织文件的下载路径为：" + fileDownloadUrl);
                        String filePath = DownloadFileUtil.downloadFile(fileDownloadUrl, oaDataPath);
                        logger.info("文件下载完成，返回的文件路径为：" + filePath);
                        String[] split = filePath.split("\\.");
                        if (split.length > 0) {
                            filePathBak = split[0].concat("_bak").concat(".db");
                        }
                        DESEncryptUtil desEncryptUtil = new DESEncryptUtil(fileSecretKey);
                        bakFile(filePathBak);
                        boolean b = desEncryptUtil.decrypt(filePath, filePathBak);
                        if (b) {
                            File deleteFile = new File(filePathBak.concat("_delete"));
                            if (deleteFile.exists()) {
                                deleteFile.delete();
                            }
                        }
                        logger.info("nuc组织文件同步成功");
                    }
                }
            }
        } catch (Exception e) {
            saveFile(filePathBak);
            logger.error("同步nuc文件发生错误：" + ExceptionUtils.getStackTrace(e));
        }
    }

    // 备份文件
    private void bakFile(String filePathBak) {
        if (StringUtils.isNotBlank(filePathBak)) {
            File originFile = new File(filePathBak);
            if (originFile.exists()) {
                File bakFile = new File(filePathBak.concat("_delete"));
                originFile.renameTo(bakFile);
            }
        }
    }

    // 还原文件
    private void saveFile(String filePathBak) {
        File file = new File(filePathBak);
        if (file.exists()) {
            file.delete();
        }
        if (StringUtils.isNotBlank(filePathBak)) {
            String deletePath = filePathBak.concat("_delete");
            File originFile = new File(filePathBak);
            File deleteFile = new File(deletePath);
            if (deleteFile.exists()) {
                deleteFile.renameTo(originFile);
            }
        }
    }
}
