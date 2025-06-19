package one.mini.springframework.aop;

/**
 * 组合 Pointcut 和 Advice，Pointcut 用于获取 JoinPoint，而 Advice 决定 JoinPoint 执行什么操作
 */
public interface PointcutAdvisor extends Advisor {
    /**
     * Get the Pointcut that drives this advisor.
     */
    Pointcut getPointcut();
}
