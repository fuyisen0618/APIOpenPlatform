# Api开放平台


本项目是一个面向开发者的API平台，提供API接口供开发者调用。用户通过注册登录，可以开通接口调用权限，并可以
浏览和调用接口。每次调用都会进行统计，用户可以根据统计数据进行分析和优化。管理员可以发布接口、下线接口、接
入接口，并可视化接口的调用情况和数据。

## 技术选型
### 前端
- React 18
- Ant Design Pro 5.x脚手架
- Ant Design & Procomponents组件库
- Umi4前端框架
- OpenAPI 前端代码生成
- 数据可视化
### 后端
- Java Spring Boot框架
- MySQL数据库
- MyBatis-Plus及MyBatisX自动生成
- API签名认证(Http调用)
- Spring Boot Starter (SDK开发)
- Dubbo 分布式(RPC、Nacos)
- Spring Cloud Gateway微服务网关
- Swagger + Knife4j接口文档生成
- Hutool、Apache Common Utils、Gson等工具库