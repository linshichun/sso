package win.scolia.cloud.sso.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicateException extends RuntimeException {
    private static final long serialVersionUID = 2610477518423283258L;

    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateException(Throwable cause) {
        super(cause);
    }
}
