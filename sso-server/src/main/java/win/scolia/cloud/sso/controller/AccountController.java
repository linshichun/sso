package win.scolia.cloud.sso.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import win.scolia.cloud.sso.annotation.CheckEntry;
import win.scolia.cloud.sso.bean.entity.User;
import win.scolia.cloud.sso.bean.entity.UserSafely;
import win.scolia.cloud.sso.bean.vo.entry.ChangePasswordEntry;
import win.scolia.cloud.sso.bean.vo.entry.UserEntry;
import win.scolia.cloud.sso.bean.vo.export.UserExport;
import win.scolia.cloud.sso.exception.DuplicateUserException;
import win.scolia.cloud.sso.service.PermissionService;
import win.scolia.cloud.sso.service.RoleService;
import win.scolia.cloud.sso.service.UserService;
import win.scolia.cloud.sso.util.ResponseUtils;
import win.scolia.cloud.sso.util.ShiroUtils;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * 主要负责登录注册等功能
 */
@Controller
@RequestMapping("account")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 用户注册
     *
     * @param entry         用户的信息
     * @param bindingResult 数据校验的结果
     * @return 201 成功 409 用户名已被占用
     */
    @PostMapping("register")
    @CheckEntry
    public ResponseEntity register(@RequestBody @Validated(UserEntry.Register.class) UserEntry entry, BindingResult bindingResult) {
        try {
            userService.createUser(entry);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Register user: {}", entry.getUserName());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicateUserException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Register duplicate user: {}", entry.getUserName());
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * 注册时, 检查用户名是否可用
     *
     * @param entry 用户名
     * @return 200 可用, 409 不可用
     */
    @GetMapping("register/userName")
    @CheckEntry
    public ResponseEntity checkRepeatUserName(@RequestBody @Validated(UserEntry.UserName.class) UserEntry entry, BindingResult bindingResult) {
        String userName = entry.getUserName();
        if (userService.checkUserNameUsable(userName)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Check Repeat user: {}, can use", userName);
            }
            return ResponseEntity.ok().build();
        } else {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Check Repeat user: {}, can not use", userName);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * 使用用户名和密码登录
     *
     * @param entry         用户信息
     * @param bindingResult 数据校验的结果
     * @return 200 登录成功, 401登录失败
     */
    @PostMapping("login")
    @CheckEntry
    public ResponseEntity login(@RequestBody @Validated(UserEntry.Login.class) UserEntry entry, BindingResult bindingResult) {
        try {
            Subject subject = SecurityUtils.getSubject();
            AuthenticationToken token = new UsernamePasswordToken(entry.getUserName(), entry.getPassword(), entry.getRememberMe());
            subject.login(token);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Login user: {}", entry.getUserName());
            }
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Login user fail: {}", entry.getUserName());
            }
            return ResponseUtils.makeAuthenticationResponseEntity("UserName or password error");
        }
    }

    /**
     * 登出
     *
     * @return 200 成功 401 未登录
     */
    @GetMapping("logout")
    @RequiresUser
    public ResponseEntity logout() {
        Subject subject = SecurityUtils.getSubject();
        if (LOGGER.isInfoEnabled()) {
            User user = (User) subject.getPrincipal();
            LOGGER.info("User logout: {}", user.getUserName());
        }
        subject.logout();
        return ResponseEntity.ok().build();
    }

    /**
     * 获取当前用户信息
     *
     * @return 200 成功, 401 未登录
     */
    @GetMapping("current")
    @RequiresUser
    public ResponseEntity current() {
        User user = ShiroUtils.getCurrentUser();
        UserSafely userSafely = new UserSafely();
        BeanUtils.copyProperties(user, userSafely);
        Set<String> roles = roleService.getUserRolesByUserName(userSafely.getUserName());
        Set<String> permissions = new HashSet<>();
        for (String roleName : roles) {
            permissions.addAll(permissionService.getPermissionsByRoleName(roleName));
        }
        UserExport vo = new UserExport(userSafely, roles, permissions);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("User get current info: {}", user.getUserName());
        }
        return ResponseEntity.ok(vo);
    }

    /**
     * 修改密码
     *
     * @param entry         用户信息
     * @param bindingResult 数据校验的结果
     * @return 200 表示成功, 401 未登录/密码错误
     */
    @PutMapping("current/password")
    @RequiresUser
    @CheckEntry
    public ResponseEntity changePassword(@RequestBody @Valid ChangePasswordEntry entry, BindingResult bindingResult) {
        User user = ShiroUtils.getCurrentUser();
        boolean success = userService.changePasswordByOldPassword(user.getUserName(), entry.getCurrent(), entry.getTarget());
        if (success) {
            this.logout();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Change user password: {}", user.getUserName());
            }
            return ResponseEntity.ok().build();
        } else {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Change user password fail: {}", user.getUserName());
            }
            return ResponseUtils.makeAuthenticationResponseEntity("password error");
        }
    }
}
