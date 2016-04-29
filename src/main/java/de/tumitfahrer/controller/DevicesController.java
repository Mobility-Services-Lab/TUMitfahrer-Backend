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
import de.tumitfahrer.dtos.device.DeviceRequestDTO;
import de.tumitfahrer.dtos.device.DeviceResponseDTO;
import de.tumitfahrer.dtos.device.DevicesResponseDTO;
import de.tumitfahrer.entities.Device;
import de.tumitfahrer.security.annotations.Authenticated;
import de.tumitfahrer.security.annotations.Authorized;
import de.tumitfahrer.services.DeviceService;
import de.tumitfahrer.services.UserService;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users/{userId}/devices")
@Api(value = "Devices Controller", description = "Management of all devices of a user.")
public class DevicesController extends AbstractController {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserService userService;

    @GET
    @Authenticated
    @Authorized
    @ApiOperation("Returns all devices of a user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response getAllDevicesByUserId(@PathParam("userId") Integer userId) {
        ServiceResponse<List<Device>> serviceResponse = deviceService.getEnabledDevicesByUserId(userId);
        DevicesResponseDTO devicesResponseDTO = new DevicesResponseDTO();
        devicesResponseDTO.setDevices(serviceResponse.getEntity());
        return ResponseUtils.ok(devicesResponseDTO);
    }

    @POST
    @Authenticated
    @Authorized
    @ApiOperation("Register a device for a user.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response createDevice(@PathParam("userId") Integer userId, DeviceRequestDTO deviceRequestDTO) {
        Device device = mapper.map(deviceRequestDTO.getDevice(), Device.class);
        device.setUserId(userId);

        ServiceResponse<Device> serviceResponse = deviceService.createOrRenew(device);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }

        return ResponseUtils.ok(serviceResponse.getEntity());
    }

    @DELETE
    @Authenticated
    @Authorized
    @Path("/{deviceId}")
    @ApiOperation("Marks device as disabled.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "api key token", dataType = "string", paramType = "header", required = true)})
    public Response deleteDevice(@PathParam("userId") Integer userId, @PathParam("deviceId") Integer deviceId) {

        ServiceResponse<Device> serviceResponse = deviceService.markDisabled(deviceId);

        if (serviceResponse.hasErrors()) {
            return ResponseUtils.anyError(serviceResponse);
        }
        DeviceResponseDTO response = new DeviceResponseDTO();
        response.setDevice(serviceResponse.getEntity());

        return ResponseUtils.ok(response);
    }
}