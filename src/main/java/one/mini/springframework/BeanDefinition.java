package one.mini.springframework;

import lombok.Data;

@Data
public class BeanDefinition {

    private String beanName;
    private Object bean;

    public BeanDefinition(Object bean) {
        this.bean = bean;
    }

    public BeanDefinition(String beanName, Object bean) {
        this.beanName = beanName;
        this.bean = bean;
    }
}
