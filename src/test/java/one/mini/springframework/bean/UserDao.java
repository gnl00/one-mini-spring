package one.mini.springframework.bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDao {

    public void daoInit() {
        log.info("[#] daoInit UserDao");
    }

    public String queryUser(String userName, Integer id) {
        return "found user: " + userName + ", id: " + id;
    }
}
