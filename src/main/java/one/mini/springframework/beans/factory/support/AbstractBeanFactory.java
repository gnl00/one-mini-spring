package one.mini.springframework.beans.factory.support;

import one.mini.springframework.beans.factory.BeanFactory;
import one.mini.springframework.beans.factory.factory.BeanDefinition;

/**
 * AbstractBeanFactory 继承了 DefaultSingletonBeanRegistry，也就具备了使用单例注册类方法
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String beanName) {
        Object bean = getSingleton(beanName);
        if (null != bean) {
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        return createBean(beanName, beanDefinition);
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);

}
