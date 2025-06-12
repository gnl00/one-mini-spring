package one.mini.springframework.beans.factory.config;

import lombok.Data;

@Data
public class BeanReference {

    private String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }
}
