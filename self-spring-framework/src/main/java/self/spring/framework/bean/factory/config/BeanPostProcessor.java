package self.spring.framework.bean.factory.config;

/**
 * BeanPostProcessor
 *
 * @author chenzb
 * @date 2019/12/9
 */
public class BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
