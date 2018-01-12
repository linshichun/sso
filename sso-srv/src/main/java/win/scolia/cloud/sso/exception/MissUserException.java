package win.scolia.cloud.sso.exception;

import lombok.NoArgsConstructor;

/**
 * 无此用户
 */
@NoArgsConstructor
public class MissUserException extends MissException {
    private static final long serialVersionUID = -8527875069045912004L;

    public MissUserException(String message) {
        super(message);
    }

    public MissUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissUserException(Throwable cause) {
        super(cause);
    }
}
