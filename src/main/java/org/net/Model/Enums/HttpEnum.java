package org.net.Model.Enums;

/**
*  http 枚举类
**/
public enum HttpEnum {

//    请求方式
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),

//请求头
    APP_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APP_JSON("application/json"),
    APP_XML("application/xml"),
    APP_FORM_DATA("multipart/form-data"),
    APP_OCTET_STREAM("application/octet-stream"),
    ;



    public  String value;

    HttpEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
