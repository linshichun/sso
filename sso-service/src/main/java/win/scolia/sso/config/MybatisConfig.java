package win.scolia.sso.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by scolia on 2017/11/27
 *
 * mybatis 整合配置
 */
@Configuration
// 要在数据源创建后再配置
@AutoConfigureAfter(DataSourceConfig.class)
// 扫描接口, 如果使用maven管理, 且接口和xml文件在同一个包下, 注意pom中resource的配置
// 如果xml与接口分离, 则需要在sqlSessionFactoryBean中setMapperLocations来指定xml文件的位置
@MapperScan("win.scolia.sso.mapper")
public class MybatisConfig {

    /**
     * 配置SqlSessionFactory
     * @param dataSource 数据源, 这里由spring传递, 之所以不使用注入的方式, 是因为注入时得到的是null
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

}
