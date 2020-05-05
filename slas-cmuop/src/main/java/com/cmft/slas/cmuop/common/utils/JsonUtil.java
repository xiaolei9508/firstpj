package com.cmft.slas.cmuop.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Period;

/**
 * @author : zhangf002(张帆)
 * @since : 2019/5/6 4:08 PM
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper JSON = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.INDENT_OUTPUT, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static String toJson(Object obj) {
        if (obj == null)
            return null;

        try {
            return JSON.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Json Processing msg: {}", e.getMessage());
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return JSON.readValue(json, clazz);
        } catch (IOException e) {
            log.warn("Json Parse error: {}", e.getMessage());
        }
        return null;
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null) {
            return null;
        }
        try {
            return JSON.readValue(json, typeReference);
        } catch (IOException e) {
            log.warn("Json Parse error: {}", e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        String json = JsonUtil.toJson(Period.ofDays(7));
        System.out.println(json);
    }
}