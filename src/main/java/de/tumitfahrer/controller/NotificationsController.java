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
import com.wordnik.swagger.annotations.ApiOperation;
import de.tumitfahrer.services.NotificationService;
import de.tumitfahrer.util.notification.GcmResponse;
import de.tumitfahrer.util.notification.GcmServerSideSender;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/notifications")
@Api(value = "Notifications Controller", description = "Testing methods for notifications ")
public class NotificationsController extends AbstractController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private GcmServerSideSender gcm;

    @GET
    @Path("/{receiverid}")
    @ApiOperation("Send notification to device registered with 'receiverid' ")
    public Response testNotification(@PathParam("receiverid") String receiver) {
        GcmResponse response = gcm.sendNotificationV2(receiver, "this is a title", "this is a message", 0, 0);
        if (response.hasErrors() || response.hasMessageError()) {
            return Response.ok(response.getErrorMessage()).build();
        }
        return Response.ok("good").build();
    }

    @GET
    @Path("/test/{userid}/{rideid}")
    @ApiOperation("Test notification for requesting a ride")
    public Response testNotification(@PathParam("userid") Integer userId, @PathParam("rideid") Integer rideId) {
        if (!notificationService.notifyRequestRide(userId, rideId)) {
            return Response.ok("failed ").build();
        }
        return Response.ok("good").build();
    }

    @GET
    @Path("/test/{rideid}")
    @ApiOperation("Test notification for deleteing a ride")
    public Response testNotification(@PathParam("rideid") Integer rideId) {
        notificationService.notifyRideCanceled(rideId);
        return Response.ok("good").build();
    }
}
