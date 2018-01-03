package win.scolia.sso.bean.vo.export;

import java.util.HashMap;
import java.util.Map;

/**
 * 信息输出VO对象
 */
public class MessageExport {

    private Map<String, Object> messages;

    public MessageExport() {
        this.messages = new HashMap<>();
    }

    public MessageExport(Map<String, Object> messages) {
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
        return "MessageExportVO{" +
                "messages=" + messages +
                '}';
    }

}
