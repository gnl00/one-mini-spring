package one.mini.springframework.beans.factory.support;

import one.mini.springframework.beans.BeansException;
import one.mini.springframework.beans.factory.HierarchicalBeanFactory;
import one.mini.springframework.beans.factory.config.BeanDefinition;
import one.mini.springframework.beans.factory.config.BeanPostProcessor;
import one.mini.springframework.beans.factory.config.ConfigurableBeanFactory;
import one.mini.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractBeanFactory 继承了 DefaultSingletonBeanRegistry，也就具备了使用单例注册类方法
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory, HierarchicalBeanFactory {

    /** BeanPostProcessors to apply in createBean */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    /** ClassLoader to resolve bean class names with, if necessary */
    private final ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    @Override
    public Object getBean(String beanName) throws BeansException {
        return doGetBean(beanName, null);
    }

    @Override
    public Object getBean(String beanName, Object...args) {
        return doGetBean(beanName, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

    protected <T> T doGetBean(final String name, final Object[] args) {
        Object bean = getSingleton(name);
        if (bean != null) {
            return (T) bean;
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        return (T) createBean(name, beanDefinition, args);
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object...args);

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * Return the list of BeanPostProcessors that will get applied
     * to beans created with this factory.
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }
}
