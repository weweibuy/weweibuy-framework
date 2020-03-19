# common-log
  场景: 对于Http请求我们需要输出请求/响应数据,并对部分字段进行脱敏; 而且有traceId 可以关联整个链路,且traceId在多线程下也能正确输出.
  适用于Springboot + SpringMVC5x + Logback
  
### 1.1 加入依赖：

`maven:`
```
<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>common-log</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 1.2 日志输出
  使用springBoot 自动配置引入依赖即可, 如需关闭自动配置:

`application.properites:`
```
common.log.enable = false
```

### 1.3 日志脱敏
#### 1、实现接口
```java
package com.weiweibuy.framework.common.log.config;

import com.weiweibuy.framework.common.log.desensitization.LogMessageConverter;
import com.weiweibuy.framework.common.log.desensitization.PatternReplaceConfig;

import java.util.Map;
import java.util.Set;

/**
 * 脱敏 映射配置
 *
 * @author durenhao
 * @date 2020/3/1 21:14
 **/
public interface SensitizationMappingConfig {

    /**
     * 增加 映射
     *
     * @param mappingMap key  ->  匹配脱敏路径(生成形式 path + "_" + Method)   v -> 脱敏字段
     *                   {@link  LogMessageConverter}  内置部分字段处理方式
     *                   {@link PatternReplaceConfig  自定义字段处理方式}
     */
    void addSensitizationMapping(Map<String, Set<String>> mappingMap);

}
```
 重写 addSensitizationMapping(Map<String, Set<String>> mappingMap); 方法配置脱敏的请求路径与字段,
 如: [RequestSensitizationMappingConfig](../../samples/src/main/java/com/weweibuy/framework/samples/log/RequestSensitizationMappingConfig.java)

#### 2、配置字段与替换规则
##### 2.1、实现接口定义替换规则
```java
package com.weiweibuy.framework.common.log.desensitization;

import java.util.Map;

/**
 * JDK java.util.ServiceLoader 加载 自定配置
 * 需要 classPath/META-INF/services/  目录下自定义实现
 *
 * @author durenhao
 * @date 2020/3/1 23:30
 **/
public interface PatternReplaceConfig {

    /**
     * 增加 字段处方式  {@link LogMessageConverter}
     *
     * @param patternReplaceMap
     */
    void addPatternReplace(Map<String, LogMessageConverter.PatternReplace> patternReplaceMap);
}
```
##### 2.2、加载自定义替换规则
  
  使用 JDK java.util.ServiceLoader 加载 自定配置, 需要 classPath/META-INF/services/  目录下自定义实现
  如: [PatternReplaceConfig](../../samples/src/main/resources/META-INF/services/com.weiweibuy.framework.common.log.desensitization.PatternReplaceConfig)

##### 2.3、内置的替换规则(可覆盖)
  内置了部分脱敏替换规则 [LogMessageConverter#init](src/main/java/com/weiweibuy/framework/common/log/desensitization/LogMessageConverter.java)
  可以使用: 2.2 的方式进行覆盖
  
##### 2.4、logback配置自定义的日志msg转化器

`logback-spring.xml`
```
<configuration>
    <conversionRule conversionWord="msg" converterClass="com.weiweibuy.framework.common.log.desensitization.LogMessageConverter"/>
    <conversionRule conversionWord="m" converterClass="com.weiweibuy.framework.common.log.desensitization.LogMessageConverter"/>
    <conversionRule conversionWord="message" converterClass="com.weiweibuy.framework.common.log.desensitization.LogMessageConverter"/>
</configuration>
```
  
### 1.4 MDC与 Trace
  配置: 
`application.properites:`
```
common.log.trace.enable = true
```
  自动为Http请求响应绑定MDC 属性 tid  
  日志输出tid: Pattern加入:  [%X{tid}]
#### 1 TTL MDC
  TTL  [transmittable-thread-local](https://github.com/alibaba/transmittable-thread-local)
  加入依赖
`maven:`
```
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>transmittable-thread-local</artifactId>
</dependency>
```
 配置logback
 
`logback-spring.xml`
```
<configuration>
    <contextListener class="com.weiweibuy.framework.common.log.ttl.TtlMdcListener"></contextListener>
</configuration>
```
 修饰线程池
  [修饰线程池](https://github.com/alibaba/transmittable-thread-local#22-%E4%BF%AE%E9%A5%B0%E7%BA%BF%E7%A8%8B%E6%B1%A0)  
  [使用Java Agent来修饰JDK线程池实现类](https://github.com/alibaba/transmittable-thread-local#23-%E4%BD%BF%E7%94%A8java-agent%E6%9D%A5%E4%BF%AE%E9%A5%B0jdk%E7%BA%BF%E7%A8%8B%E6%B1%A0%E5%AE%9E%E7%8E%B0%E7%B1%BB)  