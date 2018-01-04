package win.scolia.sso.controller;

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
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.entity.UserSafely;
import win.scolia.sso.bean.vo.entry.UserEntry;
import win.scolia.sso.bean.vo.export.Base64ImageCaptchaExport;
import win.scolia.sso.bean.vo.export.CaptchaExport;
import win.scolia.sso.bean.vo.export.MessageExport;
import win.scolia.sso.bean.vo.export.UserExport;
import win.scolia.sso.exception.DuplicateUserException;
import win.scolia.sso.service.PermissionService;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.CaptchaUtils;
import win.scolia.sso.util.MessageUtils;
import win.scolia.sso.util.ShiroUtils;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

/**
 * 主要负责登录注册等功能
 */
@Controller
@RequestMapping(value = "account")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private CaptchaUtils captchaUtils;

    /**
     * 用户注册
     *
     * @param userEntry   用户的信息
     * @param bindingResult 数据校验的结果
     * @return 201 表示注册成功, 400 参数错误, 409 用户名已被占用
     */
    @PostMapping("register")
    public ResponseEntity<MessageExport> register(@Validated(UserEntry.Register.class) UserEntry userEntry,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            MessageExport messageExport = MessageUtils.makeValidMessage(bindingResult);
            return ResponseEntity.badRequest().body(messageExport);
        }
        try {
            userService.createUser(userEntry);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Register user: {}", userEntry.getUserName());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicateUserException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Register duplicate user: {}", userEntry.getUserName());
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * 注册时, 检查用户名是否可用
     *
     * @param userName 用户名
     * @return 200 可用, 409 不可用, 400 参数错误
     */
    @PostMapping("register/check")
    public ResponseEntity<Void> checkRepeatUserName(@RequestParam String userName) {
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
     * 获取登录的验证码
     * @param session session
     * @return base64编码的验证码图片
     */
    @GetMapping("login")
    public ResponseEntity<CaptchaExport> login(HttpSession session) throws Exception {
        Base64ImageCaptchaExport export = new Base64ImageCaptchaExport(captchaUtils.setBase64Image(session));
        return ResponseEntity.ok(export);
    }

    /**
     * 使用用户名和密码登录
     *
     * @param userEntry   用户信息
     * @param bindingResult 数据校验的结果
     * @return 200 登录成功, 400 参数错误/登录失败
     */
    @PostMapping("login")
    public ResponseEntity<MessageExport> login(@Validated(UserEntry.LoginByPassword.class) UserEntry userEntry,
                                               @RequestParam boolean rememberMe, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            MessageExport messageExport = MessageUtils.makeValidMessage(bindingResult);
            return ResponseEntity.badRequest().body(messageExport);
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            AuthenticationToken token = new UsernamePasswordToken(userEntry.getUserName(), userEntry.getPassword(), rememberMe);
            subject.login(token);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Login user: {}", userEntry.getUserName());
            }
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Login user fail: {}", userEntry.getUserName());
            }
            MessageExport vo = new MessageExport();
            MessageUtils.putMessage(vo, "authentication", "用户名或密码错误");
            return ResponseEntity.badRequest().body(vo);
        }
    }

    /**
     * 登出
     *
     * @return 200 成功 401 未登录
     */
    @GetMapping("logout")
    @RequiresUser
    public ResponseEntity<Void> logout() {
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
    public ResponseEntity<UserExport> current() {
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
     * @param userEntry   用户信息
     * @param bindingResult 数据校验的结果
     * @return 200 表示成功, 400 参数错误/旧密码错误 401 未登录
     */
    @PutMapping("current/password")
    @RequiresUser
    public ResponseEntity<MessageExport> changePassword(@Validated(UserEntry.ChangePassword.class) UserEntry userEntry,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            MessageExport messageExport = MessageUtils.makeValidMessage(bindingResult);
            return ResponseEntity.badRequest().body(messageExport);
        }
        boolean success = userService.changePasswordByOldPassword(userEntry.getUserName(), userEntry.getOldPassword(),
                userEntry.getNewPassword());
        if (success) {
            this.logout();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Change user password: {}", userEntry.getUserName());
            }
            return ResponseEntity.ok().build();
        } else {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Change user password fail: {}", userEntry.getUserName());
            }
            MessageExport vo = new MessageExport();
            MessageUtils.putMessage(vo, "authentication", "密码错误");
            return ResponseEntity.badRequest().body(vo);
        }
    }
}
