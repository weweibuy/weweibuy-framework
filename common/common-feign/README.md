# common-feign
  场景: 使用feign调用接口时,可能对接不同语言的系统,其接口报文风格可能也不同,我们希望可以在不改变DTO对象的命名风格下灵活改变接口报文风格,并输出日志
  使用: SpringBoot2x + jackson + spring-cloud-openfeign2x 


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
### 1.2 Feign报文风格：
#### 1. 配置
  [CamelCaseEncoderAndDecoder](src/main/java/com/weiweibuy/framework/common/feign/support/CamelCaseEncoderAndDecoder.java) JSON序列化反序列使用驼峰风格  
  [SnakeCaseEncoderAndDecoder](src/main/java/com/weiweibuy/framework/common/feign/support/SnakeCaseEncoderAndDecoder.java) JSON序列化反序列使用下划线风格


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
  
### 1.3 HttpClient：
#### 1. 配置HttpClient：
  feign 默认使用的JDK的 HttpURLConnection 发请HTTP请求, 使用HttpClient替换默认方式:
  
`maven:`
```
<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>common-feign</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-httpclient</artifactId>
</dependency>
```
  [HttpClient配置](src/main/java/com/weiweibuy/framework/common/feign/config/HttpClientProperties.java) 
  
#### 2. 注意
  在SpringCloud LB 配置的情况下, 由于LB需要通过服务名称获取服务地址, 所以需要在自行配置LB的 feiClient, 参考: 
```
org.springframework.cloud.openfeign.ribbon.HttpClientFeignLoadBalancedConfiguration
```
