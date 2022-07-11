# common-db
  场景: 
  - 在数据库中存储敏感信息时对其脱敏
  - 密码进行不可逆加密  
  - 身份证进行可逆加密   
  - 多数源  
  适用于 mybatis + mybatis-generator
  
### 1.1 加入依赖：

`maven:`
```
<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>common-db</artifactId>
    <version>1.3-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>common-codec</artifactId>
    <version>1.3-SNAPSHOT</version>
</dependency>
```
  其中 common-db 为基础的数据量相关依赖, common-codec(可选依赖) 提供加密功能;  
  不可逆加密算法使用: BCrypt  
  可逆加密算法使用: AES (支持秘钥长度: 128-bit, 也就是长度为16的字符串)


### 1.2 数据脱敏/加密功能
   1.2.1. 开启AES加密:   
`application.properites:`
```
common.db.encrypt.enable = true  ## 默认为false
```
   1.2.2. 配置AES秘钥: 
`application.properites:`
```
common.db.encrypt.password =    ## 以配置形式设置秘钥
common.db.encrypt.passwordFile =    ## 秘钥文件 
```  
  注意:
     AES passwordFile 与 password 配置 二选一  
     passwordFile 支持 eg: classpath:xxx or RelativePath or AbsolutePath    
     
   1.2..3 mybatis-generator 配置
  使用mybatis-generator 配置脱敏字段的typeHandler eg:

`generatorConfig.xml:`
```
<table tableName="tb_user" domainObjectName="User">
    <columnOverride column="password" typeHandler="com.weweibuy.framework.common.db.type.BCryptEncryptTypeHandler"/>
    <columnOverride column="id_no" typeHandler="com.weweibuy.framework.common.db.type.AesEncryptTypeHandler"/>
</table>
```

   1.2..4 使用: 
   1. 通过 mybatis-generator 生成的Mapper 操作数据库 AesEncryptTypeHandler 加自动加解密
   2. BCryptEncryptTypeHandler在保存,更新数据时将自动加密; 需要匹配密码时调用:  
   com.weweibuy.framework.common.codec.bcrypt.BCryptUtils.match() 方法
 
### 1.3 多数源功能
   1.3.1 说明:  
   springBoot 默认只支持单数据源, 其针对于数据源的自动配置都是基于单数据源的; 
   这里我们基于配置动态的生成数据源,事务管理器与Mybatis组件;  
   使用前先排除springBoot与Mybatis的自动配置:  
```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        MybatisAutoConfiguration.class, XADataSourceAutoConfiguration.class})
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```    
   1.3.2 使用
   只需几个简单的配置即可创建数据源与Mybatis组件: eg:
`application.yml:`
```yaml
common:
  db:
    multiple-datasource:
      ds1:  # 数据源名称, 自定义
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource # 目前支持 HikariDataSource
        url: jdbc:mysql://localhost:3306/db1
        username: u
        password: p
        primary: true  # 是否是主要数据源默认false, 若为true, 创建事务管理器也为主要
        create-transaction-manager: true  #是否创建事务管理器 默认false
        transaction-manager-name: myDs1TransactionManager  # 事务管理器在Spring中的BeanName, 默认: 数据源名称 + TransactionManager
        hikari: # hikari连接池配置,参考SpringBoot 原生的 hikari连接池配置 spring.datasource.hikari
          maximum-pool-size: 20
        mybatis:  # mybatis 配置, 可以配置多个, 配置项参考原生mybatis配置,  mybatis.  
          - mapper-locations: classpath*:config/mybatis/mapper/user/*.xml
            mapper-scan-packages: com.weweibuy.test.user.mapper  # mapper包扫描路径,多个逗号分隔
      ds2:
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
        url: jdbc:mysql://localhost:3306/db2
        username: u
        password: p
        mybatis:
          - mapper-locations: classpath*:config/mybatis/mapper/gw/*.xml
            mapper-scan-packages: com.weweibuy.test.gw.mapper
```  
   使用时直接注入,被包扫描的Mapper接口即可,数据操作与单数据源无异.   
   注意若没有指定主要的事务管理器(primary: false)时, 需要在开启事务的时候请指定事务管理器 eg:  
```
@Transactional(value = "myDs1TransactionManager")
```