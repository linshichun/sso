package win.scolia.sso.controller;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import win.scolia.sso.bean.entity.Permission;
import win.scolia.sso.exception.DuplicatePermissionException;
import win.scolia.sso.exception.MissPermissionException;
import win.scolia.sso.service.PermissionService;
import win.scolia.sso.util.ShiroUtils;

@Controller
@RequestMapping(value = "account/permissions")
@RequiresAuthentication
public class PermissionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    /**
     * 新增权限
     *
     * @param permission 权限
     * @return 200 成功, 409 权限已存在
     */
    @PostMapping
    @RequiresPermissions("system:permission:add")
    public ResponseEntity<Void> addPermission(@RequestParam String permission) {
        try {
            permissionService.createPermission(permission);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add permission: {}", ShiroUtils.getCurrentUserName(), permission);
            }
            return ResponseEntity.ok().build();
        } catch (DuplicatePermissionException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add duplicate permission: {}", ShiroUtils.getCurrentUserName(), permission);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * 删除权限
     *
     * @param permission 权限
     * @return 200 成功 404 权限不存在
     */
    @DeleteMapping("{permission}")
    @RequiresPermissions("system:permission:delete")
    public ResponseEntity<Void> deletePermission(@PathVariable("permission") String permission) {
        try {
            permissionService.removePermission(permission);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete permission: {}", ShiroUtils.getCurrentUserName(), permission);
            }
            return ResponseEntity.ok().build();
        } catch (MissPermissionException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete miss permission: {}", ShiroUtils.getCurrentUserName(), permission);
            }
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 更新权限
     *
     * @param oldPermission 旧权限
     * @param newPermission 新权限
     * @return 200 成功 404 旧权限不存在 409 新权限已存在
     */
    @PutMapping("{oldPermission}")
    @RequiresPermissions("system:permission:update")
    public ResponseEntity<Void> updatePermission(@PathVariable("oldPermission") String oldPermission,
                                                 @RequestParam String newPermission) {
        try {
            permissionService.changePermission(oldPermission, newPermission);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} update permission: {} to {}", ShiroUtils.getCurrentUserName(), oldPermission, newPermission);
            }
            return ResponseEntity.ok().build();
        } catch (MissPermissionException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} update miss permission: {} to {}", ShiroUtils.getCurrentUserName(), oldPermission, newPermission);
            }
            return ResponseEntity.notFound().build();
        } catch (DuplicatePermissionException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} update duplicate miss permission: {} to {}", ShiroUtils.getCurrentUserName(), oldPermission, newPermission);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * 获取权限详情
     *
     * @param permission 权限
     * @return 200 成功 404 找不到该权限
     */
    @GetMapping("{permission}")
    @RequiresPermissions("system:permission:get")
    public ResponseEntity<Permission> getPermission(@PathVariable("permission") String permission) {
        Permission p = permissionService.getPermission(permission);
        if (p == null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} get miss permission: {}", ShiroUtils.getCurrentUserName(), permission);
            }
            return ResponseEntity.notFound().build();
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} get permission: {}", ShiroUtils.getCurrentUserName(), permission);
        }
        return ResponseEntity.ok().body(p);
    }

    /**
     * 获取权限列表
     *
     * @param pageNum 页面
     * @return 200 成功
     */
    @RequestMapping("list/{pageNum}")
    @RequiresPermissions("system:permission:list")
    public ResponseEntity<PageInfo> listPermissions(@PathVariable("pageNum") Integer pageNum) {
        return ResponseEntity.ok(permissionService.listAllPermission(pageNum));
    }
}
