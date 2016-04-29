package de.tumitfahrer.security.controller;

import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.AvatarService;
import de.tumitfahrer.services.UserService;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAvatarSecurity extends AbstractSecurity {

    @Autowired
    AvatarService avatarService;
    @Autowired
    UserService userService;

    public void removeAvatarFromUserId() {
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

        return requestContext.getHeaderString("Authorization").equals(user.getApiKey());

    }


    public void createAvatar() {
        if (isAdmin() || checkIfSameUser()) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

}
