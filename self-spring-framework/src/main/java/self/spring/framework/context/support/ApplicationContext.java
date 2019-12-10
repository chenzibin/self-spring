package self.spring.framework.context.support;

import self.spring.framework.annotation.Autowired;
import self.spring.framework.annotation.Controller;
import self.spring.framework.annotation.Service;
import self.spring.framework.bean.BeanWrapper;
import self.spring.framework.bean.BeansException;
import self.spring.framework.bean.factory.config.BeanDefinition;
import self.spring.framework.bean.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
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

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        try {
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();

            Object instance = instantiateBean(beanDefinition);

            if (instance == null) {
                return null;
            }

            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);

            BeanWrapper beanWrapper = new BeanWrapper(instance);

            factoryBeanInstanceCache.put(beanName, beanWrapper);

            beanPostProcessor.postProcessAfterInitialization(instance, beanName);

            populateBean(beanName, instance);
            return beanWrapper.getWrappedInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void populateBean(String beanName, Object instance) {

        Class clazz = instance.getClass();
        if (clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }
                Autowired autowired = field.getAnnotation(Autowired.class);
                String name = autowired.value();
                if ("".equals(name)) {
                    name = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(instance, factoryBeanInstanceCache.get(name).getWrappedInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object instantiateBean(BeanDefinition beanDefinition) {

        Object instance = null;
        String className = beanDefinition.getBeanClassName();

        if (factoryBeanObjectCache.containsKey(className)) {
            instance = factoryBeanObjectCache.get(className);
        } else {
            try {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(), instance);
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
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