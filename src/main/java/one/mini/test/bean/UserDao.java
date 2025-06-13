package one.mini.test.bean;

public class UserDao {
    public String queryUser(String userName, Integer id) {
        return "found user: " + userName + ", id: " + id;
    }
}
