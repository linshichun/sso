package win.scolia.sso.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.vo.UserVO;
import win.scolia.sso.dao.UserMapper;
import win.scolia.sso.exception.DuplicateUserException;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.CacheUtils;
import win.scolia.sso.util.EncryptUtils;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private EncryptUtils encryptUtils;

    @Override
    public void createUser(UserVO userVO) {
        User cacheUser = getUserByUserName(userVO.getUserName());
        if (cacheUser != null) {
            throw new DuplicateUserException("该用户已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        user.setSalt(encryptUtils.getRandomSalt());
        user.setPassword(encryptUtils.getEncryptedPassword(user.getPassword(), user.getSalt()));
        user.setCreateTime(new Date());
        user.setLastModified(new Date());
        try {
            userMapper.insertUser(user);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUserException("该用户已存在", e);
        }
    }

    @Override
    public User getUserByUserName(String userName) {
        User user = cacheUtils.getUser(userName);
        if (user != null) {
            return user;
        }
        user = userMapper.selectUserByUserName(userName);
        cacheUtils.cacheUser(user);
        return user;
    }

    @Override
    public boolean changePasswordByOldPassword(String userName, String oldPassword, String newPassword) {
        User user = this.getUserByUserName(userName);
        String tempPassword = encryptUtils.getEncryptedPassword(oldPassword, user.getSalt());
        if (StringUtils.equals(tempPassword, user.getPassword())) {
            String password = encryptUtils.getEncryptedPassword(newPassword, user.getSalt());
            userMapper.updatePasswordByUserName(userName, password);
            cacheUtils.clearUser(userName);
            return true;
        }
        return false;
    }

    @Override
    public void removeUserByUserName(String userName) {
        cacheUtils.clearUser(userName);
        userMapper.deleteUserByUserName(userName);
    }

}
