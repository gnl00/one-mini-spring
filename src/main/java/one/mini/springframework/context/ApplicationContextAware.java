package one.mini.springframework.context;

import one.mini.springframework.beans.BeansException;

/**
 * Interface to be implemented by any object that wishes to be notifiedof the {@link ApplicationContext} that it runs in.
 */
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
