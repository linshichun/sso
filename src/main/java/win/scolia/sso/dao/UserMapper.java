package win.scolia.sso.dao;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.entity.UserSafely;

import java.util.List;

/**
 * Created by scolia on 2017/11/27
 */
@Repository
public interface UserMapper extends Mapper<User> {

    /**
     * 获取所有的用户, 不包含敏感信息
     * @return 用户列表
     */
    List<UserSafely> selectAllUserSafely();
}
