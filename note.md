# SpringSecurity

## 1.介绍

 一般Web应用的需要进行**认证**和**授权**。

​		**认证：验证当前访问系统的是不是本系统的用户，并且要确认具体是哪个用户**

​		**授权：经过认证后判断当前用户是否有权限进行某个操作**

​	而认证和授权也是SpringSecurity作为安全框架的核心功能。

## 2.认证

### 2.1 登陆校验流程

![image-20211215094003288](C:\Users\84799\Documents\找工作\项目\博客\SpringSecurity\img\image-20211215094003288.png)

### 2.2 原理初探

#### 2.2.1 SpringSecurity完整流程

​	SpringSecurity的原理其实就是一个过滤器链，内部包含了提供各种功能的过滤器。这里我们可以看看入门案例中的过滤器。

![image-20211214144425527](C:\Users\84799\Documents\找工作\项目\博客\SpringSecurity\img\image-20211214144425527.png)

​	图中只展示了核心过滤器，其它的非核心过滤器并没有在图中展示。

**UsernamePasswordAuthenticationFilter**:负责处理我们在登陆页面填写了用户名密码后的登陆请求。入门案例的认证工作主要有它负责。

**ExceptionTranslationFilter：**处理过滤器链中抛出的任何AccessDeniedException和AuthenticationException 。

**FilterSecurityInterceptor：**负责权限校验的过滤器。

​	

​	我们可以通过Debug查看当前系统中SpringSecurity过滤器链中有哪些过滤器及它们的顺序。

![image-20211214145824903](C:\Users\84799\Documents\找工作\项目\博客\SpringSecurity\img\image-20211214145824903.png)





#### 2.2.2 认证流程详解

![image-20211214151515385](C:\Users\84799\Documents\找工作\项目\博客\SpringSecurity\img\image-20211214151515385.png)

概念速查:

Authentication接口: 它的实现类，表示当前访问系统的用户，封装了用户相关信息。

AuthenticationManager接口：定义了认证Authentication的方法 

UserDetailsService接口：加载用户特定数据的核心接口。里面定义了一个根据用户名查询用户信息的方法。

UserDetails接口：提供核心用户信息。通过UserDetailsService根据用户名获取处理的用户信息要封装成UserDetails对象返回。然后将这些信息封装到Authentication对象中。



### 2.3 解决问题

#### 2.3.1 思路分析

登录

​	①自定义登录接口  

​				调用ProviderManager的方法进行认证 如果认证通过生成jwt

​				把用户信息存入redis中

​	②自定义UserDetailsService 

​				在这个实现类中去查询数据库

校验：

​	①定义Jwt认证过滤器

​				获取token

​				解析token获取其中的userid

​				从redis中获取用户信息

​				存入SecurityContextHolder



### 2.4 核心代码实现

1. 创建一个类实现UserDetailsService接口，重写其中的方法。更加用户名从数据库中查询用户信息

2. 因为UserDetailsService方法的返回值是UserDetails类型，所以需要定义一个类，实现该接口，把用户信息封装在其中。
3. 我们需要自定义一个过滤器，这个过滤器会去获取请求头中的token，对token进行解析取出其中的userid。使用userid去redis中获取对应的LoginUser对象。然后封装Authentication对象存入SecurityContextHolder



## 3.授权

总结起来就是**不同的用户可以使用不同的功能**。这就是权限系统要去实现的效果。

### 3.1 授权基本流程

​	在SpringSecurity中，会使用默认的FilterSecurityInterceptor来进行权限校验。在FilterSecurityInterceptor中会从SecurityContextHolder获取其中的Authentication，然后获取其中的权限信息。当前用户是否拥有访问当前资源所需的权限。

SecurityConfig

~~~~java
@EnableGlobalMethodSecurity(prePostEnabled = true)
~~~~

PermissionService

hasPermisson

~~~~java
@Service("ps")
public class PermissionService {

    /**
     * 判断当前用户是否具有permission
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission){
        //如果是超级管理员  直接返回true
        if(SecurityUtils.isAdmin()){
            return true;
        }
        //否则  获取当前登录用户所具有的权限列表 如何判断是否存在permission
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
~~~~



方法上添加@PreAuthorize("@ps.hasPermission('content:category:export')")

