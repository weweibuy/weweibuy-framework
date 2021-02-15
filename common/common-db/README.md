# common-db
  场景: 在数据库中存储敏感信息时对其脱敏
  1. 密码进行不可逆加密
  2. 身份证进行可逆加密
  适用于 mybatis + mybatis-generator
  
### 1.1 加入依赖：

`maven:`
```
<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>common-db</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>common-codec</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
  其中 common-db 为基础的数据量相关依赖, common-codec(可选依赖) 提供加密功能;  
  不可逆加密算法使用: BCrypt  
  可逆加密算法使用: AES (支持秘钥长度: 128-bit, 也就是长度为16的字符串)


### 1.2 AES  配置
   1. 开启AES加密:   
`application.properites:`
```
common.db.encrypt.enable = true  ## 默认为false
```
   2. 配置AES秘钥: 
`application.properites:`
```
common.db.encrypt.password =    ## 以配置形式设置秘钥
common.db.encrypt.passwordFile =    ## 秘钥文件 
```  
  注意:
     AES passwordFile 与 password 配置 二选一  
     passwordFile 支持 eg: classpath:xxx or RelativePath or AbsolutePath    
     
### 1.3 mybatis-generator 配置
  使用mybatis-generator 配置脱敏字段的typeHandler eg:

`generatorConfig.xml:`
```
<table tableName="tb_user" domainObjectName="User">
    <columnOverride column="password" typeHandler="com.weweibuy.framework.common.db.type.BCryptEncryptTypeHandler"/>
    <columnOverride column="id_no" typeHandler="com.weweibuy.framework.common.db.type.AesEncryptTypeHandler"/>
</table>
```

### 1.4 使用: 
   1. 通过 mybatis-generator 生成的Mapper 操作数据库 AesEncryptTypeHandler 加自动加解密
   2. BCryptEncryptTypeHandler在保存,更新数据时将自动加密; 需要匹配密码时调用:  
   com.weweibuy.framework.common.codec.bcrypt.BCryptUtils.match() 方法
   