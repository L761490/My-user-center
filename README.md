# 用户中心后端
## 技术选型
java
spring（依赖注入框架，帮助你管理 Java 对象，集成一些其他的内容）
springmvc（web 框架，提供接口访问、restful接口等能力）
mybatis（Java 操作数据库的框架，持久层框架，对 jdbc 的封装）
mybatis-plus（对 mybatis 的增强，不用写 sql 也能实现增删改查）
springboot（快速启动 / 快速集成项目。不用自己管理 spring 配置，不用自己整合各种框架）
junit 单元测试库
mysql 数据库

## 实现功能
注册功能
登录功能
删除功能
搜索功能
管理员鉴定

## 项目优化
使用 MyBatis + MyBatis-Plus 进行数据访问层开发、复用大多数通用方法，并且通过继承定制了自己的通用操作模板，大幅提升了项目开发效率
自定义通用返回类，让前端明确后端方法返回的是什么信息
为了明确接口的返回，自定义统一的错误码，并封装了全局异常处理器，从而规范了异常返回、屏蔽了项目冗余的报错细节。
