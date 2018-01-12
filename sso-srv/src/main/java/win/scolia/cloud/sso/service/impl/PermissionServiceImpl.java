package win.scolia.cloud.sso.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import win.scolia.cloud.sso.annotation.NotBlank;
import win.scolia.cloud.sso.bean.entity.Permission;
import win.scolia.cloud.sso.bean.entity.RolePermission;
import win.scolia.cloud.sso.dao.PermissionMapper;
import win.scolia.cloud.sso.dao.RolePermissionMapper;
import win.scolia.cloud.sso.exception.DuplicatePermissionException;
import win.scolia.cloud.sso.exception.MissPermissionException;
import win.scolia.cloud.sso.service.PermissionService;
import win.scolia.cloud.sso.util.PageUtils;
import win.scolia.cloud.sso.util.cache.PermissionCacheUtils;
import win.scolia.cloud.sso.util.cache.RolePermissionCacheUtils;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private PermissionCacheUtils permissionCacheUtils;

    @Autowired
    private RolePermissionCacheUtils rolePermissionCacheUtils;

    @Autowired
    private PageUtils pageUtils;

    @Override
    public void createPermission(@NotBlank String permission) {
        Permission p = permissionCacheUtils.get(permission);
        if (p != null) {
            throw new DuplicatePermissionException(String.format("Permission already exist: %s", permission));
        }
        try {
            permissionMapper.insert(Permission.of(permission).forCreate());
        } catch (DuplicateKeyException e) {
            throw new DuplicatePermissionException(String.format("Permission already exist: %s", permission), e);
        }
    }

    @Transactional
    @Override
    public void removePermission(@NotBlank String permission) {
        Permission p = this.getPermission(permission);
        if (p == null) {
            throw new MissPermissionException(String.format("Permission can not be found: %s", permission));
        }
        permissionMapper.deleteByPrimaryKey(p);
        rolePermissionMapper.delete(
                RolePermission.builder().permissionId(p.getPermissionId()).build()
        );
        permissionCacheUtils.delete(permission);
        rolePermissionCacheUtils.deleteAll();
    }

    @Override
    public Permission getPermission(String permission) {
        Permission p = permissionCacheUtils.get(permission);
        if (p == null) {
            p = permissionMapper.selectOne(Permission.of(permission));
            permissionCacheUtils.cache(permission, p);
        }
        return p;
    }

    @Override
    public PageInfo<Permission> listAllPermission(Integer pageNum) {
        pageUtils.startPage(pageNum);
        List<Permission> permissions = permissionMapper.selectAll();
        return pageUtils.getPageInfo(permissions);
    }
}
