package win.scolia.cloud.sso.controller;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import win.scolia.cloud.sso.annotation.CheckEntry;
import win.scolia.cloud.sso.bean.entity.Permission;
import win.scolia.cloud.sso.bean.vo.entry.PermissionEntry;
import win.scolia.cloud.sso.exception.DuplicatePermissionException;
import win.scolia.cloud.sso.exception.MissPermissionException;
import win.scolia.cloud.sso.service.PermissionService;
import win.scolia.cloud.sso.util.ShiroUtils;

import javax.validation.Valid;

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
     * @param entry 权限
     * @return 201 成功, 409 权限已存在
     */
    @PostMapping
    @RequiresPermissions("system:permission:add")
    @CheckEntry
    public ResponseEntity<Object> addPermission(@RequestBody @Valid PermissionEntry entry, BindingResult bindingResult) {
        String permission = entry.getPermission();
        try {
            permissionService.createPermission(permission);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add permission: {}", ShiroUtils.getCurrentUserName(), permission);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
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
     * @param current 旧权限
     * @param entry  新权限
     * @return 200 成功 404 旧权限不存在 409 新权限已存在
     */
    @PutMapping("{permission}")
    @RequiresPermissions("system:permission:update")
    @CheckEntry
    public ResponseEntity<Object> updatePermission(@PathVariable("permission") String current,
                                                 @RequestBody @Valid PermissionEntry entry, BindingResult bindingResult) {
        String target = entry.getPermission();
        try {
            permissionService.changePermission(current, target);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} update permission: {} to {}", ShiroUtils.getCurrentUserName(), current, target);
            }
            return ResponseEntity.ok().build();
        } catch (MissPermissionException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} update miss permission: {} to {}", ShiroUtils.getCurrentUserName(), current, target);
            }
            return ResponseEntity.notFound().build();
        } catch (DuplicatePermissionException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} update duplicate miss permission: {} to {}", ShiroUtils.getCurrentUserName(), current, target);
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
     * @param pageNum 页码
     * @return 200 成功
     */
    @GetMapping
    @RequiresPermissions("system:permission:list")
    public ResponseEntity<PageInfo> listPermissions(@RequestParam(defaultValue = "1") Integer pageNum) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} list permissions in page: {}", ShiroUtils.getCurrentUserName(), pageNum);
        }
        return ResponseEntity.ok(permissionService.listAllPermission(pageNum));
    }
}
