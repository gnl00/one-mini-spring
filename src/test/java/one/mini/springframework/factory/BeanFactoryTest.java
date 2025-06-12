package one.mini.springframework.factory;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import one.mini.springframework.beans.PropertyValue;
import one.mini.springframework.beans.PropertyValues;
import one.mini.springframework.beans.factory.config.BeanDefinition;
import one.mini.springframework.beans.factory.config.BeanReference;
import one.mini.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Log4j2
class BeanFactoryTest {

    @Data
    static class UserService {
        private Integer id;
        private String name;
        private UserDao userDao;
        public UserService() {
        }
        public UserService(String name) {
            this.name = name;
        }
        public String getUserInfo() {
            return userDao.queryUser(this.name, this.id);
        }
    }

    static class UserDao {
        public String queryUser(String userName, Integer id) {
            return "found user: " + userName + ", id: " + id;
        }
    }

    @Test
    public void testPopulateProperties() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("id", 10001));
        propertyValues.addPropertyValue(new PropertyValue("name", "zhangsan"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

        beanFactory.registerBeanDefinition("userService", new BeanDefinition(UserService.class, propertyValues));
        UserService userService = (UserService) beanFactory.getBean("userService");
        log.info(userService.getUserInfo());

    }

    @Test
    public void testGetBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("userService", new BeanDefinition(UserService.class));

        // first get bean
        UserService userService = (UserService) beanFactory.getBean("userService", "zhangsan");
        // second get bean from singleton
        UserService userServiceSingleton = (UserService) beanFactory.getBean("userService");
        Assertions.assertNotNull(userService);
        Assertions.assertNotNull(userService.getUserInfo());
        Assertions.assertSame(userService, userServiceSingleton);
    }

}