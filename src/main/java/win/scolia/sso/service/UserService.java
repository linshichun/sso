package win.scolia.sso.service;

import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.vo.UserVO;

import java.util.List;

public interface UserService {

    /**
     * 插入一个新的用户
     * @param userVO 插入的用户对象
     */
    void createUser(UserVO userVO);

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
     * @return 返回true表示修改成功, false表示修改失败
     */
    boolean changePasswordByOldPassword(String userName, String oldPassword, String newPassword);

    /**
     * 根据用户名获得用户信息
     * @param userName 要查询的用户名
     * @return 返回查询得到的用户密码
     */
    User getUserByUserName(String userName);

    /**
     * 列出所有的用户
     * @return 用户列表
     */
    List<User> listUsers();
}
