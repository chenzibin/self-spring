package self.spring.framework.context.support;

/**
 * ApplicationContext初始化完成后， 将自动对实现该接口的实例调用setApplicationContext方法
 */
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext);
}
