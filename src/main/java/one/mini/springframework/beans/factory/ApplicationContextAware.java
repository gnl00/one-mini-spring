package one.mini.springframework.beans.factory;

import one.mini.springframework.beans.BeansException;
import one.mini.springframework.context.ApplicationContext;

/**
 * Interface to be implemented by any object that wishes to be notifiedof the {@link ApplicationContext} that it runs in.
 */
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
