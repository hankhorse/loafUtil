package org.net.Test;

import org.net.Util.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpTest {


    /**
    *  测试高请求
    **/
    public static void main(String[] args) {
        Map<String, Object> body  =new HashMap<>();
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        body.put("lng", 116.561817);
        body.put("lat", 35.320359);
//        for (int i = 0; i < 10000; i++) {
//           String s =  OkHttpUtil.post("http://localhost:5555/localGeo/getGeo", header,body );
//            System.out.println(s);
//        }
        int numberOfThreads = 10; // 设置线程数
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        long startTime = System.currentTimeMillis(); // 记录开始时间

        for (int i = 0; i < 10000; i++) {
            final int requestNumber = i;
            executorService.submit(() -> {
                String s = OkHttpUtil.post("http://localhost:5555/localGeo/getGeo", header, body);
                System.out.println("Request " + requestNumber + ": " + s);
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis(); // 记录结束时间
        long duration = endTime - startTime; // 计算总耗时

        System.out.println("Total time taken: " + duration + " ms");
    }
}
