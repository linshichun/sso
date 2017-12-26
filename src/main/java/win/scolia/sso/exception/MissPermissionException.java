package win.scolia.sso.exception;

/**
 * 无此权限
 */
public class MissPermissionException extends MissException {
    private static final long serialVersionUID = 1506861854619535326L;

    public MissPermissionException() {
    }

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
