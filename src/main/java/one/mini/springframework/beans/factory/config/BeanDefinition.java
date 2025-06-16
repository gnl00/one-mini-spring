package one.mini.springframework.beans.factory.config;

import lombok.Data;
import one.mini.springframework.beans.PropertyValues;

@Data
public class BeanDefinition {

    /*
     * 单例模式和原型模式的区别就在于是否存放到内存中，如果是原型模式那么就不会存放到内存中，
     * 每次获取都重新创建对象，另外非 Singleton 类型的 Bean 不需要执行销毁方法。
     */
    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    /**
     * 把 Object bean 替换为 Class，这样就可以把 Bean 的实例化操作放到容器中处理了
     */
    private Class<?> beanClass;

    private PropertyValues propertyValues;

    private String initMethodName;

    private String destroyMethodName;

    private String scope = SCOPE_SINGLETON;

    private boolean singleton = true;

    private boolean prototype = false;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

}
