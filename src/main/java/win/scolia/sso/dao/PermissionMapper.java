package win.scolia.sso.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import win.scolia.sso.bean.entity.Permission;

import java.util.Set;

@Repository
public interface PermissionMapper extends Mapper<Permission> {

    /**
     * 根据角色名获取角色的权限
     *
     * @param roleName 角色名称
     * @return 返回权限列表
     */
    Set<String> selectPermissionsByRoleName(@Param("roleName") String roleName);
}
