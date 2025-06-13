package one.mini.springframework.beans.factory;

public interface DisposableBean {
    void destroy() throws Exception;
}
