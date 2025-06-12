package one.mini.springframework.beans.factory.support;

import lombok.extern.slf4j.Slf4j;
import one.mini.springframework.beans.BeansException;
import one.mini.springframework.beans.factory.factory.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {
        log.info("[beans] - createBean");
        Object bean = null;
        try {
            Constructor<?>[] constructors = beanDefinition.getBeanClass().getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                constructor.setAccessible(true);
                // 如果传入了参数优先使用有参构造器
                if (null != beanDefinition.getConstructorArgs() && constructor.getParameterCount() == beanDefinition.getConstructorArgs().length) {
                    bean = constructor.newInstance(beanDefinition.getConstructorArgs());
                }
                // 否则调用无参构造器创建对象
                else if (0 == constructor.getParameterCount()) {
                    bean = constructor.newInstance();
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BeansException("Instantiation of bean failed", e);
        }
        addSingleton(beanName, bean);
        return bean;
    }
}
