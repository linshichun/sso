package win.scolia.cloud.sso.config;


import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import win.scolia.cloud.sso.shiro.SSOCredentialsMatcher;
import win.scolia.cloud.sso.shiro.SSORealm;

@Configuration
public class ShiroConfig {

    /**
     * 配置session管理器, 使用Shiro直接使用容器的session, 而不是自己管理session.
     */
    @Bean
    public SessionManager sessionManager(){
        return new ServletContainerSessionManager();
    }

    /**
     * 配置安全管理器
     * @param sessionManager Session管理器
     * @param realm 验证和鉴权器
     * @param matcher 密码匹配器
     */
    @Bean
    public SecurityManager securityManager(SessionManager sessionManager, SSORealm realm, SSOCredentialsMatcher matcher) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        realm.setCredentialsMatcher(matcher);
        manager.setRealm(realm);
        manager.setSessionManager(sessionManager);
        return manager;
    }

    // 生命周期管理器
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    // 过滤链
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        return shiroFilterFactoryBean;
    }

    /**
     * 启用注解
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    /**
     * 启用注解
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
