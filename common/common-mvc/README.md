# common-mvc
  场景: 对于非JSON请求,我们希望将下划线风格的请求数据转为DTO的驼峰形式, 同一异常处理, 响应数据脱敏
  适用于Springboot + SpringMVC5x + Jackson
  
### 1.1 加入依赖：

`maven:`
```
<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>common-mvc</artifactId>
</dependency>
```
    
### 1.2 下划线风格转化：
 使用注解,标记mvc绑定实体
```
@SnakeCaseRequestParamBody
```
 示例: [MvcController#request2](../../samples/src/main/java/com/weweibuy/framework/samples/controller/MvcController.java)


### 1.3 数据脱敏(jackson):
 使用注解,ResponseBody 响应数据DTO 定义脱敏规则 
```
@SensitiveData
```
  示例: [SensitiveController](../../samples/src/main/java/com/weweibuy/framework/samples/controller/SensitiveController.java)

### 1.4 同一异常处理:
  [CommonExceptionAdvice](src/main/java/com/weiweibuy/framework/common/mvc/advice/CommonExceptionAdvice.java)