package win.scolia.sso.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.scolia.sso.api.pojo.User;
import win.scolia.sso.api.service.AccountService;
import win.scolia.sso.mapper.AccountMapper;
import win.scolia.sso.utils.EncryptUtil;

import java.util.List;

/**
 * Created by scolia on 2017/11/27
 */
@Component
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private EncryptUtil encryptUtil;

    @Override
    public Integer register(User user) {
        try {
            String selfSalt = encryptUtil.getRandomSalt();
            user.setSalt(selfSalt);
            String rowPassword = user.getPassword();
            String encryptedPassword = encryptUtil.getEncryptedPassword(rowPassword, user.getSalt());
            user.setPassword(encryptedPassword);
            return accountMapper.insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean login(String username, String password) {
        return false;
    }

    @Override
    public boolean logout(String username) {
        return false;
    }

    @Override
    public List<String> getRoles(String username) {
        return null;
    }

    @Override
    public List<String> getPermissions(String roleName) {
        return null;
    }
}
