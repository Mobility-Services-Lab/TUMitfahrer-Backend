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

package de.tumitfahrer.util;

import de.tumitfahrer.dtos.http.ErrorResponseDTO;
import de.tumitfahrer.dtos.http.ResponseDTO;
import de.tumitfahrer.services.util.ServiceResponse;
import de.tumitfahrer.services.util.ServiceResponseFactory;
import de.tumitfahrer.util.exceptions.InvalidUtilCallException;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;

public class ResponseUtils {

    public static Response ok() {
        return ok(new ResponseDTO());
    }

    public static Response ok(Object o) {
        if (o instanceof ServiceResponse) {
            throw new InvalidUtilCallException("Do not use ServiceResponse without providing a body.");
        }
        return Response.ok(o).build();
    }

    public static Response badRequest(Object o) {
        return Response.status(Response.Status.BAD_REQUEST).entity(o).build();
    }

    public static Response notFound(Object o) {
        return Response.status(Response.Status.NOT_FOUND).entity(o).build();
    }

    public static Response unsupportedMediaType(Object o) {
        return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).entity(o).build();
    }

    public static Response anyError(ServiceResponse serviceResponse) {


        if (serviceResponse.getErrors().getStatus().equals(HttpStatus.BAD_REQUEST)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(serviceResponse.getErrors()).build();
        } else if (serviceResponse.getErrors().getStatus().equals(HttpStatus.FORBIDDEN)) {
            return Response.status(Response.Status.FORBIDDEN).entity(serviceResponse.getErrors()).build();
        } else if (serviceResponse.getErrors().getStatus().equals(HttpStatus.UNAUTHORIZED)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(serviceResponse.getEntity()).build();
        } else if (serviceResponse.getErrors().getStatus().equals(HttpStatus.NOT_FOUND)) {
            return Response.status(Response.Status.NOT_FOUND).entity(serviceResponse.getErrors()).build();
        } else if (serviceResponse.getErrors().getStatus().equals(HttpStatus.CONFLICT)) {
            return Response.status(Response.Status.CONFLICT).entity(serviceResponse.getErrors()).build();
        }

        return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(serviceResponse.getErrors()).build();
    }

    public static Response unauthorized() {
        ServiceResponse serviceResponse = ServiceResponseFactory.createInstance();
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.add("Provide right api key in header Authorization!");
        errorResponseDTO.setStatus(HttpStatus.UNAUTHORIZED);
        serviceResponse.setErrors(errorResponseDTO);
        return anyError(serviceResponse);
    }

    public static Response serviceUnavailable(String message) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(message).build();
    }
}
