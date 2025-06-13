package one.mini.springframework.beans.factory.config;

public interface ConfigurableBeanFactory extends SingletonBeanRegistry {

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

}
