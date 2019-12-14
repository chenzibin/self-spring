package context.support;

import org.junit.Assert;
import org.junit.Test;
import self.spring.framework.context.support.ApplicationContext;
import self.spring.framework.test.service.api.HelloService;

/**
 * ApplicationContextTest
 *
 * @author chenzb
 * @date 2019/12/14
 */
public class ApplicationContextTest {

    @Test
    public void testGetBean() {
        ApplicationContext context = new ApplicationContext("application.properties");

        HelloService helloService = (HelloService) context.getBean("HelloService");

        Assert.assertNotNull(helloService);
    }
}
