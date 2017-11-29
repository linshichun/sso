package win.scolia.sso.mapper;

import org.apache.ibatis.annotations.Param;
import win.scolia.sso.api.pojo.User;

import java.util.List;

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
     * 根据用户名获得用户信息
     * @param username 要查询的用户名
     * @return 返回查询得到的用户密码
     */
    User selectPasswordAndSaltByUsername(@Param("username") String username);

    /**
     * 根据用户名获取用户的角色
     * @param username 用户名
     * @return 返回角色列表
     */
    List<String> selectRolesByUserName(@Param("username") String username);

    List<String> selectPermissionsByRoleName(@Param("roleName") String roleName);
}
