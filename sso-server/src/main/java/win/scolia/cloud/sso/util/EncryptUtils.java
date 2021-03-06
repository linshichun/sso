package win.scolia.cloud.sso.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.scolia.cloud.sso.autoconfigure.SSOProperties;

import java.util.UUID;

/**
 * Created by scolia on 2017/11/28
 * 实现加密的工具类
 */
@Component
public class EncryptUtils {

    @Autowired
    private SSOProperties properties;

    /**
     * 生成私有盐
     * @return 返回uuid字符串
     */
    public String getRandomSalt() {
        return UUID.randomUUID().toString();
    }

    /**
     * 先用全局盐md5加密一次, 再用私有盐sha256加密一次
     * @param rowPassword 明文密码
     * @param selfSalt 私有盐
     * @return 加密后的密码
     */
    public String getEncryptedPassword(String rowPassword, String selfSalt) {
        String temp = DigestUtils.md5Hex(rowPassword + properties.getEncrypt().getSalt());
        return  DigestUtils.sha256Hex(temp + selfSalt);
    }

}
