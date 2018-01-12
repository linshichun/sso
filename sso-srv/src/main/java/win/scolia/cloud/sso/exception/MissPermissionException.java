package win.scolia.cloud.sso.exception;

import lombok.NoArgsConstructor;

/**
 * 无此权限
 */
@NoArgsConstructor
public class MissPermissionException extends MissException {
    private static final long serialVersionUID = 1506861854619535326L;

    public MissPermissionException(String message) {
        super(message);
    }

    public MissPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissPermissionException(Throwable cause) {
        super(cause);
    }
}
