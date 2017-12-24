package win.scolia.sso.service.Impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.entity.UserSafely;
import win.scolia.sso.bean.vo.entry.UserEntryVO;
import win.scolia.sso.dao.RoleMapper;
import win.scolia.sso.dao.UserMapper;
import win.scolia.sso.exception.DuplicateUserException;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.CacheUtils;
import win.scolia.sso.util.EncryptUtils;
import win.scolia.sso.util.PageUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private EncryptUtils encryptUtils;

    @Autowired
    private PageUtils pageUtils;

    /**
     * 获取用户对象, 并且获得到的用户对象不进行缓存
     * @param userName 用户名
     * @return 用户对象或null
     */
    private User getUserSimply(String userName) {
        assert !StringUtils.isEmpty(userName) : "UserName can not be empty";
        User user = cacheUtils.getUser(userName);
        if (user == null) {
            user = userMapper.selectUserByUserName(userName);
        }
        return user;
    }

    @Override
    public void createUser(UserEntryVO userEntryVO) {
        User cacheUser = this.getUserByUserName(userEntryVO.getUserName());
        if (cacheUser != null) {
            throw new DuplicateUserException("User already exist");
        }
        User user = new User();
        BeanUtils.copyProperties(userEntryVO, user);
        user.setSalt(encryptUtils.getRandomSalt());
        user.setPassword(encryptUtils.getEncryptedPassword(user.getPassword(), user.getSalt()));
        user.setCreateTime(new Date());
        user.setLastModified(new Date());
        try {
            userMapper.insertUser(user);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUserException("User already exist", e);
        }
    }

    @Transactional
    @Override
    public void removeUserByUserName(String userName) {
        User user = this.getUserSimply(userName);
        if (user == null) {
            throw new IllegalArgumentException("User not exist"); // 用户不存在
        }
        userMapper.deleteUserByUserName(userName); // 删除角色表中的记录
        roleMapper.deleteUserRoleMapByUserId(user.getUserId()); // 删除 用户-角色 表中的映射
        cacheUtils.clearUser(userName); // 清除缓存
    }

    @Override
    public boolean changePasswordByOldPassword(String userName, String oldPassword, String newPassword) {
        User user = this.getUserByUserName(userName);
        String tempPassword = encryptUtils.getEncryptedPassword(oldPassword, user.getSalt());
        if (StringUtils.equals(tempPassword, user.getPassword())) {
            String password = encryptUtils.getEncryptedPassword(newPassword, user.getSalt());
            user.setPassword(password);
            user.setLastModified(new Date());
            userMapper.updatePasswordByUserName(user);
            cacheUtils.clearUser(userName);
            return true;
        }
        return false;
    }

    @Override
    public User getUserByUserName(String userName) {
        User user = this.getUserSimply(userName);
        cacheUtils.cacheUser(user);
        return user;
    }

    @Override
    public UserSafely getUserSafelyByUserName(String userName) {
        User user = this.getUserByUserName(userName);
        if (user == null) {
            return null;
        }
        UserSafely userSafely = new UserSafely();
        BeanUtils.copyProperties(user, userSafely);
        return userSafely;
    }

    @Override
    public PageInfo<UserSafely> listUsersSafely(Integer pageNum) {
        pageUtils.startPage(pageNum);
        List<UserSafely> userSafelyList = userMapper.selectAllUserSafely();
        return pageUtils.getPageInfo(userSafelyList);
    }

    @Override
    public boolean checkUserNameUsable(String userName) {
        User user = this.getUserSimply(userName);
        return user == null;
    }
}
