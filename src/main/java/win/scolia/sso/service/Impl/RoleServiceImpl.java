package win.scolia.sso.service.Impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import win.scolia.sso.bean.entity.Role;
import win.scolia.sso.dao.PermissionMapper;
import win.scolia.sso.dao.RoleMapper;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.util.CacheUtils;
import win.scolia.sso.util.PageUtils;

import java.util.List;
import java.util.Set;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private PageUtils pageUtils;

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
        roleMapper.deleteUserRoleMapByRoleId(role.getRoleId()); // 删除 用户-角色的映射
        permissionMapper.deleteRolePermissionMapByRoleId(role.getRoleId()); // 删除 角色-权限 的映射
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
    public PageInfo<Role> listRoles(Integer pageNum) {
        pageUtils.startPage(pageNum);
        List<Role> roles = roleMapper.selectAllRoles();
        return pageUtils.getPageInfo(roles);
    }


}
