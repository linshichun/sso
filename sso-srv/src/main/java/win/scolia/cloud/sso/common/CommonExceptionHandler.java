package win.scolia.cloud.sso.common;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 */
@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    private static ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 参数错误
     *
     * @return 400
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> bedRequestHandler(HttpServletRequest request) {
        if (log.isWarnEnabled()) {
            String param;
            try {
                param = MAPPER.writeValueAsString(request.getParameterMap());
            } catch (JsonProcessingException e) {
                param = null;
            }
            log.warn("{} try to access {}, but parameter error in: {}", request.getRemoteHost(),
                    request.getRequestURI(), param);
        }
        return ResponseEntity.badRequest().body("Parameter error");
    }

    /**
     * 不支持的媒体类型
     * @return 415
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Void> UnsupportedMediaType() {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
    }

    /**
     * 请求方法不允许
     *
     * @return 405
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Void> methodNotAllowedHandler(HttpServletRequest request) {
        if (log.isWarnEnabled()) {
            log.warn("{} try to access {}, but method not allowed", request.getRemoteHost(), request.getRequestURI());
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    /**
     * 通用异常处理
     *
     * @return 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> exception(HttpServletRequest request, Exception e) {
        log.error("Error happen in: {}", request.getRequestURI(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
