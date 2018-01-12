package win.scolia.cloud.sso.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import win.scolia.cloud.sso.util.ResponseUtils;

@Aspect
@Component
public class CheckEntryAspect {

    @Pointcut("@annotation(win.scolia.cloud.sso.annotation.CheckEntry)")
    public void checkEntryAnnotation() {
    }

    @Around("checkEntryAnnotation()")
    public Object checkEntry(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) arg;
                if (bindingResult.hasErrors()) {
                    return ResponseUtils.makeVerificationResponseEntity(bindingResult);
                }
            }
        }
        return point.proceed();
    }

}
