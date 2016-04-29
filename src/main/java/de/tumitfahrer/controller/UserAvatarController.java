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
import de.tumitfahrer.entities.Avatar;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.security.annotations.Authenticated;
import de.tumitfahrer.security.annotations.Authorized;
import de.tumitfahrer.services.AvatarService;
import de.tumitfahrer.services.UserService;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Path("/users/{userId}/avatar")
@Api(value = "Avatar Controller", description = "Controller for the profile picture")
public class UserAvatarController extends AbstractController {

    @Autowired
    private AvatarService avatarService;
    @Autowired
    private UserService userService;

    @GET
    @Path("/{avatarId}")
    @Produces("image/*")
    @ApiOperation("Returns the avatar of a user claimed by the avatar id.")
    public Response getAvatarFromAvatarId(@PathParam("avatarId") Integer avatarId) {
        Avatar avatar = avatarService.getAvatar(avatarId);

        return createFileForDownload(avatar);
    }

    @GET
    @Produces("image/*")
    @ApiOperation("Returns the current avatar of a user.")
    public Response getAvatarFromUserId(@PathParam("userId") Integer userId) {
        User user = userService.load(userId);
        if (user == null) {
            return ResponseUtils.notFound("User Not Found");
        }
        Avatar avatar = avatarService.getAvatarByUserId(userId);
        return createFileForDownload(avatar);
    }

    @DELETE
    @Authenticated
    @Authorized
    @ApiOperation("Removes the avatar from the user. The file is still stored in the database")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response removeAvatarFromUserId(@PathParam("userId") Integer userId) {
        avatarService.removeAvatarByUserId(userId);

        return ResponseUtils.ok();
    }

    @POST
    @Authenticated
    @Authorized
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @ApiOperation("Uploads an avatar for the user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response createAvatar(@PathParam("userId") Integer userId, @FormDataParam("uploadImage") FormDataBodyPart body, @FormDataParam("uploadImage") InputStream fileInputStream, @FormDataParam("uploadImage") FormDataContentDisposition contentDispositionHeader) {

        ServiceResponse<Avatar> serviceResponse = avatarService.createAvatar(userId, fileInputStream, contentDispositionHeader, body);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        return ResponseUtils.ok();
    }

    /**
     * createFileForDownload
     * Takes an Byte-Array and content-type and builds an response with the data file.
     *
     * @return Response with the given picture
     */
    private Response createFileForDownload(Avatar avatar) {
        // Return Dummy-Avatar, if no avatar is found
        if (avatar == null) {
            try {
                ClassPathResource resource = new ClassPathResource("assets/missing.png");
                return Response.ok(resource.getInputStream()).header("Content-Type", "image/png").build();
            } catch (IOException e) {
                e.printStackTrace();
                return Response.status(Response.Status.NOT_IMPLEMENTED).build();
            }
        }

        ByteArrayInputStream is = new ByteArrayInputStream(avatar.getData());

        try {
            return Response.ok(is).header("Content-Type", avatar.getExt()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        }
    }
}