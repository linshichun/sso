package win.scolia.cloud.sso.bean.vo.export;

import java.util.List;

/**
 * 参数验证信息
 */
public class VerificationExport {

    private List<String> verification;

    public List<String> getVerification() {
        return verification;
    }

    public void setVerification(List<String> verification) {
        this.verification = verification;
    }
}
