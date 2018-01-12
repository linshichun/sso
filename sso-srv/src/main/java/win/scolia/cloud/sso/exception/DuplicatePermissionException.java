package win.scolia.cloud.sso.exception;

import lombok.NoArgsConstructor;

/**
 * 重复的权限
 */
@NoArgsConstructor
public class DuplicatePermissionException extends DuplicateException {
    private static final long serialVersionUID = -2208113928341093248L;

    public DuplicatePermissionException(String message) {
        super(message);
    }

    public DuplicatePermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatePermissionException(Throwable cause) {
        super(cause);
    }
}
