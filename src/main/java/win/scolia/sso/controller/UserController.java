package win.scolia.sso.controller;


import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import win.scolia.sso.bean.entity.UserSafely;
import win.scolia.sso.service.UserService;

/**
 * 主要负责用户管理的相关信息
 */
@Controller
@RequestMapping(value = "account/users")
public class UserController {

    @Autowired
    private UserService userService;

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

    @GetMapping("{userName}")
    @RequiresPermissions("system:user:get")
    public ResponseEntity<UserSafely> getUser(@PathVariable("userName") String userName) {
        UserSafely userSafely = userService.getUserSafelyByUserName(userName);
        if (userSafely == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userSafely);
    }

    /**
     * 列出所有的用户
     * @param pageNum 页码
     * @return 200 成功
     */
    @GetMapping("/list")
    @RequiresPermissions("system:user:list")
    public ResponseEntity<PageInfo> listUsers(@RequestParam(value = "pageNum") Integer pageNum) {
        return ResponseEntity.ok(userService.listUsersSafely(pageNum));
    }
}
