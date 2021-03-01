package com.weweibuy.framework.lb.endpoint;

import com.weweibuy.framework.lb.support.LoadBalanceOperator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * lb 端点
 *
 * @author durenhao
 * @date 2020/9/24 22:48
 **/
@RestController
@RequestMapping("/endpoint/_common/_lb")
public class LoadBalanceEndpoint {

    private final LoadBalanceOperator loadBalanceOperator;

    public LoadBalanceEndpoint(LoadBalanceOperator loadBalanceOperator) {
        this.loadBalanceOperator = loadBalanceOperator;
    }


//    /**
//     * 列表
//     *
//     * @return
//     */
//    @GetMapping("/_list")
//    public CommonDataResponse<Map<String, List<Server>>> listLb() {
//        return CommonDataResponse.success(loadBalanceOperator.allServerMap());
//
//    }

//    /**
//     * 更新
//     *
//     * @return
//     */
//    @PostMapping("/_update/{name}")
//    public CommonCodeResponse updateLb(@PathVariable String name) {
//        loadBalanceOperator.update(name);
//        return CommonCodeResponse.success();
//
//    }


//    /**
//     * 更新全部
//     *
//     * @return
//     */
//    @PostMapping("/_update")
//    public CommonCodeResponse updateLb() {
//        loadBalanceOperator.update("");
//        return CommonCodeResponse.success();
//
//    }

}
