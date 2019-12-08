package self.spring.framework.context.support;

import self.spring.framework.bean.BeanWrapper;
import self.spring.framework.bean.BeansException;
import self.spring.framework.bean.factory.config.BeanDefinition;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext extends AbstractApplicationContext {

    private String[] configLocations;

    private BeanDefinitionReader reader;

    /**
     * 单例的IOC容器
     */
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    /**
     * 通用的IOC容器
     */
    private Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        // 1、定位：定位配置文件
        reader = new BeanDefinitionReader(this.configLocations);

        // 2、加载：加载配置文件，扫描相关的类，并封装成BeanDefinition
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        // 3、注册：把配置信息放到容器里
        doRegisterBeanDefinition(beanDefinitions);

        // 4、把非延时加载的类提前初始化
        doAutowired();
    }

    private void doAutowired() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) throws Exception {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception(String.format("The %s is exists!", beanDefinition.getFactoryBeanName()));
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    /**
     * 通过反射创建一个实例，并包装成BeanWrapper返回
     * 装饰器模式：
     * 1、保留原OOP关系
     * 2、需要进行扩展、增强（AOP）
     */
    @Override
    public Object getBean(String beanName) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return (T) getBean(requiredType.getName());
    }

    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return reader.getConfig();
    }
}