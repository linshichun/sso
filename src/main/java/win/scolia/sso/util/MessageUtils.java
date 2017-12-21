package win.scolia.sso.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import win.scolia.sso.bean.vo.export.MessageExportVO;

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
    public static MessageExportVO makeValidMessage(BindingResult bindingResult) {
        MessageExportVO messageExportVO = new MessageExportVO();
        List<String> errorList = new ArrayList<>();
        for (ObjectError error : bindingResult.getAllErrors()) {
            errorList.add(error.getDefaultMessage());
        }
        messageExportVO.getMessages().put("error", errorList);
        return messageExportVO;
    }

    /**
     * 向消息对象中添加信息
     *
     * @param messageExportVO 消息对象
     * @param key 消息键
     * @param message   消息
     */
    @SuppressWarnings("unchecked")
    public static void addMessage(MessageExportVO messageExportVO, String key, String message) {
        Object entity = messageExportVO.getMessages().get(key);
        if (entity == null) {
            List<String> errorList = new ArrayList<>();
            errorList.add(message);
            messageExportVO.getMessages().put(key, errorList);
        } else {
            if (entity instanceof Collection) {
                ((Collection) entity).add(message);
            } else {
                messageExportVO.getMessages().put(key, message);
            }
        }
    }

    /**
     * 向消息对象中添加信息
     *
     * @param messageExportVO 消息对象
     * @param key 消息键
     * @param message   消息
     */
    public static void putMessage(MessageExportVO messageExportVO, String key, String message) {
        messageExportVO.getMessages().put(key, message);
    }

}
