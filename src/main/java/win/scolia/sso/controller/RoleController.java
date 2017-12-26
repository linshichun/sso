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
import win.scolia.sso.bean.entity.Role;
import win.scolia.sso.exception.DuplicatePermissionException;
import win.scolia.sso.exception.DuplicateRoleException;
import win.scolia.sso.exception.MissPermissionException;
import win.scolia.sso.exception.MissRoleException;
import win.scolia.sso.service.PermissionService;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.util.ShiroUtils;

@Controller
@RequestMapping(value = "account/roles")
@RequiresAuthentication
public class RoleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 新增一个角色
     *
     * @param roleName 角色名
     * @return 200 成功 409 该角色已存在
     */
    @PostMapping
    @RequiresPermissions("system:role:add")
    public ResponseEntity<Void> addRole(@RequestParam String roleName) {
        try {
            roleService.createRole(roleName);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add role: {}", ShiroUtils.getCurrentUserName(), roleName);
            }
            return ResponseEntity.ok().build();
        } catch (DuplicateRoleException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add role: {}", ShiroUtils.getCurrentUserName(), roleName);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    /**
     * 删除一个角色
     *
     * @param roleName 角色名
     * @return 200 成功 404 角色不存在
     */
    @DeleteMapping("{roleName}")
    @RequiresPermissions("system:role:delete")
    public ResponseEntity<Void> deleteRole(@PathVariable("roleName") String roleName) {
        try {
            roleService.removeRole(roleName);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete role: {}", ShiroUtils.getCurrentUserName(), roleName);
            }
            return ResponseEntity.ok().build();
        } catch (MissRoleException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete miss role: {}", ShiroUtils.getCurrentUserName(), roleName);
            }
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 修改一个角色名
     *
     * @param oldRoleName 旧角色名
     * @param newRoleName 新角色名
     * @return 200 成功 404 旧角色不存在 400 新角色已存在
     */
    @PutMapping("{oldRoleName}")
    @RequiresPermissions("system:role:update")
    public ResponseEntity<Void> updateRole(@PathVariable("oldRoleName") String oldRoleName, @RequestParam String newRoleName) {
        try {
            roleService.changeRoleName(oldRoleName, newRoleName);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} update role's name: {} to {}", ShiroUtils.getCurrentUserName(), oldRoleName, newRoleName);
            }
            return ResponseEntity.ok().build();
        } catch (MissRoleException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} update role's name: {} to {}, but miss role", ShiroUtils.getCurrentUserName(), oldRoleName,
                        newRoleName);
            }
            return ResponseEntity.notFound().build();
        } catch (DuplicateRoleException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} update role's name: {} to {}, but new role already exist", ShiroUtils.getCurrentUserName(),
                        oldRoleName, newRoleName);
            }
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取角色的详细信息
     *
     * @param roleName 角色名
     * @return 200 成功 404 角色不存在
     */
    @GetMapping("{roleName}")
    @RequiresPermissions("system:role:get")
    public ResponseEntity<Role> getRole(@PathVariable("roleName") String roleName) {
        Role role = roleService.getRoleByRoleName(roleName);
        if (role == null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} get miss role info: {}", ShiroUtils.getCurrentUserName(), roleName);
            }
            return ResponseEntity.notFound().build();
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} get role info: {}", ShiroUtils.getCurrentUserName(), roleName);
        }
        return ResponseEntity.ok().body(role);
    }

    /**
     * 列出所有的角色信息
     *
     * @param pageNum 页面
     * @return 200 分页对象
     */
    @GetMapping("list/{pageNum}")
    @RequiresPermissions("system:role:list")
    public ResponseEntity<PageInfo> listRoles(@PathVariable("pageNum") Integer pageNum) {
        PageInfo pageInfo = roleService.listRoles(pageNum);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} list roles in page: {}", ShiroUtils.getCurrentUserName(), pageNum);
        }
        return ResponseEntity.ok(pageInfo);
    }

    /**
     * 为角色添加权限
     *
     * @param roleName   角色名
     * @param permission 权限
     * @return 200 成功 404 角色不存在 400 权限不存在 409 权限已添加
     */
    @PostMapping("{roleName}/permissions")
    @RequiresPermissions("system:role:edit")
    public ResponseEntity<Void> addPermissionToRole(@PathVariable("roleName") String roleName, @RequestParam String permission) {
        try {
            permissionService.addPermissionToRole(roleName, permission);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add role's permission: {}:{}", ShiroUtils.getCurrentUserName(), roleName, permission);
            }
            return ResponseEntity.ok().build();
        } catch (MissRoleException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add role's permission: {}:{}, but miss role", ShiroUtils.getCurrentUserName(), roleName, permission);
            }
            return ResponseEntity.notFound().build();
        } catch (MissPermissionException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add role's permission: {}:{}, but miss permission", ShiroUtils.getCurrentUserName(), roleName, permission);
            }
            return ResponseEntity.badRequest().build();
        } catch (DuplicatePermissionException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add duplicate role's permission: {}:{}", ShiroUtils.getCurrentUserName(), roleName, permission);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    /**
     * 为角色删除权限
     *
     * @param roleName   角色名
     * @param permission 权限名
     * @return 200 成功 404 角色不存在 400 权限不存在
     */
    @DeleteMapping("{roleName}/permissions/{permission}")
    @RequiresPermissions("system:role:edit")
    public ResponseEntity<Void> deleteRolePermission(@PathVariable("roleName") String roleName,
                                                     @PathVariable("permission") String permission) {
        try {
            permissionService.removeRolePermission(roleName, permission);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete role's permission: {}:{}", ShiroUtils.getCurrentUserName(), roleName, permission);
            }
            return ResponseEntity.ok().build();
        } catch (MissRoleException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete role's permission: {}:{}, but miss role", ShiroUtils.getCurrentUserName(), roleName, permission);
            }
            return ResponseEntity.notFound().build();
        } catch (MissPermissionException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete role's permission: {}:{}, but miss permission", ShiroUtils.getCurrentUserName(), roleName, permission);
            }
            return ResponseEntity.badRequest().build();
        }

    }

}
