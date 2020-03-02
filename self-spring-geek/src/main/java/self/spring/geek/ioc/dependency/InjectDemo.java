package self.spring.geek.ioc.dependency;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import self.spring.geek.ioc.domain.User;

/**
 * InjectDemo
 *
 * @author chenzb
 * @date 2020/2/28
 */
public class InjectDemo {

    public static void main(String[] args) throws Exception {

        BeanFactory context = new ClassPathXmlApplicationContext("spring-context.xml");

        ObjectFactory<User> userFactory = (ObjectFactory<User>) context.getBean("userFactory");

        User user = userFactory.getObject();

        System.out.println(user);
    }
}
