package one.mini.springframework.beans.factory.factory;

import lombok.Data;

@Data
public class BeanDefinition {

    /**
     * 把 Object bean 替换为 Class，这样就可以把 Bean 的实例化操作放到容器中处理了
     */
    private Class<?> beanClass;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

}
