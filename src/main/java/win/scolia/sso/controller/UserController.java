package win.scolia.sso.controller;


import com.github.pagehelper.PageInfo;
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
import win.scolia.sso.bean.entity.UserSafely;
import win.scolia.sso.bean.vo.entry.UserEntryVO;
import win.scolia.sso.bean.vo.export.MessageExportVO;
import win.scolia.sso.bean.vo.export.UserExportVO;
import win.scolia.sso.exception.DuplicateUserException;
import win.scolia.sso.service.PermissionService;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.MessageUtils;
import win.scolia.sso.util.ShiroUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 主要负责用户管理的相关信息
 */
@Controller
@RequestMapping(value = "account/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 新增用户
     *
     * @param userEntryVO   用户信息
     * @param bindingResult 验证信息
     * @return 201 成功, 400 参数错误
     */
    @PostMapping
    @RequiresPermissions("system:user:add")
    public ResponseEntity<MessageExportVO> addUser(@Validated(UserEntryVO.Register.class) UserEntryVO userEntryVO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            MessageExportVO messageExportVO = MessageUtils.makeValidMessage(bindingResult);
            return ResponseEntity.badRequest().body(messageExportVO);
        }
        try {
            userService.createUser(userEntryVO);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("{} add user: {}", ShiroUtils.getCurrentUserName(), userEntryVO.getUserName());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicateUserException e) {
            MessageExportVO vo = new MessageExportVO();
            MessageUtils.putMessage(vo, "error", "该用户已被占用");
            return ResponseEntity.badRequest().body(vo);
        }
    }

    /**
     * 删除某个用户
     *
     * @param userName 用户名
     * @return 200 成功, 403 权限不足, 500 服务器错误
     */
    @DeleteMapping("{userName}")
    @RequiresPermissions("system:user:delete")
    public ResponseEntity<Void> deleteUser(@PathVariable("userName") String userName) {
        userService.removeUserByUserName(userName);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} delete user: {}", ShiroUtils.getCurrentUserName(), userName);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 修改用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 200 成功
     */
    @PutMapping("{userName}/password")
    @RequiresPermissions("system:user:edit")
    public ResponseEntity<Void> changePassword(@PathVariable("userName") String userName, @RequestParam String password) {
        userService.changePasswordDirectly(userName, password);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} change user password: {}", ShiroUtils.getCurrentUserName(), userName);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 获取某个用户的信息
     *
     * @param userName 用户名
     * @return 200 成功, 404 没有此用户
     */
    @GetMapping("{userName}")
    @RequiresPermissions("system:user:get")
    public ResponseEntity<UserExportVO> getUser(@PathVariable("userName") String userName) {
        UserSafely userSafely = userService.getUserSafelyByUserName(userName);
        if (userSafely == null) {
            return ResponseEntity.notFound().build();
        }
        Set<String> roles = roleService.getUserRolesByUserName(userSafely.getUserName());
        Set<String> permissions = new HashSet<>();
        for (String roleName : roles) {
            permissions.addAll(permissionService.getPermissionsByRoleName(roleName));
        }
        UserExportVO vo = new UserExportVO(userSafely, roles, permissions);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} get user info: {}", ShiroUtils.getCurrentUserName(), userName);
        }
        return ResponseEntity.ok(vo);
    }

    /**
     * 列出所有的用户
     *
     * @param pageNum 页码
     * @return 200 成功
     */
    @GetMapping("list")
    @RequiresPermissions("system:user:list")
    public ResponseEntity<PageInfo> listUsers(@RequestParam Integer pageNum) {
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
     * @param roleName 角色名
     * @return 200 成功
     */
    @PostMapping("{userName}/roles")
    @RequiresPermissions("system:user:edit")
    public ResponseEntity<Void> addRoleToUser(@PathVariable("userName") String userName,
                                              @RequestParam String roleName) {
        roleService.addRoleToUser(userName, roleName);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} add user's role: {}:{}", ShiroUtils.getCurrentUserName(), userName, roleName);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 为用户删除一个角色
     *
     * @param userName 用户名
     * @param roleName 角色名
     * @return 200 成功
     */
    @DeleteMapping("{userName}/roles/{roleName}")
    @RequiresPermissions("system:user:edit")
    public ResponseEntity<Void> deleteUserRole(@PathVariable("userName") String userName,
                                               @PathVariable("roleName") String roleName) {
        roleService.romeUserRole(userName, roleName);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} delete user's role: {}:{}", ShiroUtils.getCurrentUserName(), userName, roleName);
        }
        return ResponseEntity.ok().build();
    }
}
