package win.scolia.sso.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import win.scolia.sso.api.bean.vo.MessageVO;
import win.scolia.sso.api.bean.vo.UserVO;
import win.scolia.sso.api.server.AccountService;

import java.util.List;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    /**
     * 用户注册
     *
     * @param userVO        用户的信息
     * @param bindingResult 数据校验的结果
     * @return
     */
    @PostMapping
    public ResponseEntity<MessageVO> register(@Validated(UserVO.register.class) UserVO userVO,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            MessageVO messageVO = new MessageVO();
            List<String> messages = messageVO.getMessages();
            for (ObjectError error : bindingResult.getAllErrors()) {
                messages.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(messageVO);
        }
        try {
            accountService.register(userVO);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Register user: {}", userVO);
            }
            return ResponseEntity.ok().build();
        } catch (DuplicateKeyException e) {
            MessageVO messageVO = new MessageVO();
            messageVO.getMessages().add("该用户名已被占用");
            return ResponseEntity.badRequest().body(messageVO);
        } catch (Exception e) {
            LOGGER.error("User register error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
