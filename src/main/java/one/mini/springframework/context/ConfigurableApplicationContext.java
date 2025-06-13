package one.mini.springframework.context;

import one.mini.springframework.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext {
    void refresh() throws BeansException;
}