# common-mvc
  场景: 
   - 下滑线风格的url请求参数,将其绑定到小驼峰的Java对象属性上
    eg: localhost/oauth/token?client_id=demoClientId 将client_id映射成clientId
   - Http响应报文部分字段脱敏(如:手机号)
   - 统一异常处理,上抛Feign调用异常
  适用于 Springboot + SpringMVC5x + Jackson
  
### 1 加入依赖：

```
<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>common-mvc</artifactId>
</dependency>
```
    
### 2 下划线风格转化：
 使用注解,标记mvc绑定实体,可以将 下划线风格转为小驼峰风格
```
@SnakeCaseRequestParamBody
```
 示例: [MvcController#request2](../../samples/src/main/java/com/weweibuy/framework/samples/controller/MvcController.java)


### 3 数据脱敏(jackson):
 使用注解@SensitiveData 定义脱敏规则, 在响应是会按规则进行报文脱敏
```
@SensitiveData
```
  示例: [SensitiveController](../../samples/src/main/java/com/weweibuy/framework/samples/controller/SensitiveController.java)

### 4 统一异常处理:
  1. 说明
    SpringCloud 我们遵循其 Rest的原则来定义异常报文  
  2. 全局异常处理  
   [CommonExceptionAdvice](src/main/java/com/weweibuy/framework/common/mvc/advice/CommonExceptionAdvice.java)
  3. 上抛Feign调用异常  
   [FeignExceptionAdvice](src/main/java/com/weweibuy/framework/common/mvc/advice/FeignExceptionAdvice.java)

### 5 可读取的请求响应处理:
  1. 说明
    正常情况,springMvc的请求与响应body体只能进行一次读取, 多次读取会报错, 因此我们提供了可以进行多次读取的接口
  2. 可读取请求体的接口
    [ReadableBodyRequestHandler](../common-core/src/main/java/com/weweibuy/framework/common/core/support/ReadableBodyRequestHandler.java)
  3. 可读取响应体的接口
    [ReadableBodyResponseHandler](../common-core/src/main/java/com/weweibuy/framework/common/core/support/ReadableBodyRequestHandler.java)
  4. 适用场景:
     - 请求/响应日志输出
     - 请求/响应签约/验签