package win.scolia.cloud.sso.bean.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@ToString(callSuper = true)
public class UserSafely extends BaseEntity {
    private static final long serialVersionUID = 3837481784346296742L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
}
