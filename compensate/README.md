# compensate
  适用于补偿异步任务场景的组件.  
   场景: 
   - 如A服务调用B服务,B服务在做完主业务流程后,在B服务内部需要异步执行B-1任务,若B-1任务失败希望有机制可以重试  
   - 重试机制可以灵活配置 

  补偿基于注解的形式,依赖于Spring5.提供两种形式的补偿:   
   1. 自动将被注解标记的方法的参数(只支持普通POJO)序列化,等到补偿触发时在反序列化,然后重新调用方法.   
   2. 获取方法参数中的业务id号,等到补偿触发时自行根据id号组装方法参数,然后调用方法.  
  
  
## 使用 

### 1.加入依赖
`maven:`
```
<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>compensate-core</artifactId>
</dependency>
```
  开启补偿: 在配置类加上注解: 
  ```
  @EnableCompensate
  ```
### 2.使用
####  2.1使用注解: 
  使用注解:
```
@Compensate
```
  标记在异常时需要补偿的方法:
  示例:   [CompensateSimpleService](../samples/src/main/java/com/weweibuy/framework/samples/compensate/service/CompensateSimpleService.java) 

####  2.2配置补偿信息:
  组件内置了以配置文件进行配置的形式: 
`application.properites:`
```
compensate.yourCompensateKey.compensate-type =  # yourCompensateKey 关联@Compensate注解里的key属性值, 
    compensate-type: 0表示自动序列方法参数补偿, 1表示同步业务id自行组装
compensate.yourCompensateKey.retry-rule =  # 重试规则  示例: 1m 3m 1h  表示重试3次,分别在 1m, 3m, 1h 时重试
compensate.yourCompensateKey.alarm-rule =  # 报警规则 示例: 10s 20s ...  表示在重试失败后10s, 20s时报警, 之后没间隔20s 报警一次
```
  自定义配置源:
    组件默认使用配置文件的形式配置补偿规则,也可以通过实现:  [CompensateConfigStore](interface/src/main/java/com/weweibuy/framework/compensate/interfaces/CompensateConfigStore.java)接口,交给spring管理覆盖默认配置
  
####  2.3 根据业务id组装方法参数
  在 配置compensate-type为1的情况下, 需要实现 [BizIdCompensateAssemble](interface/src/main/java/com/weweibuy/framework/compensate/interfaces/BizIdCompensateAssemble.java)接口,
  交给spring管理,来自行根据compensateKey与业务id自行组装补偿参数
  
####  2.4 自定义组件
##### 2.4.1 补偿触发器
   [CompensateTrigger](interface/src/main/java/com/weweibuy/framework/compensate/interfaces/CompensateTrigger.java)
   触发补偿器默认实现: SimpleCompensateTrigger 是基于JDK Timer 定时任务的触发器, 可自行实现接口替换
 
##### 2.4.2 补偿数据存储器:                                  
   [CompensateStore](interface/src/main/java/com/weweibuy/framework/compensate/interfaces/CompensateStore.java)
   触发补偿器默认实现:  SimpleCompensateStore 是基于内存的存储;可以引入:  
`maven:`
```
<dependency>
 <groupId>com.weweibuy.framework</groupId>
 <artifactId>compensate-mybatis</artifactId>
</dependency>
```      
  使用JDBC + mybatis 存储数据

##### 2.4.3 线程池设置: 
  示例: [ThreadPoolCompensateConfigurer](../samples/src/main/java/com/weweibuy/framework/samples/compensate/service/ThreadPoolCompensateConfigurer.java)

##### 2.4.4 报警: 
   [CompensateAlarmService](interface/src/main/java/com/weweibuy/framework/compensate/interfaces/CompensateAlarmService.java)
   默认为输出日志,可以自行实现接口替换