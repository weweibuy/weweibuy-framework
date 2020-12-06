package com.weweibuy.framework.common.lc.endpoint;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.common.lc.cache.LocalCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 本地缓存 管理端点
 *
 * @author durenhao
 * @date 2020/11/28 10:02
 **/
@RestController
@RequestMapping("/endpoint/_common/_local_cache")
public class LocalCacheEndpoint {

    @Autowired
    private LocalCacheService ruleMetaCacheService;

    @GetMapping
    public CommonDataResponse<Map<String, List<String>>> listCacheKey() {
        return CommonDataResponse.success(ruleMetaCacheService.listCacheKey());
    }

    @DeleteMapping("/current")
    public CommonCodeResponse evictCache() {
        ruleMetaCacheService.evictCache();
        return CommonDataResponse.success();
    }

    @DeleteMapping("/cluster")
    public CommonCodeResponse evictClusterCache() {
        ruleMetaCacheService.evictClusterCache();
        return CommonDataResponse.success();
    }

    @DeleteMapping("/current/{name}")
    public CommonCodeResponse evictCache(@PathVariable String name) {
        ruleMetaCacheService.evictCache(name);
        return CommonDataResponse.success();
    }

    @DeleteMapping("/cluster/{name}")
    public CommonCodeResponse evictCacheCluster(@PathVariable String name) {
        ruleMetaCacheService.evictClusterCache(name);
        return CommonDataResponse.success();
    }


}
