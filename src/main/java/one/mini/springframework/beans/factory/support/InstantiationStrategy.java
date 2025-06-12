package one.mini.springframework.beans.factory.support;

import one.mini.springframework.beans.BeansException;
import one.mini.springframework.beans.factory.factory.BeanDefinition;

import java.lang.reflect.Constructor;

public interface InstantiationStrategy {

    /**
     * 在实例化接口 instantiate 方法中添加必要的入参信息，包括：beanDefinition、 beanName、ctor、args
     */
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException;

}
