package one.mini.springframework;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import one.mini.springframework.beans.factory.factory.BeanDefinition;
import one.mini.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Log4j2
class BeanFactoryTest {

    @Data
    static class UserService {
        private String name;
        public UserService(String name) {
            this.name = name;
        }
        public String getUser() {
            return this.name;
        }
    }

    @Test
    public void testLog() {
        log.info("test ----");
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
        Assertions.assertNotNull(userService.getUser());
        Assertions.assertSame(userService, userServiceSingleton);
    }

}