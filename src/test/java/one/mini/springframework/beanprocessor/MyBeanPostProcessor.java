package one.mini.springframework.beanprocessor;

import lombok.extern.slf4j.Slf4j;
import one.mini.springframework.beans.BeansException;
import one.mini.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("[#] - beanName: {} postProcessBeforeInitialization", beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("[#] - beanName: {} postProcessAfterInitialization", beanName);
        return bean;
    }
}
