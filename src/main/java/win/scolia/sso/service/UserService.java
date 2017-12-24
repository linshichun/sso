package win.scolia.sso.service;

import com.github.pagehelper.PageInfo;
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.entity.UserSafely;
import win.scolia.sso.bean.vo.entry.UserEntryVO;

public interface UserService {

    /**
     * 插入一个新的用户
     * @param userEntryVO 插入的用户对象
     */
    void createUser(UserEntryVO userEntryVO);

    /**
     * 根据用户名删除用户
     * @param userName 用户名
     */
    void removeUserByUserName(String userName);

    /**
     * 根据旧密码修改新密码
     * @param userName 用户名
     * @param oldPassword 旧密码, 明文
     * @param newPassword 新密码, 明文
     * @return 返回true表示修改成功, false表示修改失败(密码或用户名不对)
     */
    boolean changePasswordByOldPassword(String userName, String oldPassword, String newPassword);

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
