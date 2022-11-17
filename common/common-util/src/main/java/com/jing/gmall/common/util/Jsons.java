package com.jing.gmall.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Jsons {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 将对象转换成json字符串
     * @param obj
     * @return
     */
    public static String toJsonStr(Object obj){
        String json = null;
        try {
            json =  mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }


    public static <T> T json2Obj(String json,Class<T> clazz){
        T obj = null;
        try {
            obj = mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
