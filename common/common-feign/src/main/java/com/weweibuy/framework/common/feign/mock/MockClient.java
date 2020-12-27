package com.weweibuy.framework.common.feign.mock;

import feign.Client;
import feign.Request;
import feign.Response;

import java.io.IOException;

/**
 * feign mock
 *
 * @author durenhao
 * @date 2020/12/27 22:36
 **/
public class MockClient implements Client {

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {

        return Response.builder()
                .request(request)
                .reason("")
                .build();
    }
}
