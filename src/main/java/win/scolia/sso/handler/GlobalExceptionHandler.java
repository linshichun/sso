package win.scolia.sso.handler;


import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * spring mvc 的全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 未登录的异常处理
     * @return 401
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Void> authorizationExceptionHandler(HttpServletRequest request){
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("{} try to access {}, but not signin", request.getRemoteHost(), request.getRequestURI());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * 权限不足时的异常处理
     * @return 403
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Void> authorizedExceptionHandler(HttpServletRequest request){
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("{} try to access {}, but miss permission", request.getRemoteHost(), request.getRequestURI());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * 通用异常处理
     * @return 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> exceptionHandler(HttpServletRequest request, Exception e){
        LOGGER.error("Error happen in: {}", request.getRequestURI(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
