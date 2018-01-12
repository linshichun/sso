package win.scolia.cloud.sso.exception;

import lombok.NoArgsConstructor;

/**
 * 无此角色
 */
@NoArgsConstructor
public class MissRoleException extends MissException {
    private static final long serialVersionUID = 836403182055684615L;

    public MissRoleException(String message) {
        super(message);
    }

    public MissRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissRoleException(Throwable cause) {
        super(cause);
    }
}
