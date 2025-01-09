package org.net.Thread;


import org.net.Util.UUIDUtils;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Logger;


public class ConcurrentTreeExample {

    private static Logger logger = Logger.getLogger(ConcurrentTreeExample.class.getName());

    public static void main(String[] args) {
        ConcurrentSkipListMap<String, String> concurrentMap = new ConcurrentSkipListMap<>();

        // 启动多个线程并发地插入数据
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    concurrentMap.put( UUIDUtils.getUUID(),String.valueOf(j));
//                    logger.info("Thread " + Thread.currentThread().getName() + " inserted " + j);
                    //logger.info("UUID -> "+ UUIDUtils.getUUID());
                }
            }).start();
        }

        // 等待所有线程完成
        try {
            Thread.sleep(1000); // 假设所有线程都在1秒内完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Map size after concurrent modifications: " + concurrentMap.size());

    }

}
