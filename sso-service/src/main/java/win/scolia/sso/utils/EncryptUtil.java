package win.scolia.sso.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * Created by scolia on 2017/11/28
 * 实现加密的工具类
 */
@Component
@PropertySource("classpath:encrypt.properties")
public class EncryptUtil {

    @Value("${encrypt.salt}")
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
        MessageDigest md5 = DigestUtils.getMd5Digest();
        md5.update(rowPassword.getBytes());
        md5.update(systemSalt.getBytes());
        String temp = Hex.encodeHexString(md5.digest());
        MessageDigest sha = DigestUtils.getSha256Digest();
        sha.update(selfSalt.getBytes());
        sha.update(temp.getBytes());
        return  Hex.encodeHexString(sha.digest());
    }

}
