package win.scolia.cloud.sso.bean.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
@ToString(callSuper = true)
public class Permission extends BaseEntity {
    private static final long serialVersionUID = 6544068627532597615L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @NonNull
    private String permission;
}
