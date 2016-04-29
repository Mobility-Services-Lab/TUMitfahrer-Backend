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
import de.tumitfahrer.assembler.RatingAssembler;
import de.tumitfahrer.dtos.rating.request.RatingRequestDTO;
import de.tumitfahrer.entities.Rating;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.security.annotations.Authenticated;
import de.tumitfahrer.services.RatingService;
import de.tumitfahrer.services.UserService;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users/{userId}/ratings")
@Api(value = "Ratings Controller", description = "Management for ratings.")
public class RatingsController extends AbstractController {

    @Autowired
    private RatingService ratingService;
    @Autowired
    private RatingAssembler ratingAssembler;
    @Autowired
    private UserService userService;

    @GET
    @Authenticated
    @ApiOperation("Returns all ratings for a user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getAllRatings(@PathParam("userId") Integer userId, @QueryParam("given") boolean given) {
        ServiceResponse<List<Rating>> serviceResponse;
        User user = userService.load(userId);
        if (user == null) {
            return ResponseUtils.notFound("User Not Found");
        }
        if (given) {
            serviceResponse = ratingService.getRatingsFromUserId(userId);
        } else {
            serviceResponse = ratingService.getRatingsToUserId(userId);
        }

        return ResponseUtils.ok(ratingAssembler.toResponse(serviceResponse.getEntity()));
    }

    @POST
    @Authenticated
    @ApiOperation("Inserts a rating for a user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response createRating(@PathParam("userId") Integer userId, RatingRequestDTO ratingRequestDTO) {
        ServiceResponse<Rating> serviceResponse;
        User user = userService.load(userId);
        if (user == null) {
            return ResponseUtils.notFound("User Not Found");
        }
        Rating rating = ratingAssembler.toEntity(ratingRequestDTO);
        rating.setFromUserId(userId);
        serviceResponse = ratingService.create(rating);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        return ResponseUtils.ok(ratingAssembler.toResponse(serviceResponse.getEntity()));
    }
}