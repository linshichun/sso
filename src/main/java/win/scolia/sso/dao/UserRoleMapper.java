package win.scolia.sso.dao;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import win.scolia.sso.bean.entity.UserRole;

@Repository
public interface UserRoleMapper extends Mapper<UserRole> {
}
