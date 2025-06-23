package one.mini.springframework.bean;

import one.mini.springframework.context.annotation.Component;

@Component("userServiceA")
public class UserAnnotationService implements IUserService {
    @Override
    public String queryAll() {
        return "Annotation UserService query users info";
    }
}
