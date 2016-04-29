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

package de.tumitfahrer.services.util;

import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ServiceResponseFactory {

    public static <T> ServiceResponse<T> createInstance() {
        ServiceResponse<T> serviceResponse = new ServiceResponse<T>();
        return serviceResponse;
    }

    public static <T> ServiceResponse<T> fromViolations(final Set<ConstraintViolation<T>> constraintViolationSet) {
        final ServiceResponse<T> serviceResponse = createInstance();

        serviceResponse.getErrors().addConstraintViolations(constraintViolationSet);
        serviceResponse.getErrors().setStatus(HttpStatus.BAD_REQUEST);

        return serviceResponse;
    }

}
