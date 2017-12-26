package win.scolia.sso.exception;

public class MissException extends RuntimeException {
    private static final long serialVersionUID = 6028075798806736244L;

    public MissException() {
    }

    public MissException(String message) {
        super(message);
    }

    public MissException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissException(Throwable cause) {
        super(cause);
    }
}
