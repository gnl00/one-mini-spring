package one.mini.springframework.beans.factory;


import one.mini.springframework.beans.BeansException;
import one.mini.springframework.beans.factory.config.BeanDefinition;
import one.mini.springframework.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

}
