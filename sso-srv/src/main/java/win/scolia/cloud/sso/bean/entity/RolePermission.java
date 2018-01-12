package win.scolia.cloud.sso.bean.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Id;

/**
 * 角色和权限的映射关系
 */
@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
@Builder
@ToString(callSuper = true)
public class RolePermission extends BaseEntity {
    private static final long serialVersionUID = -4885042955282179774L;

    @Id
    @NonNull
    private Long roleId;

    @Id
    @NonNull
    private Long permissionId;
}
