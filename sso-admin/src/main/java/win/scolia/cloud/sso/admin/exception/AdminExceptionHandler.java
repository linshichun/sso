package win.scolia.cloud.sso.admin.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import win.scolia.cloud.sso.common.CommonExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class AdminExceptionHandler extends CommonExceptionHandler {

    /**
     * 未认证
     *
     * @return 401
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Void> unauthorizedHandler(HttpServletRequest request) {
        if (log.isWarnEnabled()) {
            log.warn("{} try to access {}, but not signin", request.getRemoteHost(), request.getRequestURI());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * 权限不足时的异常处理
     *
     * @return 403
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Void> forbiddenHandler(HttpServletRequest request) {
        if (log.isWarnEnabled()) {
            log.warn("{} try to access {}, but miss permission", request.getRemoteHost(), request.getRequestURI());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}

