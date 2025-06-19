package one.mini.springframework.aop;

import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * 用于把代理、拦截、匹配的各项属性包装到一个类中，方便在 Proxy 实现类进行使用
 */
@Data
public class AdvisedSupport {

    private boolean proxyTargetClass = false;

    // 被代理的目标对象
    private TargetSource targetSource;

    // 方法拦截器
    private MethodInterceptor methodInterceptor;

    // 方法匹配器(检查目标方法是否符合通知条件)
    private MethodMatcher methodMatcher;

}
