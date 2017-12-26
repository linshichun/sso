## SSO 单点登录系统

    使用spring boot, spring session, mybatis, redis, mysql等搭建, 采用RESTFul风格.

## API:

    下面是RESTFul风格的API, 某些特定的接口需要登录后才能访问, 而登录的用户可能还需要特定的权限.
    {PathVariable} 中括号表示使用路径参数, 请安装实际需求填写内容. 当使用PUT提交表单时, 
    由于springMVC的限制, 只能采取 application/x-www-form-urlencoded 的方式, 还请注意.

#### 登录注册

##### 注册
    POST account/register
    form-data:
        userName: scolia(用户名)
        password: 123456(密码)
    返回:
        201 成功
        400 参数错误, 会伴有详细的错误信息.
        
##### 检查用户名是否可用
    POST account/register/check
    form-data:
            userName: scolia(用户名)
    返回:
        200 可用
        409 不可用

##### 登录
    POST account/login
        userName: scolia(用户名)
        password: 123456(密码)
        rememberMe: true/false(记住我)
    返回:
        200 成功, 并设置SESSION和remeberMe的相关cookie
        400 参数错误
        401 登录失败
        
##### 登出
    GET account/logout
    返回:
        200 成功
        401 未登录
        
##### 获取当前登录用户的信息
    GET account/current
    返回:
        200 成功, 并附带当前用户的详细信息, 包含用户的角色和权限
        401 未登录

##### 修改当前用户的密码
    PUT account/current/password
    x-www-form-urlencoded:
        oldPassword: 123465(当前的密码)
        newPassword: 654321(要修改成什么密码)
    返回:
        200 成功
        400 参数错误/登录失败
        401 未登录
                
#### 权限管理

##### 添加权限
    POST account/permissions
    form-data: 
        permission: system:user:add(采用shiro的格式)
    返回:
        200 成功
        409 权限已存在
    需求权限:
        system:permission:add
    
#####  删除权限
    DELETE account/permissions/{permission}
    返回:
        200 成功
        404 要删除的权限存在
    需求权限:
        system:permission:delete