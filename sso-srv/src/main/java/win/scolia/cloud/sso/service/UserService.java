package win.scolia.cloud.sso.service;

import com.github.pagehelper.PageInfo;
import win.scolia.cloud.sso.bean.entity.User;
import win.scolia.cloud.sso.bean.entity.UserSafely;
import win.scolia.cloud.sso.exception.*;

import java.util.Set;

public interface UserService {

    /**
     * 插入一个新的用户
     *
     * @param userName 用户名
     * @param password 密码
     * @throws DuplicateUserException 用户重复
     */
    void createUser(String userName, String password);

    /**
     * 根据用户名删除用户
     *
     * @param userName 用户名
     * @throws MissUserException 用户不存在
     */
    void removeUserByName(String userName);

    /**
     * 根据旧密码修改新密码
     *
     * @param userName 用户名
     * @param current 当前密码
     * @param target 新密码
     * @throws MissUserException 用户不存在
     * @throws AuthenticationException 密码错误
     */
    void changePassword(String userName, String current, String target);

    /**
     * 直接修改密码
     *
     * @param userName 用户名
     * @param password 新密码
     * @throws MissUserException 用户不存在
     */
    void changePasswordDirect(String userName, String password);

    /**
     * 根据用户名获取用户
     *
     * @param userName 用户名
     * @return 用户对象, 无法获取时返回null
     */
    User getUserByName(String userName);

    /**
     * 列出所有的用户, 不包含敏感信息
     *
     * @param pageNum 页码数
     * @return 用户列表
     */
    PageInfo<UserSafely> listUsersSafely(Integer pageNum);

    /**
     * 获取用户的角色
     *
     * @param userName 用户名
     * @return 角色名集合
     * @throws MissUserException 用户不存在
     */
    Set<String> getUserRoles(String userName);

    /**
     * 获取用户的权限
     *
     * @param userName 用户名
     * @return 用户的权限集合
     */
    Set<String> getUserPermissions(String userName);

    /**
     * 为用户添加角色
     *
     * @param userName 用户名
     * @param roleName 角色名
     * @throws MissUserException 用户不存在
     * @throws MissRoleException 角色不存在
     * @throws DuplicateRoleException 重复的角色
     */
    void addUserRole(String userName, String roleName);

    /**
     * 为用户删除角色
     *
     * @param userName 用户名
     * @param roleName 角色名
     * @throws MissUserException 用户不存在
     * @throws MissRoleException 角色不存在
     */
    void removeUserRole(String userName, String roleName);
}
