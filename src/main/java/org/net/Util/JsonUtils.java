package org.net.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.net.Model.Entity.BaiduTranslateVO;

import java.util.List;
import java.util.Map;

public class JsonUtils {
    private JsonUtils() {
    }

    private static ObjectMapper mapper = new ObjectMapper();

    public static String bean2Json(Object data) {
        try {
            String result = mapper.writeValueAsString(data);
            return result;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T json2Bean(String jsonData, Class<T> beanType) {
        try {
            T result = mapper.readValue(jsonData, beanType);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> json2List(String jsonData, Class<T> beanType) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> resultList = mapper.readValue(jsonData, javaType);
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <K, V> Map<K, V> json2Map(String jsonData, Class<K> keyType, Class<V> valueType) {
        JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        try {
            Map<K, V> resultMap = mapper.readValue(jsonData, javaType);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String jsonString = "{\"from\":\"zh\",\"to\":\"en\",\"trans_result\":[{\"src\":\"\\u653e\\u5f03\",\"dst\":\"give up\"}]}";

        BaiduTranslateVO baiduTranslateVO = JsonUtils.json2Bean(jsonString, BaiduTranslateVO.class);
        baiduTranslateVO.getTrans_result().toString();

    }
}
