package com.weweibuy.framework.common.mvc.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2020/9/25 20:45
 **/
@RestController
@RequestMapping("/endpoint/_common/_health")
public class CustomHealthController {

    @GetMapping
    public ResponseEntity health() {
        return ResponseEntity.noContent().build();
    }


}
