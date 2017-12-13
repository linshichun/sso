package win.scolia.sso.exception;

public class DuplicateUserException extends RuntimeException {
    private static final long serialVersionUID = 6996877237677519976L;

    public DuplicateUserException() {
        super();
    }

    public DuplicateUserException(String message) {
        super(message);
    }

    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
