package win.scolia.sso.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import win.scolia.sso.dao.RoleMapper;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.util.CacheUtils;

import java.util.Set;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private CacheUtils cacheUtils;

    @Override
    public Set<String> getRolesByUserName(String userName) {
        Set<String> roles = cacheUtils.getRoles(userName);
        if (roles != null) {
            return roles;
        }
        roles = roleMapper.selectRolesByUserName(userName);
        cacheUtils.cacheRoles(userName, roles);
        return roles;
    }
}
