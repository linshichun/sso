package win.scolia.sso.controller;


import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import win.scolia.sso.bean.entity.UserSafely;
import win.scolia.sso.bean.vo.export.UserExportVO;
import win.scolia.sso.service.PermissionService;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.service.UserService;

import java.util.HashSet;
import java.util.Set;

/**
 * 主要负责用户管理的相关信息
 */
@Controller
@RequestMapping(value = "account/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

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

    /**
     * 获取某个用户的信息
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
        return ResponseEntity.ok(vo);
    }

    /**
     * 为用户添加一个角色
     * @param userName 用户名
     * @param roleName 角色名
     * @return 200 成功
     */
    @PostMapping("{userName}/roles")
    @RequiresPermissions("system:user:edit")
    public ResponseEntity<Void> addRoleToUser(@PathVariable("userName") String userName,
                                              @RequestParam String roleName) {
        roleService.addRoleToUser(userName, roleName);
        return ResponseEntity.ok().build();
    }

    /**
     * 为用户删除一个角色
     * @param userName 用户名
     * @param roleName 角色名
     * @return 200 成功
     */
    @DeleteMapping("{userName}/roles/{roleName}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable("userName") String userName,
                                               @PathVariable("roleName") String roleName) {
        roleService.romeUserRole(userName, roleName);
        return ResponseEntity.ok().build();
    }

    /**
     * 列出所有的用户
     * @param pageNum 页码
     * @return 200 成功
     */
    @GetMapping("list")
    @RequiresPermissions("system:user:list")
    public ResponseEntity<PageInfo> listUsers(@RequestParam Integer pageNum) {
        return ResponseEntity.ok(userService.listUsersSafely(pageNum));
    }
}
