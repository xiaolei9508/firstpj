package com.cmft.slas.cmuop.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import okhttp3.*;
import okhttp3.Request.Builder;

/**
 * HTTP 请求工具类
 * 
 * @author xiaojp001
 *
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private final static OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * 设置请求头
     * 
     * @param token
     * @return
     */
    public static Map<String, String> setHeaders(String token) {
        StringBuffer BearerToken = new StringBuffer();
        BearerToken.append("Bearer ").append(token);
        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("Authorization", BearerToken.toString());
        headerMap.put("Content-Type", "application/json");
        return headerMap;
    }

    public static String setHeaders(Map<String, String> headerMap) {
        if (StringUtils.isNotBlank(headerMap.get("Content-Type"))) {
            headerMap.put("Content-Type", "application/json");
        }
        return JSON.toJSONString(headerMap);
    }

    /**
     * 
     * post 请求
     * 
     * @param url
     * @param bodyJson
     * @param headerMap
     * @return
     * @throws Exception
     */
    public static String post(String url, String bodyJson, Map<String, String> headerMap) throws Exception {

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyJson);

        Builder builder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(headerMap)) {
            headerMap.forEach((name, value) -> builder.addHeader(name, value));
        }
        Request request = builder.post(requestBody).build();
        Response response = okHttpClient.newCall(request).execute();
        ResponseBody body = null;
        if (response.code() == HttpStatus.SC_OK) {
            body = response.body();
        } else if (response.code() == HttpStatus.SC_UNAUTHORIZED) {
            logger.error("request error, url:{}, code:{}, msg{}", url, response.code(), response.body());
            return String.valueOf(response.code());
        } else {
            logger.error("request error, url:{}, code:{}, msg{}", url, response.code(), response.body());
        }
        return resolver(body);
    }

    /**
     * get 请求
     * 
     * @param url
     * @param headerMap
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String, String> headerMap) throws Exception {
        Builder builder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(headerMap)) {
            headerMap.forEach((name, value) -> builder.addHeader(name, value));
        }
        Request request = builder.get().build();

        Response response = okHttpClient.newCall(request).execute();
        ResponseBody body = null;
        if (response.code() == HttpStatus.SC_OK) {
            body = response.body();
        } else if (response.code() == HttpStatus.SC_UNAUTHORIZED) {
            logger.error("request error, url:{}, code:{}, msg{}", url, response.code(), response.body());
            return String.valueOf(response.code());
        } else {
            logger.error("request error, url:{}, code:{}, msg{}", url, response.code(), response.body());
        }
        return resolver(body);
    }

    /**
     * 解析返回值
     * 
     * @param responseBody
     * @return
     */
    public static String resolver(ResponseBody responseBody) {
        InputStream is = null;
        String result = null;
        if (responseBody != null) {
            try {
                is = responseBody.byteStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String body = null;
                StringBuilder sb = new StringBuilder();
                while ((body = br.readLine()) != null) {
                    sb.append(body);
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                logger.error("resolver error, msg{}", ExceptionUtils.getStackTrace(e));
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {
                    logger.error("resolver final error, msg{}", ExceptionUtils.getStackTrace(e));
                }
            }
        }

        return result;
    }

    /**
     * post 无返回body 根据code确认调用成功
     * 
     * @param url
     * @param bodyJson
     * @param headerMap
     * @return
     * @throws Exception
     */
    public static boolean postWithNoReponseBody(String url, String bodyJson, Map<String, String> headerMap)
        throws Exception {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyJson);

        Builder builder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(headerMap)) {
            headerMap.forEach((name, value) -> builder.addHeader(name, value));
        }
        Request request = builder.post(requestBody).build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.code() == HttpStatus.SC_OK) {
            return true;
        } else {
            logger.error("request error, url:{}, code:{}, msg{}", url, response.code(), response.body());
        }
        return false;
    }

    public static String postXMLAuthWithHttpPost(String url, String xmlStr, String AUTH_KEY) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpEntity entity = null;
        HttpEntity Rentity = null;
        String retStr = "";
        CloseableHttpResponse response = null;
        try {
            entity = new StringEntity(xmlStr, "UTF-8");
            HttpPost hp = new HttpPost(url);
            hp.addHeader("Content-Type", "application/soap+xml;charset=UTF-8");
            if (StringUtils.isNotBlank(AUTH_KEY)) {
                hp.addHeader("AUTH_KEY", AUTH_KEY);
            }

            hp.setEntity(entity);
            response = httpclient.execute(hp);
            Rentity = response.getEntity();
            if (Rentity != null) {
                retStr = EntityUtils.toString(Rentity, "UTF-8");
            }
        } catch (Exception e) {
            logger.error("使用HttpPost传输XML格式字符串失败：" + e);
        } finally {
            // 关闭连接,释放资源
            try {
                if (response != null)
                    response.close();
                if (httpclient != null)
                    httpclient.close();
            } catch (IOException e) {
            }
        }
        return retStr;
    }
}
