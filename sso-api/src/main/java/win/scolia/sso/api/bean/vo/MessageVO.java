package win.scolia.sso.api.bean.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 响应信息的VO
 */
public class MessageVO {

    private List<String> messages;

    public MessageVO() {
        messages = new ArrayList<>();
    }

    public MessageVO(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
