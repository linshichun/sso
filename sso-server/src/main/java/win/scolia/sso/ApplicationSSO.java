package win.scolia.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by scolia on 2017/11/27
 *
 * spring boot 入口程序
 */

@SpringBootApplication
@MapperScan("win.scolia.sso.dao")
public class ApplicationSSO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationSSO.class);

    public static void main(String[] args) {
        SpringApplication.run(ApplicationSSO.class, args);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("spring boot started....");
        }
    }
}
