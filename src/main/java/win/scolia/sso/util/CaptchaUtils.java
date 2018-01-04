package win.scolia.sso.util;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class CaptchaUtils {

    @Autowired
    private DefaultKaptcha kaptcha;

    /**
     * 生成base64编码的图片验证码
     * @param session session
     * @return base64编码的图片
     */
    public String setBase64Image(HttpSession session) throws IOException {
        String capText = kaptcha.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        BufferedImage bi = kaptcha.createImage(capText);
        // 将图片数据写到二进制数组中去.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", outputStream);
        // 转换为base64编码
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    /**
     * 确定验证码是否正确
     * @param session session
     * @param input 用户输入的验证码
     * @return bool
     */
    public boolean verify(HttpSession session, String input) {
        String captcha = session.getAttribute(Constants.KAPTCHA_SESSION_KEY).toString();
        return StringUtils.equals(captcha, input);
    }
}
