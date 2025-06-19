package one.mini.springframework.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import one.mini.springframework.beans.BeansException;
import one.mini.springframework.context.ApplicationContextAware;
import one.mini.springframework.beans.factory.BeanClassLoaderAware;
import one.mini.springframework.beans.factory.BeanFactory;
import one.mini.springframework.beans.factory.BeanFactoryAware;
import one.mini.springframework.beans.factory.BeanNameAware;
import one.mini.springframework.beans.factory.DisposableBean;
import one.mini.springframework.beans.factory.InitializingBean;
import one.mini.springframework.context.ApplicationContext;

@Slf4j
@Data
public class UserService implements IUserService, InitializingBean, DisposableBean
        , BeanNameAware, BeanClassLoaderAware, ApplicationContextAware, BeanFactoryAware {
    private Integer id;
    private String name;
    private UserDao userDao;
    public UserService() {}

    public String getUserInfo() {
        return userDao.queryUser(this.name, this.id);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("[#] - afterPropertiesSet exec for UserService");
    }

    @Override
    public void destroy() throws Exception {
        log.info("[#] - destroy exec for UserService");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("[#] - UserService applicationContext: {}", applicationContext);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        log.info("[#] - UserService classloader: {}", classLoader);
    }

    @Override
    public void setBeanName(String name) {
        log.info("[#] - UserService beanName: {}", name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.info("[#] - UserService beanFactory: {}", beanFactory);
    }

    @Override
    public String queryAll() {
        return "\nqueryAll()\n";
    }
}
