/*
 * Copyright 2016 TUM Technische Universität München
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.tumitfahrer.services;

import de.tumitfahrer.daos.IUserDao;
import de.tumitfahrer.dtos.user.request.PasswordRequestDTO;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.enums.Language;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.BaseAuthUtils;
import de.tumitfahrer.util.DateUtils;
import de.tumitfahrer.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PasswordService extends AbstractService {

    @Autowired
    private IUserDao userDao;
    @Autowired
    private MailService mailService;

    public ServiceResponse<User> resetPassword(String email) {
        ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();

        User user = userDao.getUser(email);

        if (user == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("user.not_found"));
            serviceResponse.getErrors().setStatus(HttpStatus.NOT_FOUND);
            return serviceResponse;
        }

        // Get new salt, password and the hash
        String password = PasswordUtils.generatePassword();
        String salt = PasswordUtils.generateSalt();
        String sha_password = PasswordUtils.sha512(password + salt);

        user.setPassword(sha_password);
        user.setSalt(salt);
        user.setUpdatedAt(DateUtils.getCurrentDate());
        userDao.update(user);

        mailService.forgotPassword(user.getEmail(), user.getFirstName(), password, Language.EN);

        serviceResponse.setEntity(user);
        return serviceResponse;
    }

    public ServiceResponse<User> changePassword(String basicAuth, PasswordRequestDTO passwordRequestDTO) {
        ServiceResponse<User> serviceResponse = ServiceResponseFactory.createInstance();

        if (basicAuth == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.no_authentication"));
            return serviceResponse;
        }

        if (!basicAuth.startsWith("Basic")) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.wrong_authentication"));
            return serviceResponse;
        }

        if (passwordRequestDTO == null || passwordRequestDTO.getPassword().length() < 4) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.password.min"));
            return serviceResponse;
        }

        String username = BaseAuthUtils.getUsername(basicAuth);
        String tryPassword = BaseAuthUtils.getPassword(basicAuth);

        if (username == null || tryPassword == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.wrong_authentication"));
            return serviceResponse;
        }

        User user = userDao.getUser(username.toLowerCase());

        if (user == null) {
            // hide mail existence
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.wrong_credentials"));
            return serviceResponse;
        }

        boolean isAuthenticated = PasswordUtils.isAuthenticated(user, tryPassword);

        if (!isAuthenticated) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("session.wrong_credentials"));
            return serviceResponse;
        }

        String salt = PasswordUtils.generateSalt();
        String sha_password = PasswordUtils.sha512(passwordRequestDTO.getPassword() + salt);

        user.setPassword(sha_password);
        user.setSalt(salt);
        user.setUpdatedAt(DateUtils.getCurrentDate());
        userDao.update(user);

        serviceResponse.setEntity(user);
        return serviceResponse;
    }
}