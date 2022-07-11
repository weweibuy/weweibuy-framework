# common-log
  场景:  
   - 对于Http请求我们需要输出请求/响应数据,特定请求可以屏蔽日志(如响应Base64图片)  
   - 对请求响应日志部分字段进行脱敏  
   - 日志链路追踪APM功能(并且在多线程下也能正常使用)  
   - 日志策略分环境, 非开发环境不输出控制台日志  
  适用于Springboot + SpringMVC5x + Logback
  
### 1 加入依赖：

```
<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>common-log</artifactId>
    <version>1.3-SNAPSHOT</version>
</dependency>
```

### 2 http日志输出
  引入依赖即可, 如需关闭:

`application.properites:`
```
common.log.enable = false
```
  1. 说明
    该功能基于: Servlet-Filter,MVC-ControllerAdvice, MVC-Interceptor 组合实现,输出日志准确可信,业务开发完全无感
  2. 特定接口屏蔽日志
   方法一: 配置:   
`application.properites:`
```
common.log.http.disablePath[0] = REQ_/image/**  ## 屏蔽 /image/** 全部请求日志
common.log.http.disablePath[1] = RESP_/image    ## 屏蔽 /image 响应日志
```    
   方法二: 代码:    
   实现[LogDisableConfigurer](src/main/java/com/weweibuy/framework/common/log/support/LogDisableConfigurer.java)接口,增加配置,并将Bean交给Spring管理  
   eg:  [CustomPatternReplaceConfig](../../samples/src/main/java/com/weweibuy/framework/samples/log/CustomDisablePathConfig.java)


### 3 日志脱敏
   1.  实现[PatternReplaceConfig](src/main/java/com/weweibuy/framework/common/log/desensitization/PatternReplaceConfig.java)接口  
   增加字段处方式或增加脱敏规则配置 并将Bean交给Spring管理  
   2. 通过 PatternReplaceConfig.addPatternReplace 增加或修改匹配与替换规则
```text
提示: 我们内置部分规则: 可以在 DesensitizationLogMessageConverter 中查看; 
这些字段包括:  
    mobile,phone,phoneNo,certId,idCard,idNo,password,pwd,appSecret,fullName,address
如果这些不满足要求可以通过addPatternReplace方法增加或者替换
```
   3. 通过 PatternReplaceConfig.addDesensitizationRule 配置脱敏信息  
   eg: [CustomPatternReplaceConfig](../../samples/src/main/java/com/weweibuy/framework/samples/log/CustomPatternReplaceConfig.java)


### 4 日志链路追踪
  引入依赖即可, 如需关闭:   
`application.properites:`
```
common.log.trace.enable = false
```
  1. 说明
     该功能基于MDC实现,自动为Http请求响应绑定Logback MDC属性为: tid  
  2. 多线程MDC值传递  
     该功能基于 TTL  [transmittable-thread-local](https://github.com/alibaba/transmittable-thread-local)实现  
  首先需要加入依赖  

```maven
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>transmittable-thread-local</artifactId>
</dependency>  
```
   然后修饰线程池:  
  [修饰线程池](https://github.com/alibaba/transmittable-thread-local#22-%E4%BF%AE%E9%A5%B0%E7%BA%BF%E7%A8%8B%E6%B1%A0)  
  [使用Java Agent来修饰JDK线程池实现类](https://github.com/alibaba/transmittable-thread-local#23-%E4%BD%BF%E7%94%A8java-agent%E6%9D%A5%E4%BF%AE%E9%A5%B0jdk%E7%BA%BF%E7%A8%8B%E6%B1%A0%E5%AE%9E%E7%8E%B0%E7%B1%BB)  

### 4 日志策略分环境
  common-log 模块配置了[logback-spring.xml](src/main/resources/logback-spring.xml), 只有在spring激活dev环境的情况下才输出控制台日志    
  非dev环境输入文件日志,默认输出路径为:  /logs/${app_name} 文件名: application.log  
  同时也支持通过环境变量 log.path 指定日志文件输出路径  
  
