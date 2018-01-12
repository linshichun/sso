package win.scolia.cloud.sso.bean.output;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 参数验证信息
 */
@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class VerificationOutput {

    @NonNull
    private List<String> verification;
}
