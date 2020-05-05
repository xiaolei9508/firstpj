package com.cmft.slas.cmuop.common.utils;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

import com.alibaba.fastjson.JSON;

public class SignVerifyUtil {

    public static String generateSign(Map jsonMap, String timestamp, String appKey, String secretKey) {
        String signData = "";
        if (!MapUtils.isEmpty(jsonMap)) {
            String jsonMapStr = JSON.toJSONString(jsonMap);
            char[] chs = jsonMapStr.toCharArray();
            Arrays.sort(chs);
            String sortJson = new String(chs).replace(" ", "");
            signData += "jsonStr=" + sortJson + "&";
        }
        signData += "appKey=" + appKey + "&secretKey=" + secretKey + "&timestamp=" + timestamp;
        String sign = MD5Util.MD5Encode(signData);
        sign = MD5Util.MD5Encode(sign);
        return sign;
    }
}
