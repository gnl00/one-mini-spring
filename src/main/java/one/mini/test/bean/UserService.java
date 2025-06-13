package one.mini.test.bean;

import lombok.Data;

@Data
public class UserService {
    private Integer id;
    private String name;
    private UserDao userDao;
    public UserService() {
    }
    public UserService(String name) {
        this.name = name;
    }
    public String getUserInfo() {
        return userDao.queryUser(this.name, this.id);
    }
}
