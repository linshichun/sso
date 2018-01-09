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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import win.scolia.cloud.sso.annotation.CheckEntry;
import win.scolia.cloud.sso.bean.entity.UserSafely;
import win.scolia.cloud.sso.bean.vo.entry.RoleEntry;
import win.scolia.cloud.sso.bean.vo.entry.UserEntry;
import win.scolia.cloud.sso.bean.vo.export.UserExport;
import win.scolia.cloud.sso.exception.DuplicateRoleException;
import win.scolia.cloud.sso.exception.DuplicateUserException;
import win.scolia.cloud.sso.exception.MissRoleException;
import win.scolia.cloud.sso.exception.MissUserException;
import win.scolia.cloud.sso.service.PermissionService;
import win.scolia.cloud.sso.service.RoleService;
import win.scolia.cloud.sso.service.UserService;
import win.scolia.cloud.sso.util.ShiroUtils;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * 主要负责用户管理的相关信息
 */
@Controller
@RequestMapping(value = "account/users")
@RequiresAuthentication // 要求不能是rememberMe登录的
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final int ROLE_NOT_FOUND_STATUS = 460;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 新增用户
     *
     * @param entry         用户信息
     * @param bindingResult 验证信息
     * @return 201 成功, 409 该用户已存在
     */
    @PostMapping
    @RequiresPermissions("system:user:add")
    @CheckEntry
    public ResponseEntity addUser(@RequestBody @Validated(UserEntry.Register.class) UserEntry entry, BindingResult bindingResult) {
        try {
            userService.createUser(entry);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add user: {}", ShiroUtils.getCurrentUserName(), entry.getUserName());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicateUserException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add duplicate user: {}", ShiroUtils.getCurrentUserName(), entry.getUserName());
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * 删除某个用户
     *
     * @param userName 用户名
     * @return 200 成功, 404 要删除的用户不存在
     */
    @DeleteMapping("{userName}")
    @RequiresPermissions("system:user:delete")
    public ResponseEntity<Void> deleteUser(@PathVariable("userName") String userName) {
        try {
            userService.removeUserByUserName(userName);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete user: {}", ShiroUtils.getCurrentUserName(), userName);
            }
            return ResponseEntity.ok().build();
        } catch (MissUserException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete miss user: {}", ShiroUtils.getCurrentUserName(), userName);
            }
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 修改用户密码
     *
     * @param userName 用户名
     * @param entry    密码
     * @return 200 成功, 404 用户不存在
     */
    @PutMapping("{userName}/password")
    @RequiresPermissions("system:user:update")
    @CheckEntry
    public ResponseEntity<Object> changePassword(@PathVariable("userName") String userName,
                                                 @RequestBody @Validated(UserEntry.UpdatePassword.class) UserEntry entry, BindingResult bindingResult) {
        try {
            userService.changePasswordDirectly(userName, entry.getPassword());
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} change user password: {}", ShiroUtils.getCurrentUserName(), userName);
            }
            return ResponseEntity.ok().build();
        } catch (MissUserException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} change miss user password: {}", ShiroUtils.getCurrentUserName(), userName);
            }
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取某个用户的信息
     *
     * @param userName 用户名
     * @return 200 成功, 404 用户不存在
     */
    @GetMapping("{userName}")
    @RequiresPermissions("system:user:get")
    public ResponseEntity<UserExport> getUser(@PathVariable("userName") String userName) {
        UserSafely userSafely = userService.getUserSafelyByUserName(userName);
        if (userSafely == null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} get miss user info: {}", ShiroUtils.getCurrentUserName(), userName);
            }
            return ResponseEntity.notFound().build();
        }
        Set<String> roles = roleService.getUserRolesByUserName(userSafely.getUserName());
        Set<String> permissions = new HashSet<>();
        for (String roleName : roles) {
            permissions.addAll(permissionService.getPermissionsByRoleName(roleName));
        }
        UserExport vo = new UserExport(userSafely, roles, permissions);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} get user info: {}", ShiroUtils.getCurrentUserName(), userName);
        }
        return ResponseEntity.ok(vo);
    }

    /**
     * 列出所有的用户
     *
     * @param pageNum 页码, 默认第一页
     * @return 200 成功
     */
    @GetMapping
    @RequiresPermissions("system:user:list")
    public ResponseEntity<PageInfo> listUsers(@RequestParam(defaultValue = "1") Integer pageNum) {
        PageInfo pageInfo = userService.listUsersSafely(pageNum);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} list users in page: {}", ShiroUtils.getCurrentUserName(), pageNum);
        }
        return ResponseEntity.ok(pageInfo);
    }

    /**
     * 为用户添加一个角色
     *
     * @param userName 用户名
     * @param entry    角色名
     * @return 200 成功 404 用户不存在 409 用户角色重复 460 角色不存在
     */
    @PostMapping("{userName}/roles")
    @RequiresPermissions("system:user:edit")
    @CheckEntry
    public ResponseEntity<Object> addRoleToUser(@PathVariable("userName") String userName, @RequestBody @Valid RoleEntry entry, BindingResult bindingResult) {
        String roleName = entry.getRoleName();
        try {
            roleService.addRoleToUser(userName, roleName);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add user's role: {}:{}", ShiroUtils.getCurrentUserName(), userName, roleName);
            }
            return ResponseEntity.ok().build();
        } catch (MissUserException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add user's role: {}:{}, but miss user", ShiroUtils.getCurrentUserName(), userName, roleName);
            }
            return ResponseEntity.notFound().build();
        } catch (MissRoleException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add user's role: {}:{}, but miss role", ShiroUtils.getCurrentUserName(), userName, roleName);
            }
            return ResponseEntity.status(ROLE_NOT_FOUND_STATUS).build();
        } catch (DuplicateRoleException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add duplicate user's role: {}:{}", ShiroUtils.getCurrentUserName(), userName, roleName);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * 为用户删除一个角色
     *
     * @param userName 用户名
     * @param roleName 角色名
     * @return 200 成功 404 用户不存在 460 角色不存在
     */
    @DeleteMapping("{userName}/roles/{roleName}")
    @RequiresPermissions("system:user:edit")
    public ResponseEntity<Void> deleteUserRole(@PathVariable("userName") String userName, @PathVariable("roleName") String roleName) {
        try {
            roleService.romeUserRole(userName, roleName);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete user's role: {}:{}", ShiroUtils.getCurrentUserName(), userName, roleName);
            }
            return ResponseEntity.ok().build();
        } catch (MissUserException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete user's role: {}:{}, but miss user", ShiroUtils.getCurrentUserName(), userName, roleName);
            }
            return ResponseEntity.notFound().build();
        } catch (MissRoleException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} delete user's role: {}:{}, but miss role", ShiroUtils.getCurrentUserName(), userName, roleName);
            }
            return ResponseEntity.status(ROLE_NOT_FOUND_STATUS).build();
        }
    }
}
