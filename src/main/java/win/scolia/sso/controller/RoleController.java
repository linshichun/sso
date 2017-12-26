package win.scolia.sso.controller;


import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import win.scolia.sso.bean.entity.Role;
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
     * @return 200 成功
     */
    @PostMapping
    @RequiresPermissions("system:role:add")
    public ResponseEntity<Void> addRole(@RequestParam String roleName) {
        roleService.createRole(roleName);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} add role: {}", ShiroUtils.getCurrentUserName(), roleName);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 删除一个角色
     *
     * @param roleName 角色名
     * @return 200 成功
     */
    @DeleteMapping("{roleName}")
    @RequiresPermissions("system:role:delete")
    public ResponseEntity<Void> deleteRole(@PathVariable("roleName") String roleName) {
        roleService.removeRole(roleName);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} delete role: {}", ShiroUtils.getCurrentUserName(), roleName);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 修改一个角色名
     *
     * @param oldRoleName 旧角色名
     * @param newRoleName 新角色名
     * @return 200 成功
     */
    @PutMapping("{oldRoleName}")
    @RequiresPermissions("system:role:update")
    public ResponseEntity<Void> updateRole(@PathVariable("oldRoleName") String oldRoleName, @RequestParam String newRoleName) {
        roleService.changeRoleName(oldRoleName, newRoleName);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} update role's name: {} to {}", ShiroUtils.getCurrentUserName(), oldRoleName, newRoleName);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 获取角色的详细信息
     *
     * @param roleName 角色名
     * @return 角色信息
     */
    @GetMapping("{roleName}")
    @RequiresPermissions("system:role:get")
    public ResponseEntity<Role> getRole(@PathVariable("roleName") String roleName) {
        Role role = roleService.getRoleByRoleName(roleName);
        if (role == null) {
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
    @GetMapping("list")
    @RequiresPermissions("system:role:list")
    public ResponseEntity<PageInfo> listRoles(@RequestParam Integer pageNum) {
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
     * @return 200 成功
     */
    @PostMapping("{roleName}/permissions")
    @RequiresPermissions("system:role:edit")
    public ResponseEntity<Void> addPermissionToRole(@PathVariable("roleName") String roleName, @RequestParam String permission) {
        permissionService.addPermissionToRole(roleName, permission);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} add role's permission: {}:{}", ShiroUtils.getCurrentUserName(), roleName, permission);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 为角色删除权限
     *
     * @param roleName   角色名
     * @param permission 权限名
     * @return 200 成功
     */
    @DeleteMapping("{roleName}/permissions/{permission}")
    @RequiresPermissions("system:role:edit")
    public ResponseEntity<Void> deleteRolePermission(@PathVariable("roleName") String roleName,
                                                     @PathVariable("permission") String permission) {
        permissionService.removeRolePermission(roleName, permission);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} delete role's permission: {}:{}", ShiroUtils.getCurrentUserName(), roleName, permission);
        }
        return ResponseEntity.ok().build();
    }

}
