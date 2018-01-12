package win.scolia.cloud.sso.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthenticationException extends RuntimeException {
    private static final long serialVersionUID = 3109074098188458835L;

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
