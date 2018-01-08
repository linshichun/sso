package win.scolia.cloud.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import win.scolia.cloud.sso.autoconfigure.SSOProperties;

/**
 * Created by scolia on 2017/11/27
 *
 * spring boot 入口程序
 */

@SpringBootApplication
@MapperScan("win.scolia.cloud.sso.dao")
@EnableTransactionManagement
@ServletComponentScan
@EnableConfigurationProperties(SSOProperties.class)
@EnableDiscoveryClient
public class SSOApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSOApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SSOApplication.class, args);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SSOApplication started!!");
        }
    }
}
