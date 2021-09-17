## 项目框架搭建
    1.SpringBoot环境搭建
    2.集成Thymeleaf，RespBean
    3.MybatisPlus
## 分布式会话
    1.用户登录
        a.设计数据库
        b.明文密码二次MD5加密
        c.参数校验+全局异常处理
    2.共享session
        a.SpringSession
        b.Redis
## 功能开发
    1.商品列表
    2.商品详情
    3.秒杀
    4.订单详情

## 系统压测
    1.JMeter
    2.自定义变量模拟多用户
    3.JMeter命令的使用
    4.正式压测
        a.商品列表
        b.秒杀
## 页面优化
    1.页面缓存+URL缓存+对象缓存
    2.页面静态化，前后端分离
    3.静态资源优化
    4.CDN优化（用redis做缓存）a
    
## 接口优化
    1.Redis预减库存减少数据库的访问
    2.内存标记减少Redis的访问（用map存信息）
    3.RabbitMQ异步下单
        a.SpringBoot整合RabbitMQ
        b.交换机
## 安全优化
    1.秒杀接口的隐藏
    2.算术验证码
    3.接口防刷

## 主流秒杀方案

