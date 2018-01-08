package win.scolia.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import win.scolia.sso.autoconfigure.SSOProperties;

/**
 * Created by scolia on 2017/11/27
 *
 * spring boot 入口程序
 */

@SpringBootApplication
@MapperScan("win.scolia.sso.dao")
@EnableTransactionManagement
@ServletComponentScan
@EnableConfigurationProperties(SSOProperties.class)
@EnableDiscoveryClient
public class SSOApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSOApplication.class, args);
    }
}
