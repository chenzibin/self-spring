package self.spring.framework.bean.factory;

import self.spring.framework.bean.BeansException;
import self.spring.framework.bean.NoSuchBeanDefinitionException;

public interface BeanFactory {

    String FACTORY_BEAN_PREFIX = "&";

    /**
     * 根据bean名称获取IOC容器中的bean实例
     */
    Object getBean(String name) throws BeansException;

    /**
     * 根据bean名称和Class类型获取bean实例， 类型安全验证
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    /**
     * 根据bean名称和和参数列表获取bean实例， 类型安全验证
     */
    Object getBean(String name, Object... args) throws BeansException;

    /**
     * 根据Class类型获取bean实例， 类型安全验证
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;

    /**
     * 根据Class类型和参数列表获取bean实例， 类型安全验证
     */
    <T> T getBean(Class<T> requiredType, Object... args) throws BeansException;

    /**
     * 检索是否包含该名称的bean
     */
    boolean containsBean(String name);

    /**
     * 根据bean的名称判断bean是否单例
     */
    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

    /**
     * 根据bean的名称判断bean是否原型
     */
    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

    /**
     * 根据bean的名称获取bean的Class类型
     */
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

    /**
     * 根据bean的名称（类名、别名）获取该bean的所有名称
     */
    String[] getAliases(String name);
}
