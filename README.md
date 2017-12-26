## SSO 单点登录系统

    使用spring boot, spring session, mybatis, redis, mysql等搭建, 采用RESTFul风格.

## API:
    下面是RESTFul风格的API, 某些特定的接口需要登录后才能方法, 而登录的用户需要特定的权限.
    *{PathVariable}* 中括号表示使用路径参数, 请安装实际需求填写内容. 当使用PUT提交表单时, 
    由于springMVC的限制, 只能采取 **application/x-www-form-urlencoded** 的方式, 还请注意.

#### 权限管理

##### 添加权限
    POST account/permissions
    form-data: 
        key:permission 
        value:system:user:add(采用shiro的格式)
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