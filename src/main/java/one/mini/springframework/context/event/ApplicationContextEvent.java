package one.mini.springframework.context.event;

import one.mini.springframework.context.ApplicationContext;
import one.mini.springframework.context.ApplicationEvent;

/**
 * 定义容器事件的抽象类，所有的事件包括关闭、刷新，以及用户自己实现的事件，都需要继承这个类。
 */
public class ApplicationContextEvent extends ApplicationEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ApplicationContextEvent(Object source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
