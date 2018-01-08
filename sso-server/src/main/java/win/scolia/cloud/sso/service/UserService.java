package win.scolia.cloud.sso.service;

import com.github.pagehelper.PageInfo;
import win.scolia.cloud.sso.bean.entity.User;
import win.scolia.cloud.sso.bean.entity.UserSafely;
import win.scolia.cloud.sso.bean.vo.entry.UserEntry;

public interface UserService {

    /**
     * 插入一个新的用户
     * @param vo 插入的用户对象
     */
    void createUser(UserEntry vo);

    /**
     * 根据用户名删除用户
     * @param userName 用户名
     */
    void removeUserByUserName(String userName);

    /**
     * 根据旧密码修改新密码
     * @param userName 用户名
     * @param current 旧密码, 明文
     * @param target 新密码, 明文
     * @return 返回true表示修改成功, false表示修改失败(密码或用户名不对)
     */
    boolean changePasswordByOldPassword(String userName, String current, String target);

    /**
     * 直接修改密码, 仅作为后台管理使用
     * @param userName 用户名
     * @param target 新密码
     */
    void changePasswordDirectly(String userName, String target);

    /**
     * 根据用户名获得用户信息
     * @param userName 要查询的用户名
     * @return 返回查询得到的用户对象, 包含敏感信息
     */
    User getUserByUserName(String userName);

    /**
     * 获取用户对象, 不包含敏感信息
     * @param userName 用户名
     * @return 安全的用户对象
     */
    UserSafely getUserSafelyByUserName(String userName);

    /**
     * 列出所有的用户, 不包含敏感信息
     * @param pageNum 页码数
     * @return 用户列表
     */
    PageInfo<UserSafely> listUsersSafely(Integer pageNum);

    /**
     *  检查用户名是否可用
     * @param userName 用户名
     * @return 可用是返回true, 否则返回false
     */
    boolean checkUserNameUsable(String userName);
}
