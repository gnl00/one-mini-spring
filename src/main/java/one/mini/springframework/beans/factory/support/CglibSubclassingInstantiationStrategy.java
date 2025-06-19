package one.mini.springframework.beans.factory.support;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import one.mini.springframework.beans.BeansException;
import one.mini.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * Cglib 实例化
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        enhancer.setCallback(NoOp.INSTANCE);
        return null == ctor ? enhancer.create() : enhancer.create(ctor.getParameterTypes(), args);
    }
}
