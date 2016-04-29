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

package de.tumitfahrer.security.filter;

import de.tumitfahrer.dtos.http.ErrorResponseDTO;
import de.tumitfahrer.security.annotations.ValidateDTO;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.http.HttpStatus;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@ValidateDTO
@Priority(Priorities.ENTITY_CODER)
public class ValidationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        boolean isValid = true; // TODO: validation of input ?

        if (isValid) {
            return;
        }

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.add("Invalid DTO.");
        errorResponseDTO.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        requestContext.abortWith(ResponseUtils.unsupportedMediaType(errorResponseDTO));
    }
}