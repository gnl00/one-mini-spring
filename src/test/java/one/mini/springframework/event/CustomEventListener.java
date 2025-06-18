package one.mini.springframework.event;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import one.mini.springframework.context.event.ApplicationListener;

@Slf4j
public class CustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        log.info("[event] - receive {}", event);
    }
}
