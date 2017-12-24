package win.scolia.sso.controller;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import win.scolia.sso.service.PermissionService;

@Controller
@RequestMapping(value = "account/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("list")
    @RequiresPermissions("system:permission:list")
    public ResponseEntity<PageInfo> listPermissions(@RequestParam Integer pageNum) {
        return ResponseEntity.ok(permissionService.listAllPermission(pageNum));
    }
}
