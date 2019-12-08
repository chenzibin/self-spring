package self.spring.framework.bean.factory.config;

import lombok.Data;

@Data
public class BeanDefinition {

    private String beanClassName;

    private boolean lazyInit = false;

    private String factoryBeanName;

}
