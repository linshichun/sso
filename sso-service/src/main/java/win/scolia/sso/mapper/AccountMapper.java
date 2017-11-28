package win.scolia.sso.mapper;

import org.apache.ibatis.annotations.Param;
import win.scolia.sso.api.pojo.User;

/**
 * Created by scolia on 2017/11/27
 */
public interface AccountMapper {

    /**
     * 插入一个新的用户
     * @param user 插入的用户对象
     * @return 返回主键
     */
    Integer insertUser(@Param("user") User user);

    /**
     * 根据用户名用户的密码
     * @param username 要查询的用户名
     * @return 返回查询得到的用户密码
     */
    User selectPasswordAndSaltByUsername(@Param("username") String username);
}
