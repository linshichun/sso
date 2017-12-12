package win.scolia.sso.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.scolia.sso.api.bean.entity.User;
import win.scolia.sso.api.bean.vo.UserVO;
import win.scolia.sso.api.server.AccountService;
import win.scolia.sso.dao.AccountMapper;
import win.scolia.sso.util.EncryptUtil;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by scolia on 2017/11/27
 */
@Component
public class AccountServiceImpl implements AccountService {

    @Autowired
    private EncryptUtil encryptUtil;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Long register(UserVO userVO) {
        if(StringUtils.isEmpty(userVO.getUserName()) || StringUtils.isEmpty(userVO.getPassword())){
            throw new IllegalArgumentException("用户名和密码不能为空");
        }
        User user = new User();
        user.setUsername(userVO.getUserName());
        user.setSalt(encryptUtil.getRandomSalt());
        user.setPassword(encryptUtil.getEncryptedPassword(userVO.getPassword(), user.getSalt()));
        user.setCreateTime(new Date());
        user.setLastModified(new Date());
        accountMapper.insertUser(user);
        return user.getUserId();
    }

    @Override
    public String login(UserVO userVO) {
        return null;
    }

    @Override
    public String login(String token) {
        return null;
    }

    @Override
    public void logout(UserVO userVO) {

    }

    @Override
    public void logout(String token) {

    }

    @Override
    public List<String> getRoles(String token) {
        return null;
    }

    @Override
    public Set<String> getPermissions(String token) {
        return null;
    }
}
