package win.scolia.sso.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by scolia on 2017/11/28
 * 实现加密的工具类
 */
@Component
public class EncryptUtil {

    @Value("${sso.encrypt.salt}")
    private String systemSalt;

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
        String temp = DigestUtils.md5Hex(rowPassword + systemSalt);
        return  DigestUtils.sha256Hex(temp + selfSalt);
    }

}
