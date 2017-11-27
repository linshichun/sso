package win.scolia.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Created by scolia on 2017/11/27
 *
 * spring boot 入口程序
 */

@SpringBootApplication
public class MainConfig {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MainConfig.class, args);
        System.in.read();
    }
}
