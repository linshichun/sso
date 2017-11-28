package win.scolia.sso.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by scolia on 2017/11/27
 *
 * 配置dubbo
 */
@Configuration
@PropertySource("classpath:dubbo.properties")
public class DubboConfig {

    @Value("${dubbo.application.name}")
    private String applicationName;

    @Value("${dubbo.registry.protocol}")
    private String registryProtocol;

    @Value("${dubbo.registry.address}")
    private String registryAddress;

    @Value("${dubbo.registry.port}")
    private Integer registryPort = 2181;

    @Value("${dubbo.provider.protocol}")
    private String providerProtocol;

    @Value("${dubbo.provider.port}")
    private Integer providerPort;

    @Value("${dubbo.provider.package}")
    private String providerPackage;


    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(applicationName);
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol(registryProtocol);
        registryConfig.setAddress(registryAddress);
        registryConfig.setPort(registryPort);
        return registryConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(providerProtocol);
        protocolConfig.setPort(providerPort);
        return protocolConfig;
    }

    @Bean
    public AnnotationBean annotationBean() {
        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage(providerPackage);
        return annotationBean;
    }

}
