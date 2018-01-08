package win.scolia.cloud.sso.dao;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import win.scolia.cloud.sso.bean.entity.UserRole;

@Repository
public interface UserRoleMapper extends Mapper<UserRole> {
}
