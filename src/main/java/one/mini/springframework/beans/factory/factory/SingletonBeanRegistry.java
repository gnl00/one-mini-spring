package one.mini.springframework.beans.factory.factory;

public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);

}
