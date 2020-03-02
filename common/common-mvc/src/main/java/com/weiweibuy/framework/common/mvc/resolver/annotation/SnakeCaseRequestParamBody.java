package com.weiweibuy.framework.common.mvc.resolver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将非Json  下划线风格的请求参数 映射到Java实体(驼峰风格)上
 * 注意 @InitBinder  对此类的参数无效, 其余参数转化正常使用
 * 注意 参数必须有空参构造
 *
 * @author durenhao
 * @date 2020/2/17 22:09
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SnakeCaseRequestParamBody {


}
