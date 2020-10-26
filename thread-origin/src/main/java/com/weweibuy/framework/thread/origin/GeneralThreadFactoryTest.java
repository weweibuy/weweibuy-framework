package com.weweibuy.framework.thread.origin;

/**
 * @author zhang.suxing
 * @date 2020/10/25 10:15
 **/
public class GeneralThreadFactoryTest {


    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        myThread.start();
        // worker 线程中断
        myThread.interrupt();
        System.out.println("worker 线程中断状态" + myThread.isInterrupted());

        //main 线程中断
        Thread.currentThread().interrupt();
        System.out.println("第一次返回调用的值---" + Thread.interrupted());
        System.out.println("第一次返回调用的值---" + Thread.interrupted() + "\n \n \n");


        Thread t1 = new Thread(() -> {
            while (true) {
                Thread thread = Thread.currentThread();
                try {
                    //此处线程本该blocking 10s ---会一直在里面循环打印线程信息
                    //线程处于WAITING或TIMED_WAITING下,业务代码处理InterruptedException中断成功
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    System.out.println("收到中断异常,退出线程执行");
                    break;
                }
                // 获取当前线程中断标志但是不会清除
                boolean result = thread.isInterrupted();
                System.out.println(String.format("当前线程:[%s],中断状态:[%b]", thread.getName(), result));
                if (result) {
                    System.out.println("捕获中断异常---");
                    break;
                }
            }
        });
        t1.setName("t1");
        t1.start();
        Thread.sleep(6000);
        System.out.println("t1.getState() = " + t1.getState());
        t1.interrupt();
    }

    /**
     * 什么是线程中断
     *     java中的线程中断并不是指强制线程中断执行,而是指调用线程中断起到一个通知作用,让线程知道自己被中断了.
     *     至于线程是否应该继续执行下去,这个取决于业务代码本身.而原来提供的stop()方法,因为固有不安全性,现在已经不推荐使用该方式来实现线程中断.
     *
     */


}
