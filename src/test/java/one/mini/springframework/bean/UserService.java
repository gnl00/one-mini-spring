package one.mini.springframework.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import one.mini.springframework.beans.factory.DisposableBean;
import one.mini.springframework.beans.factory.InitializingBean;

@Slf4j
@Data
public class UserService implements InitializingBean, DisposableBean {
    private Integer id;
    private String name;
    private UserDao userDao;
    public UserService() {}
    public UserService(String name) {
        this.name = name;
    }
    public String getUserInfo() {
        return userDao.queryUser(this.name, this.id);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("[#] - afterPropertiesSet exec for UserService");
    }

    @Override
    public void destroy() throws Exception {
        log.info("[#] - destroy exec for UserService");
    }
}
