package self.spring.framework.bean;

public class BeanWrapper {

    private Object wrappedInstance;

    private Class<?> wrappedClass;

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    /**
     * 获取代理后的class
     */
    public Class<?> getWrappedClass() {
        return wrappedInstance.getClass();
    }
}
