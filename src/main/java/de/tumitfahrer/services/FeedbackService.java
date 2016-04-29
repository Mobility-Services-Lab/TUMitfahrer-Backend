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

import de.tumitfahrer.daos.IFeedbackDao;
import de.tumitfahrer.entities.Feedback;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.enums.Language;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.DateUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Service
public class FeedbackService extends AbstractService implements EntityService<Feedback> {

    @Autowired
    private IFeedbackDao feedbackDao;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;

    @Override
    public ServiceResponse<Feedback> create(Feedback feedback) {

        final Set<ConstraintViolation<Feedback>> violations = validator.validate(feedback);
        if (violations.size() > 0) {
            return ServiceResponseFactory.fromViolations(violations);
        }

        feedback.setCreatedAt(DateUtils.getCurrentDate());
        feedback.setUpdatedAt(DateUtils.getCurrentDate());

        // insert and return
        feedbackDao.save(feedback);
        sendEmail(feedback);

        ServiceResponse<Feedback> serviceResponse = ServiceResponseFactory.createInstance();
        serviceResponse.setEntity(feedback);

        return serviceResponse;
    }

    @Override
    public ServiceResponse<Feedback> update(Feedback object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Feedback> delete(Feedback object) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public ServiceResponse<Feedback> delete(Integer id) {
        throw new NotImplementedException("TODO");
    }

    private void sendEmail(Feedback feedback) {
        User user = userService.load(feedback.getUserId());

        if (user != null) {
            mailService.contactMail(user.getEmail(), user.getFirstName(), feedback.getTitle(), feedback.getContent(), Language.EN);
        } else {
            mailService.contactMail("unknown@tumitfahrer.de", "N/A", feedback.getTitle(), feedback.getContent(), Language.EN);
        }
    }
}