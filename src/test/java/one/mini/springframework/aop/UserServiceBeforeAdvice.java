package one.mini.springframework.aop;

import java.lang.reflect.Method;

public class UserServiceBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("UserServiceBeforeAdvice 拦截方法：" + method.getName());
    }
}
