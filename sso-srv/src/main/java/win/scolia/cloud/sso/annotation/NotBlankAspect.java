package win.scolia.cloud.sso.annotation;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NotBlankAspect {

    @Pointcut("@annotation(win.scolia.cloud.sso.annotation.NotBlank)")
    public void notBlankAnnotation() {
    }

    @Before("notBlankAnnotation()")
    public void notBlank(JoinPoint joinPoint) {
        String target = (String) joinPoint.getArgs()[0];
        if (StringUtils.isBlank(target)) {
            throw new IllegalArgumentException("Parameter can not be blank");
        }
    }
}
