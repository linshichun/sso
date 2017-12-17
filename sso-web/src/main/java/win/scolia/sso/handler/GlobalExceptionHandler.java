package win.scolia.sso.handler;


import org.apache.shiro.authz.UnauthenticatedException;
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

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Void> authorizationExceptionHandler(HttpServletRequest request){
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("{} try to access {}, but unauthenticated", request.getRemoteHost(), request.getRequestURI());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
