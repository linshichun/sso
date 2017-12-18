package win.scolia.sso.service;

import java.util.Set;

public interface RoleService {

    /**
     * 根据用户名获取用户的角色
     * @param username 用户名
     * @return 返回角色列表
     */
    Set<String> getRolesByUserName(String username);

}
