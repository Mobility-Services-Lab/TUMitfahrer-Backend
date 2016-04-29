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

package de.tumitfahrer.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import de.tumitfahrer.dtos.feedback.FeedbackRequestDTO;
import de.tumitfahrer.entities.Feedback;
import de.tumitfahrer.services.FeedbackService;
import de.tumitfahrer.services.UserService;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/feedback")
@Api(value = "Feedback Controller", description = "Management for user feedbacks.")
public class FeedbackController extends AbstractController {

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private UserService userService;

    @POST
    @ApiOperation("Creates feedback for TUMitfahrer and sends it as email.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = false)})
    public Response createFeedback(@HeaderParam("Authorization") String authorization, FeedbackRequestDTO feedbackRequestDTO) {
        Feedback feedback = mapper.map(feedbackRequestDTO, Feedback.class);
        feedback.setUserId(userService.getUserIdByApiKey(authorization));
        ServiceResponse<Feedback> serviceResponse = feedbackService.create(feedback);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        return ResponseUtils.ok(serviceResponse.getEntity());
    }
}