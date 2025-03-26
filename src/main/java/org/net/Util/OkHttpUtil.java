/**   使用依赖
*   <dependency>
 *             <groupId>com.squareup.okhttp3</groupId>
 *             <artifactId>okhttp</artifactId>
 *             <version>5.0.0-alpha.3</version>
 *         </dependency>
**/
package org.net.Util;
import okhttp3.*;
import org.net.Model.Entity.BaiduTranslateVO;
import org.net.Model.Enums.HttpEnum;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
* @Author : YangFeng
* @Desc : okhttp 封装工具类
* @Date : 2024/7/16
**/

public class OkHttpUtil {

    private static final Logger logger = Logger.getLogger(MD5Utils.class.getName());

    //绕过证书
    public static X509TrustManager x509TrustManager = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
        public X509Certificate[] getAcceptedIssuers() {  return new X509Certificate[0];}
    };
    public static SSLSocketFactory sslSocketFactory = createSslSocketFactory();

    private static SSLSocketFactory createSslSocketFactory(){
        try {
            // 信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    *  OkHttpClient 实例
    **/
    private static final OkHttpClient client =  new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, x509TrustManager)
            // 设置连接超时时间
            .connectTimeout(20, TimeUnit.SECONDS)
            // 设置读取超时时间
            .readTimeout(20, TimeUnit.SECONDS)
            // 设置写入超时时间
            .writeTimeout(15, TimeUnit.SECONDS)
            // 设置重定向策略，默认为跟随重定向
            .followRedirects(true)
            .build();

    /**
     * 普通GET 请求
     **/
    public static String get(String url) {

        return get(url,null,null);
    }

    /**
     * GET 请求 带参数
     **/
    public static String get(String url,Map<String, String> params) {

        return get(url,null,params);
    }

    /**
     * GET 请求 带请求头、参数
     **/
    public static String get(String url, Map<String, String> headers,Map<String, String> params) {
        //参数拼接
        StringBuilder sb = new StringBuilder(url);
        if (params != null && !params.keySet().isEmpty()) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(params.get(key));
                    firstFlag = false;
                } else {
                    sb.append("&").append(key).append("=").append(params.get(key));
                }
            }
        }
        if(headers==null){
            headers = new HashMap<>();
        }
        Request request = new Request.Builder()
                .url(sb.toString())
                .headers(Headers.of(headers))
                .build();
        return doExcute(request);
    }

    /**
    *  post 请求
    **/
    public static String post(String url, Map<String, Object> body) {

        //默认是 application/x-www-form-urlencoded
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", HttpEnum.APP_X_WWW_FORM_URLENCODED.getValue());

        return post(url,map,body);
    }
    /**
     * POST 带请求头 请求体
     **/
    public static  String post(String url, Map<String, String> headers,Map<String, Object> body) {

        if(headers==null){
            headers = new HashMap<>();
        }
        //如果 headers不存在Content-Type，则默认是 application/x-www-form-urlencoded
        headers.computeIfAbsent("Content-Type", k -> HttpEnum.APP_X_WWW_FORM_URLENCODED.getValue());

        Request request = new Request.Builder()
                .url(url)
                .post(Objects.requireNonNull(getRequestBody(headers.get("Content-Type"), body)))
                .headers(Headers.of(headers))
                .build();

        return doExcute(request);
    }
    /**
    *  POST 请求 返回对应的 T 对象
    **/
    public static <T> T post(String url, Map<String, String> headers,Map<String, Object> body,Class<T> resultClass) {

        return JsonUtils.json2Bean(post(url,headers,body),resultClass);
    }


    /**
    *  通过 client 单例发起请求
    **/
    private static String doExcute(Request request){
        try {
            Call call = client.newCall(request);
            Response response= call.execute();
            return response.body().string();
        } catch (IOException e) {
            logger.info("请求失败 IOException ");
            e.printStackTrace();
        }
        return "";
    }
    /**
    *  okhttp 支持异步调用 并发量较大的情况下可以使用 目前先写一下 执行方法 目前业务不需要（万一以后需要呢）
    *  @param request okhttp的request体
    *  @param action  成功执行后的回调方法/动作
    **/
    private static void doExcuteAsync(Request request,Runnable action){
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.info("请求失败 IOException ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                logger.info("请求成功");
                action.run();
            }
        });
    }

    /**
    *  根据不同的contentType类型获取不同的请求体
    **/
    private static RequestBody getRequestBody(String contentType, Map<String, Object> body){

        try {
            // 1、content-type是 json类型
            if(HttpEnum.APP_JSON.getValue().equals(contentType)){
                return RequestBody.create(MediaType.parse(contentType+"; charset=utf-8"), JsonUtils.bean2Json(body));
            }
            // 2、content-type是 x-www-form-urlencoded类型
            else if (HttpEnum.APP_X_WWW_FORM_URLENCODED.getValue().equals(contentType)){
                FormBody.Builder formBuilder = new FormBody.Builder();
                body.forEach((k,v)->{
                    formBuilder.add(k,v.toString());
                });
                return  formBuilder.build();
            }
            // 3、content-type是  xml类型
            else if (HttpEnum.APP_XML.getValue().equals(contentType)){
                String xmlBody = XmlUtils.mapToXml(convertMap(body));
                return RequestBody.create(MediaType.parse(contentType + "; charset=utf-8"), xmlBody);
            }
            // 4. content-type是 multipart/form-data类型
            else if (HttpEnum.APP_FORM_DATA.getValue().equals(contentType)){
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                body.forEach((k,v)->{
                    if (v instanceof File) {
                        multipartBuilder.addFormDataPart(k, ((File) v).getName(), RequestBody.create(MediaType.parse(contentType), (File) v));
                    } else {
                        multipartBuilder.addFormDataPart(k, v.toString());
                    }
                });
                return multipartBuilder.build();
            }
            // 5. content-type是 application/octet-stream类型
            else if (HttpEnum.APP_OCTET_STREAM.getValue().equals(contentType)){
                byte[] bytes =(byte[]) body.get("bytes");
                return RequestBody.create(MediaType.parse(contentType), bytes);
            }
        } catch (Exception e) {
            logger.info("获取RequestBody 出错");
           e.printStackTrace();
        }
        logger.info("不支持的content-type类型");
        return null;
    }

    /**
    *  Map<String, Object> 变成 Map<String, String>
    **/
    private static Map<String, String> convertMap(Map<String, Object> originalMap) {
        Map<String, String> convertedMap = new HashMap<>();

        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String stringValue = String.valueOf(value); // 转换成String类型

            convertedMap.put(key, stringValue);
        }

        return convertedMap;
    }





    public static void main(String[] args) {
        //测试百度api
        String sign = "202422100704"+"放弃"+"123456"+"4nzrRvl2k0yx1_8f5A";
//        //构建body
//        RequestBody body = new FormBody.Builder()
//                .add("q", "xx")
//                .add("from", "zh")
//                .add("to", "en")
//                .add("appid", "20240716002100704")
//                .add("salt", "123456")
//                .add("sign",MD5Utils.getMD5(sign))
//                .build();
//
//        Request request = new Request.Builder()
//                .url("https://fanyi-api.baidu.com/api/trans/vip/translate")
//                .addHeader("Content-Type", HttpEnum.APP_X_WWW_FORM_URLENCODED.getValue())
//                .post(body)
//                .build();
//
//        Call call = client.newCall(request);
//        try {
//            Response response = call.execute();
//            System.out.println(response.body().string());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", HttpEnum.APP_X_WWW_FORM_URLENCODED.getValue());

        Map<String,Object> body = new HashMap<>();
        body.put("q", "放弃");
        body.put("from", "zh");
        body.put("to", "en");
        body.put("appid", "202422100704");
        body.put("salt", "123456");
        body.put("sign",MD5Utils.getMD5(sign));
        BaiduTranslateVO result =OkHttpUtil.post("https://fanyi-api.baidu.com/api/trans/vip/translate",map,body,BaiduTranslateVO.class);
        System.out.println("result = " + result.toString());
    }

}
