package com.weweibuy.framework.common.lc.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 本地缓存刷新完成 事件
 *
 * @author durenhao
 * @date 2020/12/6 17:48
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalCacheEvictedEvent {

    private String body;

}
