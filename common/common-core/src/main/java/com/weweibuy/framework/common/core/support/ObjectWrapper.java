package com.weweibuy.framework.common.core.support;

import lombok.*;

/**
 * 对象的包装类
 *
 * @author durenhao
 * @date 2020/6/21 20:40
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Setter
public class ObjectWrapper<T> {

    private T object;

}
