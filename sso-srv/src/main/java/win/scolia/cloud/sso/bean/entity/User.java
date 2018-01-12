package win.scolia.cloud.sso.bean.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import win.scolia.cloud.sso.bean.Convert;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 用户详细信息
 */
@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -3188105297711919345L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NonNull
    private String userName;

    private String password;

    private String salt;

    public UserSafely convertToUserSafely() {
        return new UserSafelyConvert().convert(this);
    }

    private static class UserSafelyConvert implements Convert<User, UserSafely> {

        @Override
        public UserSafely convert(User user) {
            UserSafely userSafely = new UserSafely();
            BeanUtils.copyProperties(user, userSafely);
            return userSafely;
        }
    }


}
