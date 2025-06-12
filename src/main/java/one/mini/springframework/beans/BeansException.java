package one.mini.springframework.beans;

public class BeansException extends RuntimeException {
    public BeansException(String message, Throwable e) {
        super(message, e);
    }
}
