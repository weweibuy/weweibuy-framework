package com.weweibuy.framework.common.log.config;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/6/24 23:34
 **/
@Data
@ConfigurationProperties(prefix = "common.log")
public class CommonLogProperties {

    private Boolean enable = true;

    private CommonLogHttpProperties http = new CommonLogHttpProperties();

    @Data
    public static class CommonLogHttpProperties {

        /**
         * 配置形式:  REQ_/**; RESP_/**; ALL_/**
         */
        private Set<String> disablePath = new HashSet<>();


        public List<LogDisablePath> toLogDisablePath() {
            if (CollectionUtils.isEmpty(disablePath)) {
                return Collections.emptyList();
            }
            return disablePath.stream()
                    .map(d -> {
                        int indexOf = d.indexOf("_");
                        if (indexOf == -1 || indexOf == d.length()) {
                            return null;
                        }
                        String path = d.substring(indexOf + 1, d.length());
                        String typeStr = d.substring(0, indexOf);
                        return LogDisablePath.parseType(typeStr)
                                .map(t -> LogDisablePath.builder()
                                        .type(t)
                                        .path(path)
                                        .build())
                                .orElse(null);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }


    }


}
