package org.net.Thread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
*  同步 \ 单线程 \ 线程池  速度对比
 *
 *
 *  总结:
 *
 *  线程池:如果量比较大,且 任务复杂!,最好提交多次任务,如果任务简单,尽量就提交一次就行
**/
public class ThreadSpeedCompare {

    private static final Logger LOGGER = Logger.getLogger(ThreadSpeedCompare.class.getName());
    // 使用线程池代替直接创建线程，提高效率并减少资源消耗
    static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws  InterruptedException {
        Queue<String> queueOne = new ConcurrentLinkedQueue<>();
        Queue<String> queueTwo  = new ConcurrentLinkedQueue<>();

        // 测试1: 单线程存入操作
        testSingleThreadInsert(queueOne);

//        while (true){

        // 测试2: 同步读取并存入
        testSyncReadAndInsert(queueOne, queueTwo);

        // 测试3: 多线程读取并存入
        testMultiThreadReadAndInsert(queueOne, queueTwo);

        // 测试4：线程池读取并存入
        testMultiThreadPoolReadAndInsert(queueOne, queueTwo);

        Thread.sleep(5000L);
//        }

    }

    private static void testSingleThreadInsert(Queue<String> map) {
        long startTime = System.nanoTime();
        for (int i = 0; i < 19000000; i++) {
            map.add(String.valueOf(i));
        }
        long endTime = System.nanoTime();
        LOGGER.log(Level.INFO, "ConcurrentLinkedQueue 存入基础数据耗时：" + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
    }

    private static void testSyncReadAndInsert(Queue<String> sourceMap, Queue<String> targetMap) {
        long startTime = System.nanoTime();
        commonMethod(sourceMap, targetMap);
        long endTime = System.nanoTime();
        LOGGER.log(Level.INFO,("ONE："+sourceMap.size()+"  TWO："+targetMap.size()+"  for循环同步耗时==》：" + TimeUnit.NANOSECONDS.toMillis(endTime - startTime)));
    }

    private static void testMultiThreadReadAndInsert(Queue<String> sourceMap, Queue<String> targetMap) {

        Thread thread = new Thread(() -> {
            commonMethod(targetMap,sourceMap);
        });
        long startTime = System.nanoTime();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "线程执行被中断", e);
            Thread.currentThread().interrupt();
        }
        long endTime = System.nanoTime();
        LOGGER.log(Level.INFO, ("ONE："+sourceMap.size()+"  TWO："+targetMap.size()+ " 单个线程取耗时==》：" + TimeUnit.NANOSECONDS.toMillis(endTime - startTime)));
    }
    private static void testMultiThreadPoolReadAndInsert( Queue<String> sourceMap,Queue<String> targetMap) {
        long startTime = System.nanoTime();



        // 提交任务到线程池
        Runnable task = () -> {
            try {
                commonMethod( sourceMap,targetMap);
            } catch (Exception e) {
                Thread.currentThread().interrupt(); // 重新设置中断状态
                LOGGER.log(Level.SEVERE, "线程执行被中断", e);
            }
        };
        executorService.submit(task);
        executorService.submit(task);
        executorService.submit(task);
        executorService.submit(task);
        // 关闭线程池，并等待所有任务完成
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "线程池等待中断", e);
            Thread.currentThread().interrupt();
        }

        long endTime = System.nanoTime();
        LOGGER.log(Level.INFO, ("ONE："+sourceMap.size()+"  TWO："+targetMap.size()+" 线程池取出耗时==》：" + TimeUnit.NANOSECONDS.toMillis(endTime - startTime)));
    }


    /**
    *  替换 元素 并入队列
    **/
    public static void commonMethod(Queue<String> source, Queue<String> target){
        while (true) {
            String element = source.poll();
            if (element == null) {
                break;
            }
            element.toLowerCase().toUpperCase();
            element+="222";
            element+="333";
            element+="444";
            element.replaceAll("222","555");
            element.replaceAll("333","666");
            element.replaceAll("444","777");
            element.trim();
            target.add(element);
        }
    }


}
