package win.scolia.sso.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import win.scolia.sso.bean.vo.export.MessageExport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageUtils {

    /**
     * 处理数据校验后的信息
     *
     * @param bindingResult 校验信息
     * @return 返回消息对象
     */
    public static MessageExport makeValidMessage(BindingResult bindingResult) {
        MessageExport messageExport = new MessageExport();
        List<String> errorList = new ArrayList<>();
        for (ObjectError error : bindingResult.getAllErrors()) {
            errorList.add(error.getDefaultMessage());
        }
        messageExport.getMessages().put("error", errorList);
        return messageExport;
    }

    /**
     * 向消息对象中添加信息
     *
     * @param messageExport 消息对象
     * @param key 消息键
     * @param message   消息
     */
    @SuppressWarnings("unchecked")
    public static void addMessage(MessageExport messageExport, String key, String message) {
        Object entity = messageExport.getMessages().get(key);
        if (entity == null) {
            List<String> errorList = new ArrayList<>();
            errorList.add(message);
            messageExport.getMessages().put(key, errorList);
        } else {
            if (entity instanceof Collection) {
                ((Collection) entity).add(message);
            } else {
                messageExport.getMessages().put(key, message);
            }
        }
    }

    /**
     * 向消息对象中添加信息
     *
     * @param messageExport 消息对象
     * @param key 消息键
     * @param message   消息
     */
    public static void putMessage(MessageExport messageExport, String key, String message) {
        messageExport.getMessages().put(key, message);
    }

}
