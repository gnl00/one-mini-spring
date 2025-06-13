package one.mini.test.bean;

import one.mini.springframework.beans.BeansException;
import one.mini.springframework.beans.PropertyValue;
import one.mini.springframework.beans.factory.ConfigurableListableBeanFactory;
import one.mini.springframework.beans.factory.config.BeanDefinition;
import one.mini.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * BeanFactoryPostProcessor 在 Bean 对象注册后但未实例化之前，对 Bean 的定义信息 BeanDefinition 执行修改操作
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition bd = beanFactory.getBeanDefinition("userService");
        bd.getPropertyValues().addPropertyValue(new PropertyValue("name", "update:name:in:postProcessBeanFactory"));
    }
}
