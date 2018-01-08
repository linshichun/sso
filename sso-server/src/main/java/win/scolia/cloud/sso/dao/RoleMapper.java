package win.scolia.cloud.sso.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import win.scolia.cloud.sso.bean.entity.Role;

import java.util.Set;

@Repository
public interface RoleMapper extends Mapper<Role> {

    /**
     * 根据用户名获取用户的角色
     * @param userName 用户名
     * @return 返回角色列表
     */
    Set<String> selectUserRolesByUserName(@Param("userName") String userName);
}
