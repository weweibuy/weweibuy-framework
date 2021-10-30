package com.weweibuy.framework.common.feign.support;

import feign.Client;
import feign.Request;
import feign.Response;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * 过滤feign 请求响应
 *
 * @author durenhao
 * @date 2021/10/30 16:55
 **/
public class FilterRequestFeignClient implements Client {

    private final Client delegate;

    private final List<FeignFilter> feignFilterList;

    public FilterRequestFeignClient(Client delegate, List<FeignFilter> feignFilterList) {
        this.delegate = delegate;
        this.feignFilterList = feignFilterList;
    }


    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        if (CollectionUtils.isNotEmpty(feignFilterList)) {
            // 过滤请求
            FeignFilterEnter filterEnter = new FeignFilterEnter(delegate, feignFilterList);
            return filterEnter.doFilter(request, options);
        }
        return delegate.execute(request, options);
    }


}
