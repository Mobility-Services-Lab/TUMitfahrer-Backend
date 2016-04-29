package de.tumitfahrer.security.controller;

import de.tumitfahrer.daos.IUserDao;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.UserService;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity extends AbstractSecurity {

    @Autowired
    IUserDao userDao;
    @Autowired
    UserService userService;

    public void updateUser() {
        if (isAdmin() || checkIfSameUser()) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

    private boolean checkIfSameUser() {
        Integer userId;
        try {
            userId = Integer.parseInt(pathParams.getFirst("userId"));
        } catch (ClassCastException e) {
            return false;
        }

        User user = userService.load(userId);

        if (user == null) {
            return false;
        }

        return currentUser.getId().equals(user.getId());

    }

    public void deleteUser() {
        if (isAdmin() || checkIfSameUser()) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

}

