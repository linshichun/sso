## SSO 单点登录系统

    使用spring boot, spring session, mybatis, redis, mysql等搭建, 使用spring cloud相关技术.

## 简介

使用spring cloud搭建的登录系统微服务.

## API:

下面是 *RESTFul* 风格的API, 某些特定的接口需要登录后才能访问, 而登录的用户可能还需要特定的权限.

*{PathVariable}* 中括号表示使用路径参数, 请按照实际需求填写内容.

API采用的是http状态码, 如不作特殊说明时, 出现下面的状态码时, 表示是意思是:

    200: 成功
    201: 创建成功
    400: 参数错误
    401: 未登录/登录时表示用户名或密码错误
    403: 权限不足
    404: 用户/角色/权限不存在
    409: 冲突, 常见于用户名/角色名/权限重复, 或为用户/角色添加重复的角色/权限
    422: 表单校验错误

下面的api接口描述中, 只描述特殊的情况.

*注意, 提交表单的时候, 必须使用json数据, 否则会视为参数错误.*

- - -

#### 登录注册

##### 注册
    POST account/register
    json:
        {"userName": "用户名", "password": "密码"}
        
##### 检查用户名是否可用
    GET account/register/userName
    json:
        {"userName": "用户名"}

##### 登录
    POST account/login
    json:
        {"userName": "用户名", "password": "密码", "rememberMe": 布尔值, 默认为false/非必须}
        
##### 登出
    GET account/logout
        
##### 获取当前登录用户的信息
    GET account/current

##### 修改当前用户的密码
    PUT account/current/password
    json:
        {"current": "当前的密码", "target": "新密码"}

- - -

#### 用户管理

##### 添加用户
    POST account/users
    json:
        {"userName": "用户名", "password": "密码"}
    需求权限:
        system:user:add

##### 删除用户
    DELETE account/users/{userName}
    PathVariable:
        userName: 要删除的用户名
    需求权限:
        system:user:delete

##### 修改某用户的密码
    PUT account/users/{userName}/password
    PathVariable:
        userName: 要修改密码的用户名
    json:
        {"password": "新密码"}
    需求权限:
        system:user:update

##### 获取某个用户的详细信息
    GET account/users/{userName}
    PathVariable:
        userName: 要获取的用户名
    需求权限:
        system:user:get

##### 列出所有的用户
    GET account/users
    QueryString:
        pageNum: 指定页码
    需求权限:
        system:user:list

##### 为用户添加角色
    POST account/users/{userName}/roles
    PathVariable:
        userName: 要添加的用户名
    json:
        {"roleName": "要添加的角色"}
    返回:
        460 要添加的角色不存在
    需求权限:
        system:user:edit

##### 为用户删除角色
    DELETE account/users/{userName}/roles/{roleName}
    PathVariable:
        userName: scolia(要操作的用户名)
        roleName: admin(要删除的角色名)
    返回:
        460 要添加的角色不存在
    需求权限:
        system:user:edit

- - -

#### 角色管理

##### 添加角色
    POST account/roles
    json:
        {"roleName": "角色名"}
    需求权限:
        system:role:add

##### 删除一个角色
    DELETE account/roles/{roleName}
    PathVariable:
        roleName: 要删除的角色名
    需求权限:
        system:role:delete

##### 修改角色名
    PUT account/roles/{roleName}
    PathVariable:
        roleName: admin(原先的角色名)
    json:
        {"roleName": "新的角色名"}
    需求权限:
        system:role:update

##### 获取角色的详细信息
    GET account/roles/{roleName}
    PathVariable:
        roleName: 要获取的角色名
    需求权限:
        system:role:get

##### 列出所有的角色信息
    GET account/roles
    QueryString:
        pageNum: 页码
    需求权限:
        system:role:list

##### 为角色添加权限
    POST account/roles/{roleName}/permissions
    PathVariable:
        roleName: 角色名
    json:
        {"permission": "system:user:add"}
    返回:
        461 权限不存在
    需求权限:
        system:role:edit

##### 为角色删除一个权限
    DELETE account/roles/{roleName}/permissions/{permission}
    PathVariable:
        roleName: 要操作的角色名
        permission: system:user:add(要删除的权限)
    返回:
        461 权限不存在
    需求权限:
        system:role:edit

- - -

#### 权限管理

##### 添加权限
    POST account/permissions
    json:
        {"permission": "system:user:add"}
    需求权限:
        system:permission:add
    
#####  删除权限
    DELETE account/permissions/{permission}
    PathVariable:
        permission: 要删除的权限
    需求权限:
        system:permission:delete

##### 修改权限
    PUT account/permissions/{permission}
    PathVariable:
        permission: system:user:add(旧权限)
    json:
        {"permission": "system:user:delete(新权限)"}
    需求权限:
        system:permission:update

##### 获取权限详情
    GET account/permissions/{permission}
    PathVariable:
        permission: 要获取的权限
    需求权限:
        system:permission:get

##### 列出所有权限
    GET account/permissions
    QueryString:
        pageNum: 页码
    需求权限:
        system:permission:list