package one.mini.springframework.beans.factory;

import one.mini.springframework.beans.BeansException;

public interface BeanFactory {

    /*Object getBean(String beanName) throws BeansException;*/

    Object getBean(String beanName, Object...args) throws BeansException;

}
