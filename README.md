# one-mini-spring

> a simple spring-framework

## IOC

### 容器的简单实现

1. Spring Bean 容器的简单实现

```java
class BeanDefinition {
    private String beanName;
    private Object bean;
    public BeanDefinition(Object bean) {
        this.bean = bean;
    }
    public BeanDefinition(String beanName, Object bean) {
        this.beanName = beanName;
        this.bean = bean;
    }
}

class BeanFactory {
    // bean 容器低层实现实际上是一个 Map
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    public Object getBean(String beanName) {
        return beanDefinitionMap.get(beanName).getBean();
    }
    public Object getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }
}
```

上面就是一个简单的 bean 容器了，容器中存放了 bean 的定义信息，以及 bean 实例。

看起来是正常的，从 registerBeanDefinition/getBeanDefinition 这两个方法也能正常实现注册和获取 bean 的功能。

但是，容器中存放的 bean 实例是直接 new 出来的，这样会存在什么问题呢？

### 控制反转

这就和 IOC 容器的实际意义相违背了。IOC，控制反转。顾名思义，就是将创建对象的过程交给容器，而不是程序员自己 new 对象。所以这里我们需要改一下之前的代码实现：

不直接存放 Object bean，而是将 bean 的 Class 信息存放起来，在需要的时候由容器帮我们创建对象实例。

```java
class BeanDefinition {
    /**
     * 把 Object bean 替换为 Class，这样就可以把 Bean 的实例化操作放到容器中处理了
     */
    private Class<?> beanClass;
    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
}
abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            Constructor<?>[] constructors = beanDefinition.getBeanClass().getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                constructor.setAccessible(true);
                if (0 == constructor.getParameterCount()) {
                    bean = constructor.newInstance(); // 优先调用无参构造器创建对象
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BeansException("Instantiation of bean failed", e);
        }
        addSingleton(beanName, bean);
        return bean;
    }
}
```

### 有参构造器创建实例

之前的代码中，我们取巧，创建对象时使用的是无参构造器。如果需要从一个有参构造器创建对象，如何修改？

```java
interface InstantiationStrategy {
    /**
     * 在实例化接口 instantiate 方法中添加必要的入参信息，包括：beanDefinition、 beanName、ctor、args
     */
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException;
}
/**
 * JDK 实例化
 */
class SimpleInstantiationStrategy implements InstantiationStrategy {
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException {
        Class<?> clazz = beanDefinition.getBeanClass();
        try {
            if (null != ctor) {
                return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
            } else {
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (InstantiationException | RuntimeException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BeansException("Failed to instantiate [" + clazz.getName() + "]", e);
        }
    }
}
/**
 * Cglib 实例化
 */
class CglibSubclassingInstantiationStrategy implements InstantiationStrategy{
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        enhancer.setCallback(NoOp.INSTANCE);
        return null == ctor ? enhancer.create() : enhancer.create(ctor.getParameterTypes(), args);
    }
}
```

### 参数填充

现在，无论参与有无，我们都能正常根据构造方法创建出需要的对象实例了。但是少了点东西：对象的参数。

应该在什么时候将参数填充到 bean 实例对象中？当然是创建完成之后，也就是 createBean 方法中在 bean 实例创建完成之后。我们可以学习 spring，定义一个 applyPropertyValues 方法。

> 对于属性的填充不只是 int、Long、String，还包括还没有实例化的对象属性，都需要在 Bean 创建时进行填充操作。

```java
protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
    try {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
            String name = propertyValue.getName();
            Object value = propertyValue.getValue();
            // A 依赖 B，获取 B 的实例
            if (value instanceof BeanReference beanReference) {
                value = getBean(beanReference.getBeanName());
            }
            // 填充 bean 属性
            BeanUtil.setFieldValue(bean, name, value);
        }
    } catch (Exception e) {
        throw new BeansException("Error setting property values：" + beanName);
    }
}
```

到这里 Bean 的创建算是完成了。回顾代码，目前存在的问题是什么？

```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

PropertyValues propertyValues = new PropertyValues();
propertyValues.addPropertyValue(new PropertyValue("id", 10001));
propertyValues.addPropertyValue(new PropertyValue("name", "zhangsan"));
propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

beanFactory.registerBeanDefinition("userService", new BeanDefinition(UserService.class, propertyValues));
UserService userService = (UserService) beanFactory.getBean("userService");
log.info(userService.getUserInfo());
```

在创建和和获取 Bean 的时候太麻烦了。在使用 spring 的时候是不需要手动创建的，只需要配置在 xml 文件中，spring 会帮助我们自动注册到容器中。

我们配置完了之后只管获取，然后使用就行了。

### 资源加载

想要从 xml 中获取到配置信息，就涉及到 xml 文件的加载的解析。spring 中的资源如何加载呢？让我们来定义一个 ResourceLoader。

ResourceLoader 类，包含 loadResource 方法。并且支持从 classpath/filesystem/url 中加载资源。

```java
public Resource getResource(String location) {
    if (location.startsWith(CLASSPATH_URL_PREFIX)) {
        return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
    }
    else {
        try {
            return new UrlResource(URI.create(location).toURL());
        } catch (MalformedURLException e) {
            return new FileSystemResource(location);
        }
    }
}
```

资源加载完成之后我们需要解析，以 XML 文件为例，我们使用 XmlBeanDefinitionReader 对其进行解析。

```java
protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
    Document doc = XmlUtil.readXML(inputStream);
    Element root = doc.getDocumentElement();
    NodeList childNodes = root.getChildNodes();

    for (int i = 0; i < childNodes.getLength(); i++) {
        // 判断元素
        if (!(childNodes.item(i) instanceof Element)) continue;
        // 判断对象
        if (!"bean".equals(childNodes.item(i).getNodeName())) continue;

        // 解析标签
        Element bean = (Element) childNodes.item(i);
        String id = bean.getAttribute("id");
        String name = bean.getAttribute("name");
        String className = bean.getAttribute("class");
        // 获取 Class，方便获取类中的名称
        Class<?> clazz = Class.forName(className);
        // 优先级 id > name
        String beanName = StrUtil.isNotEmpty(id) ? id : name;
        if (StrUtil.isEmpty(beanName)) {
            beanName = StrUtil.lowerFirst(clazz.getSimpleName());
        }

        // 定义Bean
        BeanDefinition beanDefinition = new BeanDefinition(clazz);
        // 读取属性并填充
        for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
            if (!(bean.getChildNodes().item(j) instanceof Element property)) continue;
            if (!"property".equals(bean.getChildNodes().item(j).getNodeName())) continue;
            // 解析标签：property
            String attrName = property.getAttribute("name");
            String attrValue = property.getAttribute("value");
            String attrRef = property.getAttribute("ref");
            // 获取属性值：引入对象、值对象
            Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
            // 创建属性信息
            PropertyValue propertyValue = new PropertyValue(attrName, value);
            beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
        }
        if (getRegistry().containsBeanDefinition(beanName)) {
            throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
        }
        // 注册 BeanDefinition
        getRegistry().registerBeanDefinition(beanName, beanDefinition);
    }
}
```

如果是注解开发，则利用 AnnotatedBeanDefinitionReader 来解析类信息

```java
public void register(Class<?>... componentClasses) {
    for (Class<?> componentClass : componentClasses) {
        registerBean(componentClass);
    }
}

public void registerBean(Class<?> beanClass) {
    doRegisterBean(beanClass, null, null, null);
}

private <T> void doRegisterBean(Class<T> beanClass, String beanName,
                                Class<? extends Annotation>[] qualifiers, 
                                Object[] customizers) {
    // ...
}
```

### Bean信息修改

上面我们完成了 Bean 的配置与自动注册。相比之前的代码来说没那么麻烦了，但是问题在于，我们通过 XML 文件进行配置，如果要修改 Bean 的信息，比如添加属性，或者修改属性的值，那么我们只能修改 XML 文件。

如何不通过 XML 修改 Bean 信息？Spring 中满足于对 Bean 对象扩展的两个接口，其实也是 Spring 框架中非常具有重量级的两个接口：BeanFactoryPostProcess 和 BeanPostProcessor

BeanFactoryPostProcessor，是由 Spring 框架组建提供的容器扩展机制，允许在 Bean 对象注册后但未实例化之前，对 Bean 的定义信息 BeanDefinition 执行修改操作。

BeanPostProcessor，也是 Spring 提供的扩展机制，不过 BeanPostProcessor 是在 Bean 对象实例化之后修改 Bean 对象，也可以替换 Bean 对象。

### 添加容器上下文

目前我们关于 Bean 的操作都放在 DefaultListableBeanFactory，每次创建和获取 Bean 都是直接调用 DefaultListableBeanFactory 的方法。需要手动创建 bean 工厂和扫描累路径，加载 Bean。

将这样一套流程暴露出去给开发者使用肯定是比较麻烦的，我们就需要考虑将这两部封装起来，提供一种更好的使用方式。于是就来到 ApplicationContext。


## References

- https://github.com/fuzhengwei/small-spring