package win.scolia.sso.bean.vo.export;

/**
 * base64图片编码的验证码
 */
public class Base64ImageCaptchaExport extends CaptchaExport {

    private String base64Image;

    public Base64ImageCaptchaExport(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
