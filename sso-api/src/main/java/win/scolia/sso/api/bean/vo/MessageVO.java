package win.scolia.sso.api.bean.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应信息的VO
 */
public class MessageVO {

    private Map<String, Object> messages;

    public MessageVO() {
        this.messages = new HashMap<>();
    }

    public MessageVO(Map<String, Object> messages) {
        this.messages = messages;
    }

    public Map<String, Object> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Object> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "MessageVO{" +
                "messages=" + messages +
                '}';
    }

}
