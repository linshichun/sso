package win.scolia.cloud.sso.dao;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import win.scolia.cloud.sso.bean.entity.User;
import win.scolia.cloud.sso.bean.entity.UserSafely;

import java.util.List;

/**
 * Created by scolia on 2017/11/27
 */
@Repository
public interface UserMapper extends Mapper<User> {

    List<UserSafely> selectAllUserSafely();
}
