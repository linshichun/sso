package win.scolia.cloud.sso.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import win.scolia.cloud.sso.bean.vo.export.AuthenticationExport;
import win.scolia.cloud.sso.bean.vo.export.VerificationExport;

import java.util.ArrayList;
import java.util.List;

/**
 * 组装 ResponseEntity
 */
public class ResponseUtils {

    /**
     * 组装表单验证错误响应对象
     *
     * @param bindingResult 校验信息
     * @return 422
     */
    public static ResponseEntity<VerificationExport> makeVerificationResponseEntity(BindingResult bindingResult) {
        VerificationExport export = new VerificationExport();
        List<String> errorList = new ArrayList<>();
        for (ObjectError error : bindingResult.getAllErrors()) {
            errorList.add(error.getDefaultMessage());
        }
        export.setVerification(errorList);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(export);
    }

    /**
     * 返回用户名密码验证错误
     * @param message 提示信息
     * @return 401
     */
    public static ResponseEntity<AuthenticationExport> makeAuthenticationResponseEntity(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationExport(message));
    }

}
