package org.net.Thread;

public class OneThread {

    static int a = 10;
  public static void main(String[] args) {
    //


      Thread threadOne;

      threadOne = new Thread(new Runnable() {
          @Override
          public void run() {
              System.out.println("Thread One Start");
              unlimitMethod1();
          }
      });

      threadOne.start();

      Thread threadTwo = new Thread(new Runnable() {
          @Override
          public void run() {
              System.out.println("Thread Two Start");
              unlimitMethod2();
          }
      });
      threadTwo.start();
  }

  public static void unlimitMethod1() {
      while(a>0) {
          System.out.println("ThreadOne---->a:"+a);
           a--;
          try{
//              System.out.println("===》线程1休眠100L");
              Thread.sleep(10L);
          }catch (Exception e){
              e.printStackTrace();
          }

      }
  }

  public static void unlimitMethod2() {
    while (a>0) {
      System.out.println("ThreadTow====> a:"+a);
      a--;
      try {
//        System.out.println("===》线程2休眠1000L");
        Thread.sleep(10L);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
        }


}
