package win.scolia.cloud.sso.bean.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Id;

/**
 * 用户和角色的映射关系
 */
@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
@Builder
@ToString(callSuper = true)
public class UserRole extends BaseEntity {
    private static final long serialVersionUID = 7108259844144855041L;

    @Id
    @NonNull
    private Long userId;

    @Id
    @NonNull
    private Long roleId;
}
