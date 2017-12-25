package win.scolia.sso.controller;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import win.scolia.sso.bean.entity.Permission;
import win.scolia.sso.service.PermissionService;
import win.scolia.sso.util.ShiroUtils;

@Controller
@RequestMapping(value = "account/permissions")
public class PermissionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    /**
     * 新增权限
     *
     * @param permission 权限
     * @return 200 成功
     */
    @PostMapping
    @RequiresPermissions("system:permission:add")
    public ResponseEntity<Void> addPermission(@RequestParam String permission) {
        permissionService.createPermission(permission);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} add permission: {}", ShiroUtils.getCurrentUserName(), permission);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 删除权限
     *
     * @param permission 权限
     * @return 200 成功
     */
    @DeleteMapping("{permission}")
    @RequiresPermissions("system:permission:delete")
    public ResponseEntity<Void> deletePermission(@PathVariable("permission") String permission) {
        permissionService.removePermission(permission);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} delete permission: {}", ShiroUtils.getCurrentUserName(), permission);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 更新权限
     *
     * @param oldPermission 旧权限
     * @param newPermission 新权限
     * @return 200 成功
     */
    @PutMapping("{oldPermission}")
    @RequiresPermissions("system:permission:update")
    public ResponseEntity<Void> updatePermission(@PathVariable("oldPermission") String oldPermission,
                                                 @RequestParam String newPermission) {
        permissionService.changePermission(oldPermission, newPermission);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} update permission: {} to {}", ShiroUtils.getCurrentUserName(), oldPermission, newPermission);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 获取权限详情
     *
     * @param permission 权限
     * @return 200 成功
     */
    @GetMapping("{permission}")
    @RequiresPermissions("system:permission:get")
    public ResponseEntity<Permission> getPermission(@PathVariable("permission") String permission) {
        Permission p = permissionService.getPermission(permission);
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
    @RequestMapping("list")
    @RequiresPermissions("system:permission:list")
    public ResponseEntity<PageInfo> listPermissions(@RequestParam Integer pageNum) {
        return ResponseEntity.ok(permissionService.listAllPermission(pageNum));
    }
}
