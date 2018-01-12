package win.scolia.cloud.sso.bean.output;

import lombok.Builder;
import win.scolia.cloud.sso.bean.entity.UserSafely;

import java.util.Set;

/**
 * 用户当前信息
 */
@Builder
public class UserOutput {

    private UserSafely user;

    private Set<String> roles;

    private Set<String> permissions;
}
