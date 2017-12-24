package win.scolia.sso.controller;


import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import win.scolia.sso.service.RoleService;

@Controller
@RequestMapping(value = "account/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    @RequiresPermissions("system:role:add")
    public ResponseEntity<Void> addRole(@RequestParam String roleName) {
        roleService.createRole(roleName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("list")
    @RequiresPermissions("system:role:list")
    public ResponseEntity<PageInfo> listRoles(@RequestParam Integer pageNum) {
        return ResponseEntity.ok(roleService.listRoles(pageNum));
    }

}
