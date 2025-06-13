package one.mini.springframework.beans.factory;

public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
