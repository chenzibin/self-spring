package self.spring.framework.context.support;

import org.junit.Test;

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

        context.getBean("");
    }
}
