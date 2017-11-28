package test.scolia.sso.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import win.scolia.sso.api.pojo.User;
import win.scolia.sso.api.service.AccountService;

/**
 * Created by scolia on 2017/11/27
 */
public class AccountServiceImplTest {

    private AccountService accountService;

    @Before
    public void init() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:dubbo/application-dubbo.xml");
        accountService = (AccountService) context.getBean("accountService");
    }

    @Test
    public void register() throws Exception {
        User user = new User();
        user.setUsername("scolia");
        user.setPassword("123456");
        accountService.register(user);
    }

}