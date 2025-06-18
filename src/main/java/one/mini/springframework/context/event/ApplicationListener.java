package one.mini.springframework.context.event;

import one.mini.springframework.context.ApplicationEvent;

import java.util.EventListener;

public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
    void onApplicationEvent(E event);
}
