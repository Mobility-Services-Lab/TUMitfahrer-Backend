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

import de.tumitfahrer.daos.IAvatarDao;
import de.tumitfahrer.daos.IUserDao;
import de.tumitfahrer.entities.Avatar;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AvatarService extends AbstractService {

    @Autowired
    private IAvatarDao avatarDao;
    @Autowired
    private IUserDao userDao;

    @Value("${file.size}")
    private Integer maxFileSize;

    public ServiceResponse<Avatar> createAvatar(Integer userId, InputStream fileInputStream, FormDataContentDisposition contentDispositionHeader, FormDataBodyPart body) {
        ServiceResponse<Avatar> serviceResponse = ServiceResponseFactory.createInstance();

        // nullpointer checks
        if (fileInputStream == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("avatar.null_inputstream"));
        }

        if (contentDispositionHeader == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("avatar.null_form_disp"));
        }

        if (body == null) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("avatar.null_body"));
        }

        if (serviceResponse.hasErrors()) {
            return serviceResponse;
        }

        Avatar avatar = new Avatar();

        try {
            byte[] bytes = IOUtils.toByteArray(fileInputStream);

            if (bytes.length > maxFileSize) {
                serviceResponse.getErrors().add(errorMsgsUtil.get("avatar.max_size"));
                return serviceResponse;
            }
            avatar.setData(bytes);
        } catch (IOException e) {
            serviceResponse.getErrors().add(errorMsgsUtil.get("avatar.file_exception"));
            return serviceResponse;
        }

        avatar.setUserId(userId);
        avatar.setCreatedAt(DateUtils.getCurrentDate());
        avatar.setName(contentDispositionHeader.getFileName());
        avatar.setExt(body.getMediaType().toString());

        // insert
        avatarDao.saveAvatar(avatar);
        serviceResponse.setEntity(avatar);

        // Update the avatar id of the user
        User user = userDao.load(userId);
        user.setAvatarId(avatar.getId());
        userDao.update(user);

        return serviceResponse;
    }

    public Avatar getAvatar(Integer id) {
        return avatarDao.getAvatar(id);
    }

    public Avatar getAvatarByUserId(Integer userId) {
        User user = userDao.load(userId);

        if (user == null) {
            return null;
        }
        return getAvatar(user.getAvatarId());
    }

    public void removeAvatarByUserId(Integer userId) {
        User user = userDao.load(userId);

        if (user != null) {
            user.setAvatarId(null);
            userDao.update(user);
        }
    }
}