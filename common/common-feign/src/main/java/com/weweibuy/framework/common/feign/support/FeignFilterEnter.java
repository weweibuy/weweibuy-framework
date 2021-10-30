package com.weweibuy.framework.common.feign.support;

import feign.Client;
import feign.Request;
import feign.Response;

import java.io.IOException;
import java.util.List;

/**
 * @author durenhao
 * @date 2021/10/30 17:07
 **/
public class FeignFilterEnter implements FeignFilterChain {

    private final Client client;

    private final List<FeignFilter> feignFilterList;

    private int pos = 0;

    private int size;

    public FeignFilterEnter(Client client, List<FeignFilter> feignFilterList) {
        this.client = client;
        this.feignFilterList = feignFilterList;
        size = feignFilterList.size();
    }

    public Response doFilter(Request request, Request.Options options) throws IOException {
        if (pos < size) {
            return feignFilterList.get(pos++).filter(request, options, this);
        }
        return client.execute(request, options);
    }

}
