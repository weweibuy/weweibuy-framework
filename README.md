
 
weweibuy-framework 
--------

### weweibuy-framework 提供一些基于SpringBoot 2X 封装的组件,可作为企业级SpringBoot技术栈的基础依赖;  其中用合理的方式将日志,脱敏,http调用,幂等,补偿,MQ等功能融入SpringBoot中,可以极大的简化日常开发


### 1. 核心 common-core
  核心模型,异常,常用工具等
  
### 2. codec common-codec
  整理常用的加解密,签名工具(AES,BCrypt,RSA,JWT)等,并将其封装为可以直接使用的工具  

### 3. 日志 common-log
  完全无侵入的实现: 输出Http请求响应日志,日志脱敏,日志APM等功能的组件  
  适用场景:  
  1. 对于Http请求我们需要输出请求/响应数据,特定请求可以屏蔽日志(如响应Base64图片)
  2. 对请求响应日志部分字段进行脱敏
  3. 日志链路追踪APM功能(并且在多线程下也能正常使用)
  4. 日志策略分环境, 非开发环境不输出控制台日志
- [common-log](./common/common-log/README.md)：common-log 介绍

### 4. 数据库 common-db
  基于Mybatis无侵入的实现,数据库敏感字段的脱敏; 包含AES加解密,BCrypt加密功能  
  适用场景:  数据库中存储敏感信息时对其脱敏  
   1. 密码进行不可逆加密
   2. 身份证进行可逆加密
- [common-db](./common/common-db/README.md)：common-db 介绍

### 5. mvc扩展 common-mvc
  封装了报文脱敏,下滑线风格URL传参,统一异常处理  
  适用场景: 
   1. 下滑线风格的url请求参数,将其绑定到小驼峰的Java对象属性上  
      eg: localhost/oauth/token?client_id=demoClientId 将client_id映射成clientId
   2. Http响应报文部分字段脱敏(如:手机号)
   3. 统一异常处理,上抛Feign调用异常
- [common-mvc](./common/common-mvc/README.md) ：介绍common-mvc

### 6. feign调用 common-feign
  该组件是在SpringCloudFeign提供的功能扩展点上进行扩展,所有的设计思想继承了SpringCloudFeign. 在此基础上  
  实现了调用mock,无侵入日志输出,报文风格转换,跨应用异常上抛,调用APM等功能  
  适用场景:
   1. 希望输出feign请求,响应日志  
   2. 对接不同的系统,其接口报文风格为下划线风格,希望无侵入的互相映射
   3. 对接方接口还没好,我们可以无侵入的Mock接口数据,先跑通自己这边逻辑
   4. 做到调用APM,异常信息跨应用上抛
- [common-feign](./common/common-feign/README.md)：介绍common-feign。


### 4. 补偿机制 compensate
  基于注解的适用于异步场景的失败自动补偿组件  
- [compensate](./compensate/README.md) ：介绍compensate

### 5. 幂等
  基于注解的,对代码几乎无侵入的幂等组件  
- [idempotent](./idempotent/README.md) ：介绍idempotent

### 6. rocketMQ 客户端 
  基于注解形式的RocketMq客户端封装组件,极大简化消息发送/接受  
- [rocketMQ](./rocketmq/README.md) ：介绍rocketMQ客户端








