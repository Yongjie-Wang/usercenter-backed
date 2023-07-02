# 后端启动
1. 通过sql语句创建数据库
2. 修改application.yaml中的数据库登入信息及端口号
3. 在idea中尝试连接数据库
4. 若上线还需修改application-prod.yaml中数据库信息
5. 前后端口配对无误情况下，可以对接
# 流程
1. 通过sql语句创建数据库
2. 修改application.yaml中的数据库登入信息及端口号
3. 在idea中尝试连接数据库
4. 若上线还需修改application-prod.yaml中数据库信息
5. 前后端口配对无误情况下，可以对接



## 流程

1. 创建springBoot模板

2. 尝试连接数据库

3. 通过mybatis-plus实现小demo参考([快速开始 | MyBatis-Plus (baomidou.com)](https://baomidou.com/pages/226c21/#添加依赖))

4. ```
   spring:
     application:
       name: user-center
     # DataSource Config
     datasource:
       driver-class-name: com.mysql.jdbc.Driver
       url: jdbc:mysql://localhost:3306/yupi
       username: 你的数据库用户名
       password: 你的数据库密码
   server:
       port: 8080
   ```

   5. Spring Boot 启动类中添加 @MapperScan 注解，扫描 Mapper 文件夹、
   6. ...完成demo

## 正式开发

### 创表

| 用户表       |                          |                 |
| ------------ | ------------------------ | --------------- |
| 字段         | 说明                     | 类型            |
| id           | 用户id（主键）           | bigint          |
| username     | 昵称                     | varchar         |
| userAccount  | 登录账号                 | varchar         |
| avatarUrl    | 头像                     | varchar         |
| gender       | 性别                     | tinyint         |
| userPassword | 密码                     | varchar         |
| phone        | 电话                     | varchar         |
| email        | 邮箱                     | varchar         |
| userStatus   | 用户状态                 | int  (0 - 正常) |
| createTime   | 创建时间（数据插入时间） | datetime        |
| updateTime   | 更新时间（数据更新时间） | datetime        |
| isDelete     | 是否删除 0 1（逻辑删除） | tinyint         |

### 在yarm配置文件添加

```
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: isDelete
      logic-delete-value: 1
      logic-not-delete-value: 0
  configuration:
    map-underscore-to-camel-case: false
```

### mybaits-x插件安装

**==进行自动生成代码==**

- 注意class name strategy 和options Actual Column

### Test(saving)

- 推荐插件==GenerateAllSetter==

- 推荐插==GenerateAllSetter==

- 可帮助在类实例上补全**AllSetter**

- 可帮助在方法上补全**参数**

- ==断言==（Assertions.assertEquals(true, result);）

- 进行测试（插入和查询）

  ==注意==**mybatis-plus默认会将驼峰转换成下划线，所以就出现 在“字段列表”中出现未知列“user_account”，在application.yml关闭默认转换**

```
  //mybatis-plus:一级子目录位置
  configuration:
    map-underscore-to-camel-case: false
```

### ==cookie和session==

