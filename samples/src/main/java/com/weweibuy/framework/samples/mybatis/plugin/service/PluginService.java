package com.weweibuy.framework.samples.mybatis.plugin.service;

import com.weweibuy.framework.samples.mybatis.plugin.mapper.RouteFilterArgsMapper;
import com.weweibuy.framework.samples.mybatis.plugin.model.example.RouteFilterArgsExample;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.RouteFilterArgs;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/5/31 11:13
 **/
@Service
public class PluginService {

    private final RouteFilterArgsMapper routeFilterArgsMapper;

    public PluginService(RouteFilterArgsMapper routeFilterArgsMapper) {
        this.routeFilterArgsMapper = routeFilterArgsMapper;
    }

    @Transactional
    public RouteFilterArgs selectOneForUpdate() {
        return routeFilterArgsMapper.selectOneByExampleForUpdate(RouteFilterArgsExample.newAndCreateCriteria().example());
    }


    @Transactional
    public List<RouteFilterArgs> exOneForUpdate() {
        return routeFilterArgsMapper.selectByExampleForUpdate(RouteFilterArgsExample.newAndCreateCriteria().example());
    }
}
