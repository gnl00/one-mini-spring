package one.mini.springframework.bean;

import lombok.extern.slf4j.Slf4j;
import one.mini.springframework.beans.factory.FactoryBean;

@Slf4j
public class MyUserDaoFactoryBean implements FactoryBean<UserDao> {
    public MyUserDaoFactoryBean() {}

    @Override
    public UserDao getObject() throws Exception {
        log.info("[#] creating my userDao");
        return new UserDao();
    }

    @Override
    public Class<?> getObjectType() {
        return getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
