package win.scolia.sso.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import win.scolia.sso.bean.entity.Role;
import win.scolia.sso.dao.RoleMapper;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.util.CacheUtils;

import java.util.List;
import java.util.Set;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private CacheUtils cacheUtils;


    @Override
    public void createRole(String roleName) {
        roleMapper.insertRole(roleName);
    }

    @Transactional
    @Override
    public void removeRole(String roleName) {
        Role role = roleMapper.selectRoleByRoleName(roleName);
        if (role == null) {
            return;
        }
        roleMapper.deleteRoleByName(roleName);
        roleMapper.deleteRoleAllMapByRoleId(role.getRoleId());
    }

    @Override
    public void changeRoleName(String oldRoleName, String newRoleName) {
        roleMapper.updateRoleByName(oldRoleName, newRoleName);
    }

    @Override
    public Set<String> getUserRolesByUserName(String userName) {
        Set<String> roles = cacheUtils.getUserRoles(userName);
        if (roles != null) {
            return roles;
        }
        roles = roleMapper.selectUserRolesByUserName(userName);
        cacheUtils.cacheUserRoles(userName, roles);
        return roles;
    }

    @Override
    public List<Role> listRoles() {
        // TODO 获取所有的角色信息
        return null;
    }

}
