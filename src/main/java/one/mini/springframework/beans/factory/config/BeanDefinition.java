package one.mini.springframework.beans.factory.config;

import lombok.Data;
import one.mini.springframework.beans.PropertyValues;

@Data
public class BeanDefinition {

    /**
     * 把 Object bean 替换为 Class，这样就可以把 Bean 的实例化操作放到容器中处理了
     */
    private Class<?> beanClass;

    private PropertyValues propertyValues;

    private String initMethodName;

    private String destroyMethodName;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

}
