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

应该在什么时候将参数填充到 bean 实例对象中？当然是创建完成之后，也就是 createBean 方法中在 bean 实例创建完成之后。我们可以学习 spring，定义一个 populateBean 方法。

## References

- https://github.com/fuzhengwei/small-spring