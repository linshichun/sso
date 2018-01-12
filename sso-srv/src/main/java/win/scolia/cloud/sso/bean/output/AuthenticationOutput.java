package win.scolia.cloud.sso.bean.output;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class AuthenticationOutput {

    @NonNull
    private String authentication;
}
