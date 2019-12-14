package self.spring.framework.context.support;

import self.spring.framework.bean.factory.config.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BeanDefinitionReader {

    private Properties config = new Properties();

    private final String SCAN_PACKAGE = "scanPackage";

    private List<String> registryBeanClasses = new ArrayList<>();

    public BeanDefinitionReader(String... locations) {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(locations[0])) {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource(scanPackage.replace(".", "/"));
        assert url != null;
        File dir = new File(url.getFile());
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                doScanner(String.format("%s.%s", scanPackage, file.getName()));
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = String.format("%s.%s", scanPackage, file.getName().replace(".class", ""));
                registryBeanClasses.add(className);
            }
        }
    }

    public Properties getConfig() {
        return config;
    }

    public List<BeanDefinition> loadBeanDefinitions() {
        List<BeanDefinition> result = new ArrayList<>();
        try {
            for (String className : registryBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()) {
                    continue;
                }
                result.add(doCreateBeanDefinition(beanClass.getName(), beanClass.getSimpleName()));

                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> classInterface : interfaces) {
                    result.add(doCreateBeanDefinition(beanClass.getName(), classInterface.getName()));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private BeanDefinition doCreateBeanDefinition(String beanClassName, String factoryBeanName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    private String toLowerFirstCase(String name) {
        char[] chars = name.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
