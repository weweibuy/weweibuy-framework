package com.weweibuy.framework.http.conn;

import java.io.Closeable;

/**
 * Http连接
 *
 * @author durenhao
 * @date 2020/6/22 22:43
 **/
public interface HttpConnection extends Closeable {

    /**
     * 连接是否开启
     *
     * @return
     */
    boolean isOpen();

    /**
     * 设置socket超时时间, 毫秒
     *
     * @param timeOutMilli
     */
    void setSocketTime(long timeOutMilli);

}
