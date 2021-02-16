# common-feign
  场景: 
   - 希望输出请求,响应日志  
   - 对接不同的系统,其接口报文风格为下划线风格,而Java对象为小驼峰风格. 我们希望不改Java对象的还可以正常完成序列化或反序列化  
   - 对接方接口还没好,我们可以无侵入的Mock接口数据,先跑通自己这边逻辑  
   - 做到调用APM,异常信息跨应用上抛   
  适用: SpringBoot2x + jackson + spring-cloud-openfeign2x 


### 1.1 加入依赖：
  引入依赖自动配置
`maven:`
```
<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>common-feign</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
  引入依赖后自动输出请求响应日志,添加APM,异常信息跨应用上抛; 
  项目中自动依赖了HttpClient(Feign默认使用JDK的ulrConnection,没有连接池支持),并做了自动配置; feign, RestTemplate 都将使用HttpClient;  
  若要使用HttpClient,可以注入 CloseableHttpClient;


### 1.2 Feign报文风格：
#### 1. 配置
  [CamelCaseEncoderAndDecoder](src/main/java/com/weweibuy/framework/common/feign/support/CamelCaseEncoderAndDecoder.java) JSON序列化反序列使用驼峰风格  
  [SnakeCaseEncoderAndDecoder](src/main/java/com/weweibuy/framework/common/feign/support/SnakeCaseEncoderAndDecoder.java) JSON序列化反序列使用下划线风格


#### 2. 使用示例：
  [MyFeignClient](../../samples/src/main/java/com/weweibuy/framework/samples/client/MyFeignClient.java) 
  
#### 3. 注意：
  Feign的默认JSON序列化反序列风格与SpringMVC的接口的风格是保持一致的在
  ```
  org.springframework.cloud.openfeign.FeignClientsConfiguration
  ```
  注入的是SpringMVC的 HttpMessageConverters: 

`application.properites:`
```
spring.jackson.property-naming-strategy = xxxx
```
  没有配置, 默认是 DTO对象的命名风格
  

### 1.3 Mock接口：
   1. 添加启动命令或配置
  添加启动命令:  
`启动命令`
```
-Dspring.profiles.active = mock
```
   添加配置:  
`application.properites:`
```
spring.profiles.active = mock
```
   2. 添加mock配置文件 mock.json, 并创建同级文件夹mock  
   配置文件位置: 
   方法一: 
     在主启动类对应的Maven 模块下 src/test/resources/feign/ 目录下创建 mock.json 
     eg: [mock.json](../../samples/src/test/resources/feign/mock.json) 
 
   方法一: 
     通过环境变量: feign.mock.dir 指定 mock.json所在的文件夹地址
   
   mock.json 格式示例:
```json
[
  {
    "host": "http://localhost:9000",  // 请求Host
    "path": "/hello",   // 请求 Path
    "method": "post",   // 请求 Method
    "target": "hello.json",  // Mock 的响应报文文件
    "enable": true  // 是否mock, 默认: fasle
  }
]
```
  在mock文件夹下创建: hello.json
```json
{
  "status": 200,   // mock 响应HttpStatus
  "header": {      // mock 响应HttpHeader, 默认会添加Header Content-Type:application/json
  },
  "body": {       // mock 响应HttpBody
    "code": 0,
    "msg": "请求成功"
  }
}
```
  示例说明: 
    通过feign请求: POST http://localhost:9000/hello, 将读取 mock/hello.json 中的内容作为响应
```txt
  提示: 更改 mock.json 与 mock文件夹的文件都是实时生效的无需重启
```

### 1.4 APM:
   我们为Feign默认配置了请求拦截器,拦截器会取出MDC中的traceCode,并将其中以HttpHeader的形式传递给下游  
   结合 common-mvc 与 common-log 可以实现全链路的日志追踪
    [TraceContextFeignInterceptor](src/main/java/com/weweibuy/framework/common/feign/log/TraceContextFeignInterceptor.java) 


### 1.5 异常信息跨应用上抛:
   我们遵循SpringCloudFeign基于Rest的思想,通过配置Feign的ErrorEncoder,重新封装异常信息到
   [MethodKeyFeignException](../common-core/src/main/java/com/weweibuy/framework/common/core/exception/MethodKeyFeignException.java)
    中,在通过SpringMvc全局异常捕获,响应对应的Http状态码,可以将异常信息再抛给上层信息来处理.
