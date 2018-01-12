package win.scolia.cloud.sso.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import win.scolia.cloud.sso.bean.entity.Role;

import java.util.Set;

@Repository
public interface RoleMapper extends Mapper<Role> {

    /**
     * 根据角色名获取角色的权限
     *
     * @param roleName 角色名称
     * @return 返回权限列表
     */
    Set<String> selectPermissionsByRoleName(@Param("roleName") String roleName);
}
