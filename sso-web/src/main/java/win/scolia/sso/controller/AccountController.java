package win.scolia.sso.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import win.scolia.sso.bean.vo.MessageVO;
import win.scolia.sso.bean.vo.UserVO;
import win.scolia.sso.service.AccountService;
import win.scolia.sso.exception.DuplicateUserException;
import win.scolia.sso.util.MessageUtils;

@Controller
@RequestMapping(value = "account")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    /**
     * 用户注册
     *
     * @param userVO        用户的信息
     * @param bindingResult 数据校验的结果
     * @return 201 表示注册成功, 400 参数错误, 500 服务器错误
     */
    @PostMapping("register")
    public ResponseEntity<MessageVO> register(@Validated(UserVO.Register.class) UserVO userVO,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            MessageVO messageVO = MessageUtils.makeValidMessage(bindingResult);
            return ResponseEntity.badRequest().body(messageVO);
        }
        try {
            accountService.register(userVO);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Register user: {}", userVO);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicateUserException e) {
            MessageVO messageVO = new MessageVO();
            MessageUtils.putMessage(messageVO, "error", "该用户已被占用");
            return ResponseEntity.badRequest().body(messageVO);
        } catch (Exception e) {
            LOGGER.error("User Register error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 使用用户名和密码登录
     *
     * @param userVO        用户信息
     * @param bindingResult 数据校验的结果
     * @return 202 登录成功, 401 登录失败, 500 服务器错误
     */
    @PostMapping("loginByPassword")
    public ResponseEntity<MessageVO> loginByPassword(@Validated(UserVO.LoginByPassword.class) UserVO userVO,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            MessageVO messageVO = MessageUtils.makeValidMessage(bindingResult);
            return ResponseEntity.badRequest().body(messageVO);
        }
        try {
            String token = accountService.loginByPassword(userVO.getUserName(),userVO.getPassword());
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Login user: {}", userVO.getUserName());
            }
            MessageVO messageVO = new MessageVO();
            MessageUtils.putMessage(messageVO, "token", token);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageVO);
        } catch (Exception e) {
            LOGGER.error("User LoginByPassword error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 使用用户名和token进行登录
     * @param userVO 用户信息
     * @param bindingResult 数据校验的结果
     * @return 202 登录成功, 401 登录失败, 500 服务器错误
     */
    @PostMapping("loginByToken")
    public ResponseEntity<MessageVO> loginByToken(@Validated(UserVO.LoginByToken.class) UserVO userVO,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            MessageVO messageVO = MessageUtils.makeValidMessage(bindingResult);
            return ResponseEntity.badRequest().body(messageVO);
        }
        try {
            if (!accountService.loginByToken(userVO.getUserName(), userVO.getToken())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Login user: {}", userVO.getUserName());
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            LOGGER.error("User LoginByPassword error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
