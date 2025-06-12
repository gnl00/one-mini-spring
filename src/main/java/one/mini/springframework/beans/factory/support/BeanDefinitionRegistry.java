package one.mini.springframework.beans.factory.support;

import one.mini.springframework.beans.factory.factory.BeanDefinition;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

}
