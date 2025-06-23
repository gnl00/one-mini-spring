package one.mini.springframework.bean;

import lombok.Getter;
import lombok.Setter;

public class UserServiceT implements IUserService {

    @Getter
    @Setter
    private String token;

    public UserServiceT() {
    }

    public UserServiceT(String token) {
        this.token = token;
    }

    @Override
    public String queryAll() {
        return "Annotation UserService query users info " + this.token;
    }
}
