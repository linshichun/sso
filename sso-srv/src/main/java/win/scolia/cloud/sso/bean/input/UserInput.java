package win.scolia.cloud.sso.bean.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import win.scolia.cloud.sso.bean.Convert;
import win.scolia.cloud.sso.bean.entity.User;

import javax.validation.constraints.Min;

/**
 * 用户信息的输入VO对象
 */
@Getter
@Setter
@ToString
public class UserInput {

    public interface Register {
    }

    public interface Login {
    }

    public interface UpdatePassword {
    }

    @Min(value = 1, message = "用户ID必须是大于1的数字", groups = {UpdatePassword.class})
    private Long userId;

    @NotEmpty(message = "用户名不能为空", groups = {Register.class, Login.class})
    private String userName;

    @NotEmpty(message = "密码不能为空", groups = {Register.class, Login.class, UpdatePassword.class})
    private String password;

    private Boolean rememberMe = false;

    /**
     * 转换为User对象
     *
     * @return user对象
     */
    public User convertToUser() {
        UserInputConvert convert = new UserInputConvert();
        return convert.convert(this);
    }

    private static class UserInputConvert implements Convert<UserInput, User> {

        @Override
        public User convert(UserInput userInput) {
            User user = new User();
            BeanUtils.copyProperties(userInput, user);
            return user;
        }
    }

}



