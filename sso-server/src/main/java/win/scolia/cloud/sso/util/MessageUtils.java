package win.scolia.cloud.sso.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import win.scolia.cloud.sso.bean.vo.export.AuthenticationExport;
import win.scolia.cloud.sso.bean.vo.export.VerificationExport;

import java.util.ArrayList;
import java.util.List;

public class MessageUtils {

    /**
     * 处理数据校验后的信息
     *
     * @param bindingResult 校验信息
     * @return 返回消息对象
     */
    public static VerificationExport makeVerificationMessage(BindingResult bindingResult) {
        VerificationExport export = new VerificationExport();
        List<String> errorList = new ArrayList<>();
        for (ObjectError error : bindingResult.getAllErrors()) {
            errorList.add(error.getDefaultMessage());
        }
        export.setVerification(errorList);
        return export;
    }

    public static AuthenticationExport makeAuthenticationMessage(String message) {
        return new AuthenticationExport(message);
    }

}
