package com.weweibuy.framework.thread.origin;

/**
 * @author zhang.suxing
 * @date 2020/10/25 10:51
 **/
public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("=====当前worker线程正在执行业务");
    }
}
