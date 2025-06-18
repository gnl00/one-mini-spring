package one.mini.springframework.event;

import lombok.ToString;
import one.mini.springframework.context.ApplicationEvent;

@ToString
public class CustomEvent extends ApplicationEvent {

    private Long id;
    private String message;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public CustomEvent(Object source, Long id, String message) {
        super(source);
        this.id = id;
        this.message = message;
    }
}
