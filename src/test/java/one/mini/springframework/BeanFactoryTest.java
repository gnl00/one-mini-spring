package one.mini.springframework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BeanFactoryTest {

    static class UserService {
        public String getUser() {
            return "user";
        }
    }

    @Test
    public void testGetBean() {
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.registerBeanDefinition("userService", new BeanDefinition(new UserService()));
        UserService userService = (UserService) beanFactory.getBean("userService");
        Assertions.assertNotNull(userService, "userService BeanDefinition is null");
        Assertions.assertEquals("user", userService.getUser());
    }

}