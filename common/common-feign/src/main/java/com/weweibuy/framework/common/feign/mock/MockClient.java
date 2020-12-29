package com.weweibuy.framework.common.feign.mock;

import feign.*;

import java.io.IOException;

/**
 * feign mock
 *
 * @author durenhao
 * @date 2020/12/27 22:36
 **/
public class MockClient implements Client {

    private final Client delegate;

    public MockClient(Client delegate) {
        this.delegate = delegate;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        String url = request.url();
        Request.HttpMethod httpMethod = request.httpMethod();
        // TODO 根据配置进行匹配, 如果匹配读取本地的Json文件进行响应
        return delegate.execute(request, options);
    }
}
