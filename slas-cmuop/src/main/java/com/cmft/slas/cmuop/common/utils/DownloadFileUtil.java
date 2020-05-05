package com.cmft.slas.cmuop.common.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(DownloadFileUtil.class);

    public static String downloadFile(String fileDownloadUrl, String filePath) {
        String fileNamePath = "";
        String path = filePath;// 业务系统自定义存放db文件目录
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        InputStream inputStream = null;
        HttpURLConnection conn = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            logger.info("开始准备下载文件");
            URL httpUrl = new URL(fileDownloadUrl);
            conn = (HttpURLConnection)httpUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();
            logger.info("文件下载连接已经建立");
            inputStream = conn.getInputStream();
            bis = new BufferedInputStream(inputStream);
            path = path.concat(File.separator);
            fileNamePath = path.concat("origin.db");// 业务系统自定义下载文件名,文件后缀必须为 .db
            File file1 = new File(fileNamePath);
            if (file1.exists()) {
                file1.delete();
            }
            if (!file1.exists()) {
                file1.createNewFile();
            }
            logger.info("文件下载中");
            fileOut = new FileOutputStream(fileNamePath);
            bos = new BufferedOutputStream(fileOut);
            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            logger.info("文件下载成功");
        } catch (Exception e) {
            logger.error("文件下载失败：" + ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                if (null != bos) {
                    bos.close();
                }
                if (null != bis) {
                    bis.close();
                }
                if (null != conn) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                logger.error("文件下载流关闭出错：" + ExceptionUtils.getStackTrace(e));
            }
        }
        return fileNamePath;
    }
}
