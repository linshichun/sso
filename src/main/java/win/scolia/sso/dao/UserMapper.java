package win.scolia.sso.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.entity.UserSafely;

import java.util.List;

/**
 * Created by scolia on 2017/11/27
 */
@Repository
public interface UserMapper {

    /**
     * 插入一个新的用户
     *
     * @param user 插入的用户对象
     */
    void insertUser(User user);

    /**
     * 根据用户名删除用户
     * @param userName 用户名
     */
    void deleteUserByUserName(@Param("userName") String userName);

    /**
     * 修改某用户的密码
     * @param user 用户对象, 要求必须有userName, password 和 lastModified 属性
     */
    void updatePasswordByUserName(User user);

    /**
     * 根据用户名获得用户信息
     *
     * @param userName 要查询的用户名
     * @return 返回查询得到的用户
     */
    User selectUserByUserName(@Param("userName") String userName);

    /**
     * 获取所有的用户, 不包含敏感信息
     * @return 用户列表
     */
    List<UserSafely> selectAllUserSafely();
}
