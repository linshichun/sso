package win.scolia.sso.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleMapper {

    /**
     * 根据用户名获取用户的角色
     * @param username 用户名
     * @return 返回角色列表
     */
    Set<String> selectRolesByUserName(@Param("userName") String username);
}
