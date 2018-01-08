package win.scolia.cloud.sso.bean.vo.entry;

import javax.validation.constraints.NotNull;

public class ChangePasswordEntry {

    // 当前的密码
    @NotNull(message = "当前的密码不能为空")
    private String current;

    // 新密码
    @NotNull(message = "新密码不能为空")
    private String target;

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
