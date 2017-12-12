package win.scolia.sso.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import win.scolia.sso.api.bean.vo.MessageVO;

import java.util.ArrayList;
import java.util.List;

public class MessageUtils {

    /**
     * 处理数据校验后的信息
     *
     * @param bindingResult 校验信息
     * @return 返回消息对象
     */
    public static MessageVO makeValidMessage(BindingResult bindingResult) {
        MessageVO messageVO = new MessageVO();
        List<String> errorList = new ArrayList<>();
        for (ObjectError error : bindingResult.getAllErrors()) {
            errorList.add(error.getDefaultMessage());
        }
        messageVO.getMessages().put("error", errorList);
        return messageVO;
    }

    /**
     * 向消息对象中添加信息
     *
     * @param messageVO 消息对象
     * @param key 消息键
     * @param message   消息
     */
    public static void addMessage(MessageVO messageVO, String key, String message) {
        Object entity = messageVO.getMessages().get(key);
        if (entity == null) {
            List<String> errorList = new ArrayList<>();
            errorList.add(message);
            messageVO.getMessages().put(key, errorList);
        } else {
            if (entity instanceof List) {
                ((List) entity).add(message);
            } else {
                messageVO.getMessages().put(key, message);
            }
        }
    }

    /**
     * 向消息对象中添加信息
     *
     * @param messageVO 消息对象
     * @param key 消息键
     * @param message   消息
     */
    public static void putMessage(MessageVO messageVO, String key, String message) {
        messageVO.getMessages().put(key, message);
    }

}
