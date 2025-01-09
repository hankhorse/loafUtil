package org.net.Thread;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
*  阻塞队列
**/
public class BlockQueue {

    public static void main(String[] args) throws InterruptedException {

        //ArrayBlockingQueue  生产者放入数据和消费者获取数据，都是共用同一个锁对象
        BlockingQueue<String> arrBlockingQueue = new ArrayBlockingQueue<>(1000);

        // 实验 take方法 和 put方法
        // take：获取数据，如果队列为空，则阻塞等待，直到有数据为止

        Thread one  =new Thread(() -> {
            for(;;){
                arrBlockingQueue.offer(Math.random()*100+"");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        Thread two = new Thread(()->{
            for(;;){
                try {
                    String take = arrBlockingQueue.take();
                    System.out.println(Thread.currentThread().getState()+"");
                    System.out.println("take:"+take);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        one.start();
        two.start();


    }
}
