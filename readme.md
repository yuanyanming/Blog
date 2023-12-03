# 博客系统

* 纯后端
* 完整的后台代码编写
* 主流技术栈（SpringBoot,MybatisPlus,SpringSecurity,EasyExcel,Swagger2,Redis）
* 完善细致的需求分析
<<<<<<< HEAD
=======

## SpringSecurity思路分析

登录:

​	①自定义登录接口  

​				调用ProviderManager的方法进行认证 如果认证通过生成jwt

​				把用户信息存入redis中

​	②自定义UserDetailsService 

​				在这个实现类中去查询数据库

​	注意配置passwordEncoder为BCryptPasswordEncoder

校验：

​	①定义Jwt认证过滤器

​				获取token

​				解析token获取其中的userid

​				从redis中获取用户信息

​				存入SecurityContextHolder
>>>>>>> 037176c (update)
