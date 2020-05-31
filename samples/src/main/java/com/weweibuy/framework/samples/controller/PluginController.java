package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.mybatis.plugin.service.PluginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2020/5/31 11:19
 **/
@RestController
@RequestMapping("/plugin")
public class PluginController {

    private final PluginService pluginService;

    public PluginController(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @GetMapping("/one")
    public Object oneForUpdate(){
      return   pluginService.selectOneForUpdate();
    }

    @GetMapping("/ex")
    public Object exForUpdate(){
        return   pluginService.exOneForUpdate();
    }

}
