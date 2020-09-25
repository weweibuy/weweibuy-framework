package com.weweibuy.framework.lb.endpoint;

import com.netflix.loadbalancer.Server;
import com.weweibuy.framework.common.core.model.dto.CommonCodeJsonResponse;
import com.weweibuy.framework.common.core.model.dto.CommonDataJsonResponse;
import com.weweibuy.framework.lb.support.LoadBalanceOperator;
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
@RequestMapping("/_common/_lb")
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
    public CommonDataJsonResponse<Map<String, List<Server>>> listLb() {
        return CommonDataJsonResponse.success(loadBalanceOperator.allServerMap());

    }

    /**
     * 更新
     *
     * @return
     */
    @PostMapping("/_update/{name}")
    public CommonCodeJsonResponse updateLb(@PathVariable String name) {
        loadBalanceOperator.update(name);
        return CommonCodeJsonResponse.success();

    }


    /**
     * 更新全部
     *
     * @return
     */
    @PostMapping("/_update")
    public CommonCodeJsonResponse updateLb() {
        loadBalanceOperator.update("");
        return CommonCodeJsonResponse.success();

    }

}
