package win.scolia.sso.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.vo.MessageVO;
import win.scolia.sso.bean.vo.UserVO;
import win.scolia.sso.exception.DuplicateUserException;
import win.scolia.sso.service.PermissionService;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.MessageUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    /**
     * 用户注册
     *
     * @param userVO        用户的信息
     * @param bindingResult 数据校验的结果
     * @return 201 表示注册成功, 400 参数错误, 500 服务器错误
     */
    @PostMapping("register")
    public ResponseEntity<MessageVO> register(@Validated(UserVO.Register.class) UserVO userVO,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            MessageVO messageVO = MessageUtils.makeValidMessage(bindingResult);
            return ResponseEntity.badRequest().body(messageVO);
        }
        try {
            userService.createUser(userVO);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Register user: {}", userVO);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicateUserException e) {
            MessageVO messageVO = new MessageVO();
            MessageUtils.putMessage(messageVO, "error", "该用户已被占用");
            return ResponseEntity.badRequest().body(messageVO);
        }
    }

    /**
     * 使用用户名和密码登录
     *
     * @param userVO        用户信息
     * @param bindingResult 数据校验的结果
     * @return 200 登录成功, 400 参数错误/登录失败, 500 服务器错误
     */
    @PostMapping("login")
    public ResponseEntity<MessageVO> login(@Validated(UserVO.LoginByPassword.class) UserVO userVO,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            MessageVO messageVO = MessageUtils.makeValidMessage(bindingResult);
            return ResponseEntity.badRequest().body(messageVO);
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            AuthenticationToken token = new UsernamePasswordToken(userVO.getUserName(), userVO.getPassword());
            subject.login(token);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Login user: {}", userVO.getUserName());
            }
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            MessageVO messageVO = new MessageVO();
            MessageUtils.putMessage(messageVO, "error", "用户名或密码错误");
            return ResponseEntity.badRequest().body(messageVO);
        }
    }

    /**
     * 登出
     * @return 200 成功, 500 服务器错误
     */
    @GetMapping("logout")
    @RequiresAuthentication
    public ResponseEntity<Void> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResponseEntity.ok().build();
    }

    /**
     * 获取当前用户信息
     * @return 200 成功, 500 服务器错误
     */
    @GetMapping("current")
    @RequiresAuthentication
    public ResponseEntity<Map<String, String>> current() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Map<String, String> msg = new HashMap<>();
        msg.put("userName", user.getUserName());
        return ResponseEntity.ok(msg);
    }

    /**
     * 获取当前用户角色信息
     * @return 200 成功, 500 服务器错误
     */
    @GetMapping("current/roles")
    @RequiresAuthentication
    public ResponseEntity<Map<String, Set<String>>> currentRoles() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Set<String> roles = roleService.getUserRolesByUserName(user.getUserName());
        Map<String, Set<String>> msg = new HashMap<>();
        msg.put("roles", roles);
        return ResponseEntity.ok(msg);
    }

    /**
     * 获取当前用户权限信息
     * @return 200 成功, 500 服务器错误
     */
    @GetMapping("current/permissions")
    @RequiresAuthentication
    public ResponseEntity<Map<String, Set<String>>> currentPermissions() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Set<String> roles = roleService.getUserRolesByUserName(user.getUserName());
        Set<String> permissions = new HashSet<>();
        for (String role: roles) {
            permissions.addAll(permissionService.getPermissionsByRoleName(role));
        }
        Map<String, Set<String>> msg = new HashMap<>();
        msg.put("permissions", permissions);
        return ResponseEntity.ok(msg);
    }

    /**
     * 修改密码
     * @param userVO 用户信息
     * @param bindingResult 数据校验的结果
     * @return 200 表示成功, 400 参数错误/旧密码错误 500 服务器错误
     */
    @PutMapping("current/password")
    @RequiresAuthentication
    public ResponseEntity<MessageVO> changePassword(@Validated(UserVO.ChangePassword.class) UserVO userVO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            MessageVO messageVO = MessageUtils.makeValidMessage(bindingResult);
            return ResponseEntity.badRequest().body(messageVO);
        }
        boolean success = userService.changePasswordByOldPassword(userVO.getUserName(), userVO.getOldPassword(),
                userVO.getNewPassword());
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            MessageVO messageVO = new MessageVO();
            MessageUtils.putMessage(messageVO, "error", "用户名或密码错误");
            return ResponseEntity.badRequest().body(messageVO);
        }
    }

    /**
     * 删除某个用户
     * @param userName 用户名
     * @return 200 成功, 403 权限不足, 500 服务器错误
     */
    @DeleteMapping("{userName}")
    @RequiresPermissions("system:user:delete")
    public ResponseEntity<Void> deleteUser(@PathVariable("userName") String userName) {
        userService.removeUserByUserName(userName);
        return ResponseEntity.ok().build();
    }

}
