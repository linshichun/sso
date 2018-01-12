package win.scolia.cloud.sso.exception;

import lombok.NoArgsConstructor;

/**
 * 重复的角色
 */
@NoArgsConstructor
public class DuplicateRoleException extends DuplicateException {
    private static final long serialVersionUID = -8063075780869477108L;

    public DuplicateRoleException(String message) {
        super(message);
    }

    public DuplicateRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateRoleException(Throwable cause) {
        super(cause);
    }
}
