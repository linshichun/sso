package win.scolia.sso.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.scolia.sso.api.pojo.User;
import win.scolia.sso.api.service.AccountService;
import win.scolia.sso.mapper.AccountMapper;

import java.util.List;

/**
 * Created by scolia on 2017/11/27
 */
@Component
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Integer register(User user) {
        return accountMapper.insertUser(user);
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
