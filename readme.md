# 博客系统

* 纯后端
* 完整的后台代码编写
* 主流技术栈（SpringBoot,MybatisPlus,SpringSecurity,EasyExcel,Swagger2,Redis）
* 完善细致的需求分析



## SpringSecurity思路分析

### 1.认证

![image-20231204002825110](C:\Users\84799\IdeaProjects\SGBlog\img\image-20231204002825110.png)

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

核心代码实现

1. 创建一个类实现UserDetailsService接口，重写其中的方法。更加用户名从数据库中查询用户信息

2. 因为UserDetailsService方法的返回值是UserDetails类型，所以需要定义一个类，实现该接口，把用户信息封装在其中。
3. 我们需要自定义一个过滤器，这个过滤器会去获取请求头中的token，对token进行解析取出其中的userid。使用userid去redis中获取对应的LoginUser对象。然后封装Authentication对象存入SecurityContextHolder

### 2.授权

在SpringSecurity中，会使用默认的FilterSecurityInterceptor来进行权限校验。在FilterSecurityInterceptor中会从SecurityContextHolder获取其中的Authentication，然后获取其中的权限信息。当前用户是否拥有访问当前资源所需的权限。

1.SecurityConfig

~~~~java
@EnableGlobalMethodSecurity(prePostEnabled = true)
~~~~

2.PermissionService（名称ps）类中自定义方法hasPermission

3.方法上添加@PreAuthorize("@ps.hasPermission('content:category:export')")
