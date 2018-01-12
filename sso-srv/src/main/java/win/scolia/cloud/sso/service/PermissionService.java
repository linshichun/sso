package win.scolia.cloud.sso.service;

import com.github.pagehelper.PageInfo;
import win.scolia.cloud.sso.bean.entity.Permission;
import win.scolia.cloud.sso.exception.DuplicatePermissionException;
import win.scolia.cloud.sso.exception.MissPermissionException;

public interface PermissionService {

    /**
     * 创建一个权限
     *
     * @param permission 权限
     * @throws DuplicatePermissionException 权限重复
     */
    void createPermission(String permission);


    /**
     * 删除一个权限
     *
     * @param permission 权限
     * @throws MissPermissionException 权限不存在
     */
    void removePermission(String permission);

    /**
     * 获取权限对象
     *
     * @param permission 具体的权限
     * @return 权限对象
     */
    Permission getPermission(String permission);

    /**
     * 列出所有的权限
     *
     * @return 权限列表
     */
    PageInfo<Permission> listAllPermission(Integer pageNum);
}
