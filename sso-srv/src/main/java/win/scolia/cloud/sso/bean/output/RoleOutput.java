package win.scolia.cloud.sso.bean.output;

import lombok.Builder;
import win.scolia.cloud.sso.bean.entity.Role;

import java.util.Set;

@Builder
public class RoleOutput {

    private Role role;

    private Set<String> permissions;
}
