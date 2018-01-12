package win.scolia.cloud.sso.bean.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ChangePasswordInput {

    // 当前的密码
    @NotNull(message = "当前的密码不能为空")
    private String current;

    // 新密码
    @NotNull(message = "新密码不能为空")
    private String target;
}
