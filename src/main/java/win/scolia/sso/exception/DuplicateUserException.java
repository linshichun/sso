package win.scolia.sso.exception;

/**
 * 重复的用户
 */
public class DuplicateUserException extends DuplicateException {
    private static final long serialVersionUID = 6996877237677519976L;

    public DuplicateUserException() {
    }

    public DuplicateUserException(String message) {
        super(message);
    }

    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateUserException(Throwable cause) {
        super(cause);
    }
}
