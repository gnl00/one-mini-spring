package one.mini.springframework;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import one.mini.springframework.aop.AdvisedSupport;
import one.mini.springframework.aop.TargetSource;
import one.mini.springframework.aop.UserServiceInterceptor;
import one.mini.springframework.aop.aspectj.AspectJExpressionPointcut;
import one.mini.springframework.aop.framework.Cglib2AopProxy;
import one.mini.springframework.aop.framework.JdkDynamicAopProxy;
import one.mini.springframework.bean.IUserService;
import one.mini.springframework.beans.PropertyValue;
import one.mini.springframework.beans.PropertyValues;
import one.mini.springframework.beans.factory.config.BeanDefinition;
import one.mini.springframework.beans.factory.config.BeanReference;
import one.mini.springframework.beans.factory.support.DefaultListableBeanFactory;
import one.mini.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import one.mini.springframework.context.support.ClassPathXmlApplicationContext;
import one.mini.springframework.core.io.DefaultResourceLoader;
import one.mini.springframework.core.io.Resource;
import one.mini.springframework.core.io.ResourceLoader;
import one.mini.springframework.beanprocessor.MyBeanFactoryPostProcessor;
import one.mini.springframework.beanprocessor.MyBeanPostProcessor;
import one.mini.springframework.bean.UserDao;
import one.mini.springframework.bean.UserService;
import one.mini.springframework.event.CustomEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

@Slf4j
class MainTest {

    @Test
    public void testAnnotation() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-scan.xml");
        IUserService userService = (IUserService) applicationContext.getBean("userServiceA");
        IUserService userServiceT = (IUserService) applicationContext.getBean("userServiceT");
        System.out.println(userService.queryAll());
        System.out.println(userServiceT.queryAll());
    }

    @Test
    public void testAOP() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-aop.xml");
        IUserService userService = (IUserService) applicationContext.getBean("userService");
        System.out.println(userService.queryAll());
    }

    @Test
    public void testDynamicProxy() {
        // 目标对象
        IUserService userService = new UserService();

        // 组装代理信息
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setMethodInterceptor(new UserServiceInterceptor());
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* one.mini.springframework.bean.IUserService.*(..))"));

        JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupport);
        IUserService proxy = (IUserService) jdkDynamicAopProxy.getProxy();
        System.out.println(proxy.queryAll());

        Cglib2AopProxy cglib2AopProxy = new Cglib2AopProxy(advisedSupport);
        IUserService cglib2AopProxyProxy = (IUserService) cglib2AopProxy.getProxy();
        System.out.println(cglib2AopProxyProxy.queryAll());
    }

    @Test
    public void testPointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* one.mini.springframework.bean.UserService.*(..))");
        System.out.println(pointcut.matches(UserService.class));
        Class<?> clazz = UserService.class;
        Method method = clazz.getDeclaredMethod("getUserInfo");
        System.out.println(pointcut.matches(method, UserService.class));
    }

    @Test
    public void testEvent() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 10001L, "a new event arrival"));
    }

    @Test
    public void testApplicationContext() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.getBean("userService");
        applicationContext.getBeansOfType(UserService.class);
    }

    @Test
    public void testBeanScope() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        UserService userServiceProto = (UserService) applicationContext.getBean("userServiceProto");
        UserService userServiceProto2 = (UserService) applicationContext.getBean("userServiceProto");
        Assertions.assertFalse(userServiceProto == userServiceProto2);
    }

    @Test
    public void testBeanPostProcessor() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        MyBeanFactoryPostProcessor myBeanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
        myBeanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

        MyBeanPostProcessor myBeanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(myBeanPostProcessor);

        UserService userService = (UserService) beanFactory.getBean("userService");
        log.info(userService.getUserInfo());
    }

    @Test
    public void testBeanRegisterFromXML() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        UserService userService = (UserService) beanFactory.getBean("userService");
        log.info(userService.getUserInfo());
    }


    @Test
    public void testResourceLoadFromClasspath() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:config.properties");
        InputStream inputStream = resource.getInputStream();
        String readUtf8 = IoUtil.readUtf8(inputStream);
        log.info("read from stream: {}", readUtf8);
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