package one.mini.springframework.beans;

public class BeansException extends RuntimeException {

    private String message;

    public BeansException(String message) {
        super(message);
        this.message = message;
    }
    
    public BeansException(String message, Throwable e) {
        super(message, e);
    }
}
