package win.scolia.sso.service.Impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.entity.UserRole;
import win.scolia.sso.bean.entity.UserSafely;
import win.scolia.sso.bean.vo.entry.UserEntry;
import win.scolia.sso.dao.UserMapper;
import win.scolia.sso.dao.UserRoleMapper;
import win.scolia.sso.exception.DuplicateUserException;
import win.scolia.sso.exception.MissUserException;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.EncryptUtils;
import win.scolia.sso.util.PageUtils;
import win.scolia.sso.util.cache.UserCacheUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserCacheUtils userCacheUtils;

    @Autowired
    private EncryptUtils encryptUtils;

    @Autowired
    private PageUtils pageUtils;

    /**
     * 获取用户对象, 并且获得到的用户对象不进行缓存
     *
     * @param userName 用户名
     * @return 用户对象或null
     */
    private User getUserSimply(String userName) {
        assert !StringUtils.isEmpty(userName) : "UserName can not be empty";
        User user = userCacheUtils.get(userName);
        if (user == null) {
            User query = new User(userName);
            user = userMapper.selectOne(query);
        }
        return user;
    }

    @Override
    public void createUser(UserEntry vo) {
        String userName = vo.getUserName();
        User cacheUser = this.getUserSimply(userName);
        if (cacheUser != null) {
            throw new DuplicateUserException(String.format("%s already exist", vo.getUserName()));
        }
        String salt = encryptUtils.getRandomSalt();
        String password = encryptUtils.getEncryptedPassword(vo.getPassword(), salt);
        User record = new User(userName, password, salt);
        record.forCreate();
        try {
            userMapper.insert(record);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUserException(String.format("%s already exist", vo.getUserName()), e);
        }
    }

    @Transactional
    @Override
    public void removeUserByUserName(String userName) {
        User record = this.getUserSimply(userName);
        if (record == null) {
            throw new MissUserException(String.format("%s not exist", userName)); // 用户不存在
        }
        // 删除角色表中的记录
        userMapper.deleteByPrimaryKey(record);
        // 删除 用户-角色 表中的映射
        UserRole userRoleRecord = new UserRole();
        userRoleRecord.setUserId(record.getUserId());
        userRoleMapper.delete(userRoleRecord);
        // 清除缓存
        userCacheUtils.delete(userName);
    }

    @Override
    public boolean changePasswordByOldPassword(String userName, String oldPassword, String newPassword) {
        User user = this.getUserByUserName(userName);
        if (user == null) {
            throw new MissUserException(String.format("%s not exist", userName)); // 用户不存在
        }
        String tempPassword = encryptUtils.getEncryptedPassword(oldPassword, user.getSalt());
        if (StringUtils.equals(tempPassword, user.getPassword())) {
            String password = encryptUtils.getEncryptedPassword(newPassword, user.getSalt());
            User record = new User(user.getUserId(), password);
            record.forUpdate();
            userMapper.updateByPrimaryKeySelective(record);
            userCacheUtils.delete(userName);
            return true;
        }
        return false;
    }

    @Override
    public void changePasswordDirectly(String userName, String newPassword) {
        User user = this.getUserByUserName(userName);
        if (user == null) {
            throw new MissUserException(String.format("%s not exist", userName)); // 用户不存在
        }
        String password = encryptUtils.getEncryptedPassword(newPassword, user.getSalt());
        User record = new User(user.getUserId(), password);
        record.forUpdate();
        userMapper.updateByPrimaryKeySelective(record);
        userCacheUtils.delete(userName);
    }

    @Override
    public User getUserByUserName(String userName) {
        User user = userCacheUtils.get(userName);
        if (user == null) {
            User query = new User(userName);
            user = userMapper.selectOne(query);
            userCacheUtils.cache(userName, user);
        }
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
        return this.getUserSimply(userName) == null ;
    }
}
