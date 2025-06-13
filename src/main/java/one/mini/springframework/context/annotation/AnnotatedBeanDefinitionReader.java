package one.mini.springframework.context.annotation;

import one.mini.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.lang.annotation.Annotation;

public class AnnotatedBeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void register(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            registerBean(componentClass);
        }
    }

    public void registerBean(Class<?> beanClass) {
        doRegisterBean(beanClass, null, null, null);
    }

    private <T> void doRegisterBean(Class<T> beanClass, String beanName,
                                    Class<? extends Annotation>[] qualifiers,
                                    Object[] customizers) {
        // ...
    }
}
