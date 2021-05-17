package com.weweibuy.framework.lb.endpoint;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.lb.support.LoadBalanceOperator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * lb 端点
 *
 * @author durenhao
 * @date 2020/9/24 22:48
 **/
@RestController
@RequestMapping(CommonConstant.HttpServletConstant.ENDPOINT_PATH_PREFIX + "/_common/_lb")
public class LoadBalanceEndpoint {

    private final LoadBalanceOperator loadBalanceOperator;

    public LoadBalanceEndpoint(LoadBalanceOperator loadBalanceOperator) {
        this.loadBalanceOperator = loadBalanceOperator;
    }


    /**
     * 列表
     *
     * @return
     */
    @GetMapping("/_list")
    public CommonDataResponse<Map<String, List<ServiceInstance>>> listLb() {
        return CommonDataResponse.success(loadBalanceOperator.allServerMap());

    }

    /**
     * 列表
     *
     * @return
     */
    @GetMapping("/_list/{name}")
    public CommonDataResponse<List<ServiceInstance>> listServer(@PathVariable String name) {
        return CommonDataResponse.success(loadBalanceOperator.listServer(name));
    }

    /**
     * 更新
     *
     * @return
     */
    @PostMapping("/_update/{name}")
    public CommonCodeResponse updateLb(@PathVariable String name) {
        loadBalanceOperator.update(name);
        return CommonCodeResponse.success();

    }


    /**
     * 更新全部
     *
     * @return
     */
    @PostMapping("/_update")
    public CommonCodeResponse updateLb() {
        loadBalanceOperator.update();
        return CommonCodeResponse.success();

    }

}
