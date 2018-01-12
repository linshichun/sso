//package win.scolia.cloud.sso.admin.controller;
//
//
//import com.github.pagehelper.PageInfo;
//import org.apache.shiro.authz.annotation.RequiresAuthentication;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import win.scolia.cloud.sso.service.PermissionService;
//import win.scolia.cloud.sso.annotation.CheckEntry;
//import win.scolia.cloud.sso.bean.output.RoleOutput;
//import win.scolia.cloud.sso.bean.entity.Role;
//import win.scolia.cloud.sso.exception.DuplicatePermissionException;
//import win.scolia.cloud.sso.exception.DuplicateRoleException;
//import win.scolia.cloud.sso.exception.MissPermissionException;
//import win.scolia.cloud.sso.exception.MissRoleException;
//import win.scolia.cloud.sso.service.RoleCommonService;
//import win.scolia.cloud.sso.util.ShiroUtils;
//
//import javax.validation.Valid;
//import java.util.Set;
//
//@Controller
//@RequestMapping(value = "account/roles")
//@RequiresAuthentication
//public class RoleController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
//
//    private static final int PERMISSION_NOT_FOUND = 461;
//
//    @Autowired
//    private RoleCommonService roleCommonService;
//
//    @Autowired
//    private PermissionService permissionService;
//
//    /**
//     * 新增一个角色
//     *
//     * @param entry 角色名
//     * @return 201 成功 409 该角色已存在
//     */
//    @PostMapping
//    @RequiresPermissions("system:role:add")
//    @CheckEntry
//    public ResponseEntity<Object> addRole(@RequestBody @Valid RoleInputDTO entry, BindingResult bindingResult) {
//        String roleName = entry.getRoleName();
//        try {
//            roleCommonService.createRole(roleName);
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} add role: {}", ShiroUtils.getCurrentUserName(), roleName);
//            }
//            return ResponseEntity.status(HttpStatus.CREATED).build();
//        } catch (DuplicateRoleException e) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} add duplicate role: {}", ShiroUtils.getCurrentUserName(), roleName);
//            }
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//
//    }
//
//    /**
//     * 删除一个角色
//     *
//     * @param roleName 角色名
//     * @return 200 成功 404 角色不存在
//     */
//    @DeleteMapping("{roleName}")
//    @RequiresPermissions("system:role:delete")
//    public ResponseEntity<Void> deleteRole(@PathVariable("roleName") String roleName) {
//        try {
//            roleCommonService.removeRole(roleName);
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} delete role: {}", ShiroUtils.getCurrentUserName(), roleName);
//            }
//            return ResponseEntity.ok().build();
//        } catch (MissRoleException e) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} delete miss role: {}", ShiroUtils.getCurrentUserName(), roleName);
//            }
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    /**
//     * 修改一个角色名
//     *
//     * @param current 旧角色名
//     * @param entry   新角色名
//     * @return 200 成功 404 旧角色不存在 409 新角色已存在
//     */
//    @PutMapping("{roleName}")
//    @RequiresPermissions("system:role:update")
//    @CheckEntry
//    public ResponseEntity<Object> updateRole(@PathVariable("roleName") String current,
//                                             @RequestBody @Valid RoleInputDTO entry, BindingResult bindingResult) {
//        String roleName = entry.getRoleName();
//        try {
//            roleCommonService.changeRoleName(current, roleName);
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} update role's name: {} to {}", ShiroUtils.getCurrentUserName(), current, roleName);
//            }
//            return ResponseEntity.ok().build();
//        } catch (MissRoleException e) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} update role's name: {} to {}, but miss role", ShiroUtils.getCurrentUserName(), current,
//                        roleName);
//            }
//            return ResponseEntity.notFound().build();
//        } catch (DuplicateRoleException e) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} update role's name: {} to {}, but new role already exist", ShiroUtils.getCurrentUserName(),
//                        current, roleName);
//            }
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//    }
//
//    /**
//     * 获取角色的详细信息
//     *
//     * @param roleName 角色名
//     * @return 200 成功 404 角色不存在
//     */
//    @GetMapping("{roleName}")
//    @RequiresPermissions("system:role:get")
//    public ResponseEntity<RoleOutput> getRole(@PathVariable("roleName") String roleName) {
//        Role role = roleCommonService.getRoleByName(roleName);
//        if (role == null) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} get miss role info: {}", ShiroUtils.getCurrentUserName(), roleName);
//            }
//            return ResponseEntity.notFound().build();
//        }
//        if (LOGGER.isInfoEnabled()) {
//            LOGGER.info("{} get role info: {}", ShiroUtils.getCurrentUserName(), roleName);
//        }
//        Set<String> permissions = permissionService.getPermissionsByRoleName(role.getRoleName());
//        RoleOutput roleOutput = new RoleOutput(role, permissions);
//        return ResponseEntity.ok().body(roleOutput);
//    }
//
//    /**
//     * 列出所有的角色信息
//     *
//     * @param pageNum 页码, 默认为1
//     * @return 200 分页对象
//     */
//    @GetMapping
//    @RequiresPermissions("system:role:list")
//    public ResponseEntity<PageInfo> listRoles(@RequestParam(defaultValue = "1") Integer pageNum) {
//        PageInfo pageInfo = roleCommonService.listRoles(pageNum);
//        if (LOGGER.isInfoEnabled()) {
//            LOGGER.info("{} list roles in page: {}", ShiroUtils.getCurrentUserName(), pageNum);
//        }
//        return ResponseEntity.ok(pageInfo);
//    }
//
//    /**
//     * 为角色添加权限
//     *
//     * @param roleName 角色名
//     * @param entry    权限
//     * @return 200 成功 404 角色不存在 409 权限已添加 461 权限不存在
//     */
//    @PostMapping("{roleName}/permissions")
//    @RequiresPermissions("system:role:edit")
//    @CheckEntry
//    public ResponseEntity<Object> addPermissionToRole(@PathVariable("roleName") String roleName,
//                                                      @RequestBody @Valid PermissionInputDTO entry, BindingResult bindingResult) {
//        String permission = entry.getPermission();
//        try {
//            permissionService.addPermissionToRole(roleName, permission);
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} add role's permission: {}:{}", ShiroUtils.getCurrentUserName(), roleName, permission);
//            }
//            return ResponseEntity.ok().build();
//        } catch (MissRoleException e) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} add role's permission: {}:{}, but miss role", ShiroUtils.getCurrentUserName(), roleName, permission);
//            }
//            return ResponseEntity.notFound().build();
//        } catch (MissPermissionException e) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} add role's permission: {}:{}, but miss permission", ShiroUtils.getCurrentUserName(), roleName, permission);
//            }
//            return ResponseEntity.status(PERMISSION_NOT_FOUND).build();
//        } catch (DuplicatePermissionException e) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} add duplicate role's permission: {}:{}", ShiroUtils.getCurrentUserName(), roleName, permission);
//            }
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//    }
//
//    /**
//     * 为角色删除权限
//     *
//     * @param roleName   角色名
//     * @param permission 权限名
//     * @return 200 成功 404 角色不存在 461 权限不存在
//     */
//    @DeleteMapping("{roleName}/permissions/{permission}")
//    @RequiresPermissions("system:role:edit")
//    public ResponseEntity<Void> deleteRolePermission(@PathVariable("roleName") String roleName,
//                                                     @PathVariable("permission") String permission) {
//        try {
//            permissionService.removeRolePermission(roleName, permission);
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} delete role's permission: {}:{}", ShiroUtils.getCurrentUserName(), roleName, permission);
//            }
//            return ResponseEntity.ok().build();
//        } catch (MissRoleException e) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} delete role's permission: {}:{}, but miss role", ShiroUtils.getCurrentUserName(), roleName, permission);
//            }
//            return ResponseEntity.notFound().build();
//        } catch (MissPermissionException e) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("{} delete role's permission: {}:{}, but miss permission", ShiroUtils.getCurrentUserName(), roleName, permission);
//            }
//            return ResponseEntity.status(PERMISSION_NOT_FOUND).build();
//        }
//    }
//}
